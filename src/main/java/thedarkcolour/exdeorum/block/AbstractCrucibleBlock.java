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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.registry.EBlockEntities;

import java.util.function.Supplier;

public abstract class AbstractCrucibleBlock extends EBlock {
    public AbstractCrucibleBlock(Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return UnfiredCrucibleBlock.SHAPE;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // todo look at auxiliary light manager
        //if (level.getBlockEntity(pos) instanceof AbstractCrucibleBlockEntity crucible) {
        //    return crucible.getTank().getFluid().getFluid().getFluidType().getLightLevel();
        //}
        //return pos == BlockPos.ZERO ? 1 : 0;
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> type) {
        return (type == EBlockEntities.WATER_CRUCIBLE.get() || type == EBlockEntities.LAVA_CRUCIBLE.get()) ? (BlockEntityTicker<T>) new AbstractCrucibleBlockEntity.Ticker() : null;
    }
}
