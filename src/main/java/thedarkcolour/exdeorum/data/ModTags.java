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

package thedarkcolour.exdeorum.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EBlockTags;
import thedarkcolour.exdeorum.tag.EItemTags;
import thedarkcolour.exdeorum.tag.EStructureSetTags;
import thedarkcolour.modkit.data.MKTagsProvider;

class ModTags {
    public static void createBlockTags(MKTagsProvider<Block> tags) {
        var wateringCanTickable = tags.tag(EBlockTags.WATERING_CAN_TICKABLE);
        wateringCanTickable.add(Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.CACTUS, Blocks.SUGAR_CANE, Blocks.SWEET_BERRY_BUSH, Blocks.COCOA).addTags(BlockTags.SAPLINGS, BlockTags.NYLIUM, BlockTags.BEE_GROWABLES);
        for (ResourceLocation path : ModCompatData.PAMS_CROPS) {
            wateringCanTickable.addOptional(path);
        }

        tags.tag(BlockTags.MINEABLE_WITH_AXE).add(
                // Vanilla barrels
                EBlocks.OAK_BARREL.get(), EBlocks.SPRUCE_BARREL.get(), EBlocks.BIRCH_BARREL.get(), EBlocks.JUNGLE_BARREL.get(), EBlocks.ACACIA_BARREL.get(), EBlocks.DARK_OAK_BARREL.get(), EBlocks.MANGROVE_BARREL.get(), EBlocks.CHERRY_BARREL.get(), EBlocks.BAMBOO_BARREL.get(), EBlocks.CRIMSON_BARREL.get(), EBlocks.WARPED_BARREL.get(),
                // BOP barrels
                EBlocks.FIR_BARREL.get(), EBlocks.REDWOOD_BARREL.get(), EBlocks.MAHOGANY_BARREL.get(), EBlocks.JACARANDA_BARREL.get(), EBlocks.PALM_BARREL.get(), EBlocks.WILLOW_BARREL.get(), EBlocks.DEAD_BARREL.get(), EBlocks.MAGIC_BARREL.get(), EBlocks.UMBRAN_BARREL.get(), EBlocks.HELLBARK_BARREL.get(),
                // Ars Nouveau barrels
                EBlocks.ARCHWOOD_BARREL.get(),
                // Aether barrels
                EBlocks.SKYROOT_BARREL.get(),
                // Blue Skies barrels
                EBlocks.BLUEBRIGHT_BARREL.get(), EBlocks.STARLIT_BARREL.get(), EBlocks.FROSTBRIGHT_BARREL.get(), EBlocks.COMET_BARREL.get(), EBlocks.LUNAR_BARREL.get(), EBlocks.DUSK_BARREL.get(), EBlocks.MAPLE_BARREL.get(),
                // Vanilla sieves
                EBlocks.OAK_SIEVE.get(), EBlocks.SPRUCE_SIEVE.get(), EBlocks.BIRCH_SIEVE.get(), EBlocks.JUNGLE_SIEVE.get(), EBlocks.ACACIA_SIEVE.get(), EBlocks.DARK_OAK_SIEVE.get(), EBlocks.MANGROVE_SIEVE.get(), EBlocks.CHERRY_SIEVE.get(), EBlocks.BAMBOO_SIEVE.get(), EBlocks.CRIMSON_SIEVE.get(), EBlocks.WARPED_SIEVE.get(),
                // BOP sieves
                EBlocks.FIR_SIEVE.get(), EBlocks.REDWOOD_SIEVE.get(), EBlocks.MAHOGANY_SIEVE.get(), EBlocks.JACARANDA_SIEVE.get(), EBlocks.PALM_SIEVE.get(), EBlocks.WILLOW_SIEVE.get(), EBlocks.DEAD_SIEVE.get(), EBlocks.MAGIC_SIEVE.get(), EBlocks.UMBRAN_SIEVE.get(), EBlocks.HELLBARK_SIEVE.get(),
                // Ars Nouveau sieves
                EBlocks.ARCHWOOD_SIEVE.get(),
                // Aether sieves
                EBlocks.SKYROOT_SIEVE.get(),
                // Blue Skies sieves
                EBlocks.BLUEBRIGHT_SIEVE.get(), EBlocks.STARLIT_SIEVE.get(), EBlocks.FROSTBRIGHT_SIEVE.get(), EBlocks.COMET_SIEVE.get(), EBlocks.LUNAR_SIEVE.get(), EBlocks.DUSK_SIEVE.get(), EBlocks.MAPLE_SIEVE.get(),
                // Vanilla crucibles
                EBlocks.WARPED_CRUCIBLE.get(), EBlocks.CRIMSON_CRUCIBLE.get(), EBlocks.OAK_CRUCIBLE.get(), EBlocks.SPRUCE_CRUCIBLE.get(), EBlocks.BIRCH_CRUCIBLE.get(), EBlocks.JUNGLE_CRUCIBLE.get(), EBlocks.ACACIA_CRUCIBLE.get(), EBlocks.DARK_OAK_CRUCIBLE.get(), EBlocks.MANGROVE_CRUCIBLE.get(), EBlocks.CHERRY_CRUCIBLE.get(), EBlocks.BAMBOO_CRUCIBLE.get(),
                // BOP crucibles
                EBlocks.FIR_CRUCIBLE.get(), EBlocks.REDWOOD_CRUCIBLE.get(), EBlocks.MAHOGANY_CRUCIBLE.get(), EBlocks.JACARANDA_CRUCIBLE.get(), EBlocks.PALM_CRUCIBLE.get(), EBlocks.WILLOW_CRUCIBLE.get(), EBlocks.DEAD_CRUCIBLE.get(), EBlocks.MAGIC_CRUCIBLE.get(), EBlocks.UMBRAN_CRUCIBLE.get(), EBlocks.HELLBARK_CRUCIBLE.get(),
                // Ars Nouveau crucibles
                EBlocks.CASCADING_ARCHWOOD_CRUCIBLE.get(), EBlocks.BLAZING_ARCHWOOD_CRUCIBLE.get(), EBlocks.VEXING_ARCHWOOD_CRUCIBLE.get(), EBlocks.FLOURISHING_ARCHWOOD_CRUCIBLE.get(),
                // Aether crucibles
                EBlocks.SKYROOT_CRUCIBLE.get(), EBlocks.GOLDEN_OAK_CRUCIBLE.get(),
                // Blue Skies crucibles
                EBlocks.BLUEBRIGHT_CRUCIBLE.get(), EBlocks.STARLIT_CRUCIBLE.get(), EBlocks.FROSTBRIGHT_CRUCIBLE.get(), EBlocks.COMET_CRUCIBLE.get(), EBlocks.LUNAR_CRUCIBLE.get(), EBlocks.DUSK_CRUCIBLE.get(), EBlocks.MAPLE_CRUCIBLE.get()
        );
        tags.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(EBlocks.STONE_BARREL, EBlocks.PORCELAIN_CRUCIBLE, EBlocks.UNFIRED_PORCELAIN_CRUCIBLE, EBlocks.CRYSTALLIZED_BARREL, EBlocks.CRYSTALLIZED_CRUCIBLE, EBlocks.CRYSTALLIZED_SIEVE, EBlocks.MECHANICAL_SIEVE, EBlocks.MECHANICAL_HAMMER);
        tags.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(EBlocks.DUST, EBlocks.CRUSHED_NETHERRACK, EBlocks.CRUSHED_END_STONE, EBlocks.CRUSHED_DEEPSLATE, EBlocks.CRUSHED_BLACKSTONE);
        tags.tag(BlockTags.MINEABLE_WITH_HOE).add(EBlocks.INFESTED_LEAVES);
        tags.tag(BlockTags.LEAVES).add(EBlocks.INFESTED_LEAVES);
    }

