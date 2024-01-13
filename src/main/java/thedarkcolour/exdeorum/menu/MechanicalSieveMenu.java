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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.MechanicalSieveBlockEntity;
import thedarkcolour.exdeorum.network.NetworkHandler;
import thedarkcolour.exdeorum.registry.EMenus;

public class MechanicalSieveMenu extends EContainerMenu {
    private static final ResourceLocation EMPTY_SLOT_MESH = new ResourceLocation(ExDeorum.ID, "item/empty_slot_mesh");
    private static final int NUM_SLOTS = 22; // input + mesh, 20 output slots
    private static final int PLAYER_SLOTS = 36; // hotbar + inventory

    public final MechanicalSieveBlockEntity sieve;
    @Nullable
    private final ServerPlayer player;

    public int prevSieveEnergy;

    public MechanicalSieveMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, (MechanicalSieveBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos()));
        this.sieve.setRedstoneMode(data.readByte());
    }

    public MechanicalSieveMenu(int containerId, Inventory playerInventory, MechanicalSieveBlockEntity sieve) {
        super(EMenus.MECHANICAL_SIEVE.get(), containerId);

        this.sieve = sieve;

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
        // Player slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 7 + 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 149));
        }

        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            this.player = serverPlayer;
        } else {
            this.player = null;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (this.player != null) {
            if (this.prevSieveEnergy != this.sieve.energy.getEnergyStored()) {
                this.prevSieveEnergy = this.sieve.energy.getEnergyStored();

                NetworkHandler.sendMenuProperty(this.player, this.containerId, 0, this.prevSieveEnergy);
            }
        }
    }

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();

        if (this.player != null) {
            if (this.prevSieveEnergy != this.sieve.energy.getEnergyStored()) {
                this.prevSieveEnergy = this.sieve.energy.getEnergyStored();

                NetworkHandler.sendMenuProperty(this.player, this.containerId, 0, this.prevSieveEnergy);
            }
        }
    }

    @Override
    public void setClientProperty(int index, int value) {
        if (index == 0) {
            this.prevSieveEnergy = value;
        }
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
            } else if (this.sieve.getLogic().isValidInput(clickedStack)) { // attempting to move into input slot
                if (!moveItemStackTo(clickedStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }  else if (this.sieve.getLogic().isValidMesh(clickedStack)) { // attempting to move into mesh slot
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

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (0 <= id && id < 3) {
            this.sieve.setRedstoneMode(id);
            return false;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.sieve.stillValid(player);
    }
}
