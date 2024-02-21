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
import thedarkcolour.exdeorum.material.BarrelMaterial;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.material.SieveMaterial;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EBlockTags;
import thedarkcolour.exdeorum.tag.EItemTags;
import thedarkcolour.exdeorum.tag.EStructureSetTags;
import thedarkcolour.modkit.data.MKTagsProvider;

import java.util.ArrayList;
import java.util.List;

class ModTags {
    private static final List<BarrelMaterial> STONE_MATERIALS = List.of(DefaultMaterials.STONE_BARREL, DefaultMaterials.CRYSTALLIZED_BARREL);
    private static final List<BarrelMaterial> WOODEN_BARRELS = new ArrayList<>();

    static {
        for (var material : DefaultMaterials.BARRELS) {
            if (!STONE_MATERIALS.contains(material)) {
                WOODEN_BARRELS.add(material);
            }
        }
    }

    public static void createBlockTags(MKTagsProvider<Block> tags) {
        var wateringCanTickable = tags.tag(EBlockTags.WATERING_CAN_TICKABLE);
        wateringCanTickable.add(Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.CACTUS, Blocks.SUGAR_CANE, Blocks.SWEET_BERRY_BUSH, Blocks.COCOA).addTags(BlockTags.SAPLINGS, BlockTags.NYLIUM, BlockTags.BEE_GROWABLES);
        for (ResourceLocation path : ModCompatData.PAMS_CROPS) {
            wateringCanTickable.addOptional(path);
        }

        tags.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(WOODEN_BARRELS.stream().map(BarrelMaterial::getBlock).toArray(Block[]::new))
                .add(DefaultMaterials.SIEVES.stream().filter(material -> material != DefaultMaterials.CRYSTALLIZED_SIEVE).map(SieveMaterial::getBlock).toArray(Block[]::new))
                .add(
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
        tags.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(EBlocks.PORCELAIN_CRUCIBLE, EBlocks.UNFIRED_PORCELAIN_CRUCIBLE, EBlocks.CRYSTALLIZED_CRUCIBLE, EBlocks.MECHANICAL_SIEVE, EBlocks.MECHANICAL_HAMMER)
                .add(DefaultMaterials.STONE_BARREL.getBlock(), DefaultMaterials.CRYSTALLIZED_BARREL.getBlock(), DefaultMaterials.CRYSTALLIZED_SIEVE.getBlock());
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
        tags.tag(EItemTags.WOODEN_BARRELS).add(WOODEN_BARRELS.stream().map(BarrelMaterial::getItem).toArray(Item[]::new));
        tags.tag(EItemTags.STONE_BARRELS).add(DefaultMaterials.STONE_BARREL.getItem(), DefaultMaterials.CRYSTALLIZED_BARREL.getItem());
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
