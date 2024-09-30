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

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.mutable.MutableInt;
import thedarkcolour.exdeorum.compat.XeiSieveRecipe;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

class SieveCategory implements IRecipeCategory<XeiSieveRecipe> {
    private final Lazy<IDrawable> background;
    private final IDrawable slot;
    private final IDrawable row;
    private final IDrawable icon;
    private final Component title;
    private final MutableInt rows;

    // Common constructor
    SieveCategory(IGuiHelper helper, ItemLike icon, Component title, MutableInt rows) {
        this.background = Lazy.of(() -> helper.createBlankDrawable(XeiUtil.SIEVE_WIDTH, XeiUtil.SIEVE_ROW_START + XeiUtil.SIEVE_ROW_HEIGHT * rows.intValue()));
        this.slot = helper.getSlotDrawable();
        this.row = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 0, 0, 162, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(icon));
        this.title = title;
        this.rows = rows;
    }

    // Regular sieve
    SieveCategory(IGuiHelper helper) {
        this(helper, DefaultMaterials.OAK_SIEVE, Component.translatable(TranslationKeys.SIEVE_CATEGORY_TITLE), XeiSieveRecipe.SIEVE_ROWS);
    }

    @Override
    public RecipeType<XeiSieveRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.SIEVE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background.get();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, XeiSieveRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 87, 1).addItemStack(recipe.mesh());

        for (int i = 0; i < recipe.results().size(); i++) {
            var result = recipe.results().get(i);
            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + (i % 9) * 18, 1 + XeiUtil.SIEVE_ROW_START + 18 * (i / 9)).addItemStack(result.item);

            addTooltips(slot, result.byHandOnly, result.provider);
        }
    }

    public static void addTooltips(IRecipeSlotBuilder slot, boolean byHandOnly, NumberProvider provider) {
        if (byHandOnly) {
            slot.setCustomRenderer(VanillaTypes.ITEM_STACK, ClientJeiUtil.AsteriskItemRenderer.INSTANCE);
        }

        slot.addTooltipCallback((slotView, tooltip) -> {
            XeiUtil.addSieveDropTooltip(byHandOnly, provider, tooltip::add);
        });
    }

    @Override
    public void draw(XeiSieveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.slot.draw(graphics, 58, 0);
        this.slot.draw(graphics, 86, 0);

        int rows = this.rows.intValue();

        for (int i = 0; i < rows; i++) {
            this.row.draw(graphics, 0, 28 + i * 18);
        }
    }
}
