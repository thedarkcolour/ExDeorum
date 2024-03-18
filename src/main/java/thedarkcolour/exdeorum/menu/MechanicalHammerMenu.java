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

package thedarkcolour.exdeorum.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.MechanicalHammerBlockEntity;
import thedarkcolour.exdeorum.registry.EMenus;
import thedarkcolour.exdeorum.tag.EItemTags;

public class MechanicalHammerMenu extends AbstractMachineMenu<MechanicalHammerBlockEntity> {
    private static final ResourceLocation EMPTY_SLOT_HAMMER = new ResourceLocation(ExDeorum.ID, "item/empty_slot_hammer");
    private static final int NUM_SLOTS = 3;

    public MechanicalHammerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, (MechanicalHammerBlockEntity) readPayload(playerInventory, data));
    }

    public MechanicalHammerMenu(int containerId, Inventory playerInventory, MechanicalHammerBlockEntity machine) {
        super(EMenus.MECHANICAL_HAMMER.get(), containerId, playerInventory, machine);

        // input slot
        addSlot(machine.inventory.createSlot(0, 32, 35));
        // hammer slot
        addSlot(machine.inventory.createSlot(1, 56, 35).setBackground(InventoryMenu.BLOCK_ATLAS, EMPTY_SLOT_HAMMER));
        // output slot
        addSlot(machine.inventory.createSlot(2, 116, 35));

        addPlayerSlots(playerInventory, 84);

        addDataSlot(new ProgressDataSlot());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int clickedSlot) {
        var stack = ItemStack.EMPTY;
        var slot = this.slots.get(clickedSlot);

        if (slot.hasItem()) {
            var clickedStack = slot.getItem();
            stack = clickedStack.copy();

            if (clickedSlot > 1 && clickedSlot <= NUM_SLOTS) { // moving out of output slots
                if (!moveItemStackTo(clickedStack, NUM_SLOTS, PLAYER_SLOTS + NUM_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (clickedSlot < 2) { // moving out of input/mesh slot
                if (!moveItemStackTo(clickedStack, NUM_SLOTS, NUM_SLOTS + PLAYER_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (MechanicalHammerBlockEntity.isValidInput(clickedStack)) { // attempting to move into input slot
                if (!moveItemStackTo(clickedStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }  else if (clickedStack.is(EItemTags.HAMMERS)) { // attempting to move into mesh slot
                if (!moveItemStackTo(clickedStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (clickedSlot < NUM_SLOTS + 27) { // attempting to move from inventory to hotbar
                if (!moveItemStackTo(clickedStack, NUM_SLOTS + 27, NUM_SLOTS + PLAYER_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (clickedSlot < NUM_SLOTS + PLAYER_SLOTS) { // attempting to move from hotbar to inventory
                if (!moveItemStackTo(clickedStack, NUM_SLOTS, NUM_SLOTS + 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (clickedStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (clickedStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, clickedStack);
            broadcastChanges();
        }

        return stack;
    }

    private class ProgressDataSlot extends DataSlot {
        @Override
        public int get() {
            return MechanicalHammerMenu.this.machine.getGuiProgress();
        }

        @Override
        public void set(int value) {
            MechanicalHammerMenu.this.machine.setGuiProgress(value);
        }
    }
}
