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

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.Identifier;
import thedarkcolour.exdeorum.compat.XeiUtil;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;

import java.util.List;

abstract class BarrelMixingEmiRecipe extends EEmiRecipe {
    private final EmiIngredient base;
    private final EmiIngredient additive;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public BarrelMixingEmiRecipe(EmiIngredient base, EmiIngredient additive, List<EmiStack> outputs, Identifier id) {
        super(id);

        this.base = base;
        this.additive = additive;

        this.inputs = List.of(base, additive);
        this.outputs = outputs;
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
        return XeiUtil.BARREL_MIXING_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return XeiUtil.BARREL_MIXING_HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(this.base, 0, 0);
        widgets.addTexture(EmiTexture.PLUS, 22, 2);
        widgets.addSlot(this.additive, 39, 0);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 63, 1);
        widgets.addSlot(this.outputs.getFirst(), 78 + 15, 0).recipeContext(this);
    }

    static class Items extends BarrelMixingEmiRecipe {
        public Items(BarrelMixingRecipe recipe, Identifier id) {
            super(NeoForgeEmiIngredient.of(recipe.fluid), EmiIngredient.of(recipe.ingredient), EmiUtil.outputs(recipe.result), id);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.BARREL_MIXING;
        }
    }

    static class Fluids extends BarrelMixingEmiRecipe {
        public Fluids(BarrelFluidMixingRecipe recipe, Identifier id) {
            super(NeoForgeEmiIngredient.of(recipe.baseFluid()), NeoForgeEmiIngredient.of(recipe.additiveFluid()), EmiUtil.outputs(recipe.result()), id);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.BARREL_FLUID_MIXING;
        }
    }
}
