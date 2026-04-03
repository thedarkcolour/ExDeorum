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
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;

import java.util.List;

class BarrelCompostEmiRecipe extends EEmiRecipe {
    private final List<EmiIngredient> inputs;
    private final int volume;

    public BarrelCompostEmiRecipe(BarrelCompostRecipe recipe, Identifier id) {
        super(id);

        this.inputs = EmiUtil.inputs(recipe);
        this.volume = recipe.getVolume();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ExDeorumEmiPlugin.BARREL_COMPOST;
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
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(this.inputs.getFirst(), 0, 0);

        var volumeLabel = Component.translatable(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, this.volume);
        widgets.addText(volumeLabel, 24, 5, 0xff808080, false);
    }
}
