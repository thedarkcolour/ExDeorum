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

package thedarkcolour.exdeorum.compat;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

// Since no JEI code is used here, this can be reused for REI
public record GroupedSieveRecipe(Ingredient ingredient, ItemStack mesh, List<Result> results) {
    public static int maxSieveRows;

    public static ImmutableList<GroupedSieveRecipe> getAllRecipesGrouped() {
        maxSieveRows = 1;

        var recipes = CompatUtil.collectAllRecipes(ERecipeTypes.SIEVE.value(), Function.identity());
        Multimap<Ingredient, SieveRecipe> ingredientGrouper = ArrayListMultimap.create();

        for (int i = 0; i < recipes.size(); i++) {
            var recipe = recipes.get(i);

            ingredientGrouper.put(recipe.getIngredient(), recipe);

            for (int j = i + 1; j < recipes.size(); j++) {
                var other = recipes.get(j);

                if (RecipeUtil.areIngredientsEqual(recipe.getIngredient(), other.getIngredient())) {
                    ingredientGrouper.put(recipe.getIngredient(), other);
                    recipes.remove(other);
                    j--;
                }
            }
        }

        ImmutableList.Builder<GroupedSieveRecipe> jeiRecipes = new ImmutableList.Builder<>();
        // Sort based on expected count of result
        var resultSorter = Comparator.comparingDouble(Result::expectedCount).reversed();
        // Sort based on order of sieve tier
        var meshSorter = Comparator.comparingInt(GroupedSieveRecipe::meshOrder);

        // ingredients with common ingredients are grouped into lists (ex. dirt)
        for (var ingredient : ingredientGrouper.keySet()) {
            Multimap<Item, SieveRecipe> meshGrouper = ArrayListMultimap.create();
            var values = ingredientGrouper.get(ingredient);

            // these lists are grouped into sub lists based on their meshes (ex. dirt with string mesh)
            for (var recipe : values) {
                meshGrouper.put(recipe.mesh, recipe);
            }

            // the sub lists have their results combined for displaying in JEI
            var meshes = new ArrayList<>(meshGrouper.keySet());
            meshes.sort(meshSorter);

            for (var mesh : meshes) {
                var meshRecipes = meshGrouper.get(mesh);
                var results = new ArrayList<Result>(meshRecipes.size());

                for (var recipe : meshRecipes) {
                    int resultCount = recipe.resultAmount instanceof ConstantValue constant ? Math.round(constant.value()) : 1;
                    results.add(new Result(new ItemStack(recipe.result, resultCount), recipe.resultAmount, recipe.byHandOnly));
                }

                results.sort(resultSorter);

                var jeiRecipe = new GroupedSieveRecipe(ingredient, new ItemStack(mesh), results);
                jeiRecipes.add(jeiRecipe);

                var rows = Mth.ceil((float) meshRecipes.size() / 9f);
                if (rows > maxSieveRows) {
                    maxSieveRows = rows;
                }
            }
        }
        return jeiRecipes.build();
    }

    private static int meshOrder(Item mesh) {
        if (mesh == EItems.STRING_MESH.get()) {
            return -5;
        } else if (mesh == EItems.FLINT_MESH.get()) {
            return -4;
        } else if (mesh == EItems.IRON_MESH.get()) {
            return -3;
        } else if (mesh == EItems.GOLDEN_MESH.get()) {
            return -2;
        } else if (mesh == EItems.DIAMOND_MESH.get()) {
            return -1;
        } else if (mesh == EItems.NETHERITE_MESH.get()) {
            return 0;
        } else {
            return BuiltInRegistries.ITEM.getId(mesh);
        }
    }

    public static final class Result {
        public final ItemStack item;
        public final NumberProvider provider;
        public final boolean byHandOnly;
        private final double expectedCount;

        Result(ItemStack item, NumberProvider provider, boolean byHandOnly) {
            this.item = item;
            this.provider = provider;
            this.byHandOnly = byHandOnly;
            this.expectedCount = RecipeUtil.getExpectedValue(this.provider);
        }

        public double expectedCount() {
            return this.expectedCount;
        }
    }
}
