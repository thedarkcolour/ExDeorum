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

package thedarkcolour.exdeorum.recipe.crucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public record CrucibleHeatRecipe(BlockPredicate blockPredicate, int heatValue) implements Recipe<Container> {
    public static final Codec<CrucibleHeatRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPredicate.CODEC.fieldOf("block_predicate").forGetter(CrucibleHeatRecipe::blockPredicate),
            Codec.INT.fieldOf("heat_value").forGetter(CrucibleHeatRecipe::heatValue)
    ).apply(instance, CrucibleHeatRecipe::new));

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.CRUCIBLE_HEAT_SOURCE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.CRUCIBLE_HEAT_SOURCE.get();
    }

    public static class Serializer implements RecipeSerializer<CrucibleHeatRecipe> {
        @Override
        public Codec<CrucibleHeatRecipe> codec() {
            return CODEC;
        }

        @Override
        public CrucibleHeatRecipe fromNetwork(FriendlyByteBuf buffer) {
            BlockPredicate blockPredicate = RecipeUtil.readBlockPredicateNetwork(buffer);
            int heatValue = buffer.readVarInt();
            return new CrucibleHeatRecipe(blockPredicate, heatValue);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleHeatRecipe recipe) {
            recipe.blockPredicate.toNetwork(buffer);
            buffer.writeVarInt(recipe.heatValue);
        }
    }
}
