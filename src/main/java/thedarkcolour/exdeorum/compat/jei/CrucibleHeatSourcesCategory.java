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

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.List;

class CrucibleHeatSourcesCategory implements IRecipeCategory<CrucibleHeatSourceRecipe> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 48;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    private final IFocusFactory focusFactory;
    private final IIngredientManager ingredientManager;
    private final IModIdHelper modIdHelper;

    public CrucibleHeatSourcesCategory(IJeiHelpers helpers) {
        var helper = helpers.getGuiHelper();
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.title = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_TITLE);
        this.icon = helper.createDrawableItemStack(new ItemStack(EItems.PORCELAIN_CRUCIBLE.get()));

        this.focusFactory = helpers.getFocusFactory();
        this.ingredientManager = helpers.getIngredientManager();
        this.modIdHelper = helpers.getModIdHelper();
    }

    @Override
    public RecipeType<CrucibleHeatSourceRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleHeatSourceRecipe recipe, IFocusGroup focuses) {
        if (recipe.ingredientType() != null) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredient(recipe.ingredientType(), recipe.ingredient());
        } else {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredient(VanillaTypes.ITEM_STACK, ItemStack.EMPTY);
        }
    }

    @Override
    public void draw(CrucibleHeatSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        var volume = recipe.meltRate();
        var volumeLabel = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_MULTIPLIER, volume);
        var font = Minecraft.getInstance().font;

        graphics.drawString(font, volumeLabel, 60 - font.width(volumeLabel) / 2, 5, 0xff808080, false);

        ClientJeiUtil.renderBlock(graphics, recipe.blockState(), 60, 24, 10, 20F, (block, poseStack, buffers) -> {
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block, poseStack, buffers, 15728880, OverlayTexture.NO_OVERLAY);
        });
    }

    @Override
    public List<Component> getTooltipStrings(CrucibleHeatSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
            if (recipe.ingredientType() != null) {
                var tooltip = this.ingredientManager.getIngredientRenderer(recipe.ingredientType()).getTooltip(recipe.ingredient(), Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
                return this.modIdHelper.addModNameToIngredientTooltip(tooltip, recipe.ingredient(), this.ingredientManager.getIngredientHelper(recipe.ingredientType()));
            } else {
                var block = recipe.blockState().getBlock();
                var modId = ForgeRegistries.BLOCKS.getKey(block).getNamespace();
                return List.of(Component.translatable(block.getDescriptionId()), Component.literal(this.modIdHelper.getFormattedModNameForModId(modId)));
            }
        }

        return List.of();
    }

    @Override
    public boolean handleInput(CrucibleHeatSourceRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() == InputConstants.Type.MOUSE && (input.getValue() == InputConstants.MOUSE_BUTTON_LEFT || input.getValue() == InputConstants.MOUSE_BUTTON_RIGHT)) {
            if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
                if (recipe.ingredientType() != null) {
                    ClientJeiUtil.checkTypedIngredient(this.ingredientManager, recipe.ingredientType(), recipe.ingredient(), ingredient -> {
                        if (input.getValue() == InputConstants.MOUSE_BUTTON_LEFT) {
                            ClientJeiUtil.showRecipes(this.focusFactory, ingredient);
                        } else if (input.getValue() == InputConstants.MOUSE_BUTTON_RIGHT) {
                            ClientJeiUtil.showUsages(this.focusFactory, ingredient);
                        }
                    });
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public int getHeight() {
        return 48;
    }
}
