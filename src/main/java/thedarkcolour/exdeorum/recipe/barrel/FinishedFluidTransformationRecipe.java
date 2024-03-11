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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.WeightedList;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedFluidTransformationRecipe extends EFinishedRecipe {
    private final Fluid baseFluid;
    private final Fluid resultFluid;
    private final int resultColor;
    private final BlockPredicate catalyst;
    private final WeightedList<BlockState> byproducts;
    private final int duration;

    public FinishedFluidTransformationRecipe(ResourceLocation id, Fluid baseFluid, Fluid resultFluid, int resultColor, BlockPredicate catalyst, WeightedList<BlockState> byproducts, int duration) {
        super(id);
        this.baseFluid = baseFluid;
        this.resultFluid = resultFluid;
        this.resultColor = resultColor;
        this.catalyst = catalyst;
        this.byproducts = byproducts;
        this.duration = duration;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.addProperty("base_fluid", RecipeUtil.getFluidId(this.baseFluid));
        json.addProperty("result_fluid", RecipeUtil.getFluidId(this.resultFluid));
        json.addProperty("result_color", this.resultColor);
        json.addProperty("duration", this.duration);
        json.add("catalyst", this.catalyst.toJson());
        json.add("byproducts", this.byproducts.toJson(RecipeUtil::writeBlockState));
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_FLUID_TRANSFORMATION.get();
    }
}
