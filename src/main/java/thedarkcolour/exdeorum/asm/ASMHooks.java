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

package thedarkcolour.exdeorum.asm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.fml.ModList;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.registry.EWorldPresets;
import thedarkcolour.exdeorum.voidworld.VoidChunkGenerator;

import java.util.Properties;

@SuppressWarnings("unused")
public final class ASMHooks {
    /**
     * Called in {@link net.minecraft.world.level.levelgen.structure.structures.EndCityStructure#findGenerationPoint(Structure.GenerationContext)}
     * by {@link thedarkcolour.exdeorum.coremod.EndCityStructureTransformer}
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
     * Called in {@link net.minecraft.server.dedicated.DedicatedServerProperties#DedicatedServerProperties(Properties)}
     * by {@link thedarkcolour.exdeorum.coremod.DedicatedServerPropertiesTransformer}
     * where {@code WorldPresets.NORMAL} is used in the line that looks like {@code WorldPresets.NORMAL.location().toString()}
     */
    public static ResourceKey<WorldPreset> overrideDefaultWorldPreset() {
        if (ModList.get().isLoaded(ModIds.SKYBLOCK_BUILDER)) {
            return ResourceKey.create(Registries.WORLD_PRESET, Identifier.fromNamespaceAndPath(ModIds.SKYBLOCK_BUILDER, "skyblock"));
        }
        return EConfig.COMMON.setVoidWorldAsDefault.get() ? EWorldPresets.VOID_WORLD : WorldPresets.NORMAL;
    }
}
