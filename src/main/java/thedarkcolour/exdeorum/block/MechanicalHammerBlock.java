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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.MechanicalHammerBlockEntity;
import thedarkcolour.exdeorum.blockentity.MechanicalSieveBlockEntity;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EBlockEntities;

import java.util.List;

public class MechanicalHammerBlock extends EBlock {
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public MechanicalHammerBlock(Properties properties) {
        super(properties, EBlockEntities.MECHANICAL_HAMMER);

        registerDefaultState(defaultBlockState().setValue(RUNNING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUNNING, FACING);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == EBlockEntities.MECHANICAL_HAMMER.get() && !level.isClientSide ? (BlockEntityTicker<T>) new MechanicalHammerBlockEntity.ServerTicker<>() : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        var nbt = BlockItem.getBlockEntityData(stack);
        if (nbt != null) {
            var inventoryNbt = nbt.getCompound("inventory");
            var inventory = new ItemStackHandler();
            inventory.deserializeNBT(inventoryNbt);
            var hammer = inventory.getStackInSlot(MechanicalHammerBlockEntity.HAMMER_SLOT);
            if (!hammer.isEmpty()) {
                tooltip.add(Component.translatable(TranslationKeys.MECHANICAL_HAMMER_HAMMER_LABEL).withStyle(ChatFormatting.GRAY).append(Component.translatable(hammer.getDescriptionId())));
            }
            var energy = nbt.getInt("energy");
            tooltip.add(Component.translatable(TranslationKeys.ENERGY).withStyle(ChatFormatting.GRAY).append(Component.translatable(TranslationKeys.FRACTION_DISPLAY, energy, EConfig.SERVER.mechanicalSieveEnergyStorage.get())).append(" FE"));
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState pState, Player player) {
        if (!level.isClientSide && player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (level.getBlockEntity(pos) instanceof MechanicalHammerBlockEntity sieve) {
                if (!sieve.inventory.getStackInSlot(MechanicalHammerBlockEntity.HAMMER_SLOT).isEmpty()) {
                    var stack = new ItemStack(this);
                    BlockItem.setBlockEntityData(stack, EBlockEntities.MECHANICAL_HAMMER.get(), sieve.saveWithoutMetadata());
                    var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                }
            }
        }

        super.playerWillDestroy(level, pos, pState, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var nbt = BlockItem.getBlockEntityData(context.getItemInHand());
        var state = defaultBlockState();
        if (nbt != null && nbt.contains("progress") && nbt.getInt("progress") != MechanicalHammerBlockEntity.NOT_RUNNING) {
            state = state.setValue(RUNNING, true);
        }

        return state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MechanicalHammerBlockEntity hammer) {
                hammer.checkPoweredState(level, pos);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof MechanicalHammerBlockEntity hammer) {
            hammer.checkPoweredState(level, pos);
        }
    }
}
