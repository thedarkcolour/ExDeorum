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

package thedarkcolour.exdeorum.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class BarrelCompostRecipe extends SingleIngredientRecipe {
    public static final MapCodec<BarrelCompostRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.ingredientField(),
            Codec.INT.fieldOf("volume").forGetter(BarrelCompostRecipe::getVolume)
    ).apply(instance, BarrelCompostRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BarrelCompostRecipe> STREAM_CODEC = StreamCodec.of(BarrelCompostRecipe::toNetwork, BarrelCompostRecipe::fromNetwork);

    private final int volume;

    public BarrelCompostRecipe(Ingredient ingredient, int volume) {
        super(ingredient);

        this.volume = volume;
    }

    public int getVolume() {
        return this.volume;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_COMPOST.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_COMPOST.get();
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, BarrelCompostRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
        buffer.writeVarInt(recipe.getVolume());
    }

    public static BarrelCompostRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int volume = buffer.readVarInt();

        return new BarrelCompostRecipe(ingredient, volume);
    }

    public static class Serializer implements RecipeSerializer<BarrelCompostRecipe> {
        @Override
        public MapCodec<BarrelCompostRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BarrelCompostRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
