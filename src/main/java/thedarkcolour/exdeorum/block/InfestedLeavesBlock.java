/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class InfestedLeavesBlock extends LeavesBlock implements EntityBlock {
    public static final BooleanProperty FULLY_INFESTED = BooleanProperty.create("fully_infested");

    public InfestedLeavesBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FULLY_INFESTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FULLY_INFESTED);
    }

    @Nullable
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity leaves) {
            return leaves.getMimic().getCloneItemStack(target, level, pos, player);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (decaying(state)) {
            // doesn't drop unless crook
            //dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return (EConfig.CLIENT_SPEC.isLoaded() && EConfig.CLIENT.useFastInfestedLeaves.get()) || RenderUtil.IRIS_ACCESS.areShadersEnabled() ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }
}