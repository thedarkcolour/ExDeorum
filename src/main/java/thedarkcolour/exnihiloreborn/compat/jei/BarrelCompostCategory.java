package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import thedarkcolour.exnihiloreborn.block.EBlock;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

import java.util.Arrays;

public class BarrelCompostCategory implements IRecipeCategory<BarrelCompostRecipe> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 18;

    private final IDrawable background;
    private final IDrawable slot;
    private final IDrawable icon;
    private final Component localizedName;

    public BarrelCompostCategory(IJeiHelpers helpers) {
        IGuiHelper guiHelpers = helpers.getGuiHelper();

        this.background = guiHelpers.createBlankDrawable(WIDTH, HEIGHT);
        this.slot = guiHelpers.getSlotDrawable();
        this.icon = guiHelpers.createDrawableItemStack(new ItemStack(EBlocks.OAK_BARREL.get()));
        this.localizedName = Component.translatable(TranslationKeys.BARREL_COMPOST_CATEGORY_TITLE);
    }

    @Override
    public RecipeType<BarrelCompostRecipe> getRecipeType() {
        return ExNihiloRebornJeiPlugin.BARREL_COMPOST;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BarrelCompostRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
    }

    @Override
    public void draw(BarrelCompostRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        slot.draw(graphics);

        var volume = recipe.getVolume();
        var volumeLabel = Component.translatable(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, volume);

        graphics.drawString(Minecraft.getInstance().font, volumeLabel, 24, 5, 0xff808080, false);
    }
}
