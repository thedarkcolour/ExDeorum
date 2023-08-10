/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.registry.EBlocks;

public class BarrelMixingCategory implements IRecipeCategory<BarrelMixingRecipe> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 18;

    private final IDrawable background;
    private final IDrawable slot;
    private final IDrawable plus;
    private final IDrawable arrow;
    private final IDrawable icon;
    private final Component title;

    public BarrelMixingCategory(IGuiHelper helper, IDrawable plus, IDrawable arrow) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.slot = helper.getSlotDrawable();
        this.plus = plus;
        this.arrow = arrow;
        this.icon = helper.createDrawableItemStack(new ItemStack(EBlocks.STONE_BARREL.get()));
        this.title = Component.translatable(TranslationKeys.BARREL_MIXING_CATEGORY_TITLE);
    }

    @Override
    public RecipeType<BarrelMixingRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.BARREL_MIXING;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BarrelMixingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 33, 1).addFluidStack(recipe.fluid, recipe.fluidAmount).setFluidRenderer(Math.max(1000, recipe.fluidAmount), false, 16, 16);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 1).addItemStack(new ItemStack(recipe.result));
    }

    @Override
    public void draw(BarrelMixingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        slot.draw(graphics);
        plus.draw(graphics, 21, 5);
        slot.draw(graphics, 18 + 3 + 3 + 8, 0);
        arrow.draw(graphics, 53, 1);
        slot.draw(graphics, 78, 0);
    }
}
