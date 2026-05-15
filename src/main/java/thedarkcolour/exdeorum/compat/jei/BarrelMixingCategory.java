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

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;

public abstract class BarrelMixingCategory<T> implements IRecipeCategory<T> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 18;

    private final IDrawable slot;
    private final IDrawable plus;
    private final IDrawable arrow;
    private final IDrawable icon;
    private final Component title;

    public BarrelMixingCategory(IGuiHelper helper, IDrawable plus, IDrawable arrow, String titleKey, Item iconItem) {
        this.slot = helper.getSlotDrawable();
        this.plus = plus;
        this.arrow = arrow;
        this.icon = helper.createDrawableItemStack(new ItemStack(iconItem));
        this.title = Component.translatable(titleKey);
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.slot.draw(graphics);
        this.plus.draw(graphics, 21, 5);
        this.slot.draw(graphics, 18 + 3 + 3 + 8, 0);
        this.arrow.draw(graphics, 53, 1);
        this.slot.draw(graphics, 78, 0);
    }

    public static class Items extends BarrelMixingCategory<BarrelMixingRecipe> {
        public Items(IGuiHelper helper, IDrawable plus, IDrawable arrow) {
            super(helper, plus, arrow, TranslationKeys.BARREL_MIXING_CATEGORY_TITLE, DefaultMaterials.OAK_BARREL.getItem());
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, BarrelMixingRecipe recipe, IFocusGroup focuses) {
            JeiUtil.addFluidIngredient(builder.addSlot(RecipeIngredientRole.INPUT, 1, 1), recipe.fluid).setFluidRenderer(1000, false, 16, 16);
            builder.addSlot(RecipeIngredientRole.INPUT, 33, 1).add(recipe.ingredient());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 1).add(recipe.result.create());
        }

        @Override
        public IRecipeType<BarrelMixingRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.BARREL_MIXING;
        }
    }

    public static class Fluids extends BarrelMixingCategory<BarrelFluidMixingRecipe> {
        private static final Component CONTENTS_ARE_CONSUMED_TOOLTIP = Component.translatable(TranslationKeys.BARREL_FLUID_MIXING_CONTENTS_ARE_CONSUMED).withStyle(ChatFormatting.RED);

        public Fluids(IGuiHelper helper, IDrawable plus, IDrawable arrow) {
            super(helper, plus, arrow, TranslationKeys.BARREL_FLUID_MIXING_CATEGORY_TITLE, DefaultMaterials.STONE_BARREL.getItem());
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, BarrelFluidMixingRecipe recipe, IFocusGroup focuses) {
            JeiUtil.addFluidIngredient(builder.addSlot(RecipeIngredientRole.INPUT, 1, 1), recipe.baseFluid())
                    .setFluidRenderer(1000, false, 16, 16);
            var additiveSlot = JeiUtil.addFluidIngredient(builder.addSlot(RecipeIngredientRole.INPUT, 33, 1), recipe.additiveFluid(), 1000)
                    .setFluidRenderer(1000, false, 16, 16);
            if (recipe.consumesAdditive()) {
                additiveSlot.addRichTooltipCallback((_, tooltip) -> tooltip.add(CONTENTS_ARE_CONSUMED_TOOLTIP));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 1).add(recipe.result().create());
        }

        @Override
        public IRecipeType<BarrelFluidMixingRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.BARREL_FLUID_MIXING;
        }

        @Override
        public void draw(BarrelFluidMixingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
            super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);

            if (recipe.consumesAdditive()) {
                ClientXeiUtil.renderAsterisk(graphics, 18 + 3 + 3 + 8, 0);
            }
        }
    }
}
