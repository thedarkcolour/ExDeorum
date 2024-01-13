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
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.helper.EnergyHelper;
import thedarkcolour.exdeorum.blockentity.helper.ItemHelper;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.client.screen.RedstoneControlWidget;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.menu.MechanicalSieveMenu;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.tag.EItemTags;

import javax.annotation.Nonnull;

public class MechanicalSieveBlockEntity extends AbstractSieveBlockEntity implements MenuProvider {
    private static final Component TITLE = Component.translatable(TranslationKeys.MECHANICAL_SIEVE_SCREEN_TITLE);
    private static final int INPUT_SLOT = 0;
    public static final int MESH_SLOT = 1;

    public final ItemHelper inventory;
    public final EnergyHelper energy;
    private int redstoneMode;
    // not saved to NBT
    public boolean hasRedstonePower;

    private final LazyOptional<ItemHelper> capabilityInventory;
    private final LazyOptional<EnergyStorage> capabilityEnergy;

    public MechanicalSieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.MECHANICAL_SIEVE.get(), pos, state, owner -> new SieveLogic(owner, false, true));

        this.inventory = new ItemHandler(22);
        this.energy = new EnergyHelper(40000);

        this.capabilityInventory = LazyOptional.of(() -> this.inventory);
        this.capabilityEnergy = LazyOptional.of(() -> this.energy);
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

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, buffer -> {
                buffer.writeBlockPos(getBlockPos());
                buffer.writeByte(this.redstoneMode);
            });
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.capabilityEnergy.cast();
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.capabilityInventory.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.capabilityEnergy.invalidate();
        this.capabilityInventory.invalidate();
    }

    private void serverTick() {
        if (this.redstoneMode == RedstoneControlWidget.REDSTONE_MODE_IGNORED || ((this.redstoneMode == RedstoneControlWidget.REDSTONE_MODE_UNPOWERED)) != this.hasRedstonePower) {
            var energyConsumption = EConfig.SERVER.mechanicalSieveEnergyConsumption.get();

            if (this.energy.getEnergyStored() >= energyConsumption) {
                if (this.logic.getContents().isEmpty()) {
                    var input = this.inventory.getStackInSlot(INPUT_SLOT);

                    if (this.logic.isValidInput(input)) {
                        this.logic.startSifting(AbstractSieveBlockEntity.singleCopy(input));
                        input.shrink(1);
                    }
                }
                if (!this.logic.getContents().isEmpty()) {
                    this.energy.extractEnergy(energyConsumption, false);
                    this.logic.sift(0.01f);
                }
            }
        }
    }

    @Override
    public boolean handleResultItem(ItemStack result, ServerLevel level, RandomSource rand) {
        var remainder = result.copy();

        for (int i = 2; i < 22; ++i) {
            // Try to forcefully insert remainder into the output slots, since insertItem will deny it
            // See ItemStackHandler.insertItem for reference
            var existing = this.inventory.getStackInSlot(i);
            // The maximum number of items that can be added to the slot
            var limit = this.inventory.getSlotLimit(i);

            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(remainder, existing)) {
                    continue;
                }
                limit -= existing.getCount();
            }

            // If slot is full
            if (limit <= 0) {
                continue;
            }

            // If only part of the remainder can fit into the slot
            var splitRemainder = remainder.getCount() > limit;

            if (existing.isEmpty()) {
                this.inventory.setStackInSlot(i, splitRemainder ? ItemHandlerHelper.copyStackWithSize(remainder, limit) : remainder);
            } else {
                existing.grow(splitRemainder ? limit : remainder.getCount());
            }
            if (splitRemainder) {
                remainder = ItemHandlerHelper.copyStackWithSize(remainder, remainder.getCount() - limit);
            } else {
                return true;
            }
        }

        // item was "handled" if the remainder is smaller than the original result
        return remainder.getCount() < result.getCount();
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player pPlayer) {
        return new MechanicalSieveMenu(containerId, playerInventory, this);
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    public SieveLogic getLogic() {
        return this.logic;
    }

    public void setRedstoneMode(int redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    public int getRedstoneMode() {
        return this.redstoneMode;
    }

    public void checkPoweredState(Level level, BlockPos pos) {
        this.hasRedstonePower = level.hasNeighborSignal(pos);
    }

    private class ItemHandler extends ItemHelper {
        public ItemHandler(int size) {
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT) {
                return !RecipeUtil.getSieveRecipes(getStackInSlot(1).getItem(), stack).isEmpty();
            } else if (slot == MESH_SLOT) {
                return stack.is(EItemTags.SIEVE_MESHES);
            } else {
                return false;
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == MESH_SLOT ? 1 : super.getSlotLimit(slot);
        }

        @Override
        public boolean canMachineExtract(int slot) {
            return slot > 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (slot == MESH_SLOT) {
                MechanicalSieveBlockEntity.this.logic.setMesh(MechanicalSieveBlockEntity.this.inventory.getStackInSlot(MESH_SLOT));
            }
        }

        @Override
        protected void onLoad() {
            MechanicalSieveBlockEntity.this.logic.setMesh(MechanicalSieveBlockEntity.this.inventory.getStackInSlot(MESH_SLOT), false);
        }
    }

    public static class ServerTicker implements BlockEntityTicker<MechanicalSieveBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, MechanicalSieveBlockEntity sieve) {
            sieve.serverTick();
        }
    }
}
