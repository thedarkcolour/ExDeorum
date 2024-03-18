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

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public class SieveRecipe extends ProbabilityRecipe {
    public static final Codec<SieveRecipe> CODEC = RecordCodecBuilder.create(instance -> commonFields(instance).and(
            instance.group(
                    CodecUtil.itemField("mesh", SieveRecipe::getMesh),
                    Codec.BOOL.optionalFieldOf("by_hand_only", false).forGetter(SieveRecipe::isByHandOnly)
            )).apply(instance, SieveRecipe::new));

    public final Item mesh;
    public final boolean byHandOnly;

    public SieveRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount, Item mesh, boolean byHandOnly) {
        super(ingredient, result, resultAmount);

        this.mesh = mesh;
        this.byHandOnly = byHandOnly;
    }

    public Item getMesh() {
        return this.mesh;
    }

    public boolean isByHandOnly() {
        return this.byHandOnly;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.SIEVE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.SIEVE.get();
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        @Override
        public Codec<SieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public SieveRecipe fromNetwork(FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item mesh = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return new SieveRecipe(ingredient, result, resultAmount, mesh, buffer.readBoolean());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SieveRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.mesh);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
            buffer.writeBoolean(recipe.byHandOnly);
        }
    }
}
