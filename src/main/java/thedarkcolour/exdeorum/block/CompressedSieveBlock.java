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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class CompressedSieveBlock extends SieveBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            box(0, 8, 0, 16, 14, 16),
            box(1, 0, 1, 3, 8, 3),
            box(13, 0, 1, 15, 8, 3),
            box(1, 0, 13, 3, 8, 15),
            box(13, 0, 13, 15, 8, 15)
    );

    public CompressedSieveBlock(Properties properties) {
        super(properties, EBlockEntities.COMPRESSED_SIEVE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
