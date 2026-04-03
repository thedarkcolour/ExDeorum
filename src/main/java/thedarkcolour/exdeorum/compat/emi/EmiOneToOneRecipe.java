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

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.Identifier;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;

import java.util.List;

abstract class EmiOneToOneRecipe extends EEmiRecipe {
    private final List<EmiIngredient> inputs;

    EmiOneToOneRecipe(SingleIngredientRecipe recipe, Identifier id) {
        super(id);

        this.inputs = EmiUtil.inputs(recipe);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public int getDisplayWidth() {
        return XeiUtil.ONE_TO_ONE_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return XeiUtil.ONE_TO_ONE_HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // todo replace with first in 1.21
        widgets.addSlot(this.inputs.get(0), 0, 0);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 1);
        widgets.addSlot(getOutputs().get(0), 54, 0).recipeContext(this);
    }
}
