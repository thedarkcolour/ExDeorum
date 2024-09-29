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
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

class CrookCategory implements IRecipeCategory<CrookJeiRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;
    private final Component title;

    private final IFocusFactory focusFactory;
    private final IIngredientManager ingredientManager;

    private final CycleTimer timer = new CycleTimer(0);

    CrookCategory(IJeiHelpers helpers, IDrawable arrow) {
        var helper = helpers.getGuiHelper();
        this.background = helper.createBlankDrawable(120, 48);
        this.icon = helper.createDrawableItemStack(new ItemStack(EItems.CROOK.get()));
        this.arrow = arrow;
        this.slot = helper.getSlotDrawable();
        this.title = Component.translatable(TranslationKeys.CROOK_CATEGORY_TITLE);

        this.focusFactory = helpers.getFocusFactory();
        this.ingredientManager = helpers.getIngredientManager();
    }

    @Override
    public RecipeType<CrookJeiRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.CROOK;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrookJeiRecipe recipe, IFocusGroup focuses) {
        recipe.addIngredients(builder);
        ItemStack result = new ItemStack(recipe.result, 1);
        result.setTag(recipe.getResultNbt());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 18).addItemStack(result).addTooltipCallback((recipeSlotView, tooltip) -> {
            tooltip.add(XeiUtil.formatChance(recipe.chance));
        });
    }

    @Override
    public void draw(CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.timer.onDraw();

        this.arrow.draw(graphics, 50, 18);
        this.slot.draw(graphics, 79, 17);

        BlockState state = this.timer.getCycledItem(recipe.states);
        ClientXeiUtil.renderBlock(graphics, state, 28, 18, 10, 20f);
    }

    @Override
    public List<Component> getTooltipStrings(CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (12 < mouseX && mouseX < 44 && 10 < mouseY && mouseY < 42) {
            var block = this.timer.getCycledItem(recipe.states).getBlock();

            // todo replace with XeiUtil
            ArrayList<Component> extraDetails = new ArrayList<>();
            if (recipe instanceof CrookJeiRecipe.StatesRecipe statesRecipe && !statesRecipe.requirements.isEmpty()) {
                extraDetails.add(XeiUtil.REQUIRES_CERTAIN_STATE);
                extraDetails.addAll(statesRecipe.requirements);
            } else if (recipe instanceof CrookJeiRecipe.TagRecipe tagRecipe) {
                extraDetails.add(Component.literal("#" + tagRecipe.tag.location()).withStyle(ChatFormatting.GRAY));
            }

            return XeiUtil.getBlockTooltip(extraDetails, block);
        }

        return List.of();
    }

    @Override
    public boolean handleInput(CrookJeiRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() == InputConstants.Type.MOUSE && (input.getValue() == InputConstants.MOUSE_BUTTON_LEFT || input.getValue() == InputConstants.MOUSE_BUTTON_RIGHT)) {
            if (12 < mouseX && mouseX < 44 && 10 < mouseY && mouseY < 42) {
                var block = this.timer.getCycledItem(recipe.states).getBlock();

                ClientJeiUtil.checkTypedIngredient(this.ingredientManager, VanillaTypes.ITEM_STACK, new ItemStack(block.asItem()), ingredient -> {
                    if (input.getValue() == InputConstants.MOUSE_BUTTON_LEFT) {
                        ClientJeiUtil.showRecipes(this.focusFactory, ingredient);
                    } else if (input.getValue() == InputConstants.MOUSE_BUTTON_RIGHT) {
                        ClientJeiUtil.showUsages(this.focusFactory, ingredient);
                    }
                });

                return true;
            }
        }
        return false;
    }
}
