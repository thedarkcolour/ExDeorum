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

import com.google.common.collect.ImmutableList;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;

import java.util.List;

class CrookEmiRecipe extends EEmiRecipe {
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;
    private final List<BlockState> states;
    private final BlockPredicate predicate;

    public CrookEmiRecipe(CrookRecipe recipe, Identifier id) {
        super(id);

        this.inputs = EmiUtil.inputs(recipe.blockPredicate());
        this.outputs = ImmutableList.of(EmiStack.of(recipe.result()));
        this.states = XeiUtil.getStates(recipe.blockPredicate());
        this.predicate = recipe.blockPredicate();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ExDeorumEmiPlugin.CROOK;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
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
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 18);
        widgets.addSlot(this.outputs.getFirst(), 79, 17).recipeContext(this);
        widgets.add(new BlockEmiWidget(this.states, XeiUtil.getExtraDetails(this.predicate), 28, 18, 20f));
    }
}
