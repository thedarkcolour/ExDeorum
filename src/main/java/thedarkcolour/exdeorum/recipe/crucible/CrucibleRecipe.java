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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.function.BiFunction;

public abstract class CrucibleRecipe extends SingleIngredientRecipe {
    private final FluidStackTemplate result;

    protected CrucibleRecipe(Ingredient ingredient, FluidStackTemplate result) {
        super(ingredient);
        this.result = result;

        if (this.dependsOnNbt) {
            throw new IllegalArgumentException("Cannot use NBT to determine Ex Deorum Crucible output");
        }
    }

    public FluidStackTemplate getResult() {
        return this.result;
    }

    public FluidStack createResult() {
        return this.result.create();
    }

    private static <R extends CrucibleRecipe> MapCodec<R> mapCodec(BiFunction<Ingredient, FluidStackTemplate, R> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                CodecUtil.ingredientField(),
                FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(CrucibleRecipe::getResult)
        ).apply(instance, factory));
    }

    private static <R extends CrucibleRecipe> StreamCodec<RegistryFriendlyByteBuf, R> streamCodec(BiFunction<Ingredient, FluidStackTemplate, R> factory) {
        return StreamCodec.of(
                CrucibleRecipe::toNetwork,
                buffer -> factory.apply(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), FluidStackTemplate.STREAM_CODEC.decode(buffer))
        );
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, CrucibleRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
        FluidStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
    }

    public static class Lava extends CrucibleRecipe {
        public static final MapCodec<Lava> CODEC = mapCodec(Lava::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, Lava> STREAM_CODEC = streamCodec(Lava::new);

        public Lava(Ingredient ingredient, FluidStackTemplate result) {
            super(ingredient, result);
        }

        @Override
        public RecipeSerializer<Lava> getSerializer() {
            return ERecipeSerializers.LAVA_CRUCIBLE.get();
        }

        @Override
        public RecipeType<CrucibleRecipe> getType() {
            return ERecipeTypes.LAVA_CRUCIBLE.get();
        }
    }

    public static class Water extends CrucibleRecipe {
        public static final MapCodec<Water> CODEC = mapCodec(Water::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, Water> STREAM_CODEC = streamCodec(Water::new);

        public Water(Ingredient ingredient, FluidStackTemplate result) {
            super(ingredient, result);
        }

        @Override
        public RecipeSerializer<Water> getSerializer() {
            return ERecipeSerializers.WATER_CRUCIBLE.get();
        }

        @Override
        public RecipeType<CrucibleRecipe> getType() {
            return ERecipeTypes.WATER_CRUCIBLE.get();
        }
    }

}
