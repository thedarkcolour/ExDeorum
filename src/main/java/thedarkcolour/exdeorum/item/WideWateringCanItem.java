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

package thedarkcolour.exdeorum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WideWateringCanItem extends WateringCanItem {
    public WideWateringCanItem(boolean usableInMachines, Properties properties) {
        super(usableInMachines, properties);
    }

    @Override
    protected void tryWatering(ServerLevel level, BlockPos pos, BlockState state) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (level.random.nextInt(3) != 0) {
                    var newPos = pos.offset(i, 0, j);
                    var newState = state;

                    if (newPos != pos) {
                        newState = level.getBlockState(newPos);
                    }

                    super.tryWatering(level, newPos, newState);
                }
            }
        }
    }

    @Override
    protected void waterParticles(Level level, BlockPos pos, BlockState state) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var newPos = pos.offset(i, 0, j);
                var newState = state;

                if (newPos != pos) {
                    newState = level.getBlockState(newPos);
                }

                super.waterParticles(level, newPos, newState);
            }
        }
    }
}
