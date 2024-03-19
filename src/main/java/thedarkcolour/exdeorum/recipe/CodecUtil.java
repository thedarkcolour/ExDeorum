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

package thedarkcolour.exdeorum.recipe;

import com.google.gson.JsonElement;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CodecUtil {
    public static final Codec<FluidStack> FLUIDSTACK_CODEC = new FluidStackCodec();
    private static final Codec<FluidStack> ALTERNATIVE_FLUIDSTACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            fluidField("fluid", FluidStack::getFluid),
            Codec.INT.fieldOf("amount").forGetter(FluidStack::getAmount),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(stack -> Optional.ofNullable(stack.getTag()))
    ).apply(instance, (fluid, amount, optionalTag) -> {
        var stack = new FluidStack(fluid, amount);
        optionalTag.ifPresent(stack::setTag);
        return stack;
    }));

    public static <T extends SingleIngredientRecipe> App<RecordCodecBuilder.Mu<T>, Ingredient> ingredientField() {
        return Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SingleIngredientRecipe::getIngredient);
    }

    public static <T> App<RecordCodecBuilder.Mu<T>, Item> itemField(String name, Function<T, Item> getter) {
        return BuiltInRegistries.ITEM.byNameCodec().fieldOf(name).forGetter(getter);
    }

    public static <T> App<RecordCodecBuilder.Mu<T>, Block> blockField(String name, Function<T, Block> getter) {
        return BuiltInRegistries.BLOCK.byNameCodec().fieldOf(name).forGetter(getter);
    }

    public static <T> App<RecordCodecBuilder.Mu<T>, Fluid> fluidField(String name, Function<T, Fluid> getter) {
        return BuiltInRegistries.FLUID.byNameCodec().fieldOf(name).forGetter(getter);
    }

    public static <T> JsonElement encode(Codec<T> codec, T object) {
        return codec.encodeStart(JsonOps.INSTANCE, object).result().get();
    }

    public static <T> T decode(Codec<T> codec, JsonElement json) {
        return codec.parse(JsonOps.INSTANCE, json).result().get();
    }

    public static <T, U extends T, I> DataResult<Pair<T, I>> cast(DataResult<Pair<U, I>> result) {
        return result.map(pair -> pair.mapFirst(Function.identity()));
    }

    private static class FluidStackCodec implements Codec<FluidStack> {
        @Override
        public <T> DataResult<Pair<FluidStack, T>> decode(DynamicOps<T> ops, T input) {
            var r1 = ALTERNATIVE_FLUIDSTACK_CODEC.decode(ops, input);
            if (r1.error().isEmpty()) {
                return r1;
            }
            var r2 = FluidStack.CODEC.decode(ops, input);
            if (r2.error().isEmpty()) {
                return r2;
            }
            return r1;
        }

        @Override
        public <T> DataResult<T> encode(FluidStack input, DynamicOps<T> ops, T prefix) {
            return ALTERNATIVE_FLUIDSTACK_CODEC.encode(input, ops, prefix);
        }
    }
}
