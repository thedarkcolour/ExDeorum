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

package thedarkcolour.exdeorum.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

// Syncs certain block entity data to the client for visual purposes
// Since some block entities might change their data multiple times a tick, this class keeps track of
// whether a block entity has updated and then pushes out the changes once at the end of each tick.
public class VisualUpdateTracker {
    // WeakHashMap is faster than Guava mapmaker because it isn't thread safe
    // Use sets to avoid duplicate updates
    private static final Map<LevelChunk, Set<BlockPos>> UPDATES = new WeakHashMap<>();

    public static void sendVisualUpdate(EBlockEntity blockEntity) {
        var level = blockEntity.getLevel();

        if (level != null && !level.isClientSide) {
            var dimension = level.getChunkAt(blockEntity.getBlockPos());
            Set<BlockPos> updatesList;
            if (!UPDATES.containsKey(dimension)) {
                UPDATES.put(dimension, updatesList = new HashSet<>());
            } else {
                updatesList = UPDATES.get(dimension);
            }
            updatesList.add(blockEntity.getBlockPos());
        }
    }

    public static void syncVisualUpdates() {
        for (var entry : UPDATES.entrySet()) {
            var pendingUpdates = entry.getValue();

            for (var updatePos : pendingUpdates) {
                var chunk = entry.getKey();

                if (chunk.getBlockEntity(updatePos) instanceof EBlockEntity blockEntity) {
                    // packet uses strong reference
                    PacketDistributor.TRACKING_CHUNK.with(chunk).send(new VisualUpdateMessage(updatePos, blockEntity, blockEntity.getType(), null));
                }
            }

            pendingUpdates.clear();
        }
    }
}
