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

package thedarkcolour.exdeorum.compat.rei;

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.compat.CompatUtil;

@SuppressWarnings("UnstableApiUsage")
@REIPluginClient
public class ExDeorumReiPlugin implements REIClientPlugin {
    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        rule.hide(() -> {
            var builder = EntryIngredient.builder();

            for (var barrel : CompatUtil.getAvailableBarrels(false)) {
                builder.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(barrel)));
            }
            for (var sieve : CompatUtil.getAvailableSieves(false, false)) {
                builder.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(sieve)));
            }
            for (var crucible : CompatUtil.getAvailableLavaCrucibles(false)) {
                builder.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(crucible)));
            }
            for (var crucible : CompatUtil.getAvailableWaterCrucibles(false)) {
                builder.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(crucible)));
            }
            return builder.build();
        });
    }
}
