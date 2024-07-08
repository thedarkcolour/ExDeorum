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

package thedarkcolour.exdeorum.recipe.crook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public record CrookRecipe(BlockPredicate blockPredicate, ItemStack result, float chance) implements Recipe<RecipeInput> {
    public static final MapCodec<CrookRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPredicate.CODEC.fieldOf("block_predicate").forGetter(CrookRecipe::blockPredicate),
            ItemStack.CODEC.fieldOf("result").forGetter(CrookRecipe::result),
            Codec.FLOAT.fieldOf("chance").forGetter(CrookRecipe::chance)
    ).apply(instance, CrookRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrookRecipe> STREAM_CODEC = StreamCodec.of(CrookRecipe::toNetwork, CrookRecipe::fromNetwork);

    @Override
    public boolean matches(RecipeInput input, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.CROOK.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.CROOK.get();
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, CrookRecipe recipe) {
        recipe.blockPredicate.toNetwork(buffer);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeFloat(recipe.chance);
    }

    public static CrookRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        BlockPredicate blockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        float chance = buffer.readFloat();

        return new CrookRecipe(blockPredicate, result, chance);
    }

    public static class Serializer implements RecipeSerializer<CrookRecipe> {
        @Override
        public MapCodec<CrookRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrookRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
