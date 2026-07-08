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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CrucibleHeatRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private Map<BlockState, RecipeHolder<CrucibleHeatRecipe>> recipes;

    public CrucibleHeatRecipeCache(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    public RecipeHolder<CrucibleHeatRecipe> getRecipe(BlockState state) {
        if (this.recipes == null) {
            buildRecipes();
        }
        return this.recipes.get(state);
    }

    private void buildRecipes() {
        this.recipes = new HashMap<>();

        for (var holder : this.recipeManager.byType(ERecipeTypes.CRUCIBLE_HEAT_SOURCE.get())) {
            var recipe = holder.value();
            recipe.blockPredicate().possibleStates().forEach(state -> this.recipes.put(state, holder));
        }

        this.recipeManager = null;
    }

    public Set<Map.Entry<BlockState, RecipeHolder<CrucibleHeatRecipe>>> getEntries() {
        if (this.recipes == null) {
            buildRecipes();
        }
        return this.recipes.entrySet();
    }
}
