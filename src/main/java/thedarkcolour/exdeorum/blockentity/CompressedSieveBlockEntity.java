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
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.blockentity.logic.CompressedSieveLogic;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class CompressedSieveBlockEntity extends AbstractSieveBlockEntity {
    private static final float COMPRESSED_SIEVE_INTERVAL = 0.075f;

    public CompressedSieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.COMPRESSED_SIEVE.get(), pos, state, COMPRESSED_SIEVE_INTERVAL, owner -> new CompressedSieveLogic(owner, false));
    }
}
