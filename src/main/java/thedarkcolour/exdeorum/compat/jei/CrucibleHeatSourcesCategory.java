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
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

class CrucibleHeatSourcesCategory implements IRecipeCategory<CrucibleHeatSourceRecipe> {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 48;
    private static final ScreenRectangle HEAT_SOURCE_AREA = new ScreenRectangle(44, 16, 32, 32);

    private final IDrawable icon;
    private final Component title;

    private final IFocusFactory focusFactory;
    private final IIngredientManager ingredientManager;
    private final IModIdHelper modIdHelper;

    public CrucibleHeatSourcesCategory(IJeiHelpers helpers) {
        var helper = helpers.getGuiHelper();
        this.title = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_TITLE);
        this.icon = helper.createDrawableItemStack(new ItemStack(DefaultMaterials.PORCELAIN_CRUCIBLE.getItem()));

        this.focusFactory = helpers.getFocusFactory();
        this.ingredientManager = helpers.getIngredientManager();
        this.modIdHelper = helpers.getModIdHelper();
    }

    @Override
    public IRecipeType<CrucibleHeatSourceRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.CRUCIBLE_HEAT_SOURCES;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleHeatSourceRecipe recipe, IFocusGroup focuses) {
        if (recipe.ingredientType() != null && recipe.ingredient() != null) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(recipe.ingredientType(), recipe.ingredient());
        } else {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(VanillaTypes.ITEM_STACK, ItemStack.EMPTY);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CrucibleHeatSourceRecipe recipe, IFocusGroup focuses) {
        builder.addInputHandler(new IJeiInputHandler() {
            @Override
            public ScreenRectangle getArea() {
                return HEAT_SOURCE_AREA;
            }

            @Override
            public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
                return CrucibleHeatSourcesCategory.this.handleHeatSourceInput(recipe, input);
            }
        });
    }

    @Override
    public void draw(CrucibleHeatSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        var volume = recipe.meltRate();
        var volumeLabel = Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_MULTIPLIER, volume);
        var font = Minecraft.getInstance().font;

        graphics.text(font, volumeLabel, 60 - font.width(volumeLabel) / 2, 5, 0xff808080, false);

        ClientXeiUtil.renderBlock(graphics, recipe.blockState(), 60, 24, 10, 20F);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CrucibleHeatSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
            if (recipe.ingredientType() != null && recipe.ingredient() != null) {
                var tooltipLines = this.ingredientManager.getIngredientRenderer(recipe.ingredientType()).getTooltip(recipe.ingredient(), Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
                tooltip.addAll(tooltipLines);
                this.ingredientManager.createTypedIngredient(recipe.ingredientType(), recipe.ingredient(), false)
                        .flatMap(this.modIdHelper::getModNameForTooltip)
                        .ifPresent(tooltip::add);
            } else {
                var block = recipe.blockState().getBlock();
                var modId = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
                tooltip.add(Component.translatable(block.getDescriptionId()));
                tooltip.add(Component.literal(this.modIdHelper.getFormattedModNameForModId(modId)));
            }
        }
    }

    private boolean handleHeatSourceInput(CrucibleHeatSourceRecipe recipe, IJeiUserInput input) {
        var key = input.getKey();
        if (key.getType() == InputConstants.Type.MOUSE && (key.getValue() == InputConstants.MOUSE_BUTTON_LEFT || key.getValue() == InputConstants.MOUSE_BUTTON_RIGHT)) {
            if (input.isSimulate()) {
                return true;
            }

            if (recipe.ingredientType() != null) {
                ClientJeiUtil.checkTypedIngredient(this.ingredientManager, recipe.ingredientType(), recipe.ingredient(), ingredient -> {
                    if (key.getValue() == InputConstants.MOUSE_BUTTON_LEFT) {
                        ClientJeiUtil.showRecipes(this.focusFactory, ingredient);
                    } else {
                        ClientJeiUtil.showUsages(this.focusFactory, ingredient);
                    }
                });
            }

            return true;
        }
        return false;
    }
}
