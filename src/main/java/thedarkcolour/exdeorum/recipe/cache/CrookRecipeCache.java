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
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.*;

public class CrookRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private Map<BlockState, List<CrookRecipe>> recipes;

    public CrookRecipeCache(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    public List<CrookRecipe> getRecipes(BlockState state) {
        if (this.recipes == null) {
            buildRecipes();
        }
        return this.recipes.getOrDefault(state, List.of());
    }

    private void buildRecipes() {
        this.recipes = new HashMap<>();

        // state -> set of possible recipes
        var tempRecipes = new HashMap<BlockState, HashSet<CrookRecipe>>();

        for (var recipe : this.recipeManager.byType(ERecipeTypes.CROOK.get()).values()) {
            recipe.value().blockPredicate().possibleStates().forEach(state -> {
                tempRecipes.computeIfAbsent(state, key -> new HashSet<>()).add(recipe.value());
            });
        }
        // map equal sets to a single list object instead of using a bunch of duplicate sets
        var dedupeMap = new HashMap<HashSet<CrookRecipe>, List<CrookRecipe>>();

        for (var entry : tempRecipes.entrySet()) {
            this.recipes.put(entry.getKey(), dedupeMap.computeIfAbsent(entry.getValue(), List::copyOf));
        }

        this.recipeManager = null;
    }
}
