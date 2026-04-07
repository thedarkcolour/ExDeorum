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

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EBlocks;

public class InfestedLeavesBlock extends LeavesBlock implements EntityBlock {
    public static final BooleanProperty FULLY_INFESTED = BooleanProperty.create("fully_infested");
    public static final MapCodec<InfestedLeavesBlock> CODEC = simpleCodec(InfestedLeavesBlock::new);

    public InfestedLeavesBlock(Properties properties) {
        super(0.005f, properties);
        registerDefaultState(defaultBlockState().setValue(FULLY_INFESTED, false));
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    public static void setBlock(Level level, BlockPos pos, BlockState fromState) {
        level.setBlock(pos, EBlocks.INFESTED_LEAVES.get().defaultBlockState()
                        .setValue(LeavesBlock.DISTANCE, fromState.hasProperty(LeavesBlock.DISTANCE) ? fromState.getValue(LeavesBlock.DISTANCE) : 0)
                        .setValue(LeavesBlock.PERSISTENT, fromState.hasProperty(LeavesBlock.PERSISTENT) ? fromState.getValue(LeavesBlock.PERSISTENT) : false),
                2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FULLY_INFESTED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (ctx.getPlayer() != null) {
            return defaultBlockState().setValue(FULLY_INFESTED, true);
        }
        return super.getStateForPlacement(ctx);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState pState, @Nullable LivingEntity player, ItemStack pStack) {
        if (player != null) {
            if (!level.isClientSide() && level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity leaves) {
                leaves.setProgress(InfestedLeavesBlockEntity.MAX_PROGRESS);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfestedLeavesBlockEntity(pos, state);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState state, BlockEntityType<T> type) {
        return (type == EBlockEntities.INFESTED_LEAVES.get()) ? (BlockEntityTicker<T>) new InfestedLeavesBlockEntity.Ticker() : null;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        if (level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity leaves) {
            return leaves.getMimic().getCloneItemStack(pos, level, includeData, null);
        }
        return state.getCloneItemStack(pos, level, includeData, null);
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (decaying(state)) {
            // doesn't drop unless crook
            level.removeBlock(pos, false);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        if (!EConfig.CLIENT_SPEC.isLoaded()) {
            return RenderShape.MODEL;
        }
        return (EConfig.CLIENT.useFastInfestedLeaves.get() || RenderUtil.IRIS_ACCESS.areShadersEnabled()) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }
}
