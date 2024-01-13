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

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.registry.EItems;

class HammerCategory extends OneToOneCategory<HammerRecipe> {
    public HammerCategory(IGuiHelper helper, IDrawable arrow) {
        super(helper, arrow, helper.createDrawableItemStack(new ItemStack(EItems.DIAMOND_HAMMER.get())), Component.translatable(TranslationKeys.HAMMER_CATEGORY_TITLE));
    }

    @Override
    public RecipeType<HammerRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.HAMMER;
    }

    @Override
    protected void addInput(IRecipeSlotBuilder slot, HammerRecipe recipe) {
        slot.addIngredients(recipe.getIngredient());
    }

    @Override
    protected void addOutput(IRecipeSlotBuilder slot, HammerRecipe recipe) {
        slot.addItemStack(new ItemStack(recipe.result));
        SieveCategory.addTooltips(slot, false, recipe.resultAmount);
    }
}