    public static void createItemTags(MKTagsProvider<Item> tags) {
        tags.tag(EItemTags.HAMMERS).add(EItems.WOODEN_HAMMER, EItems.STONE_HAMMER, EItems.GOLDEN_HAMMER, EItems.IRON_HAMMER, EItems.DIAMOND_HAMMER, EItems.NETHERITE_HAMMER);
        tags.tag(EItemTags.CROOKS).add(EItems.CROOK, EItems.BONE_CROOK);
        tags.tag(EItemTags.SIEVE_MESHES).add(EItems.STRING_MESH, EItems.FLINT_MESH, EItems.IRON_MESH, EItems.GOLDEN_MESH, EItems.DIAMOND_MESH, EItems.NETHERITE_MESH);
        tags.tag(EItemTags.PEBBLES).add(EItems.STONE_PEBBLE, EItems.DIORITE_PEBBLE, EItems.GRANITE_PEBBLE, EItems.ANDESITE_PEBBLE, EItems.DEEPSLATE_PEBBLE, EItems.TUFF_PEBBLE, EItems.CALCITE_PEBBLE, EItems.BLACKSTONE_PEBBLE, EItems.BASALT_PEBBLE);
        tags.tag(EItemTags.END_CAKE_MATERIAL).add(Items.ENDER_EYE);
        tags.tag(EItemTags.WOODEN_BARRELS).add(
                EItems.OAK_BARREL.get(), EItems.SPRUCE_BARREL.get(), EItems.BIRCH_BARREL.get(), EItems.JUNGLE_BARREL.get(), EItems.ACACIA_BARREL.get(), EItems.DARK_OAK_BARREL.get(), EItems.MANGROVE_BARREL.get(), EItems.CHERRY_BARREL.get(), EItems.BAMBOO_BARREL.get(),
                // BOP barrels
                EItems.FIR_BARREL.get(), EItems.REDWOOD_BARREL.get(), EItems.MAHOGANY_BARREL.get(), EItems.JACARANDA_BARREL.get(), EItems.PALM_BARREL.get(), EItems.WILLOW_BARREL.get(), EItems.DEAD_BARREL.get(), EItems.MAGIC_BARREL.get(), EItems.UMBRAN_BARREL.get(), EItems.HELLBARK_BARREL.get(),
                // Ars Nouveau barrels
                EItems.ARCHWOOD_BARREL.get(),
                // Aether barrels
                EItems.SKYROOT_BARREL.get(),
                // Blue Skies barrels
                EItems.BLUEBRIGHT_BARREL.get(), EItems.STARLIT_BARREL.get(), EItems.FROSTBRIGHT_BARREL.get(), EItems.COMET_BARREL.get(), EItems.LUNAR_BARREL.get(), EItems.DUSK_BARREL.get(), EItems.MAPLE_BARREL.get()
        );
        tags.tag(EItemTags.STONE_BARRELS).add(EItems.STONE_BARREL, EItems.CRYSTALLIZED_BARREL);
        tags.tag(EItemTags.BARRELS).addTags(EItemTags.WOODEN_BARRELS, EItemTags.STONE_BARRELS);
    }

    public static void createStructureSetTags(MKTagsProvider<StructureSet> tags) {
        tags.tag(EStructureSetTags.OVERWORLD_VOID_STRUCTURES);
        tags.tag(EStructureSetTags.THE_NETHER_VOID_STRUCTURES).add(BuiltinStructureSets.NETHER_COMPLEXES);
        tags.tag(EStructureSetTags.THE_END_VOID_STRUCTURES).add(BuiltinStructureSets.END_CITIES);
    }

    public static void createWorldPresetTags(MKTagsProvider<WorldPreset> tags) {
        tags.tag(net.minecraft.tags.WorldPresetTags.NORMAL).add(ResourceKey.create(Registries.WORLD_PRESET, new ResourceLocation(ExDeorum.ID, "void_world")));
    }

    public static void createFluidTags(MKTagsProvider<Fluid> tags) {
        tags.tag(FluidTags.WATER).add(EFluids.WITCH_WATER.get(), EFluids.WITCH_WATER_FLOWING.get());
    }
}
