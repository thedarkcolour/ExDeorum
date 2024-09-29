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

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.jeicompat.JEIPluginDetector;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ClientJeiUtil {
    // Required due to broken JEI implementation in REI plugin compatibility
    public static <T> void checkTypedIngredient(IIngredientManager manager, IIngredientType<T> ingredientType, T uncheckedIngredient, Consumer<ITypedIngredient<T>> action) {
        if ((uncheckedIngredient instanceof ItemStack stack && !stack.isEmpty()) || (uncheckedIngredient instanceof FluidStack fluidStack && !fluidStack.isEmpty())) {
            manager.createTypedIngredient(ingredientType, uncheckedIngredient).ifPresent(action);
        }
    }

    public static <T> void showRecipes(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().screen instanceof IRecipesGui recipesGui) {
            recipesGui.show(focusFactory.createFocus(RecipeIngredientRole.OUTPUT, ingredient));
        } else if (ModList.get().isLoaded(ModIds.REI_PC)) {
            ViewSearchBuilder.builder().addRecipesFor(JEIPluginDetector.unwrapStack(ingredient)).open();
        }
    }

    public static <T> void showUsages(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().screen instanceof IRecipesGui recipesGui) {
            // input + catalyst
            recipesGui.show(List.of(focusFactory.createFocus(RecipeIngredientRole.INPUT, ingredient), focusFactory.createFocus(RecipeIngredientRole.CATALYST, ingredient)));
        } else if (ModList.get().isLoaded(ModIds.REI_PC)) {
            ViewSearchBuilder.builder().addUsagesFor(JEIPluginDetector.unwrapStack(ingredient)).open();
        }
    }

    enum AsteriskItemRenderer implements IIngredientRenderer<ItemStack> {
        INSTANCE;

        @Override
        public void render(GuiGraphics graphics, ItemStack ingredient) {
            // From mezz.jei.library.render.ItemStackRenderer
            RenderSystem.enableDepthTest();
            ClientXeiUtil.renderItemWithAsterisk(graphics, ingredient);
            // From end of DrawableIngredient
            RenderSystem.disableDepthTest();
        }

        @Override
        public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
            // Copied from ItemStackRenderer
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            try {
                return ingredient.getTooltipLines(player, tooltipFlag);
            } catch (RuntimeException | LinkageError e) {
                List<Component> list = new ArrayList<>();
                MutableComponent crash = Component.translatable("jei.tooltip.error.crash");
                list.add(crash.withStyle(ChatFormatting.RED));
                return list;
            }
        }
    }
}
