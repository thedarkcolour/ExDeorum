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

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import thedarkcolour.exdeorum.compat.CompatUtil;

import java.util.HashSet;
import java.util.Set;

@EmiEntrypoint
public class ExDeorumEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
    }

    @Override
    public void initialize(EmiInitRegistry registry) {
        Set<ItemLike> toRemove = new HashSet<>();

        toRemove.addAll(CompatUtil.getAvailableBarrels(false));
        toRemove.addAll(CompatUtil.getAvailableSieves(false, false));
        toRemove.addAll(CompatUtil.getAvailableLavaCrucibles(false));
        toRemove.addAll(CompatUtil.getAvailableWaterCrucibles(false));

        Set<Item> toRemoveItems = new HashSet<>();

        for (var itemLike : toRemove) {
            toRemoveItems.add(itemLike.asItem());
        }

        registry.disableStacks(stack -> {
            return toRemoveItems.contains(stack.getItemStack().getItem());
        });
    }
}
