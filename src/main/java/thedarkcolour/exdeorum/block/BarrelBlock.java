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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class BarrelBlock extends ETankBlock {
    public static final float BARREL_FLUID_BOTTOM = 1f / 16f;
    public static final float BARREL_FLUID_TOP = 14f / 16f;

    public static final VoxelShape SHAPE = Shapes.join(
            box(1, 0, 1, 15, 16, 15),
            box(2, 1, 2, 14, 16, 14),
            BooleanOp.ONLY_FIRST
    );

    public BarrelBlock(Properties properties) {
        super(properties, EBlockEntities.BARREL);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> type) {
        return type == EBlockEntities.BARREL.get() ? (BlockEntityTicker<T>) new BarrelBlockEntity.Ticker() : null;
    }

    // todo fix annoying behaviour when placing blocks on inner sides of barrel
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof BarrelBlockEntity barrel) {
            var item = barrel.getItem();

            if (!item.isEmpty()) {
                EBlock.dropItem(level, pos, item);
            }
        }

        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    public void neighborChanged(BlockState pState, Level level, BlockPos pos, Block pBlock, @org.jetbrains.annotations.Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean pIsMoving) {
        if (level.getBlockEntity(pos) instanceof BarrelBlockEntity barrel) {
            barrel.tryInWorldFluidMixing();
            barrel.updateFluidTransform();
        }
    }

    @Override
    protected boolean isEntityInFluid(Level level, BlockPos pos, Entity entity, float fillRatio) {
        var fluidTop = Mth.lerp(fillRatio, BARREL_FLUID_BOTTOM, BARREL_FLUID_TOP);
        return entity.getBoundingBox().intersects(pos.getX() + 0.125, pos.getY() + BARREL_FLUID_BOTTOM, pos.getZ() + 0.125, pos.getX() + 0.875, pos.getY() + fluidTop, pos.getZ() + 0.875);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        var lightManager = level.getAuxLightManager(pos);
        if (lightManager != null) {
            return lightManager.getLightAt(pos);
        }
        return 0;
    }
}
