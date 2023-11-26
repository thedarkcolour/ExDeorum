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

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.List;

class CrucibleHeatSourcesCategory implements IRecipeCategory<Object2IntMap.Entry<Block>> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 48;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IFocusFactory focusFactory;

    public CrucibleHeatSourcesCategory(IGuiHelper helper, IFocusFactory focusFactory) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.title = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_TITLE);
        this.icon = helper.createDrawableItemStack(new ItemStack(EItems.PORCELAIN_CRUCIBLE.get()));
        this.focusFactory = focusFactory;
    }

    @Override
    public RecipeType<Object2IntMap.Entry<Block>> getRecipeType() {
        return ExDeorumJeiPlugin.CRUCIBLE_HEAT_SOURCES;
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
    public void setRecipe(IRecipeLayoutBuilder builder, Object2IntMap.Entry<Block> recipe, IFocusGroup focuses) {
        var itemForm = recipe.getKey().asItem();

        if (itemForm != Items.AIR) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(itemForm));
        }
    }

    @Override
    public void draw(Object2IntMap.Entry<Block> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        var volume = recipe.getIntValue();
        var volumeLabel = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_MULTIPLIER, volume);
        var font = Minecraft.getInstance().font;

        graphics.drawString(font, volumeLabel, 60 - font.width(volumeLabel) / 2, 5, 0xff808080, false);

        ClientJeiUtil.renderBlock(graphics, recipe.getKey().defaultBlockState(), 60, 24, 10, 20F);
    }

    @Override
    public List<Component> getTooltipStrings(Object2IntMap.Entry<Block> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
            return new ItemStack(recipe.getKey()).getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
        }
        return List.of();
    }

    @Override
    public boolean handleInput(Object2IntMap.Entry<Block> recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() == InputConstants.Type.MOUSE && (input.getValue() == InputConstants.MOUSE_BUTTON_LEFT || input.getValue() == InputConstants.MOUSE_BUTTON_RIGHT)) {
            if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
                var itemForm = recipe.getKey().asItem();

                if (itemForm != Items.AIR && Minecraft.getInstance().screen instanceof IRecipesGui recipesGui) {
                    recipesGui.show(focusFactory.createFocus(input.getValue() == InputConstants.MOUSE_BUTTON_LEFT ? RecipeIngredientRole.INPUT : RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, new ItemStack(itemForm)));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getHeight() {
        return 48;
    }
}
