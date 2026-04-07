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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class HammerRecipe extends ProbabilityRecipe {
    public static final MapCodec<HammerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> ProbabilityRecipe.commonFields(instance).apply(instance, HammerRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, HammerRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, HammerRecipe::ingredient,
            ItemStackTemplate.STREAM_CODEC, HammerRecipe::result,
            CodecUtil.NUMBER_PROVIDER_CODEC, HammerRecipe::resultAmount,
            HammerRecipe::new);

    public HammerRecipe(Ingredient ingredient, ItemStackTemplate result, NumberProvider resultAmount) {
        super(ingredient, result, resultAmount);
    }

    @Override
    public RecipeSerializer<? extends HammerRecipe> getSerializer() {
        return ERecipeSerializers.HAMMER.get();
    }

    @Override
    public RecipeType<? extends HammerRecipe> getType() {
        return ERecipeTypes.HAMMER.get();
    }

}
