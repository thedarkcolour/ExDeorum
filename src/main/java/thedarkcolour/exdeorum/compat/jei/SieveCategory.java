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

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.compat.XeiSieveRecipe;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

import java.util.Optional;

class SieveCategory implements IRecipeCategory<XeiSieveRecipe> {
    private final IJeiHelpers jeiHelpers;
    private final IDrawable slot;
    private final IDrawable row;
    private final IDrawable icon;
    private final Component title;
    private final MutableInt rows;

    /**
     * settingSlots is a workaround to prevent JEI from adding recipe tooltips with our generated recipe IDs.
     * They look ugly and clutter the screen, since they're pretty long.
      */
    private boolean settingSlots = false;

    SieveCategory(IJeiHelpers jeiHelpers, ItemLike icon, Component title, MutableInt rows) {
        this.jeiHelpers = jeiHelpers;
        var helper = jeiHelpers.getGuiHelper();
        this.slot = helper.getSlotDrawable();
        this.row = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 0, 0, 162, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(icon));
        this.title = title;
        this.rows = rows;
    }

    SieveCategory(IJeiHelpers jeiHelpers) {
        this(jeiHelpers, DefaultMaterials.OAK_SIEVE, Component.translatable(TranslationKeys.SIEVE_CATEGORY_TITLE), XeiSieveRecipe.SIEVE_ROWS);
    }

    @Override
    public RecipeType<XeiSieveRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.SIEVE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return XeiUtil.SIEVE_WIDTH;
    }

    @Override
    public int getHeight() {
        return XeiUtil.SIEVE_ROW_START + XeiUtil.SIEVE_ROW_HEIGHT * this.rows.intValue();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(XeiSieveRecipe recipe) {
        return settingSlots ? null : recipe.id();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, XeiSieveRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 87, 1).addItemStack(recipe.mesh());

        settingSlots = true;
        for (int i = 0; i < recipe.results().size(); i++) {
            var result = recipe.results().get(i);
            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + (i % 9) * 18, 1 + XeiUtil.SIEVE_ROW_START + 18 * (i / 9)).addItemStack(result.item);

            // Since we group recipes, we need to manually add the recipe ID tooltip for the given output item.
            slot.addRichTooltipCallback(new OutputSlotTooltipCallback(result.holder.id(), getRecipeType()));
            addTooltips(slot, result.byHandOnly, result.provider);
        }
        settingSlots = false;
    }

    public static void addTooltips(IRecipeSlotBuilder slot, boolean byHandOnly, NumberProvider provider) {
        if (byHandOnly) {
            slot.setCustomRenderer(VanillaTypes.ITEM_STACK, ClientJeiUtil.AsteriskItemRenderer.INSTANCE);
        }
        slot.addRichTooltipCallback((slotView, tooltip) -> {
            XeiUtil.addSieveDropTooltip(byHandOnly, provider, tooltip::add);
        });
    }

    @Override
    public void draw(XeiSieveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.slot.draw(graphics, 58, 0);
        this.slot.draw(graphics, 86, 0);

        int rows = this.rows.intValue();

        for (int i = 0; i < rows; i++) {
            this.row.draw(graphics, 0, 28 + i * 18);
        }
    }


    /**
     * Copied almost 1:1 from class of same name in JEI. Since this is a JEI implementation detail, it's not exposed in the API.
     * Modified minimally to not use any of the other internals and compile.
     */
    public class OutputSlotTooltipCallback implements IRecipeSlotRichTooltipCallback {
        private static final Logger LOGGER = LogManager.getLogger();

        private final ResourceLocation recipeName;
        private final boolean recipeFromSameModAsCategory;

        public OutputSlotTooltipCallback(ResourceLocation recipeName, RecipeType<?> recipeType) {
            this.recipeName = recipeName;
            this.recipeFromSameModAsCategory = recipeName.getNamespace().equals(recipeType.getUid().getNamespace());
        }

        @Override
        public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
            if (recipeSlotView.getRole() != RecipeIngredientRole.OUTPUT) {
                return;
            }
            Optional<ITypedIngredient<?>> displayedIngredient = recipeSlotView.getDisplayedIngredient();
            if (displayedIngredient.isEmpty()) {
                return;
            }

            addRecipeBy(tooltip, displayedIngredient.get());

            Minecraft minecraft = Minecraft.getInstance();
            boolean showAdvanced = minecraft.options.advancedItemTooltips || Screen.hasShiftDown();
            if (showAdvanced) {
                MutableComponent recipeId = Component.translatable("jei.tooltip.recipe.id", Component.literal(recipeName.toString()));
                tooltip.add(recipeId.withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        private void addRecipeBy(ITooltipBuilder tooltip, ITypedIngredient<?> displayedIngredient) {
            if (recipeFromSameModAsCategory) {
                return;
            }
            IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
            if (!modIdHelper.isDisplayingModNameEnabled()) {
                return;
            }
            String ingredientModId = getDisplayModId(displayedIngredient);
            if (ingredientModId == null) {
                return;
            }
            String recipeModId = recipeName.getNamespace();
            if (recipeModId.equals(ingredientModId)) {
                return;
            }
            String modName = modIdHelper.getFormattedModNameForModId(recipeModId);
            MutableComponent recipeBy = Component.translatable("jei.tooltip.recipe.by", modName);
            tooltip.add(recipeBy.withStyle(ChatFormatting.GRAY));
        }

        private <T> @Nullable String getDisplayModId(ITypedIngredient<T> typedIngredient) {
            IIngredientManager ingredientManager = jeiHelpers.getIngredientManager();

            IIngredientType<T> type = typedIngredient.getType();
            T ingredient = typedIngredient.getIngredient();
            IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(type);
            try {
                return ingredientHelper.getDisplayModId(ingredient);
            } catch (RuntimeException e) {
                String ingredientInfo = ingredientHelper.getErrorInfo(ingredient);
                LOGGER.error("Caught exception from ingredient without a resource location: {}", ingredientInfo, e);
                return null;
            }
        }
    }
}
