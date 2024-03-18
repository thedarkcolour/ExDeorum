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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface BlockPredicate extends Predicate<BlockState> {
    // used for network
    byte SINGLE_BLOCK = 0, BLOCK_STATE = 1, BLOCK_TAG = 2;

    // todo test this
    Codec<BlockPredicate> CODEC = new BlockPredicate.SpecialCodec();

    JsonObject toJson();

    void toNetwork(FriendlyByteBuf buffer);

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
    static BlockPredicate fromJson(@Nullable JsonObject json) {
        if (json == null) {
            return null;
        }
        if (json.has("block")) {
            var block = BuiltInRegistries.BLOCK.get(new ResourceLocation(json.get("block").getAsString()));

            if (block == Blocks.AIR) return null;

            if (json.has("state")) {
                return new BlockStatePredicate(block, CodecUtil.decode(StatePropertiesPredicate.CODEC, json.get("state")));
            } else {
                return new SingleBlockPredicate(block);
            }
        } else if (json.has("block_tag")) {
            return new TagPredicate(TagKey.create(Registries.BLOCK, new ResourceLocation(json.get("block_tag").getAsString())));
        } else {
            return null;
        }
    }

    @Nullable
    static BlockPredicate fromNetwork(FriendlyByteBuf buffer) {
        return switch (buffer.readByte()) {
            case SINGLE_BLOCK -> new SingleBlockPredicate(Objects.requireNonNull(buffer.readById(BuiltInRegistries.BLOCK)));
            case BLOCK_STATE -> new BlockStatePredicate(Objects.requireNonNull(buffer.readById(BuiltInRegistries.BLOCK)), decodeStatePredicate(JsonParser.parseString(buffer.readUtf())));
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
        private static final Codec<TagPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(TagPredicate::tag)).apply(instance, TagPredicate::new));

        @Override
        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("block_tag", this.tag.location().toString());
            return json;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
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
        private static final Codec<BlockStatePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CodecUtil.blockField("block", BlockStatePredicate::block),
                StatePropertiesPredicate.CODEC.fieldOf("properties").forGetter(BlockStatePredicate::properties)
        ).apply(instance, BlockStatePredicate::new));
        @Override
        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
            json.add("state", encodeStatePredicate(this.properties));
            return json;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeByte(BLOCK_STATE);
            buffer.writeId(BuiltInRegistries.BLOCK, this.block);
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
        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
            return json;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeByte(SINGLE_BLOCK);
            buffer.writeId(BuiltInRegistries.BLOCK, this.block);
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
        @Override
        public <T> DataResult<Pair<BlockPredicate, T>> decode(DynamicOps<T> ops, T input) {
            var tagResult = TagPredicate.CODEC.decode(ops, input);

            if (tagResult.error().isEmpty()) {
                return CodecUtil.cast(tagResult);
            } else {
                var stateResult = BlockStatePredicate.CODEC.decode(ops, input);

                if (stateResult.error().isEmpty()) {
                    return CodecUtil.cast(stateResult);
                } else {
                    var blockResult = SingleBlockPredicate.CODEC.decode(ops, input);

                    return blockResult.error().isEmpty() ? CodecUtil.cast(blockResult) : DataResult.error(() -> "Invalid block predicate");
                }
            }
        }

        @Override
        public <T> DataResult<T> encode(BlockPredicate input, DynamicOps<T> ops, T prefix) {
            // in newer java, this should be replaced with pattern matching
            if (input instanceof SingleBlockPredicate block) {
                return SingleBlockPredicate.CODEC.encode(block, ops, prefix);
            } else if (input instanceof BlockStatePredicate state) {
                return BlockStatePredicate.CODEC.encode(state, ops, prefix);
            } else {
                return TagPredicate.CODEC.encode((TagPredicate) input, ops, prefix);
            }
        }
    }
}
