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

package thedarkcolour.exdeorum.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;

import java.util.List;

class CrucibleHeatEmiRecipe extends EEmiRecipe {
    private final List<EmiIngredient> inputs;
    private final List<BlockState> states;
    private final BlockPredicate predicate;
    private final int heatValue;

    public CrucibleHeatEmiRecipe(CrucibleHeatRecipe recipe, ResourceLocation id) {
        super(id);

        this.inputs = EmiUtil.inputs(recipe.blockPredicate());
        this.states = XeiUtil.getStates(recipe.blockPredicate());
        this.predicate = recipe.blockPredicate();
        this.heatValue = recipe.heatValue();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ExDeorumEmiPlugin.CRUCIBLE_HEAT_SOURCES;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 120;
    }

    @Override
    public int getDisplayHeight() {
        return 48;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addText(Component.translatable(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_MULTIPLIER, this.heatValue), 60, 5, 0xff808080, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        widgets.add(new BlockEmiWidget(this.states, XeiUtil.getExtraDetails(this.predicate), 60, 24, 20f));
    }
}
