package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;
import thedarkcolour.exnihiloreborn.recipe.hammer.HammerRecipe;
import thedarkcolour.exnihiloreborn.registry.EItems;

class HammerCategory extends OneToOneCategory<HammerRecipe> {
    public HammerCategory(IGuiHelper helper, IDrawable arrow) {
        super(helper, arrow, helper.createDrawableItemStack(new ItemStack(EItems.DIAMOND_HAMMER.get())), Component.translatable(TranslationKeys.HAMMER_CATEGORY_TITLE));
    }

    @Override
    public RecipeType<HammerRecipe> getRecipeType() {
        return ExNihiloRebornJeiPlugin.HAMMER;
    }

    @Override
    protected void addInput(IRecipeSlotBuilder slot, HammerRecipe recipe) {
        slot.addIngredients(recipe.getIngredient());
    }

    @Override
    protected void addOutput(IRecipeSlotBuilder slot, HammerRecipe recipe) {
        slot.addItemStack(new ItemStack(recipe.result));
        SieveCategory.addTooltips(slot, recipe.resultAmount);
    }
}
