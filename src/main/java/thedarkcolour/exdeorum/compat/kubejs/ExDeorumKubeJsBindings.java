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

package thedarkcolour.exdeorum.compat.kubejs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.blockentity.LavaCrucibleBlockEntity;

class ExDeorumKubeJsBindings {
    public void setCrucibleHeatValue(BlockState state, int value) {
        LavaCrucibleBlockEntity.KUBEJS_HEAT_VALUES.put(state, value);
    }

    public void setCrucibleHeatValueForBlock(Block block, int value) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            LavaCrucibleBlockEntity.KUBEJS_HEAT_VALUES.put(state, value);
        }
    }
}
