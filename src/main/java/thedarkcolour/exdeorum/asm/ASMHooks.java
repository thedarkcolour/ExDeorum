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

package thedarkcolour.exdeorum.asm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import thedarkcolour.exdeorum.voidworld.VoidChunkGenerator;

@SuppressWarnings("unused")
public final class ASMHooks {
    /**
     * Called in {@link net.minecraft.world.level.levelgen.structure.structures.EndCityStructure#findGenerationPoint(Structure.GenerationContext)}
     * to fix End Cities not generating in void worlds.
     */
    public static BlockPos adjustPos(BlockPos pos, Structure.GenerationContext context) {
        if (context.chunkGenerator().getClass() == VoidChunkGenerator.class) {
            return new BlockPos(pos.getX(), 64, pos.getZ());
        } else {
            return pos;
        }
    }

    /**
     * Called in {@link net.minecraft.world.level.dimension.end.EndDragonFight#spawnExitPortal(boolean)}
     * right before EndPodiumFeature.place is called to fix End Portal not spawning fully,
     * with part of it being generated outside the world in the void.
     */
    public static BlockPos prePlaceEndPodium(BlockPos pos) {
        if (pos.getY() < 4) {
            return pos.above(32);
        } else {
            return pos.immutable();
        }
    }
}
