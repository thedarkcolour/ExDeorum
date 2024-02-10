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
import net.minecraft.world.item.crafting.RecipeSerializer;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedCrucibleHeatRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final BlockPredicate blockPredicate;
    private final int heatValue;

    public FinishedCrucibleHeatRecipe(ResourceLocation id, BlockPredicate blockPredicate, int heatValue) {
        this.id = id;
        this.blockPredicate = blockPredicate;
        this.heatValue = heatValue;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("block_predicate", this.blockPredicate.toJson());
        json.addProperty("heat_value", this. heatValue);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.CRUCIBLE_HEAT_SOURCE.get();
    }
}
