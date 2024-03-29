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

package thedarkcolour.exdeorum.compat.kubejs;

import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public record SieveMeshFilter(ReplacementMatch match) implements RecipeFilter {
    @Override
    public boolean test(RecipeKJS recipe) {
        return this.match instanceof ItemMatch itemMatch && recipe instanceof RecipeJS recipeJs && recipeJs.getOriginalRecipe() instanceof SieveRecipe sieveRecipe && itemMatch.contains(sieveRecipe.mesh);
    }
}
