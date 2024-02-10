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

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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

public record CrucibleHeatRecipe(ResourceLocation id, BlockPredicate blockPredicate, int heatValue) implements Recipe<Container> {
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
    public ResourceLocation getId() {
        return this.id;
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
        public CrucibleHeatRecipe fromJson(ResourceLocation id, JsonObject json) {
            BlockPredicate blockPredicate = RecipeUtil.readBlockPredicate(id, json);
            if (blockPredicate == null) return null;
            int heatValue = json.get("heat_value").getAsInt();
            return new CrucibleHeatRecipe(id, blockPredicate, heatValue);
        }

        @Override
        public CrucibleHeatRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            BlockPredicate blockPredicate = RecipeUtil.readBlockPredicateNetwork(id, buffer);
            if (blockPredicate == null) return null;
            int heatValue = buffer.readVarInt();
            return new CrucibleHeatRecipe(id, blockPredicate, heatValue);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleHeatRecipe recipe) {
            recipe.blockPredicate.toNetwork(buffer);
            buffer.writeVarInt(recipe.heatValue);
        }
    }
}
