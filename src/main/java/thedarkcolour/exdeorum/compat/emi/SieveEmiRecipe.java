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
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.XeiSieveRecipe;
import thedarkcolour.exdeorum.compat.XeiUtil;

import java.util.Arrays;
import java.util.List;

abstract class SieveEmiRecipe extends EEmiRecipe {
    private final List<EmiIngredient> inputs;
    private final int rows;
    private final List<EmiStack> outputs;
    private final XeiSieveRecipe recipe;

    SieveEmiRecipe(XeiSieveRecipe recipe, int rows) {
        super(determineId(recipe));
        this.inputs = ImmutableList.of(EmiIngredient.of(recipe.ingredient()), EmiStack.of(recipe.mesh()));
        this.rows = rows;
        this.recipe = recipe;

        ImmutableList.Builder<EmiStack> outputs = ImmutableList.builderWithExpectedSize(recipe.results().size());
        for (XeiSieveRecipe.Result result : recipe.results()) {
            outputs.add(EmiStack.of(result.item));
        }
        this.outputs = outputs.build();
    }

    private static ResourceLocation determineId(XeiSieveRecipe recipe) {
        Item mesh = recipe.mesh().getItem();
        int hashCode = Arrays.hashCode(Arrays.stream(recipe.ingredient().getItems())
                .map(ItemStack::getItem)
                .toArray());

        return new ResourceLocation(ExDeorum.ID, BuiltInRegistries.ITEM.getKey(mesh).getPath() + "_" + hashCode);
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
        return XeiUtil.SIEVE_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return XeiUtil.SIEVE_ROW_START + XeiUtil.SIEVE_ROW_HEIGHT * this.rows;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(this.inputs.get(0), 58, 1);
        widgets.addSlot(this.inputs.get(1), 86, 1);

        for (int i = 0; i < this.rows * 9; i++) {
            int x = (i % 9) * 18;
            int y = XeiUtil.SIEVE_ROW_START + 18 * (i / 9);

            if (i < this.outputs.size()) {
                XeiSieveRecipe.Result result = this.recipe.results().get(i);
                widgets.add(new SieveResultWidget(this.outputs.get(i), x, y, result.byHandOnly, result.provider)).recipeContext(this);
            } else {
                widgets.addSlot(x, y);
            }
        }
    }

    static class Sieve extends SieveEmiRecipe {
        Sieve(XeiSieveRecipe recipe) {
            super(recipe, XeiSieveRecipe.SIEVE_ROWS.intValue());
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.SIEVE;
        }
    }

    static class CompressedSieve extends SieveEmiRecipe {
        CompressedSieve(XeiSieveRecipe recipe) {
            super(recipe, XeiSieveRecipe.COMPRESSED_SIEVE_ROWS.intValue());
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.COMPRESSED_SIEVE;
        }
    }

    private static class SieveResultWidget extends SlotWidget {
        private final boolean byHandOnly;
        private final NumberProvider amount;

        public SieveResultWidget(EmiIngredient stack, int x, int y, boolean byHandOnly, NumberProvider amount) {
            super(stack, x, y);
            this.byHandOnly = byHandOnly;
            this.amount = amount;
        }

        @Override
        public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
            List<ClientTooltipComponent> list = super.getTooltip(mouseX, mouseY);
            XeiUtil.addSieveDropTooltip(this.byHandOnly, this.amount, line -> list.add(ClientTooltipComponent.create(line.getVisualOrderText())));
            return list;
        }
    }
}
