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

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import thedarkcolour.exdeorum.blockentity.helper.EnergyHelper;
import thedarkcolour.exdeorum.blockentity.helper.ItemHelper;
import thedarkcolour.exdeorum.client.screen.RedstoneControlWidget;

import java.util.function.Function;

public abstract class AbstractMachineBlockEntity<M extends AbstractMachineBlockEntity<M>> extends EBlockEntity implements MenuProvider {
    public final ItemHelper inventory;
    public final EnergyHelper energy;
    protected int redstoneMode;
    // not saved to NBT
    protected boolean hasRedstonePower;

    @SuppressWarnings("unchecked")
    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Function<M, ItemHelper> inventory, int maxEnergy) {
        super(type, pos, state);

        this.inventory = inventory.apply((M) this);
        this.energy = new EnergyHelper(maxEnergy);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("inventory", this.inventory.serializeNBT());
        nbt.putInt("energy", this.energy.getEnergyStored());
        nbt.putInt("redstoneMode", this.redstoneMode);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.energy.setStoredEnergy(nbt.getInt("energy"));
        this.redstoneMode = Mth.clamp(nbt.getInt("redstoneMode"), 0, 2);
    }

    @Override
    public void onLoad() {
        checkPoweredState(this.level, this.worldPosition);
    }

    public void checkPoweredState(Level level, BlockPos pos) {
        this.hasRedstonePower = level.hasNeighborSignal(pos);
    }

    public void setRedstoneMode(int redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    public int getRedstoneMode() {
        return this.redstoneMode;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(this, buffer -> {
                buffer.writeBlockPos(getBlockPos());
                buffer.writeByte(this.redstoneMode);
            });
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }
    protected abstract boolean isRunning();

    protected abstract void tryStartRunning();

    // Only called serverside
    protected abstract void runMachineTick();

    protected abstract int getEnergyConsumption();

    protected void noEnergyTick() {}

    public IItemHandler getItemHandler() {
        return this.inventory;
    }

    public IEnergyStorage getEnergyStorage() {
        return this.energy;
    }

    public static class ServerTicker<M extends AbstractMachineBlockEntity<M>> implements BlockEntityTicker<M> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, M machine) {
            if (machine.redstoneMode == RedstoneControlWidget.REDSTONE_MODE_IGNORED || ((machine.redstoneMode == RedstoneControlWidget.REDSTONE_MODE_UNPOWERED)) != machine.hasRedstonePower) {
                var energyConsumption = machine.getEnergyConsumption();

                if (machine.energy.getEnergyStored() >= energyConsumption) {
                    if (!machine.isRunning()) {
                        machine.tryStartRunning();
                    }
                    if (machine.isRunning()) {
                        machine.energy.extractEnergy(energyConsumption, false);
                        machine.runMachineTick();
                    }
                } else {
                    machine.noEnergyTick();
                }
            }
        }
    }
}
