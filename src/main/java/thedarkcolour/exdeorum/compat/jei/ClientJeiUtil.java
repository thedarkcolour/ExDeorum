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

package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;

import java.util.List;
import java.util.function.Consumer;

class ClientJeiUtil {
    // todo is this still needed? i'm not supporting REI anymore
    // Required due to broken JEI implementation in REI plugin compatibility
    static <T> void checkTypedIngredient(IIngredientManager manager, IIngredientType<T> ingredientType, @Nullable T uncheckedIngredient, Consumer<ITypedIngredient<T>> action) {
        if ((uncheckedIngredient instanceof ItemStack stack && !stack.isEmpty()) || (uncheckedIngredient instanceof FluidStack fluidStack && !fluidStack.isEmpty())) {
            manager.createTypedIngredient(ingredientType, uncheckedIngredient, false).ifPresent(action);
        }
    }

    static <T> void showRecipes(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().gui.screen() instanceof IRecipesGui recipesGui) {
            recipesGui.show(focusFactory.createFocus(RecipeIngredientRole.OUTPUT, ingredient));
        }
    }

    static <T> void showUsages(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().gui.screen() instanceof IRecipesGui recipesGui) {
            // input + catalyst
            recipesGui.show(List.of(focusFactory.createFocus(RecipeIngredientRole.INPUT, ingredient), focusFactory.createFocus(RecipeIngredientRole.CRAFTING_STATION, ingredient)));
        }
    }

    enum AsteriskItemRenderer implements IIngredientRenderer<ItemStack> {
        INSTANCE;

        @Override
        public void render(GuiGraphicsExtractor graphics, @Nullable ItemStack ingredient) {
            if (ingredient != null) {
                // From mezz.jei.library.render.ItemStackRenderer.render
                ClientXeiUtil.renderItemWithAsterisk(graphics, ingredient);
            }
        }

        @Override
        public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
            // Copied from ItemStackRenderer
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            return ingredient.getTooltipLines(Item.TooltipContext.EMPTY, player, tooltipFlag);
        }
    }
}
