package thedarkcolour.exnihiloreborn.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.recipe.sieve.SieveRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

record JeiSieveRecipeGroup(Ingredient ingredient, ItemStack mesh, List<Result> results) {
    public static int maxSieveRows;

    public static void addRecipes(IRecipeRegistration registration, RecipeType<JeiSieveRecipeGroup> recipeType) {
        maxSieveRows = 1;

        // copy the list so we can do removals
        List<SieveRecipe> recipes = new ArrayList<>(Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor(ERecipeTypes.SIEVE.get()));
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

        ImmutableList.Builder<JeiSieveRecipeGroup> jeiRecipes = new ImmutableList.Builder<>();
        // Sort based on expected count of result
        var sorter = Comparator.comparingDouble(Result::expectedCount).reversed();

        // ingredients with common ingredients are grouped into lists (ex. dirt)
        for (var ingredient : ingredientGrouper.keySet()) {
            Multimap<Item, SieveRecipe> meshGrouper = ArrayListMultimap.create();
            var values = ingredientGrouper.get(ingredient);

            // these lists are grouped into sub lists based on their meshes (ex. dirt with string mesh)
            for (var recipe : values) {
                meshGrouper.put(recipe.mesh, recipe);
            }

            // the sub lists have their results combined for displaying in JEI
            for (var mesh : meshGrouper.keySet()) {
                var meshRecipes = meshGrouper.get(mesh);
                var results = new ArrayList<Result>(meshRecipes.size());

                for (var recipe : meshRecipes) {
                    int resultCount = recipe.resultAmount instanceof ConstantValue constant ? Math.round(constant.value) : 1;
                    results.add(new Result(new ItemStack(recipe.result, resultCount), recipe.resultAmount));
                }

                results.sort(sorter);

                var jeiRecipe = new JeiSieveRecipeGroup(ingredient, new ItemStack(mesh), results);
                jeiRecipes.add(jeiRecipe);

                var rows = Mth.ceil((float) meshRecipes.size() / 9f);
                if (rows > maxSieveRows) {
                    maxSieveRows = rows;
                }
            }
        }

        registration.addRecipes(recipeType, jeiRecipes.build());
    }

    static final class Result {
        final ItemStack item;
        final NumberProvider provider;
        final double expectedCount;

        Result(ItemStack item, NumberProvider provider) {
            this.item = item;
            this.provider = provider;
            this.expectedCount = RecipeUtil.getExpectedValue(this.provider);
        }

        public double expectedCount() {
            return this.expectedCount;
        }
    }
}
