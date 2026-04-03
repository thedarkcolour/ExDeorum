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

package thedarkcolour.exdeorum.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import thedarkcolour.exdeorum.blockentity.AbstractMachineBlockEntity;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;

import java.util.List;
import java.util.function.Supplier;

public abstract class MachineBlock extends EBlock {
    public MachineBlock(Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == this.blockEntityType.get() ? (BlockEntityTicker<T>) new AbstractMachineBlockEntity.ServerTicker<>() : null;
    }

    // Slot in the machine's inventory where the mesh/hammer is
    protected abstract int getHighlightItemSlot();

    // Label for the item tooltip where the mesh/hammer is listed
    protected abstract MutableComponent getHighlightItemLabel();

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltip, TooltipFlag flag) {
        var lookup = level.registries();
        if (lookup != null) {
            var customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);

            // If this item is not default state
            if (customData != null) {
                @SuppressWarnings("deprecation")
                var nbt = customData.getUnsafe();

                if (nbt.contains("inventory")) {
                    var inventory = new ItemStackHandler();
                    inventory.deserializeNBT(lookup, nbt.getCompound("inventory"));

                    // Hammer or sieve mesh
                    var highlightItem = inventory.getStackInSlot(getHighlightItemSlot());

                    if (!highlightItem.isEmpty()) {
                        // display the mesh/hammer inside the machine
                        tooltip.add(getHighlightItemLabel().withStyle(ChatFormatting.GRAY).append(Component.translatable(highlightItem.getDescriptionId())));
                    }
                }

                // display the energy stored in the machine
                if (nbt.contains("energy")) {
                    var energy = nbt.getInt("energy");
                    tooltip.add(Component.translatable(TranslationKeys.ENERGY).withStyle(ChatFormatting.GRAY).append(Component.translatable(TranslationKeys.FRACTION_DISPLAY, energy, EConfig.SERVER.mechanicalSieveEnergyStorage.get())).append(" FE"));
                }
            }
        }
    }

    // Drops the item for creative mode players
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState pState, Player player) {
        if (!level.isClientSide && player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity<?> machine) {
                if (!machine.inventory.getStackInSlot(getHighlightItemSlot()).isEmpty()) {
                    var stack = new ItemStack(this);
                    // save machine properties to the item if mesh/hammer slot is not empty
                    BlockItem.setBlockEntityData(stack, this.blockEntityType.get(), machine.saveWithoutMetadata(level.registryAccess()));
                    var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                }
            }
        }

        return super.playerWillDestroy(level, pos, pState, player);
    }

    // Redstone state
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity<?> machine) {
                machine.checkPoweredState(level, pos);
            }
        }
    }

    // Redstone state
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity<?> machine) {
            machine.checkPoweredState(level, pos);
        }
    }
}
