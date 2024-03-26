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

package thedarkcolour.exdeorum.recipe.hammer;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public class HammerRecipe extends ProbabilityRecipe {
    public HammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(id, ingredient, result, resultAmount);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.HAMMER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.HAMMER.get();
    }

    public static abstract class AbstractSerializer<T extends HammerRecipe> implements RecipeSerializer<T> {
        protected abstract T createHammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount);

        @Override
        public T fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Item result = RecipeUtil.readItem(json, "result");
            NumberProvider resultAmount = RecipeUtil.readNumberProvider(json, "result_amount");
            return createHammerRecipe(name, ingredient, result, resultAmount);
        }

        @Override
        @SuppressWarnings("deprecation")
        public T fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return createHammerRecipe(name, ingredient, result, resultAmount);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
        }
    }

    public static class Serializer extends AbstractSerializer<HammerRecipe> {
        @Override
        protected HammerRecipe createHammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount) {
            return new HammerRecipe(id, ingredient, result, resultAmount);
        }
    }
}
