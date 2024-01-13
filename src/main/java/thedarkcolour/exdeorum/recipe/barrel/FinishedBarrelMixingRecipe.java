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

package thedarkcolour.exdeorum.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedBarrelMixingRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Fluid fluid;
    private final int fluidAmount;
    private final Item result;

    public FinishedBarrelMixingRecipe(ResourceLocation id, Ingredient ingredient, Fluid fluid, int fluidAmount, Item result) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.result = result;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", this.ingredient.toJson());
        json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(this.fluid).toString());
        json.addProperty("fluid_amount", this.fluidAmount);
        json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }
}
