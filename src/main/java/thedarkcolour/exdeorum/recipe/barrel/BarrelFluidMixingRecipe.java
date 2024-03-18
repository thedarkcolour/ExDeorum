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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

// A recipe where two fluids are mixed together, rather than a fluid and an item.
// The additive must be 1000mB or a source block worth of liquid.
// The additive is not consumed, however. Additives placed in the world are not consumed,
// so it would be unfair to consume the handheld additive.
public record BarrelFluidMixingRecipe(
        Fluid baseFluid,
        int baseFluidAmount,
        Fluid additiveFluid,
        Item result,
        boolean consumesAdditive
) implements Recipe<Container> {
    public static final Codec<BarrelFluidMixingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtil.fluidField("base_fluid", BarrelFluidMixingRecipe::baseFluid),
            Codec.INT.fieldOf("base_fluid_amount").forGetter(BarrelFluidMixingRecipe::baseFluidAmount),
            CodecUtil.fluidField("additive_fluid", BarrelFluidMixingRecipe::baseFluid),
            CodecUtil.itemField("result", BarrelFluidMixingRecipe::result),
            Codec.BOOL.optionalFieldOf("base_fluid", false).forGetter(BarrelFluidMixingRecipe::consumesAdditive)
    ).apply(instance, BarrelFluidMixingRecipe::new));

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(this.result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_FLUID_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_FLUID_MIXING.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelFluidMixingRecipe> {
        @Override
        public Codec<BarrelFluidMixingRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelFluidMixingRecipe recipe) {
            buffer.writeId(BuiltInRegistries.FLUID, recipe.baseFluid);
            buffer.writeVarInt(recipe.baseFluidAmount);
            buffer.writeId(BuiltInRegistries.FLUID, recipe.additiveFluid);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            buffer.writeBoolean(recipe.consumesAdditive);
        }

        @Override
        public BarrelFluidMixingRecipe fromNetwork(FriendlyByteBuf buffer) {
            Fluid baseFluid = Objects.requireNonNull(buffer.readById(BuiltInRegistries.FLUID));
            int baseFluidAmount = buffer.readVarInt();
            Fluid additiveFluid = Objects.requireNonNull(buffer.readById(BuiltInRegistries.FLUID));
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            boolean consumesAdditive = buffer.readBoolean();

            return new BarrelFluidMixingRecipe(baseFluid, baseFluidAmount, additiveFluid, result, consumesAdditive);
        }
    }
}
