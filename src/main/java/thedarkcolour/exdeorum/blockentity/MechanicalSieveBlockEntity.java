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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import thedarkcolour.exdeorum.blockentity.helper.ItemHelper;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.menu.MechanicalSieveMenu;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.tag.EItemTags;

public class MechanicalSieveBlockEntity extends AbstractMachineBlockEntity<MechanicalSieveBlockEntity> implements SieveLogic.Owner {
    private static final Component TITLE = Component.translatable(TranslationKeys.MECHANICAL_SIEVE_SCREEN_TITLE);
    private static final int INPUT_SLOT = 0;
    public static final int MESH_SLOT = 1;

    private final SieveLogic logic;

    public MechanicalSieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.MECHANICAL_SIEVE.get(), pos, state, ItemHandler::new, EConfig.SERVER.mechanicalSieveEnergyStorage.get());

        this.logic = new SieveLogic(this, false, true);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        this.logic.saveNbt(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.logic.loadNbt(nbt);
    }

    @Override
    protected boolean isRunning() {
        return !this.logic.getContents().isEmpty();
    }

    @Override
    protected void tryStartRunning() {
        var input = this.inventory.getStackInSlot(INPUT_SLOT);

        if (this.logic.isValidInput(input)) {
            this.logic.startSifting(AbstractSieveBlockEntity.singleCopy(input));
            input.shrink(1);
        }
    }

    @Override
    protected void runMachineTick() {
        this.logic.sift(0.01f, Long.MAX_VALUE);
    }

    @Override
    protected int getEnergyConsumption() {
        return EConfig.SERVER.mechanicalSieveEnergyConsumption.get();
    }

    @Override
    public void writeVisualData(FriendlyByteBuf buffer) {
        this.logic.writeVisualData(buffer);
    }

    @Override
    public void readVisualData(FriendlyByteBuf buffer) {
        this.logic.setMesh(buffer.readItem(), false);
        this.logic.setProgress(buffer.readFloat());
        this.logic.setContents(buffer.readItem());
    }

    @Override
    public void copyVisualData(BlockEntity fromIntegratedServer) {
        this.logic.copyVisualData(fromIntegratedServer);
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

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player pPlayer) {
        return new MechanicalSieveMenu(containerId, playerInventory, this);
    }

    @Override
    public SieveLogic getLogic() {
        return this.logic;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public ServerLevel getServerLevel() {
        return (ServerLevel) this.level;
    }

    private static class ItemHandler extends ItemHelper {
        private final MechanicalSieveBlockEntity sieve;

        public ItemHandler(MechanicalSieveBlockEntity sieve) {
            super(22);
            this.sieve = sieve;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
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
                this.sieve.logic.setMesh(this.sieve.inventory.getStackInSlot(MESH_SLOT));
            }
        }

        @Override
        protected void onLoad() {
            this.sieve.logic.setMesh(this.sieve.inventory.getStackInSlot(MESH_SLOT), false);
        }
    }
}
