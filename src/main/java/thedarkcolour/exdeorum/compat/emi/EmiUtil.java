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

package thedarkcolour.exdeorum.compat.emi;

import com.google.common.collect.ImmutableList;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

class EmiUtil {
    // Returns a list with 1 element.
    static List<EmiIngredient> inputs(SingleIngredientRecipe recipe) {
        return ImmutableList.of(EmiIngredient.of(recipe.ingredient));
    }

    static List<EmiIngredient> inputs(BlockPredicate predicate) {
        if (predicate instanceof BlockPredicate.SingleBlockPredicate block) {
            Item item = block.block().asItem();

            if (item != Items.AIR) {
                return ImmutableList.of(EmiStack.of(item));
            }
        } else {
            ImmutableList.Builder<EmiIngredient> builder = ImmutableList.builder();
            var items = new HashSet<Item>();

            predicate.possibleStates().forEach(state -> {
                Item item = state.getBlock().asItem();

                if (item != Items.AIR) {
                    if (items.add(item)) {
                        builder.add(EmiStack.of(item));
                    }
                }
            });

            // Need to wrap list in an ingredient to get OR behavior instead of AND behavior
            return ImmutableList.of(EmiIngredient.of(builder.build()));
        }

        return ImmutableList.of();
    }

    public static <C extends RecipeInput, R extends Recipe<C>> void addAll(EmiRegistry registry, Supplier<RecipeType<R>> type, BiFunction<R, ResourceLocation, ? extends EmiRecipe> factory) {
        for (var holder : registry.getRecipeManager().byType(type.get())) {
            registry.addRecipe(factory.apply(holder.value(), holder.id()));
        }
    }

    public static List<EmiStack> outputs(ItemStack result) {
        return ImmutableList.of(EmiStack.of(result));
    }

    public static List<EmiStack> outputs(FluidStack stack) {
        return ImmutableList.of(NeoForgeEmiStack.of(new FluidStack(stack.getFluid(), stack.getAmount())));
    }
}
