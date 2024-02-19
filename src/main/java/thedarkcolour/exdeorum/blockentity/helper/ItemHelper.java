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

package thedarkcolour.exdeorum.blockentity.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

// Has same behavior as ItemStackHandler but is more customizable.
public class ItemHelper extends ItemStackHandler {
    public ItemHelper(int size) {
        super(size);
    }

    // Whether an item can be extracted from this slot (GUI ignores this and just takes it out)
    public boolean canMachineExtract(int slot) {
        return true;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (canMachineExtract(slot)) {
            return takeOutItem(slot, amount, simulate);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack takeOutItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public ItemHelper.Slot createSlot(int index, int x, int y) {
        return new ItemHelper.Slot(index, x, y);
    }

    public class Slot extends SlotItemHandler {
        public Slot(int index, int x, int y) {
            super(ItemHelper.this, index, x, y);
        }

        @Override
        public @NotNull ItemStack remove(int amount) {
            return ItemHelper.this.takeOutItem(getContainerSlot(), amount, false);
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return !ItemHelper.this.takeOutItem(getContainerSlot(), 1, true).isEmpty();
        }
    }
}
