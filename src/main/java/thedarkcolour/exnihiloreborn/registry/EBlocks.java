package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.block.BarrelBlock;
import thedarkcolour.exnihiloreborn.block.HeavySieveBlock;
import thedarkcolour.exnihiloreborn.block.InfestedLeavesBlock;
import thedarkcolour.exnihiloreborn.block.LavaCrucibleBlock;
import thedarkcolour.exnihiloreborn.block.SieveBlock;
import thedarkcolour.exnihiloreborn.block.UnfiredCrucibleBlock;
import thedarkcolour.exnihiloreborn.block.WaterCrucibleBlock;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public class EBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExNihiloReborn.ID);

    // Materials
    public static final RegistryObject<Block> DUST = BLOCKS.register("dust", () -> new Block(of().sound(SoundType.WOOL).strength(0.4f)));
    public static final RegistryObject<Block> CRUSHED_NETHERRACK = BLOCKS.register("crushed_netherrack", () -> new Block(of().sound(SoundType.WOOL).strength(0.6f)));
    public static final RegistryObject<Block> CRUSHED_END_STONE = BLOCKS.register("crushed_end_stone", () -> new Block(of().sound(SoundType.WOOL).strength(0.6f)));

    // Compressed Blocks
    public static final RegistryObject<Block> COMPRESSED_COBBLESTONE = BLOCKS.register("compressed_cobblestone", () -> new Block(Block.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> COMPRESSED_DIRT = BLOCKS.register("compressed_dirt", () -> new Block(Block.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> COMPRESSED_SAND = BLOCKS.register("compressed_sand", () -> new Block(Block.Properties.copy(Blocks.SAND)));
    public static final RegistryObject<Block> COMPRESSED_DUST = BLOCKS.register("compressed_dust", () -> new Block(Block.Properties.copy(DUST.get())));
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_NETHERRACK = BLOCKS.register("compressed_crushed_netherrack", () -> new Block(Block.Properties.copy(CRUSHED_NETHERRACK.get())));
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_END_STONE = BLOCKS.register("compressed_crushed_end_stone", () -> new Block(Block.Properties.copy(CRUSHED_END_STONE.get())));

    // Barrels
    public static final RegistryObject<BarrelBlock> OAK_BARREL = registerBarrel("oak_barrel", false);
    public static final RegistryObject<BarrelBlock> SPRUCE_BARREL = registerBarrel("spruce_barrel", false);
    public static final RegistryObject<BarrelBlock> BIRCH_BARREL = registerBarrel("birch_barrel", false);
    public static final RegistryObject<BarrelBlock> JUNGLE_BARREL = registerBarrel("jungle_barrel", false);
    public static final RegistryObject<BarrelBlock> ACACIA_BARREL = registerBarrel("acacia_barrel", false);
    public static final RegistryObject<BarrelBlock> DARK_OAK_BARREL = registerBarrel("dark_oak_barrel", false);
    public static final RegistryObject<BarrelBlock> CRIMSON_BARREL = registerBarrel("crimson_barrel", false);
    public static final RegistryObject<BarrelBlock> WARPED_BARREL = registerBarrel("warped_barrel", false);
    public static final RegistryObject<BarrelBlock> STONE_BARREL = registerBarrel("stone_barrel", true);

    // Sieves
    public static final RegistryObject<SieveBlock> OAK_SIEVE = registerSieve("oak_sieve");
    public static final RegistryObject<SieveBlock> SPRUCE_SIEVE = registerSieve("spruce_sieve");
    public static final RegistryObject<SieveBlock> BIRCH_SIEVE = registerSieve("birch_sieve");
    public static final RegistryObject<SieveBlock> JUNGLE_SIEVE = registerSieve("jungle_sieve");
    public static final RegistryObject<SieveBlock> ACACIA_SIEVE = registerSieve("acacia_sieve");
    public static final RegistryObject<SieveBlock> DARK_OAK_SIEVE = registerSieve("dark_oak_sieve");
    public static final RegistryObject<SieveBlock> CRIMSON_SIEVE = registerSieve("crimson_sieve");
    public static final RegistryObject<SieveBlock> WARPED_SIEVE = registerSieve("warped_sieve");

    // Compressed Sieves
    //public static final RegistryObject<HeavySieveBlock> HEAVY_OAK_SIEVE = registerHeavySieve("heavy_oak_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_SPRUCE_SIEVE = registerHeavySieve("heavy_spruce_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_BIRCH_SIEVE = registerHeavySieve("heavy_birch_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_JUNGLE_SIEVE = registerHeavySieve("heavy_jungle_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_ACACIA_SIEVE = registerHeavySieve("heavy_acacia_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_DARK_OAK_SIEVE = registerHeavySieve("heavy_dark_oak_sieve", Material.WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_CRIMSON_SIEVE = registerHeavySieve("heavy_crimson_sieve", Material.NETHER_WOOD);
    //public static final RegistryObject<HeavySieveBlock> HEAVY_WARPED_SIEVE = registerHeavySieve("heavy_warped_sieve", Material.NETHER_WOOD);

    // Lava Crucibles
    public static final RegistryObject<LavaCrucibleBlock> PORCELAIN_CRUCIBLE = registerLavaCrucible("porcelain_crucible", false);
    public static final RegistryObject<LavaCrucibleBlock> WARPED_CRUCIBLE = registerLavaCrucible("warped_crucible", true);
    public static final RegistryObject<LavaCrucibleBlock> CRIMSON_CRUCIBLE = registerLavaCrucible("crimson_crucible", true);
    public static final RegistryObject<UnfiredCrucibleBlock> UNFIRED_CRUCIBLE = BLOCKS.register("unfired_crucible", () -> new UnfiredCrucibleBlock(of(Material.STONE).strength(2.0f).harvestTool(ToolType.PICKAXE)));

    // Water Crucibles
    public static final RegistryObject<WaterCrucibleBlock> OAK_CRUCIBLE = registerWaterCrucible("oak_crucible");
    public static final RegistryObject<WaterCrucibleBlock> SPRUCE_CRUCIBLE = registerWaterCrucible("spruce_crucible");
    public static final RegistryObject<WaterCrucibleBlock> BIRCH_CRUCIBLE = registerWaterCrucible("birch_crucible");
    public static final RegistryObject<WaterCrucibleBlock> JUNGLE_CRUCIBLE = registerWaterCrucible("jungle_crucible");
    public static final RegistryObject<WaterCrucibleBlock> ACACIA_CRUCIBLE = registerWaterCrucible("acacia_crucible");
    public static final RegistryObject<WaterCrucibleBlock> DARK_OAK_CRUCIBLE = registerWaterCrucible("dark_oak_crucible");

    // Misc
    public static final RegistryObject<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", () -> new InfestedLeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<FlowingFluidBlock> WITCH_WATER = BLOCKS.register("witch_water", () -> new FlowingFluidBlock(EFluids.WITCH_WATER, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

    public static RegistryObject<SieveBlock> registerSieve(String name) {
        return BLOCKS.register(name, () -> new SieveBlock(of().strength(2.0f).noOcclusion().sound(SoundType.WOOD)));
    }

    public static RegistryObject<HeavySieveBlock> registerHeavySieve(String name) {
        return BLOCKS.register(name, () -> new HeavySieveBlock(of(material).strength(2.0f).sound(SoundType.WOOD)));
    }

    public static RegistryObject<BarrelBlock> registerBarrel(String name, boolean stone) {
        return BLOCKS.register(name, () -> new BarrelBlock(of(material).noOcclusion().strength(stone ? 4.0f : 2.0f).sound(stone ? SoundType.STONE : SoundType.WOOD)));
    }

    public static RegistryObject<LavaCrucibleBlock> registerLavaCrucible(String name, boolean stem) {
        return BLOCKS.register(name, () -> {
            var props = of().noOcclusion().strength(stem ? 1.5f : 2.0f).sound(stem ? SoundType.STEM : SoundType.STONE);
            if (!stem) {
                props.requiresCorrectToolForDrops();
            }
            return new LavaCrucibleBlock(props);
        });
    }

    public static RegistryObject<WaterCrucibleBlock> registerWaterCrucible(String name) {
        return BLOCKS.register(name, () -> new WaterCrucibleBlock(of(Material.WOOD).strength(1.5f).harvestTool(ToolType.AXE).sound(SoundType.WOOD)));
    }
}
