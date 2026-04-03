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

package thedarkcolour.exdeorum.voidworld;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import thedarkcolour.exdeorum.config.EConfig;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class VoidChunkGenerator extends NoiseBasedChunkGenerator {
    public static final MapCodec<VoidChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings),
            TagKey.codec(Registries.STRUCTURE_SET).fieldOf("allowed_structure_sets").forGetter(gen -> gen.allowedStructureSets)
    ).apply(inst, inst.stable(VoidChunkGenerator::new)));
    private final Holder<NoiseGeneratorSettings> settings;
    private final TagKey<StructureSet> allowedStructureSets;
    private final boolean generateNormal;
    private final boolean allowBiomeDecoration;

    public VoidChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, TagKey<StructureSet> allowedStructureSets) {
        super(biomeSource, settings);
        this.settings = settings;
        this.allowedStructureSets = allowedStructureSets;
        this.generateNormal = (settings.is(Identifier.parse("minecraft:end")) && !EConfig.COMMON.voidEndGeneration.get()) || (settings.is(Identifier.parse("minecraft:nether")) && !EConfig.COMMON.voidNetherGeneration.get());
        this.allowBiomeDecoration = !settings.is(Identifier.parse("minecraft:overworld"));
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk) {
        if (this.generateNormal) {
            super.applyCarvers(pLevel, pSeed, pRandom, pBiomeManager, pStructureManager, pChunk);
        }
    }

    // Filter structures
    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> lookup, RandomState pRandomState, long pSeed) {
        return this.generateNormal ? super.createState(lookup, pRandomState, pSeed) : super.createState(new FilteredLookup(lookup, this.allowedStructureSets), pRandomState, pSeed);
    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
        if (this.generateNormal) {
            super.buildSurface(pLevel, pStructureManager, pRandom, pChunk);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {
        if (this.generateNormal) {
            super.spawnOriginalMobs(pLevel);
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState random, StructureManager manager, ChunkAccess chunk) {
        if (this.generateNormal) {
            return super.fillFromNoise(blender, random, manager, chunk);
        } else {
            return CompletableFuture.completedFuture(chunk);
        }
    }

    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState pRandom) {
        if (this.generateNormal) {
            return super.getBaseHeight(pX, pZ, pType, pLevel, pRandom);
        } else {
            return getMinY();
        }
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        if (this.generateNormal) {
            return super.getBaseColumn(pX, pZ, pHeight, pRandom);
        } else {
            return new NoiseColumn(0, new BlockState[0]);
        }
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {
        if (this.generateNormal) {
            super.addDebugScreenInfo(pInfo, pRandom, pPos);
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel pLevel, ChunkAccess pChunk, StructureManager pStructureManager) {
        if (this.generateNormal || this.allowBiomeDecoration) {
            super.applyBiomeDecoration(pLevel, pChunk, pStructureManager);
        }
    }

    @Override
    public void createReferences(WorldGenLevel level, StructureManager pStructureManager, ChunkAccess pChunk) {
        if (this.generateNormal || hasStructures(level.registryAccess())) {
            super.createReferences(level, pStructureManager, pChunk);
        }
    }

    @Override
    public void createStructures(RegistryAccess registries, ChunkGeneratorStructureState pStructureState, StructureManager pStructureManager, ChunkAccess pChunk, StructureTemplateManager pStructureTemplateManager) {
        if (this.generateNormal || hasStructures(registries)) {
            super.createStructures(registries, pStructureState, pStructureManager, pChunk, pStructureTemplateManager);
        }
    }

    private boolean hasStructures(RegistryAccess registries) {
        return registries.registryOrThrow(Registries.STRUCTURE_SET).getTagOrEmpty(this.allowedStructureSets).iterator().hasNext();
    }

    private record FilteredLookup(HolderLookup<StructureSet> parent,
                                  TagKey<StructureSet> allowedValues) implements HolderLookup<StructureSet> {
        @Override
        public Optional<Holder.Reference<StructureSet>> get(ResourceKey<StructureSet> key) {
            return this.parent.get(key).filter(obj -> obj.is(this.allowedValues));
        }

        @Override
        public Optional<HolderSet.Named<StructureSet>> get(TagKey<StructureSet> tagKey) {
            return this.parent.get(tagKey);
        }

        @Override
        public Stream<Holder.Reference<StructureSet>> listElements() {
            return this.parent.listElements().filter(obj -> obj.is(this.allowedValues));
        }

        @Override
        public Stream<HolderSet.Named<StructureSet>> listTags() {
            return this.parent.listTags();
        }
    }
}
