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

package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;

final class CrucibleHeatSourceRecipe {
    private final RecipeHolder<CrucibleHeatRecipe> recipeHolder;
    private final BlockState blockState;
    @Nullable
    private final IIngredientType<Object> ingredientType;
    @Nullable
    private final Object ingredient;

    @SuppressWarnings({"rawtypes", "unchecked"})
    CrucibleHeatSourceRecipe(RecipeHolder<CrucibleHeatRecipe> recipeHolder, BlockState blockState, @Nullable IIngredientType ingredientType, @Nullable Object ingredient) {
        this.recipeHolder = recipeHolder;
        this.blockState = blockState;
        this.ingredientType = ingredientType;
        this.ingredient = ingredient;
    }

    public RecipeHolder<CrucibleHeatRecipe> recipeHolder() {
        return recipeHolder;
    }

    public int meltRate() {
        return this.recipeHolder.value().heatValue();
    }

    public BlockState blockState() {
        return this.blockState;
    }

    @Nullable
    public IIngredientType<Object> ingredientType() {
        return this.ingredientType;
    }

    @Nullable
    public Object ingredient() {
        return this.ingredient;
    }
}
