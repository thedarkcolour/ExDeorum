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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

public class FinishedCrucibleRecipe extends EFinishedRecipe {
    private final RecipeSerializer<?> serializer;
    private final Ingredient ingredient;
    private final FluidStack fluidStack;

    public FinishedCrucibleRecipe(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredient, Fluid fluid, int amount) {
        this(id, serializer, ingredient, new FluidStack(fluid, amount));
    }

    public FinishedCrucibleRecipe(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredient, FluidStack fluidStack) {
        super(id);
        this.serializer = serializer;
        this.ingredient = ingredient;
        this.fluidStack = fluidStack;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", this.ingredient.toJson());
        JsonObject fluidObj = new JsonObject();
        json.add("fluid", RecipeUtil.writeFluidStackJson(this.fluidStack));
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.serializer;
    }
}
