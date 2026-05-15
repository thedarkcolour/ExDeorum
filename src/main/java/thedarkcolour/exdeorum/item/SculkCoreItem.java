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

package thedarkcolour.exdeorum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import thedarkcolour.exdeorum.registry.ESounds;

public class SculkCoreItem extends BlockTransformingItem {
    public SculkCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canTransformState(BlockState state) {
        return state.getBlock() == Blocks.SCULK_SHRIEKER && !state.getValue(SculkShriekerBlock.CAN_SUMMON);
    }

    @Override
    protected void doTransformState(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(SculkShriekerBlock.CAN_SUMMON, true), 3);
        } else {
            var rand = level.getRandom();
            for (int i = 0; i < 10; i++) {
                int j = i * 36;
                double radians = Math.toRadians(j);
                for (int k = 0; k < 3; k++) {
                    level.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5 + 0.3 * (-0.5 + rand.nextFloat()), pos.getY() + 0.5625, pos.getZ() + 0.5 + 0.3 * (-0.5 + rand.nextFloat()),
                            Math.cos(radians) * 0.15, 0.15, Math.sin(radians) * 0.15);
                }
            }
        }
        level.playSound(null, pos, ESounds.SCULK_CORE_ACTIVATE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
    }
}
