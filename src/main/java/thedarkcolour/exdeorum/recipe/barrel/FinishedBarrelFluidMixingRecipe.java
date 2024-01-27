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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedBarrelFluidMixingRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Fluid baseFluid;
    private final int baseFluidAmount;
    private final Fluid additiveFluid;
    private final Item result;
    private final boolean consumesAdditive;

    public FinishedBarrelFluidMixingRecipe(ResourceLocation id, Fluid baseFluid, int baseFluidAmount, Fluid additiveFluid, Item result, boolean consumesAdditive) {
        this.id = id;
        this.baseFluid = baseFluid;
        this.baseFluidAmount = baseFluidAmount;
        this.additiveFluid = additiveFluid;
        this.result = result;
        this.consumesAdditive = consumesAdditive;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.addProperty("base_fluid", ForgeRegistries.FLUIDS.getKey(this.baseFluid).toString());
        json.addProperty("base_fluid_amount", this.baseFluidAmount);
        json.addProperty("additive_fluid", ForgeRegistries.FLUIDS.getKey(this.additiveFluid).toString());
        json.addProperty("consumes_additive", this.consumesAdditive);
        json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_FLUID_MIXING.get();
    }
}
