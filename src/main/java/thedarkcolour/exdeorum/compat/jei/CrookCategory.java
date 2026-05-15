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
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EItems;

public class CrookCategory implements IRecipeCategory<CrookJeiRecipe> {
    private static final Component REQUIRES_CERTAIN_STATE = Component.translatable(TranslationKeys.CROOK_CATEGORY_REQUIRES_STATE).withStyle(ChatFormatting.GRAY);
    private static final int WIDTH = 120;
    private static final int HEIGHT = 48;
    private static final ScreenRectangle BLOCK_AREA = new ScreenRectangle(12, 10, 32, 32);

    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;
    private final Component title;

    private final IFocusFactory focusFactory;
    private final IIngredientManager ingredientManager;
    private final IModIdHelper modIdHelper;

    private final CycleTimer timer = new CycleTimer(0);

    public CrookCategory(IJeiHelpers helpers, IDrawable arrow) {
        var helper = helpers.getGuiHelper();
        this.icon = helper.createDrawableItemStack(new ItemStack(EItems.CROOK.get()));
        this.arrow = arrow;
        this.slot = helper.getSlotDrawable();
        this.title = Component.translatable(TranslationKeys.CROOK_CATEGORY_TITLE);

        this.focusFactory = helpers.getFocusFactory();
        this.ingredientManager = helpers.getIngredientManager();
        this.modIdHelper = helpers.getModIdHelper();
    }

    @Override
    public IRecipeType<CrookJeiRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.CROOK;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrookJeiRecipe recipe, IFocusGroup focuses) {
        recipe.addIngredients(builder);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 18).add(recipe.result).addRichTooltipCallback((_, tooltip) -> {
            tooltip.add(ClientXeiUtil.formatChance(recipe.chance));
        });
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CrookJeiRecipe recipe, IFocusGroup focuses) {
        builder.addInputHandler(new IJeiInputHandler() {
            @Override
            public ScreenRectangle getArea() {
                return BLOCK_AREA;
            }

            @Override
            public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
                return CrookCategory.this.handleBlockInput(recipe, input);
            }
        });
    }

    @Override
    public void draw(CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.timer.onDraw(Minecraft.getInstance().hasShiftDown());

        this.arrow.draw(graphics, 50, 18);
        this.slot.draw(graphics, 79, 17);

        BlockState state = this.timer.getCycledItem(recipe.states);
        ClientXeiUtil.renderBlock(graphics, state, 28, 18, 10, 20f);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (12 < mouseX && mouseX < 44 && 10 < mouseY && mouseY < 42) {
            var block = this.timer.getCycledItem(recipe.states).getBlock();
            var modId = BuiltInRegistries.BLOCK.getKey(block).getNamespace();

            tooltip.add(Component.translatable(block.getDescriptionId()));
            if (recipe instanceof CrookJeiRecipe.StatesRecipe statesRecipe && !statesRecipe.requirements.isEmpty()) {
                tooltip.add(REQUIRES_CERTAIN_STATE);
                tooltip.addAll(statesRecipe.requirements);
            } else if (recipe instanceof CrookJeiRecipe.TagRecipe tagRecipe) {
                tooltip.add(Component.literal("#" + tagRecipe.tag.location()).withStyle(ChatFormatting.GRAY));
            }
            tooltip.add(Component.literal(this.modIdHelper.getFormattedModNameForModId(modId)));
        }
    }

    private boolean handleBlockInput(CrookJeiRecipe recipe, IJeiUserInput input) {
        var key = input.getKey();
        if (key.getType() == InputConstants.Type.MOUSE && (key.getValue() == InputConstants.MOUSE_BUTTON_LEFT || key.getValue() == InputConstants.MOUSE_BUTTON_RIGHT)) {
            if (input.isSimulate()) {
                return true;
            }

            var block = this.timer.getCycledItem(recipe.states).getBlock();

            ClientJeiUtil.checkTypedIngredient(this.ingredientManager, VanillaTypes.ITEM_STACK, new ItemStack(block.asItem()), ingredient -> {
                if (key.getValue() == InputConstants.MOUSE_BUTTON_LEFT) {
                    ClientJeiUtil.showRecipes(this.focusFactory, ingredient);
                } else {
                    ClientJeiUtil.showUsages(this.focusFactory, ingredient);
                }
            });

            return true;
        }
        return false;
    }
}
