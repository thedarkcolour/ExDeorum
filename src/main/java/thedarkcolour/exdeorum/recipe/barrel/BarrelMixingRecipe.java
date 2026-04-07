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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class BarrelMixingRecipe extends SingleIngredientRecipe {
    public static final MapCodec<BarrelMixingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.ingredientField(),
            SizedFluidIngredient.CODEC.fieldOf("fluid").forGetter(BarrelMixingRecipe::getFluid),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(BarrelMixingRecipe::getResult)
    ).apply(instance, BarrelMixingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BarrelMixingRecipe> STREAM_CODEC = StreamCodec.of(BarrelMixingRecipe::toNetwork, BarrelMixingRecipe::fromNetwork);

    public final SizedFluidIngredient fluid;
    public final ItemStackTemplate result;

    public BarrelMixingRecipe(Ingredient ingredient, SizedFluidIngredient fluid, ItemStackTemplate result) {
        super(ingredient);
        this.fluid = fluid;
        this.result = result;
    }

    public SizedFluidIngredient getFluid() {
        return this.fluid;
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }

    // Do not use
    @Override
    @Deprecated
    public boolean matches(RecipeInput inventory, Level level) {
        return false;
    }

    public boolean matches(ItemStack item, FluidStack fluid) {
        return this.ingredient.test(item) && this.fluid.test(fluid);
    }

    @Override
    public RecipeSerializer<BarrelMixingRecipe> getSerializer() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }

    @Override
    public RecipeType<BarrelMixingRecipe> getType() {
        return ERecipeTypes.BARREL_MIXING.get();
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, BarrelMixingRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
        SizedFluidIngredient.STREAM_CODEC.encode(buffer, recipe.fluid);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
    }

    public static BarrelMixingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
        ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buffer);

        return new BarrelMixingRecipe(ingredient, fluid, result);
    }

}
