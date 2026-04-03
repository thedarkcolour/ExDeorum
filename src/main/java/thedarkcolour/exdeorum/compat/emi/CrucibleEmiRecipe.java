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
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.Identifier;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;

import java.util.List;

abstract class CrucibleEmiRecipe extends EmiOneToOneRecipe {
    private final List<EmiStack> outputs;

    CrucibleEmiRecipe(CrucibleRecipe recipe, Identifier id) {
        super(recipe, id);

        this.outputs = EmiUtil.outputs(recipe.getResult());
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    static class Lava extends CrucibleEmiRecipe {
        Lava(CrucibleRecipe recipe, Identifier id) {
            super(recipe, id);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.LAVA_CRUCIBLE;
        }
    }

    static class Water extends CrucibleEmiRecipe {
        Water(CrucibleRecipe recipe, Identifier id) {
            super(recipe, id);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.WATER_CRUCIBLE;
        }
    }
}
