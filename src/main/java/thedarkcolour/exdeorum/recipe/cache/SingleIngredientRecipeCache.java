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

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SingleIngredientRecipeCache<T extends SingleIngredientRecipe> {
    private final Supplier<RecipeType<T>> recipeType;
    private RecipeManager recipeManager;
    @Nullable
    private Map<Item, T> simpleRecipes;
    @Nullable
    private List<T> complexRecipes;

    public SingleIngredientRecipeCache(RecipeManager recipeManager, Supplier<RecipeType<T>> recipeType) {
        this.recipeType = recipeType;
        this.recipeManager = recipeManager;
    }

    @Nullable
    public T getRecipe(Item item) {
        return getRecipe(new ItemStack(item));
    }

    @Nullable
    public T getRecipe(ItemStack item) {
        if (this.simpleRecipes == null) {
            buildRecipes();
        }
        // simpleRecipes is guaranteed not null
        var recipe = this.simpleRecipes.get(item.getItem());

        // if there are complex recipes, test each one
        if (recipe == null && this.complexRecipes != null) {
            for (var complexRecipe : this.complexRecipes) {
                if (complexRecipe.getIngredient().test(item)) {
                    return complexRecipe;
                }
            }

            // if neither list contains a recipe for this item, return null
            return null;
        } else {
            return recipe;
        }
    }

    /**
     * Called when this recipe cache is first queried. First, scans available recipes for recipes with "simple"
     * ingredients that do not check an item's NBT. All of these recipes are added to the {@link #simpleRecipes}
     * map, which is indexed by the item(s) the recipe's ingredient accepts. Recipes whose ingredients are "complex"
     * and consider an item's NBT are added to the separate {@link #complexRecipes} list. Unlike simpleRecipes,
     * complexRecipes may be null after this method call if no complex recipes are found. Finally, after all recipes
     * have been scanned, the {@link #recipeManager} is set to null, since it is no longer needed.
     */
    private void buildRecipes() {
        this.simpleRecipes = new HashMap<>();
        var complexRecipes = ImmutableList.<T>builder();

        var allRecipes = this.recipeManager.byType(this.recipeType.get()).values();

        for (var holder : allRecipes) {
            var recipe = holder.value();
            var ingredient = recipe.getIngredient();

            if (ingredient.isSimple()) {
                for (var item : ingredient.getItems()) {
                    this.simpleRecipes.put(item.getItem(), recipe);
                }
            } else {
                complexRecipes.add(recipe);
            }
        }

        this.complexRecipes = complexRecipes.build();
        if (this.complexRecipes.isEmpty()) {
            this.complexRecipes = null;
        }

        this.recipeManager = null;
    }
}
