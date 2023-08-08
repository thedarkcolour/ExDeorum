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
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

abstract class OneToOneCategory<T> implements IRecipeCategory<T> {
    public static final int WIDTH = 72;
    public static final int HEIGHT = 18;

    private final IDrawable background;
    private final IDrawable arrow;
    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;

    public OneToOneCategory(IGuiHelper helper, IDrawable arrow, IDrawable icon, Component title) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.arrow = arrow;
        this.icon = icon;
        this.slot = helper.getSlotDrawable();
        this.title = title;
    }

    protected abstract void addInput(IRecipeSlotBuilder slot, T recipe);
    protected abstract void addOutput(IRecipeSlotBuilder slot, T recipe);

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        addInput(builder.addSlot(RecipeIngredientRole.INPUT, 1, 1), recipe);
        addOutput(builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1), recipe);
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
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        slot.draw(graphics);
        arrow.draw(graphics, 25, 1);
        slot.draw(graphics, 54, 0);
    }
}
