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
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.MechanicalSieveBlockEntity;
import thedarkcolour.exdeorum.registry.EMenus;

public class MechanicalSieveMenu extends AbstractMachineMenu<MechanicalSieveBlockEntity> {
    private static final ResourceLocation EMPTY_SLOT_MESH = new ResourceLocation(ExDeorum.ID, "item/empty_slot_mesh");
    private static final int NUM_SLOTS = 22; // input + mesh, 20 output slots

    public MechanicalSieveMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, (MechanicalSieveBlockEntity) readPayload(playerInventory, data));
    }

    public MechanicalSieveMenu(int containerId, Inventory playerInventory, MechanicalSieveBlockEntity sieve) {
        super(EMenus.MECHANICAL_SIEVE.get(), containerId, playerInventory, sieve);

        // input slot
        addSlot(sieve.inventory.createSlot(0, 26, 30));
        // mesh slot
        addSlot(sieve.inventory.createSlot(1, 26, 53).setBackground(InventoryMenu.BLOCK_ATLAS, EMPTY_SLOT_MESH));
        // output slots
        for (int r = 0; r < 4; ++r) {
            for (int c = 0; c < 5; ++c) {
                addSlot(sieve.inventory.createSlot(2 + r * 5 + c, 80 + c * 18, 15 + r * 18));
            }
        }
        addPlayerSlots(playerInventory, 91);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int clickedSlot) {
        var stack = ItemStack.EMPTY;
        var slot = this.slots.get(clickedSlot);

        if (slot.hasItem()) {
            var clickedStack = slot.getItem();
            stack = clickedStack.copy();

            if (clickedSlot > 1 && clickedSlot < NUM_SLOTS) { // moving out of output slots
                if (!moveItemStackTo(clickedStack, NUM_SLOTS, PLAYER_SLOTS + NUM_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (clickedSlot < 2) { // moving out of input/mesh slot
                if (!moveItemStackTo(clickedStack, NUM_SLOTS, NUM_SLOTS + PLAYER_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.machine.getLogic().isValidInput(clickedStack)) { // attempting to move into input slot
                if (!moveItemStackTo(clickedStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }  else if (this.machine.getLogic().isValidMesh(clickedStack)) { // attempting to move into mesh slot
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
}
