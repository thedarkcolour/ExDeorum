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
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Syncs certain block entity data to the client for visual purposes
// Since some block entities might change their data multiple times a tick, this class keeps track of
// whether a block entity has updated and then pushes out the changes once at the end of each tick.
public class VisualUpdateTracker {
    // Use sets to avoid duplicate updates
    private static final Map<ResourceKey<Level>, Map<ChunkPos, Set<BlockPos>>> UPDATES = new HashMap<>();

    public static void sendVisualUpdate(EBlockEntity blockEntity) {
        var level = blockEntity.getLevel();

        if (level != null && !level.isClientSide()) {
            Map<ChunkPos, Set<BlockPos>> chunkUpdates = UPDATES.computeIfAbsent(level.dimension(), key -> new HashMap<>());
            chunkUpdates.computeIfAbsent(new ChunkPos(blockEntity.getBlockPos().getX() >> 4, blockEntity.getBlockPos().getZ() >> 4), key -> new HashSet<>()).add(blockEntity.getBlockPos());
        }
    }

    public static void syncVisualUpdates(MinecraftServer server) {
        for (var levelUpdates : UPDATES.entrySet()) {
            var level = server.getLevel(levelUpdates.getKey());

            if (level != null) {
                var pendingUpdates = levelUpdates.getValue();

                for (var chunkUpdates : pendingUpdates.entrySet()) {
                    var chunkPos = chunkUpdates.getKey();

                    for (var updatePos : chunkUpdates.getValue()) {
                        if (level.getBlockEntity(updatePos) instanceof EBlockEntity blockEntity) {
                            PacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, new VisualUpdateMessage(updatePos, blockEntity, blockEntity.getType(), null));
                        }
                    }
                }

                pendingUpdates.clear();
            }
        }
    }
}
