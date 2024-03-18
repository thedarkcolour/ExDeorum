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

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class CrucibleHeatRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private Object2IntMap<BlockState> recipes;

    public CrucibleHeatRecipeCache(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    public int getValue(BlockState state) {
        if (this.recipes == null) {
            buildRecipes();
        }
        return this.recipes.getInt(state);
    }

    private void buildRecipes() {
        this.recipes = new Object2IntOpenHashMap<>();

        for (var holder : this.recipeManager.byType(ERecipeTypes.CRUCIBLE_HEAT_SOURCE.get()).values()) {
            var recipe = holder.value();
            recipe.blockPredicate().possibleStates().forEach(state -> this.recipes.put(state, recipe.heatValue()));
        }

        this.recipeManager = null;
    }

    public ObjectSet<Object2IntMap.Entry<BlockState>> getEntries() {
        if (this.recipes == null) {
            buildRecipes();
        }
        return this.recipes.object2IntEntrySet();
    }
}
