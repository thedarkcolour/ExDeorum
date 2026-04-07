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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class CompressedSieveRecipe extends SieveRecipe {
    public static final MapCodec<CompressedSieveRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> commonSieveFields(instance).apply(instance, CompressedSieveRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompressedSieveRecipe> STREAM_CODEC = sieveStreamCodec(CompressedSieveRecipe::new);

    public CompressedSieveRecipe(Ingredient ingredient, ItemStackTemplate result, NumberProvider resultAmount, Ingredient mesh, boolean byHandOnly) {
        super(ingredient, result, resultAmount, mesh, byHandOnly);
    }

    @Override
    public RecipeSerializer<CompressedSieveRecipe> getSerializer() {
        return ERecipeSerializers.COMPRESSED_SIEVE.get();
    }

    @Override
    public RecipeType<CompressedSieveRecipe> getType() {
        return ERecipeTypes.COMPRESSED_SIEVE.get();
    }

}
