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

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.HashMap;
import java.util.Map;

// for now, only simple recipes
public class BarrelFluidMixingRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private Map<Fluid, Map<Fluid, RecipeHolder<BarrelFluidMixingRecipe>>> recipes;

    public BarrelFluidMixingRecipeCache(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    @Nullable
    public RecipeHolder<BarrelFluidMixingRecipe> getRecipe(Fluid baseFluid, Fluid additive) {
        if (this.recipes == null) {
            buildRecipes();
        }
        var recipesForBase = this.recipes.get(baseFluid);
        if (recipesForBase != null) {
            return recipesForBase.get(additive);
        }
        return null;
    }

    private void buildRecipes() {
        this.recipes = new HashMap<>();

        for (var holder : this.recipeManager.byType(ERecipeTypes.BARREL_FLUID_MIXING.get())) {
            var recipe = holder.value();
            for (var baseStack : recipe.baseFluid().ingredient().getStacks()) {
                var map = this.recipes.computeIfAbsent(baseStack.getFluid(), key -> new HashMap<>());

                for (var additiveStack : recipe.additiveFluid().getStacks()) {
                    map.put(additiveStack.getFluid(), holder);
                }
            }
        }

        this.recipeManager = null;
    }
}
