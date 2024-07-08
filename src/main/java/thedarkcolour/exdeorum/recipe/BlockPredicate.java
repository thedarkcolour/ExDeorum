/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface BlockPredicate extends Predicate<BlockState> {
    // used for network
    byte SINGLE_BLOCK = 0, BLOCK_STATE = 1, BLOCK_TAG = 2;

    Codec<BlockPredicate> CODEC = new BlockPredicate.SpecialCodec();
    StreamCodec<RegistryFriendlyByteBuf, BlockPredicate> STREAM_CODEC = StreamCodec.of(BlockPredicate::writeBlockPredicateNetwork, BlockPredicate::readBlockPredicateNetwork);

    private static void writeBlockPredicateNetwork(RegistryFriendlyByteBuf buffer, BlockPredicate predicate) {
        predicate.toNetwork(buffer);
    }

    private static BlockPredicate readBlockPredicateNetwork(RegistryFriendlyByteBuf buffer) {
        BlockPredicate blockPredicate = fromNetwork(buffer);

        if (blockPredicate == null) {
            throw new IllegalStateException("Failed to read block predicate from network");
        }
        return blockPredicate;
    }

    void toNetwork(RegistryFriendlyByteBuf buffer);

    Stream<BlockState> possibleStates();

    static BlockPredicate singleBlock(Block block) {
        return new SingleBlockPredicate(block);
    }

    static BlockPredicate blockState(Block block, StatePropertiesPredicate properties) {
        return new BlockStatePredicate(block, properties);
    }

    static BlockPredicate blockTag(TagKey<Block> tag) {
        return new TagPredicate(tag);
    }

    @Nullable
    static BlockPredicate fromNetwork(RegistryFriendlyByteBuf buffer) {
        return switch (buffer.readByte()) {
            case SINGLE_BLOCK -> new SingleBlockPredicate(Objects.requireNonNull(buffer.readById(BuiltInRegistries.BLOCK::byId)));
            case BLOCK_STATE -> new BlockStatePredicate(Objects.requireNonNull(buffer.readById(BuiltInRegistries.BLOCK::byId)), decodeStatePredicate(JsonParser.parseString(buffer.readUtf())));
            case BLOCK_TAG -> new TagPredicate(RecipeUtil.readTag(buffer, Registries.BLOCK));
            default -> null;
        };
    }

    private static StatePropertiesPredicate decodeStatePredicate(JsonElement json) {
        return CodecUtil.decode(StatePropertiesPredicate.CODEC, json);
    }

    private static JsonElement encodeStatePredicate(StatePropertiesPredicate predicate) {
        return CodecUtil.encode(StatePropertiesPredicate.CODEC, predicate);
    }

    record TagPredicate(TagKey<Block> tag) implements BlockPredicate {
        @Override
        public void toNetwork(RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(BLOCK_TAG);
            RecipeUtil.writeTag(buffer, this.tag);
        }

        @Override
        public boolean test(BlockState state) {
            return state.is(this.tag);
        }

        @Override
        public Stream<BlockState> possibleStates() {
            return StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(this.tag).spliterator(), false)
                    .filter(holder -> holder.is(this.tag))
                    .flatMap(holder -> holder.value().getStateDefinition().getPossibleStates().stream());
        }
    }

    record BlockStatePredicate(Block block, StatePropertiesPredicate properties) implements BlockPredicate {
        @Override
        public void toNetwork(RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(BLOCK_STATE);
            buffer.writeById(BuiltInRegistries.BLOCK::getId, this.block);
            buffer.writeUtf(encodeStatePredicate(this.properties).toString());
        }

        @Override
        public boolean test(BlockState state) {
            return state.is(this.block) && this.properties.matches(state);
        }

        @Override
        public Stream<BlockState> possibleStates() {
            return this.block.getStateDefinition().getPossibleStates().stream().filter(this.properties::matches);
        }

        // Although slow, this is useful for testing
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BlockStatePredicate that = (BlockStatePredicate) o;
            return this.block == that.block && Objects.equals(encodeStatePredicate(this.properties), encodeStatePredicate(that.properties));
        }
    }

    record SingleBlockPredicate(Block block) implements BlockPredicate {
        @Override
        public void toNetwork(RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(SINGLE_BLOCK);
            buffer.writeById(BuiltInRegistries.BLOCK::getId, this.block);
        }

        @Override
        public boolean test(BlockState state) {
            return state.is(this.block);
        }

        @Override
        public Stream<BlockState> possibleStates() {
            return this.block.getStateDefinition().getPossibleStates().stream();
        }
    }

    class SpecialCodec implements Codec<BlockPredicate> {
        private static final Codec<TagPredicate> TAG_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagKey.codec(Registries.BLOCK).fieldOf("block_tag").forGetter(TagPredicate::tag)
        ).apply(instance, TagPredicate::new));
        private static final Codec<BlockStatePredicate> BLOCK_STATE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CodecUtil.blockField("block", BlockStatePredicate::block),
                StatePropertiesPredicate.CODEC.fieldOf("state").forGetter(BlockStatePredicate::properties)
        ).apply(instance, BlockStatePredicate::new));
        private static final Codec<SingleBlockPredicate> SINGLE_BLOCK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CodecUtil.blockField("block", SingleBlockPredicate::block)
        ).apply(instance, SingleBlockPredicate::new));

        @Override
        public <T> DataResult<Pair<BlockPredicate, T>> decode(DynamicOps<T> ops, T input) {
            var tagResult = TAG_CODEC.decode(ops, input);

            if (tagResult.error().isEmpty()) {
                return CodecUtil.cast(tagResult);
            } else {
                var stateResult = BLOCK_STATE_CODEC.decode(ops, input);

                if (stateResult.error().isEmpty()) {
                    return CodecUtil.cast(stateResult);
                } else {
                    var blockResult = SINGLE_BLOCK_CODEC.decode(ops, input);

                    return blockResult.error().isEmpty() ? CodecUtil.cast(blockResult) : DataResult.error(() -> "Invalid block predicate");
                }
            }
        }

        @Override
        public <T> DataResult<T> encode(BlockPredicate input, DynamicOps<T> ops, T prefix) {
            // in newer java, this should be replaced with pattern matching
            if (input instanceof SingleBlockPredicate block) {
                return SINGLE_BLOCK_CODEC.encode(block, ops, prefix);
            } else if (input instanceof BlockStatePredicate state) {
                return BLOCK_STATE_CODEC.encode(state, ops, prefix);
            } else {
                return TAG_CODEC.encode((TagPredicate) input, ops, prefix);
            }
        }
    }
}
