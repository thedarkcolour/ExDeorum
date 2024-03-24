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
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class SieveBlockEntity extends AbstractSieveBlockEntity {
    public static final float SIEVE_INTERVAL = 0.1f;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.SIEVE.get(), pos, state, SIEVE_INTERVAL, owner -> new SieveLogic(owner, false));
    }

    @Override
    protected boolean canUseSimultaneously() {
        return EConfig.SERVER.simultaneousSieveUsage.get();
    }
}
