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

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;

abstract class CrucibleCategory extends OneToOneCategory<CrucibleRecipe> {
    public CrucibleCategory(IGuiHelper helper, IDrawable arrow, Item iconItem, String titleKey) {
        super(helper, arrow, helper.createDrawableItemStack(new ItemStack(iconItem)), Component.translatable(titleKey));
    }

    @Override
    protected void addInput(IRecipeSlotBuilder slot, CrucibleRecipe recipe) {
        slot.addIngredients(recipe.getIngredient());
    }

    @Override
    protected void addOutput(IRecipeSlotBuilder slot, CrucibleRecipe recipe) {
        slot.addFluidStack(recipe.getResult().getFluid(), recipe.getResult().getAmount())
                .setFluidRenderer(Math.max(1000, recipe.getResult().getAmount()), false, 16, 16);
    }

    static class LavaCrucible extends CrucibleCategory {
        public LavaCrucible(IGuiHelper helper, IDrawable arrow) {
            super(helper, arrow, DefaultMaterials.PORCELAIN_CRUCIBLE.getItem(), TranslationKeys.LAVA_CRUCIBLE_CATEGORY_TITLE);
        }

        @Override
        public RecipeType<CrucibleRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.LAVA_CRUCIBLE;
        }
    }

    static class WaterCrucible extends CrucibleCategory {
        public WaterCrucible(IGuiHelper helper, IDrawable arrow) {
            super(helper, arrow, DefaultMaterials.OAK_CRUCIBLE.getItem(), TranslationKeys.WATER_CRUCIBLE_CATEGORY_TITLE);
        }

        @Override
        public RecipeType<CrucibleRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.WATER_CRUCIBLE;
        }
    }
}
