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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
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
    private static final Codec<HammerRecipe> CODEC = RecordCodecBuilder.create(instance -> ProbabilityRecipe.commonFields(instance).apply(instance, HammerRecipe::new));

    public HammerRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(ingredient, result, resultAmount);
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
        protected abstract T createHammerRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount);

        @Override
        public T fromNetwork(FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return createHammerRecipe(ingredient, result, resultAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
        }
    }

    public static class Serializer extends HammerRecipe.AbstractSerializer<HammerRecipe> {
        @Override
        public Codec<HammerRecipe> codec() {
            return CODEC;
        }

        @Override
        protected HammerRecipe createHammerRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount) {
            return new HammerRecipe(ingredient, result, resultAmount);
        }
    }
}
