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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

import javax.annotation.Nullable;

public class CrucibleRecipe extends SingleIngredientRecipe {
    private final RecipeType<?> type;
    private final FluidStack result;

    public CrucibleRecipe(ResourceLocation id, RecipeType<?> type, Ingredient ingredient, FluidStack result) {
        super(id, ingredient);
        this.type = type;
        this.result = result;

        if (this.dependsOnNbt) {
            throw new IllegalArgumentException("Cannot use NBT to determine Ex Deorum Crucible output");
        }
    }

    public FluidStack getResult() {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.LAVA_CRUCIBLE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        private final RecipeType<?> type;

        public Serializer(RecipeType<?> type) {
            this.type = type;
        }

        @Override
        public CrucibleRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            FluidStack stack = RecipeUtil.readFluidStack(json, "fluid");

            return new CrucibleRecipe(id, this.type, ingredient, stack);
        }

        @Nullable
        @Override
        public CrucibleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new CrucibleRecipe(id, this.type, Ingredient.fromNetwork(buffer), FluidStack.readFromPacket(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            recipe.getResult().writeToPacket(buffer);
        }
    }
}
