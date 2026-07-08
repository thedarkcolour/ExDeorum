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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.apache.commons.lang3.mutable.MutableInt;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.EItems;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Since no JEI code is used here, this can be reused for REI
public record XeiSieveRecipe(ResourceLocation id, Ingredient ingredient, ItemStack mesh, List<Result> results) {
    public static final MutableInt SIEVE_ROWS = new MutableInt(0);
    public static final MutableInt COMPRESSED_SIEVE_ROWS = new MutableInt(0);

    public static <T extends SieveRecipe> ImmutableList<XeiSieveRecipe> getAllRecipesGrouped(RecipeType<T> recipeType, MutableInt maxRows) {
        int maxSieveRows = 1;

        var recipeHolders = CompatUtil.collectAllRecipes(RecipeUtil.getClientRecipeManager(), recipeType, Function.identity());
        var recipeTypeKey = Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipeType));
        Multimap<Ingredient, RecipeHolder<T>> ingredientGrouper = ArrayListMultimap.create();

        for (int i = 0; i < recipeHolders.size(); i++) {
            var holder = recipeHolders.get(i);
            var recipe = holder.value();

            ingredientGrouper.put(recipe.ingredient(), holder);

            for (int j = i + 1; j < recipeHolders.size(); j++) {
                var otherHolder = recipeHolders.get(j);
                var other = otherHolder.value();

                if (RecipeUtil.areIngredientsEqual(recipe.ingredient(), other.ingredient())) {
                    ingredientGrouper.put(recipe.ingredient(), otherHolder);
                    recipeHolders.remove(otherHolder);
                    j--;
                }
            }
        }

        ImmutableList.Builder<XeiSieveRecipe> jeiRecipes = new ImmutableList.Builder<>();
        // Sort based on expected count of result
        var resultSorter = Comparator.comparingDouble(Result::expectedCount).reversed();
        // Sort based on order of sieve tier
        var meshSorter = Comparator.comparingInt(XeiSieveRecipe::meshOrder);

        // ingredients with common ingredients are grouped into lists (ex. dirt)
        for (var ingredient : ingredientGrouper.keySet()) {
            Multimap<Item, RecipeHolder<T>> meshGrouper = ArrayListMultimap.create();
            var values = ingredientGrouper.get(ingredient);

            // A unique ingredient ID, which can grow long. For same ingredient will always generate same ID, even over game restarts.
            var ingredientId = "ingredient-start " + Stream
                    .of(ingredient.getItems())
                    .map(ItemStack::getItem)
                    .map(BuiltInRegistries.ITEM::getKey)
                    .map(ResourceLocation::toString)
                    .sorted()
                    .collect(Collectors.joining(" - ")) + " ingredient-end ";

            // these lists are grouped into sub lists based on their meshes (ex. dirt with string mesh)
            for (var holder : values) {
                var recipe = holder.value();
                for (var stack : recipe.mesh.getItems()) {
                    meshGrouper.put(stack.getItem(), holder);
                }
            }

            // the sub lists have their results combined for displaying in JEI
            var meshes = new ArrayList<>(meshGrouper.keySet());
            meshes.sort(meshSorter);

            for (var mesh : meshes) {
                var meshRecipes = meshGrouper.get(mesh);
                var results = new ArrayList<Result>(meshRecipes.size());
                var idList = new ArrayList<String>();
                idList.add(ingredientId);
                idList.add(BuiltInRegistries.ITEM.getKey(mesh).toString());

                for (var holder : meshRecipes) {
                    var recipe = holder.value();
                    int resultCount = recipe.resultAmount instanceof ConstantValue(float value) ? Math.round(value) : 1;
                    results.add(new Result(holder, recipe.result.copyWithCount(resultCount), recipe.resultAmount, recipe.byHandOnly));

                    idList.add(holder.id().toString());
                }

                var id = ResourceLocation.fromNamespaceAndPath(recipeTypeKey.getNamespace(), recipeTypeKey.getPath() + "/" + hash512(idList));

                results.sort(resultSorter);
                var jeiRecipe = new XeiSieveRecipe(id, ingredient, new ItemStack(mesh), results);
                jeiRecipes.add(jeiRecipe);

                var rows = Mth.ceil((float) meshRecipes.size() / 9f);
                if (rows > maxSieveRows) {
                    maxSieveRows = rows;
                }
            }
        }

        maxRows.setValue(maxSieveRows);

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

    /**
     * Function to hash a collection of strings using 512 bits precision.
     * The order of the input collection does not matter.
     * The idea behind this is to hash all recipes that make a recipe group and give the recipe group an ID.
     * We need to make sure the ID doesn't end up with any collisions, so we just use a cryptographic hash.
     * It's much more likely that 100 meteors strike ones house at the same time, than that two hashes collide,
     * so that should suffice for uniqueness...
     */
    private static String hash512(Collection<String> inputs) {
        try {
            // make a unique string out of all inputs, regardless of the order they have.
            // we use a separator which is sure to be unique and never in any input: " ||| "
            var sortedInputs = inputs.stream().sorted().collect(Collectors.joining(" ||| "));
            var md = MessageDigest.getInstance("SHA-512");
            var digest = md.digest(sortedInputs.getBytes(StandardCharsets.UTF_8));
            // Create a bigint out of the digest and convert it to a hex string
            var bi = new BigInteger(1, digest);
            return bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            // This is pretty bad... This shouldn't happen
            throw new Error("Your java does not support SHA-512. Wat da hell? Report this to ExDeorum", e);
        }
    }

    public static final class Result {
        public final RecipeHolder<? extends SieveRecipe> holder;
        public final ItemStack item;
        public final NumberProvider provider;
        public final boolean byHandOnly;
        private final double expectedCount;

        Result(RecipeHolder<? extends SieveRecipe> holder, ItemStack item, NumberProvider provider, boolean byHandOnly) {
            this.holder = holder;
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
