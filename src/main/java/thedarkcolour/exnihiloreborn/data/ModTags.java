package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.tag.EItemTags;
import thedarkcolour.exnihiloreborn.tag.EStructureSetTags;
import thedarkcolour.modkit.data.MKTagsProvider;

class ModTags {
    public static void createBlockTags(MKTagsProvider<Block> tags) {
        tags.tag(BlockTags.MINEABLE_WITH_AXE).add(EBlocks.OAK_BARREL.get(), EBlocks.SPRUCE_BARREL.get(), EBlocks.BIRCH_BARREL.get(), EBlocks.JUNGLE_BARREL.get(), EBlocks.ACACIA_BARREL.get(), EBlocks.DARK_OAK_BARREL.get(), EBlocks.MANGROVE_BARREL.get(), EBlocks.CHERRY_BARREL.get(), EBlocks.BAMBOO_BARREL.get(), EBlocks.CRIMSON_BARREL.get(), EBlocks.WARPED_BARREL.get(), EBlocks.OAK_SIEVE.get(), EBlocks.SPRUCE_SIEVE.get(), EBlocks.BIRCH_SIEVE.get(), EBlocks.JUNGLE_SIEVE.get(), EBlocks.ACACIA_SIEVE.get(), EBlocks.DARK_OAK_SIEVE.get(), EBlocks.MANGROVE_SIEVE.get(), EBlocks.CHERRY_SIEVE.get(), EBlocks.BAMBOO_SIEVE.get(), EBlocks.CRIMSON_SIEVE.get(), EBlocks.WARPED_SIEVE.get(), EBlocks.WARPED_CRUCIBLE.get(), EBlocks.CRIMSON_CRUCIBLE.get(), EBlocks.OAK_CRUCIBLE.get(), EBlocks.SPRUCE_CRUCIBLE.get(), EBlocks.BIRCH_CRUCIBLE.get(), EBlocks.JUNGLE_CRUCIBLE.get(), EBlocks.ACACIA_CRUCIBLE.get(), EBlocks.DARK_OAK_CRUCIBLE.get(), EBlocks.MANGROVE_CRUCIBLE.get(), EBlocks.CHERRY_CRUCIBLE.get(), EBlocks.BAMBOO_CRUCIBLE.get());
        tags.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(EBlocks.STONE_BARREL.get(), EBlocks.PORCELAIN_CRUCIBLE.get(), EBlocks.UNFIRED_CRUCIBLE.get());
    }

    public static void createItemTags(MKTagsProvider<Item> tags) {
        tags.tag(EItemTags.HAMMERS).add(EItems.WOODEN_HAMMER, EItems.STONE_HAMMER, EItems.GOLDEN_HAMMER, EItems.IRON_HAMMER, EItems.DIAMOND_HAMMER, EItems.NETHERITE_HAMMER);
        tags.tag(EItemTags.CROOKS).add(EItems.CROOK, EItems.BONE_CROOK);
        tags.tag(EItemTags.SIEVE_MESHES).add(EItems.STRING_MESH, EItems.FLINT_MESH, EItems.IRON_MESH, EItems.GOLDEN_MESH, EItems.DIAMOND_MESH, EItems.NETHERITE_MESH);
        tags.tag(EItemTags.PEBBLES).add(EItems.STONE_PEBBLE, EItems.DIORITE_PEBBLE, EItems.GRANITE_PEBBLE, EItems.ANDESITE_PEBBLE, EItems.DEEPSLATE_PEBBLE, EItems.TUFF_PEBBLE);
        tags.tag(EItemTags.WOODEN_BARRELS).add(EItems.OAK_BARREL.get(), EItems.SPRUCE_BARREL.get(), EItems.BIRCH_BARREL.get(), EItems.JUNGLE_BARREL.get(), EItems.ACACIA_BARREL.get(), EItems.DARK_OAK_BARREL.get(), EItems.MANGROVE_BARREL.get(), EItems.CHERRY_BARREL.get(), EItems.BAMBOO_BARREL.get());
        tags.tag(EItemTags.STONE_BARRELS).add(EItems.STONE_BARREL.get());
        tags.tag(EItemTags.BARRELS).addTags(EItemTags.WOODEN_BARRELS, EItemTags.STONE_BARRELS);
    }

    public static void createStructureSetTags(MKTagsProvider<StructureSet> tags) {
        tags.tag(EStructureSetTags.OVERWORLD_VOID_STRUCTURES);
        tags.tag(EStructureSetTags.THE_NETHER_VOID_STRUCTURES).add(BuiltinStructureSets.NETHER_COMPLEXES);
        tags.tag(EStructureSetTags.THE_END_VOID_STRUCTURES).add(BuiltinStructureSets.END_CITIES);
    }

    public static void createWorldPresetTags(MKTagsProvider<WorldPreset> tags) {
        tags.tag(net.minecraft.tags.WorldPresetTags.NORMAL).add(ResourceKey.create(Registries.WORLD_PRESET, new ResourceLocation(ExNihiloReborn.ID, "void_world")));
    }

    public static void createFluidTags(MKTagsProvider<Fluid> tags) {
        tags.tag(FluidTags.WATER).add(EFluids.WITCH_WATER_STILL.get(), EFluids.WITCH_WATER_FLOWING.get());
    }
}
