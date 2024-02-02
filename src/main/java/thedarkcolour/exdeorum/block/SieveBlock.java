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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exdeorum.blockentity.SieveBlockEntity;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class SieveBlock extends EBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            box(0, 11, 0, 16, 16, 16),
            box(1, 0, 1, 2, 11, 2),
            box(14, 0, 1, 15, 11, 2),
            box(1, 0, 14, 2, 11, 15),
            box(14, 0, 14, 15, 11, 15)
    );

    public SieveBlock(Properties properties) {
        super(properties, EBlockEntities.SIEVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if (!level.isClientSide) {
            if (!state.is(newState.getBlock())) {
                if (level.getBlockEntity(pos) instanceof SieveBlockEntity sieve) {
                    var mesh = sieve.getLogic().getMesh();

                    if (!mesh.isEmpty()) {
                        dropItem(level, pos, mesh);
                    }
                }
            }
        }

        super.onRemove(state, level, pos, newState, pIsMoving);
    }
}
