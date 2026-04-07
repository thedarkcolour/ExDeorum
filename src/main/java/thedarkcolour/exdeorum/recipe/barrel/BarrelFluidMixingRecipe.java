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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

// A recipe where two fluids are mixed together, rather than a fluid and an item.
// The additive must be 1000mB or a source block worth of liquid.
// The additive is not consumed, however. Additives placed in the world are not consumed,
// so it would be unfair to consume the handheld additive.
public record BarrelFluidMixingRecipe(
        SizedFluidIngredient baseFluid,
        FluidIngredient additiveFluid,
        ItemStackTemplate result,
        boolean consumesAdditive
) implements Recipe<RecipeInput> {
    public static final MapCodec<BarrelFluidMixingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.CODEC.fieldOf("base_fluid").forGetter(BarrelFluidMixingRecipe::baseFluid),
            FluidIngredient.CODEC.fieldOf("additive_fluid").forGetter(BarrelFluidMixingRecipe::additiveFluid),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(BarrelFluidMixingRecipe::result),
            Codec.BOOL.optionalFieldOf("consumes_additive", false).forGetter(BarrelFluidMixingRecipe::consumesAdditive)
    ).apply(instance, BarrelFluidMixingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BarrelFluidMixingRecipe> STREAM_CODEC = StreamCodec.of(BarrelFluidMixingRecipe::toNetwork, BarrelFluidMixingRecipe::fromNetwork);

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<BarrelFluidMixingRecipe> getSerializer() {
        return ERecipeSerializers.BARREL_FLUID_MIXING.get();
    }

    @Override
    public RecipeType<BarrelFluidMixingRecipe> getType() {
        return ERecipeTypes.BARREL_FLUID_MIXING.get();
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, BarrelFluidMixingRecipe recipe) {
        SizedFluidIngredient.STREAM_CODEC.encode(buffer, recipe.baseFluid);
        FluidIngredient.STREAM_CODEC.encode(buffer, recipe.additiveFluid);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeBoolean(recipe.consumesAdditive);
    }

    public static BarrelFluidMixingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        SizedFluidIngredient baseFluid = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
        FluidIngredient additiveFluid = FluidIngredient.STREAM_CODEC.decode(buffer);
        ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        boolean consumesAdditive = buffer.readBoolean();

        return new BarrelFluidMixingRecipe(baseFluid, additiveFluid, result, consumesAdditive);
    }

}
