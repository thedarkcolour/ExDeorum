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

package thedarkcolour.exdeorum.recipe.sieve;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
    private final Either<Item, TagKey<Item>> result;
    private final NumberProvider resultAmount;

    public FinishedSieveRecipe(ResourceLocation id, Item mesh, Ingredient ingredient, Either<Item, TagKey<Item>> result, NumberProvider resultAmount) {
        this.id = id;
        this.mesh = mesh;
        this.ingredient = ingredient;
        this.result =  result;
        this.resultAmount = resultAmount;
    }

    @Override
    public void serializeRecipeData(JsonObject object) {
        object.add("ingredient", this.ingredient.toJson());
        object.addProperty("mesh", ForgeRegistries.ITEMS.getKey(this.mesh).toString());
        this.result.ifLeft(item -> {
            object.addProperty("result", ForgeRegistries.ITEMS.getKey(item).toString());
        }).ifRight(tag -> {
            object.addProperty("result_tag", tag.location().toString());
        });
        object.add("result_amount", LootDataType.PREDICATE.parser().toJsonTree(this.resultAmount));
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.SIEVE.get();
    }
}
