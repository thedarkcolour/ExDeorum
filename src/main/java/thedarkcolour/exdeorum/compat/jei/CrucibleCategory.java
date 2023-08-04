package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.function.Supplier;

abstract class CrucibleCategory extends OneToOneCategory<CrucibleRecipe> {
    public CrucibleCategory(IGuiHelper helper, IDrawable arrow, Supplier<? extends Item> iconItem, String titleKey) {
        super(helper, arrow, helper.createDrawableItemStack(new ItemStack(iconItem.get())), Component.translatable(titleKey));
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
            super(helper, arrow, EItems.PORCELAIN_CRUCIBLE, TranslationKeys.LAVA_CRUCIBLE_CATEGORY_TITLE);
        }

        @Override
        public RecipeType<CrucibleRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.LAVA_CRUCIBLE;
        }
    }

    static class WaterCrucible extends CrucibleCategory {
        public WaterCrucible(IGuiHelper helper, IDrawable arrow) {
            super(helper, arrow, EItems.OAK_CRUCIBLE, TranslationKeys.WATER_CRUCIBLE_CATEGORY_TITLE);
        }

        @Override
        public RecipeType<CrucibleRecipe> getRecipeType() {
            return ExDeorumJeiPlugin.WATER_CRUCIBLE;
        }
    }
}
