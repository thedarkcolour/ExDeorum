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

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Function5;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class SieveRecipe extends ProbabilityRecipe {
    public static final MapCodec<SieveRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> commonSieveFields(instance).apply(instance, SieveRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SieveRecipe> STREAM_CODEC = sieveStreamCodec(SieveRecipe::new);

    static <T extends SieveRecipe> StreamCodec<RegistryFriendlyByteBuf, T> sieveStreamCodec(Function5<Ingredient, ItemStack, NumberProvider, Ingredient, Boolean, T> factory) {
        return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, T::ingredient,
                ItemStack.STREAM_CODEC, T::result,
                CodecUtil.NUMBER_PROVIDER_CODEC, T::resultAmount,
                Ingredient.CONTENTS_STREAM_CODEC, T::mesh,
                ByteBufCodecs.BOOL, T::byHandOnly,
                factory
        );
    }

    protected static <T extends SieveRecipe> Products.P5<RecordCodecBuilder.Mu<T>, Ingredient, ItemStack, NumberProvider, Ingredient, Boolean> commonSieveFields(RecordCodecBuilder.Instance<T> instance) {
        return commonFields(instance).and(
                instance.group(
                        Ingredient.CODEC.fieldOf("mesh").forGetter(SieveRecipe::mesh),
                        Codec.BOOL.optionalFieldOf("by_hand_only", false).forGetter(SieveRecipe::byHandOnly)
                ));
    }

    public final Ingredient mesh;
    public final boolean byHandOnly;

    public SieveRecipe(Ingredient ingredient, ItemStack result, NumberProvider resultAmount, Ingredient mesh, boolean byHandOnly) {
        super(ingredient, result, resultAmount);

        this.mesh = mesh;
        this.byHandOnly = byHandOnly;
    }

    public Ingredient mesh() {
        return this.mesh;
    }

    public boolean byHandOnly() {
        return this.byHandOnly;
    }

    @Override
    public RecipeSerializer<SieveRecipe> getSerializer() {
        return ERecipeSerializers.SIEVE.get();
    }

    @Override
    public RecipeType<SieveRecipe> getType() {
        return ERecipeTypes.SIEVE.get();
    }

}
