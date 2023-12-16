/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

package thedarkcolour.exdeorum.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

public class SieveRecipeCache {
    private RecipeManager recipeManager;
    @Nullable
    private IdentityHashMap<Item, MeshRecipeCache> meshCaches;

    public SieveRecipeCache(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    public List<SieveRecipe> getRecipe(Item mesh, ItemStack input) {
        if (meshCaches == null) {
            buildRecipes();
        }
        var meshCache = meshCaches.get(mesh);
        return meshCache == null ? List.of() : meshCache.getRecipes(input);
    }

    private void buildRecipes() {
        // Group recipes based on their mesh
        var tempMap = new HashMap<Item, List<SieveRecipe>>();
        for (var recipe : recipeManager.byType(ERecipeTypes.SIEVE.get()).values()) {
            tempMap.computeIfAbsent(recipe.mesh, k -> new ArrayList<>()).add(recipe);
        }
        this.meshCaches = new IdentityHashMap<>();
        for (var mesh : tempMap.entrySet()) {
            this.meshCaches.put(mesh.getKey(), new MeshRecipeCache(mesh.getValue()));
        }
        this.recipeManager = null;
    }

    // For now, there will be no complex recipes. What that means is, recipes may not use NBT information of the
    // block being sifted to decide what its drops are. Firstly, this would be complicated for me to code. Secondly,
    // conveying this information in JEI would be difficult (ex. Bottle drops from Sand, but only if the Sand has a
    // certain NBT tag). Thirdly, I do not see anybody needing this use case, and if they do, they should contact
    // me on GitHub or Discord so that I can get around to actually implementing it.
    private static class MeshRecipeCache {
        private final IdentityHashMap<Item, List<SieveRecipe>> simpleRecipes;

        private MeshRecipeCache(List<SieveRecipe> recipes) {
            this.simpleRecipes = new IdentityHashMap<>();
            var temp = new IdentityHashMap<Item, ImmutableList.Builder<SieveRecipe>>();

            for (var recipe : recipes) {
                for (var item : recipe.ingredient.getItems()) {
                    temp.computeIfAbsent(item.getItem(), k -> ImmutableList.builder()).add(recipe);
                }
            }

            for (var entry : temp.entrySet()) {
                this.simpleRecipes.put(entry.getKey(), entry.getValue().build());
            }
        }

        public List<SieveRecipe> getRecipes(ItemStack input) {
            var result = simpleRecipes.get(input.getItem());
            return result == null ? List.of() : result;
        }
    }
}
