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

package thedarkcolour.exdeorum.recipe.sieve;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedSieveRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Item mesh;
    private final Item result;
    private final NumberProvider resultAmount;

    public FinishedSieveRecipe(ResourceLocation id, Item mesh, Ingredient ingredient, Item result, NumberProvider resultAmount) {
        this.id = id;
        this.mesh = mesh;
        this.ingredient = ingredient;
        this.result =  result;
        this.resultAmount = resultAmount;
    }

    @Override
    public void serializeRecipeData(JsonObject object) {
        object.add("ingredient", ingredient.toJson());
        object.addProperty("mesh", ForgeRegistries.ITEMS.getKey(this.mesh).toString());
        object.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
        object.add("result_amount", LootDataType.PREDICATE.parser().toJsonTree(resultAmount));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.SIEVE.get();
    }
}
