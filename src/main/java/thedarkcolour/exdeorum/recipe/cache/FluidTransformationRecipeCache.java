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

package thedarkcolour.exdeorum.recipe.cache;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.HashMap;
import java.util.Map;

public class FluidTransformationRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private Map<BlockState, Map<Fluid, FluidTransformationRecipe>> recipes;

    public FluidTransformationRecipeCache(RecipeManager manager) {
        this.recipeManager = manager;
    }

    @Nullable
    public FluidTransformationRecipe getRecipe(Fluid baseFluid, BlockState catalystState) {
        if (this.recipes == null) {
            buildRecipes();
        }
        var recipesForBase = this.recipes.get(catalystState);
        if (recipesForBase != null) {
            return recipesForBase.get(baseFluid);
        }
        return null;
    }

    private void buildRecipes() {
        this.recipes = new HashMap<>();

        for (var holder : this.recipeManager.byType(ERecipeTypes.BARREL_FLUID_TRANSFORMATION.get()).values()) {
            var recipe = holder.value();
            recipe.catalyst().possibleStates().forEach(state -> {
                this.recipes.computeIfAbsent(state, key -> new HashMap<>()).put(recipe.baseFluid(), recipe);
            });
        }

        var dedupe = new HashMap<Map<Fluid, FluidTransformationRecipe>, Map<Fluid, FluidTransformationRecipe>>();
        for (var entry : this.recipes.entrySet()) {
            entry.setValue(dedupe.computeIfAbsent(entry.getValue(), Map::copyOf));
        }
        this.recipes = Map.copyOf(this.recipes);

        this.recipeManager = null;
    }
}
