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

package thedarkcolour.exdeorum.recipe.crucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.function.BiFunction;

public abstract class CrucibleRecipe extends SingleIngredientRecipe {
    private final FluidStack result;

    protected CrucibleRecipe(Ingredient ingredient, FluidStack result) {
        super(ingredient);
        this.result = result;

        if (this.dependsOnNbt) {
            throw new IllegalArgumentException("Cannot use NBT to determine Ex Deorum Crucible output");
        }
    }

    public FluidStack getResult() {
        return this.result;
    }

    private static <R extends CrucibleRecipe> Codec<R> codec(BiFunction<Ingredient, FluidStack, R> factory) {
        return RecordCodecBuilder.create(instance -> instance.group(
                CodecUtil.ingredientField(),
                CodecUtil.FLUIDSTACK_CODEC.fieldOf("result").forGetter(CrucibleRecipe::getResult)
        ).apply(instance, factory));
    }

    public static class Lava extends CrucibleRecipe {
        public static final Codec<Lava> CODEC = codec(Lava::new);

        public Lava(Ingredient ingredient, FluidStack result) {
            super(ingredient, result);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ERecipeSerializers.LAVA_CRUCIBLE.get();
        }

        @Override
        public RecipeType<?> getType() {
            return ERecipeTypes.LAVA_CRUCIBLE.get();
        }
    }

    public static class Water extends CrucibleRecipe {
        public static final Codec<Water> CODEC = codec(Water::new);

        public Water(Ingredient ingredient, FluidStack result) {
            super(ingredient, result);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ERecipeSerializers.WATER_CRUCIBLE.get();
        }

        @Override
        public RecipeType<?> getType() {
            return ERecipeTypes.WATER_CRUCIBLE.get();
        }
    }

    public record Serializer<R extends CrucibleRecipe>(Codec<R> recipeCodec, BiFunction<Ingredient, FluidStack, R> factory) implements RecipeSerializer<R> {
        @Override
        public Codec<R> codec() {
            return this.recipeCodec;
        }

        @Override
        public R fromNetwork(FriendlyByteBuf buffer) {
            return this.factory.apply(Ingredient.fromNetwork(buffer), FluidStack.readFromPacket(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            recipe.getResult().writeToPacket(buffer);
        }
    }
}
