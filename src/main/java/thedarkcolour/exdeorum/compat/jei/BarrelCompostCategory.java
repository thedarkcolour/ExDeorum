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
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.client.ClientHandler;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;

class BarrelCompostCategory implements IRecipeCategory<BarrelCompostRecipe> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 18;

    private final IDrawable background;
    private final IDrawable slot;
    private final IDrawable icon;
    private final Component title;

    public BarrelCompostCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.slot = helper.getSlotDrawable();
        this.icon = new DrawableIcon();
        this.title = Component.translatable(TranslationKeys.BARREL_COMPOST_CATEGORY_TITLE);
    }

    @Override
    public RecipeType<BarrelCompostRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.BARREL_COMPOST;
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
    public void setRecipe(IRecipeLayoutBuilder builder, BarrelCompostRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
    }

    @Override
    public void draw(BarrelCompostRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.slot.draw(graphics);

        var volume = recipe.getVolume();
        var volumeLabel = Component.translatable(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, volume);

        graphics.drawString(Minecraft.getInstance().font, volumeLabel, 24, 5, 0xff808080, false);
    }

    private static class DrawableIcon implements IDrawable {
        private final ItemStack oakBarrel = new ItemStack(DefaultMaterials.OAK_BARREL.getItem());

        @Override
        public int getWidth() {
            return 16;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            // From mezz.jei.library.render.ItemStackRenderer
            RenderSystem.enableDepthTest();

            Minecraft mc = Minecraft.getInstance();
            var model = mc.getModelManager().getModel(ClientHandler.OAK_BARREL_COMPOSTING);
            // From GuiGraphics.renderFakeItem
            ClientJeiUtil.renderItemAlternativeModel(guiGraphics, model, this.oakBarrel, xOffset, yOffset);
            // From end of DrawableIngredient
            RenderSystem.disableDepthTest();
        }
    }
}
