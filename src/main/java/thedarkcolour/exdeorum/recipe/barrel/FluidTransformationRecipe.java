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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.WeightedList;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

// todo consider NBT tag of input fluid?
public record FluidTransformationRecipe(
        Fluid baseFluid,
        Fluid resultFluid,
        int resultColor,
        BlockPredicate catalyst,
        WeightedList<BlockState> byproducts,
        int duration
) implements Recipe<Container> {
    public static final Codec<FluidTransformationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtil.fluidField("base_fluid", FluidTransformationRecipe::baseFluid),
            CodecUtil.fluidField("result_fluid", FluidTransformationRecipe::resultFluid),
            Codec.INT.fieldOf("result_color").forGetter(FluidTransformationRecipe::resultColor),
            BlockPredicate.CODEC.fieldOf("catalyst").forGetter(FluidTransformationRecipe::catalyst),
            WeightedList.codec(Codec.STRING.xmap(RecipeUtil::parseBlockState, RecipeUtil::writeBlockState)).fieldOf("byproducts").forGetter(FluidTransformationRecipe::byproducts),
            Codec.INT.fieldOf("duration").forGetter(FluidTransformationRecipe::duration)
    ).apply(instance, FluidTransformationRecipe::new));

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
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_FLUID_TRANSFORMATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_FLUID_TRANSFORMATION.get();
    }

    public static class Serializer implements RecipeSerializer<FluidTransformationRecipe> {
        @Override
        public Codec<FluidTransformationRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FluidTransformationRecipe recipe) {
            buffer.writeId(BuiltInRegistries.FLUID, recipe.baseFluid);
            buffer.writeId(BuiltInRegistries.FLUID, recipe.resultFluid);
            buffer.writeInt(recipe.resultColor);
            recipe.catalyst.toNetwork(buffer);
            recipe.byproducts.toNetwork(buffer, (buf, state) -> buf.writeId(Block.BLOCK_STATE_REGISTRY, state));
            buffer.writeVarInt(recipe.duration);
        }

        @Override
        public FluidTransformationRecipe fromNetwork(FriendlyByteBuf buffer) {
            Fluid baseFluid = Objects.requireNonNull(buffer.readById(BuiltInRegistries.FLUID));
            Fluid resultFluid = Objects.requireNonNull(buffer.readById(BuiltInRegistries.FLUID));
            int resultColor = buffer.readInt();
            BlockPredicate catalyst = RecipeUtil.readBlockPredicateNetwork(buffer);
            WeightedList<BlockState> byproducts = WeightedList.fromNetwork(buffer, buf -> buf.readById(Block.BLOCK_STATE_REGISTRY));
            int duration = buffer.readVarInt();
            return new FluidTransformationRecipe(baseFluid, resultFluid, resultColor, catalyst, byproducts, duration);
        }
    }

}
