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

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.util.Lazy;
import thedarkcolour.exdeorum.compat.GroupedSieveRecipe;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.loot.SummationGenerator;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

class SieveCategory implements IRecipeCategory<GroupedSieveRecipe> {
    private static final Component BY_HAND_ONLY_LABEL = Component.translatable(TranslationKeys.SIEVE_RECIPE_BY_HAND_ONLY).withStyle(ChatFormatting.RED);

    public static final int WIDTH = 162;
    public static final int ROW_START = 28;

    static {
        ClientJeiUtil.FORMATTER.setMinimumFractionDigits(0);
        ClientJeiUtil.FORMATTER.setMaximumFractionDigits(3);
    }

    private final Lazy<IDrawable> background;
    private final IDrawable slot;
    private final IDrawable row;
    private final IDrawable icon;
    private final Component title;

    public SieveCategory(IGuiHelper helper) {
        this.background = Lazy.of(() -> helper.createBlankDrawable(WIDTH, ROW_START + 18 * GroupedSieveRecipe.maxSieveRows));
        this.slot = helper.getSlotDrawable();
        this.row = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 0, 0, 162, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(DefaultMaterials.OAK_SIEVE.getItem()));
        this.title = Component.translatable(TranslationKeys.SIEVE_CATEGORY_TITLE);
    }

    @Override
    public RecipeType<GroupedSieveRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.SIEVE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background.get();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GroupedSieveRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 87, 1).addItemStack(recipe.mesh());

        for (int i = 0; i < recipe.results().size(); i++) {
            var result = recipe.results().get(i);
            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + (i % 9) * 18, 1 + ROW_START + 18 * (i / 9)).addItemStack(result.item);

            addTooltips(slot, result.byHandOnly, result.provider);
        }
    }

    public static void addTooltips(IRecipeSlotBuilder slot, boolean byHandOnly, NumberProvider provider) {
        var tooltipLines = new ImmutableList.Builder<Component>();

        if (byHandOnly) {
            tooltipLines.add(BY_HAND_ONLY_LABEL);
            slot.setCustomRenderer(VanillaTypes.ITEM_STACK, ClientJeiUtil.AsteriskItemRenderer.INSTANCE);
        }
        if (provider instanceof BinomialDistributionGenerator binomial) {
            if (binomial.n instanceof ConstantValue constant && constant.value == 1) {
                var chanceLabel = ClientJeiUtil.formatChance(RecipeUtil.getExpectedValue(binomial.p));
                tooltipLines.add(chanceLabel);
            } else {
                addAvgOutput(tooltipLines, RecipeUtil.getExpectedValue(provider));
            }

            addMinMaxes(tooltipLines, 0, getMax(binomial.n));
        } else if (provider.getClass() != ConstantValue.class) {
            var val = RecipeUtil.getExpectedValue(provider);
            if (val != -1.0) {
                addAvgOutput(tooltipLines, val);

                if (provider instanceof UniformGenerator || provider instanceof SummationGenerator) {
                    addMinMaxes(tooltipLines, getMin(provider), getMax(provider));
                }
            }
        }

        var tooltipLinesList = tooltipLines.build();
        slot.addTooltipCallback((slotView, tooltip) -> {
            tooltip.addAll(tooltipLinesList);
        });
    }

    private static double getMin(NumberProvider provider) {
        if (provider instanceof ConstantValue value) {
            return value.value;
        } else if (provider instanceof UniformGenerator uniform) {
            return getMin(uniform.min);
        } else if (provider instanceof BinomialDistributionGenerator) {
            return 0;
        } else if (provider instanceof SummationGenerator summation) {
            double sum = 0;

            for (var child : summation.providers()) {
                sum += getMin(child);
            }

            return sum;
        }

        return 0;
    }

    private static double getMax(NumberProvider provider) {
        if (provider instanceof ConstantValue value) {
            return value.value;
        } else if (provider instanceof UniformGenerator uniform) {
            return getMax(uniform.max);
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            return getMax(binomial.n);
        } else if (provider instanceof SummationGenerator summation) {
            double sum = 0;

            for (var child : summation.providers()) {
                sum += getMax(child);
            }

            return sum;
        }

        return 0;
    }

    private static void addAvgOutput(ImmutableList.Builder<Component> tooltipLines, double avgValue) {
        String avgOutput = ClientJeiUtil.FORMATTER.format(avgValue);
        tooltipLines.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_AVERAGE_OUTPUT, avgOutput).withStyle(ChatFormatting.GRAY));
    }

    // when the player holds shift, they can see the min/max amounts of a drop
    private static void addMinMaxes(ImmutableList.Builder<Component> tooltipLines, double min, double max) {
        String minFormatted = ClientJeiUtil.FORMATTER.format(min);
        String maxFormatted = ClientJeiUtil.FORMATTER.format(max);

        tooltipLines.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_MIN_OUTPUT, minFormatted).withStyle(ChatFormatting.GRAY));
        tooltipLines.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_MAX_OUTPUT, maxFormatted).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void draw(GroupedSieveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.slot.draw(graphics, 58, 0);
        this.slot.draw(graphics, 86, 0);

        for (int i = 0; i < GroupedSieveRecipe.maxSieveRows; i++) {
            this.row.draw(graphics, 0, 28 + i * 18);
        }
    }
}
