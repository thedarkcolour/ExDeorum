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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SieveRecipeCache<T extends SieveRecipe> {
    private RecipeManager recipeManager;
    private final Supplier<? extends RecipeType<T>> recipeType;
    @Nullable
    private Map<Item, MeshRecipeCache<T>> meshCaches;

    public SieveRecipeCache(RecipeManager recipeManager, Supplier<? extends RecipeType<T>> recipeType) {
        this.recipeManager = recipeManager;
        this.recipeType = recipeType;
    }

    public List<T> getRecipe(Item mesh, ItemStack input) {
        if (this.meshCaches == null) {
            buildRecipes();
        }
        var meshCache = this.meshCaches.get(mesh);
        return meshCache == null ? List.of() : meshCache.getRecipes(input);
    }

    private void buildRecipes() {
        // Group recipes based on their mesh
        var tempMap = new HashMap<Item, List<T>>();
        for (var recipe : this.recipeManager.byType(this.recipeType.get()).values()) {
            tempMap.computeIfAbsent(recipe.mesh, k -> new ArrayList<>()).add(recipe);
        }
        this.meshCaches = new HashMap<>();
        for (var mesh : tempMap.entrySet()) {
            this.meshCaches.put(mesh.getKey(), new MeshRecipeCache<>(mesh.getValue()));
        }
        this.recipeManager = null;
    }

    // For now, there will be no complex recipes. What that means is, recipes may not use NBT information of the
    // block being sifted to decide what its drops are. Firstly, this would be complicated for me to code. Secondly,
    // conveying this information in JEI would be difficult (ex. Bottle drops from Sand, but only if the Sand has a
    // certain enchantment). Thirdly, I do not see anybody needing this use case, and if they do, they should contact
    // me on GitHub or Discord so that I can get around to actually implementing it.
    private static class MeshRecipeCache<T extends SieveRecipe> {
        private final Map<Item, List<T>> simpleRecipes;

        private MeshRecipeCache(List<T> recipes) {
            this.simpleRecipes = new HashMap<>();
            var temp = new HashMap<Item, ImmutableList.Builder<T>>();

            for (var recipe : recipes) {
                for (var item : recipe.ingredient.getItems()) {
                    temp.computeIfAbsent(item.getItem(), k -> ImmutableList.builder()).add(recipe);
                }
            }

            for (var entry : temp.entrySet()) {
                this.simpleRecipes.put(entry.getKey(), entry.getValue().build());
            }
        }

        public List<T> getRecipes(ItemStack input) {
            var result = this.simpleRecipes.get(input.getItem());
            return result == null ? List.of() : result;
        }
    }
}
