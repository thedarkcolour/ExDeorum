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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public class BarrelMixingRecipe extends SingleIngredientRecipe {
    public static final Codec<BarrelMixingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtil.ingredientField(),
            CodecUtil.fluidField("fluid", BarrelMixingRecipe::getFluid),
            Codec.INT.fieldOf("fluid_amount").forGetter(BarrelMixingRecipe::getFluidAmount),
            CodecUtil.itemField("result", BarrelMixingRecipe::getResult)
    ).apply(instance, BarrelMixingRecipe::new));

    public final Fluid fluid;
    public final int fluidAmount;
    public final Item result;

    public BarrelMixingRecipe(Ingredient ingredient, Fluid fluid, int fluidAmount, Item result) {
        super(ingredient);
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.result = result;
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public Item getResult() {
        return this.result;
    }

    // Do not use
    @Override
    @Deprecated
    public boolean matches(Container inventory, Level level) {
        return false;
    }

    public boolean matches(ItemStack item, FluidStack fluid) {
        return this.ingredient.test(item) && fluid.getFluid() == this.fluid && fluid.getAmount() >= this.fluidAmount;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(this.result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_MIXING.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelMixingRecipe> {
        @Override
        public Codec<BarrelMixingRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelMixingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.FLUID, recipe.fluid);
            buffer.writeVarInt(recipe.fluidAmount);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
        }

        @Override
        public BarrelMixingRecipe fromNetwork(FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Fluid fluid = Objects.requireNonNull(buffer.readById(BuiltInRegistries.FLUID));
            int fluidAmount = buffer.readVarInt();
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));

            return new BarrelMixingRecipe(ingredient, fluid, fluidAmount, result);
        }
    }
}
