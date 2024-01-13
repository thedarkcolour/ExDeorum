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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.MechanicalSieveBlockEntity;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EBlockEntities;

import java.util.List;

public class MechanicalSieveBlock extends EBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 8, 0, 16, 16, 16),
            box(1, 0, 1, 3, 8, 3),
            box(1, 0, 13, 3, 8, 15),
            box(13, 0, 1, 15, 8, 3),
            box(13, 0, 13, 15, 8, 15)
    );

    public MechanicalSieveBlock(Properties properties) {
        super(properties, EBlockEntities.MECHANICAL_SIEVE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> type) {
        return type == EBlockEntities.MECHANICAL_SIEVE.get() && !level.isClientSide ? (BlockEntityTicker<T>) new MechanicalSieveBlockEntity.ServerTicker() : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        var nbt = BlockItem.getBlockEntityData(stack);
        if (nbt != null) {
            var inventoryNbt = nbt.getCompound("inventory");
            var inventory = new ItemStackHandler();
            inventory.deserializeNBT(inventoryNbt);
            var mesh = inventory.getStackInSlot(MechanicalSieveBlockEntity.MESH_SLOT);
            if (!mesh.isEmpty()) {
                tooltip.add(Component.translatable(TranslationKeys.MECHANICAL_SIEVE_MESH_LABEL).withStyle(ChatFormatting.GRAY).append(Component.translatable(mesh.getDescriptionId())));
            }
            var energy = nbt.getInt("energy");
            tooltip.add(Component.translatable(TranslationKeys.ENERGY).withStyle(ChatFormatting.GRAY).append(Component.translatable(TranslationKeys.FRACTION_DISPLAY, energy, EConfig.SERVER.mechanicalSieveEnergyStorage.get())).append(" FE"));
        }
    }

    // Drops the item for creative mode players
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState pState, Player player) {
        if (!level.isClientSide && player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (level.getBlockEntity(pos) instanceof MechanicalSieveBlockEntity sieve) {
                if (!sieve.getMesh().isEmpty()) {
                    var stack = new ItemStack(this);
                    BlockItem.setBlockEntityData(stack, EBlockEntities.MECHANICAL_SIEVE.get(), sieve.saveWithoutMetadata());
                    var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                }
            }
        }

        super.playerWillDestroy(level, pos, pState, player);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MechanicalSieveBlockEntity sieve) {
                sieve.checkPoweredState(level, pos);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof MechanicalSieveBlockEntity sieve) {
            sieve.checkPoweredState(level, pos);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
