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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.AbstractMachineBlockEntity;
import thedarkcolour.exdeorum.network.NetworkHandler;

public abstract class AbstractMachineMenu<M extends AbstractMachineBlockEntity<M>> extends AbstractContainerMenu {
    protected static final int PLAYER_SLOTS = 36; // hotbar + inventory

    @Nullable
    private final ServerPlayer player;
    public final M machine;

    public int prevEnergy;

    protected AbstractMachineMenu(MenuType<?> pMenuType, int pContainerId, Inventory playerInventory, M machine) {
        super(pMenuType, pContainerId);

        this.machine = machine;

        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            this.player = serverPlayer;
        } else {
            this.player = null;
        }
    }

    // todo find a better way to do this
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    protected static <M extends AbstractMachineBlockEntity<M>> M readPayload(Inventory playerInventory, FriendlyByteBuf data) {
        var machine = (M) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        machine.setRedstoneMode(data.readByte());
        return machine;
    }

    // Call after own slots have been added
    protected final void addPlayerSlots(Inventory playerInventory, int startY) {
        // Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, startY + i * 18));
            }
        }
        // Hotbar
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, startY + 58));
        }
    }

    // When the server sends a menu property message, the client handles the synced property here.
    public void setClientProperty(int index, int value) {
        if (index == 0) {
            this.prevEnergy = value;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (this.player != null) {
            syncProperties(this.player);
        }
    }

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();

        if (this.player != null) {
            syncProperties(this.player);
        }
    }

    protected void syncProperties(ServerPlayer player) {
        if (this.prevEnergy != this.machine.energy.getEnergyStored()) {
            this.prevEnergy = this.machine.energy.getEnergyStored();

            NetworkHandler.sendMenuProperty(player, this.containerId, 0, this.prevEnergy);
        }
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int id) {
        if (0 <= id && id < 3) {
            this.machine.setRedstoneMode(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.machine.stillValid(player);
    }
}
