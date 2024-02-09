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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

public class CrookCategory implements IRecipeCategory<CrookJeiRecipe> {
    private static final Component REQUIRES_CERTAIN_STATE = Component.translatable(TranslationKeys.CROOK_CATEGORY_REQUIRES_STATE);

    private final IDrawable background;
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
        this.background = helper.createBlankDrawable(120, 48);
        this.icon = helper.createDrawableItemStack(new ItemStack(EItems.CROOK.get()));
        this.arrow = arrow;
        this.slot = helper.getSlotDrawable();
        this.title = Component.translatable(TranslationKeys.CROOK_CATEGORY_TITLE);

        this.focusFactory = helpers.getFocusFactory();
        this.ingredientManager = helpers.getIngredientManager();
        this.modIdHelper = helpers.getModIdHelper();
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
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 18).addItemStack(new ItemStack(recipe.result));
    }

    @Override
    public void draw(CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.timer.onDraw();

        this.arrow.draw(graphics, 52, 18);
        this.slot.draw(graphics, 79, 17);

        BlockState state = this.timer.getCycledItem(recipe.states);
        if (state.is(EBlocks.INFESTED_LEAVES.get())) {
            state = Blocks.OAK_LEAVES.defaultBlockState();
        }
        ClientJeiUtil.renderBlock(graphics, state, 28, 18, 10, 20F);
    }

    @Override
    public List<Component> getTooltipStrings(CrookJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (44.0 < mouseX && mouseX < 76.0 && 16 < mouseY && mouseY < 48) {
            var block = this.timer.getCycledItem(recipe.states).getBlock();
            var modId = ForgeRegistries.BLOCKS.getKey(block).getNamespace();
            var tooltip = new ArrayList<Component>();
            tooltip.add(Component.translatable(block.getDescriptionId()));
            if (recipe.getClass() == CrookJeiRecipe.StatesRecipe.class) {
                tooltip.add(REQUIRES_CERTAIN_STATE);
            }
            tooltip.add(Component.literal(this.modIdHelper.getFormattedModNameForModId(modId)));
            return tooltip;
        }

        return List.of();
    }
}
