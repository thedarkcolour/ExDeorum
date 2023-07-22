package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.item.CrookItem;
import thedarkcolour.exnihiloreborn.item.ETab;
import thedarkcolour.exnihiloreborn.item.FluidBucketItem;
import thedarkcolour.exnihiloreborn.item.HammerItem;
import thedarkcolour.exnihiloreborn.item.SilkWormItem;

public class EItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExNihiloReborn.ID);

    // Silk Worm
    public static final RegistryObject<SilkWormItem> SILK_WORM = ITEMS.register("silk_worm", () -> new SilkWormItem(props()));
    public static final RegistryObject<Item> COOKED_SILK_WORM = ITEMS.register("cooked_silk_worm", () -> new Item(props().food(new Food.Builder().nutrition(2).saturationMod(0.6f).build())));

    // Crooks
    public static final RegistryObject<Item> CROOK = ITEMS.register("crook", () -> new CrookItem(props().durability(64)));
    public static final RegistryObject<Item> COMPRESSED_CROOK = ITEMS.register("compressed_crook", () -> new CrookItem(props().durability(192)));
    public static final RegistryObject<Item> BONE_CROOK = ITEMS.register("bone_crook", () -> new CrookItem(props().durability(256)));

    // Sieve Meshes
    public static final RegistryObject<Item> STRING_MESH = ITEMS.register("string_mesh", () -> new Item(props().stacksTo(1)));
    public static final RegistryObject<Item> FLINT_MESH = ITEMS.register("flint_mesh", () -> new Item(props().stacksTo(1)));
    public static final RegistryObject<Item> IRON_MESH = ITEMS.register("iron_mesh", () -> new Item(props().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new Item(props().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_MESH = ITEMS.register("netherite_mesh", () -> new Item(props().stacksTo(1)));

    // Hammers
    public static final RegistryObject<Item> WOODEN_HAMMER = ITEMS.register("wooden_hammer", () -> new HammerItem(ItemTier.WOOD, props()));
    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer", () -> new HammerItem(ItemTier.STONE, props()));
    public static final RegistryObject<Item> GOLDEN_HAMMER = ITEMS.register("golden_hammer", () -> new HammerItem(ItemTier.GOLD, props()));
    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer", () -> new HammerItem(ItemTier.IRON, props()));
    public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer", () -> new HammerItem(ItemTier.DIAMOND, props()));
    public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer", () -> new HammerItem(ItemTier.NETHERITE, props()));

    // Compressed Hammers
    public static final RegistryObject<Item> COMPRESSED_WOODEN_HAMMER = ITEMS.register("compressed_wooden_hammer", () -> new HammerItem(ItemTier.WOOD, props()));
    public static final RegistryObject<Item> COMPRESSED_STONE_HAMMER = ITEMS.register("compressed_stone_hammer", () -> new HammerItem(ItemTier.STONE, props()));
    public static final RegistryObject<Item> COMPRESSED_GOLDEN_HAMMER = ITEMS.register("compressed_golden_hammer", () -> new HammerItem(ItemTier.GOLD, props()));
    public static final RegistryObject<Item> COMPRESSED_IRON_HAMMER = ITEMS.register("compressed_iron_hammer", () -> new HammerItem(ItemTier.IRON, props()));
    public static final RegistryObject<Item> COMPRESSED_DIAMOND_HAMMER = ITEMS.register("compressed_diamond_hammer", () -> new HammerItem(ItemTier.DIAMOND, props()));
    public static final RegistryObject<Item> COMPRESSED_NETHERITE_HAMMER = ITEMS.register("compressed_netherite_hammer", () -> new HammerItem(ItemTier.NETHERITE, props()));

    // Ore Pieces todo re evaluate
    public static final RegistryObject<Item> IRON_ORE_PIECES = registerSimpleItem("iron_ore_pieces");
    public static final RegistryObject<Item> COPPER_ORE_PIECES = registerSimpleItem("copper_ore_pieces");
    public static final RegistryObject<Item> GOLD_ORE_PIECES = registerSimpleItem("gold_ore_pieces");

    // Pebbles
    public static final RegistryObject<Item> STONE_PEBBLE = registerSimpleItem("stone_pebble");
    public static final RegistryObject<Item> DEEPSLATE_PEBBLE = registerSimpleItem("deepslate_pebble");

    // Misc
    public static final RegistryObject<Item> PORCELAIN_CLAY = registerSimpleItem("porcelain_clay");

    // Fluids
    public static final RegistryObject<FluidBucketItem> WITCH_WATER_BUCKET = ITEMS.register("witch_water_bucket", () -> new FluidBucketItem(props().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static RegistryObject<Item> registerSimpleItem(String name) {
        return ITEMS.register(name, () -> new Item(props()));
    }

    // Returns new properties with creative tab set
    public static Item.Properties props() {
        return new Item.Properties();
    }

    // Register a block item
    public static RegistryObject<BlockItem> registerItemBlock(RegistryObject<? extends Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), props()));
    }

    // BlockItems
    public static final RegistryObject<BlockItem> DUST;
    public static final RegistryObject<BlockItem> CRUSHED_NETHERRACK;
    public static final RegistryObject<BlockItem> CRUSHED_END_STONE;

    public static final RegistryObject<BlockItem> COMPRESSED_COBBLESTONE;
    public static final RegistryObject<BlockItem> COMPRESSED_DIRT;
    public static final RegistryObject<BlockItem> COMPRESSED_SAND;
    public static final RegistryObject<BlockItem> COMPRESSED_DUST;
    public static final RegistryObject<BlockItem> COMPRESSED_CRUSHED_NETHERRACK;
    public static final RegistryObject<BlockItem> COMPRESSED_CRUSHED_END_STONE;

    // Barrels
    public static final RegistryObject<BlockItem> OAK_BARREL = registerItemBlock(EBlocks.OAK_BARREL);
    public static final RegistryObject<BlockItem> SPRUCE_BARREL = registerItemBlock(EBlocks.SPRUCE_BARREL);
    public static final RegistryObject<BlockItem> BIRCH_BARREL = registerItemBlock(EBlocks.BIRCH_BARREL);
    public static final RegistryObject<BlockItem> JUNGLE_BARREL = registerItemBlock(EBlocks.JUNGLE_BARREL);
    public static final RegistryObject<BlockItem> ACACIA_BARREL = registerItemBlock(EBlocks.ACACIA_BARREL);
    public static final RegistryObject<BlockItem> DARK_OAK_BARREL = registerItemBlock(EBlocks.DARK_OAK_BARREL);
    public static final RegistryObject<BlockItem> CRIMSON_BARREL = registerItemBlock(EBlocks.CRIMSON_BARREL);
    public static final RegistryObject<BlockItem> WARPED_BARREL = registerItemBlock(EBlocks.WARPED_BARREL);
    public static final RegistryObject<BlockItem> STONE_BARREL = registerItemBlock(EBlocks.STONE_BARREL);

    // Sieves
    public static final RegistryObject<BlockItem> OAK_SIEVE = registerItemBlock(EBlocks.OAK_SIEVE);
    public static final RegistryObject<BlockItem> SPRUCE_SIEVE = registerItemBlock(EBlocks.SPRUCE_SIEVE);
    public static final RegistryObject<BlockItem> BIRCH_SIEVE = registerItemBlock(EBlocks.BIRCH_SIEVE);
    public static final RegistryObject<BlockItem> JUNGLE_SIEVE = registerItemBlock(EBlocks.JUNGLE_SIEVE);
    public static final RegistryObject<BlockItem> ACACIA_SIEVE = registerItemBlock(EBlocks.ACACIA_SIEVE);
    public static final RegistryObject<BlockItem> DARK_OAK_SIEVE = registerItemBlock(EBlocks.DARK_OAK_SIEVE);
    public static final RegistryObject<BlockItem> CRIMSON_SIEVE = registerItemBlock(EBlocks.CRIMSON_SIEVE);
    public static final RegistryObject<BlockItem> WARPED_SIEVE = registerItemBlock(EBlocks.WARPED_SIEVE);

    // Compressed Sieves
    //public static final RegistryObject<BlockItem> HEAVY_OAK_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_SPRUCE_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_BIRCH_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_JUNGLE_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_ACACIA_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_DARK_OAK_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_CRIMSON_SIEVE;
    //public static final RegistryObject<BlockItem> HEAVY_WARPED_SIEVE;

    // Lava Crucibles
    public static final RegistryObject<BlockItem> PORCELAIN_CRUCIBLE = registerItemBlock(EBlocks.PORCELAIN_CRUCIBLE);
    public static final RegistryObject<BlockItem> WARPED_CRUCIBLE = registerItemBlock(EBlocks.WARPED_CRUCIBLE);
    public static final RegistryObject<BlockItem> CRIMSON_CRUCIBLE = registerItemBlock(EBlocks.CRIMSON_CRUCIBLE);
    public static final RegistryObject<BlockItem> UNFIRED_CRUCIBLE = registerItemBlock(EBlocks.UNFIRED_CRUCIBLE);

    // Water Crucibles
    public static final RegistryObject<BlockItem> OAK_CRUCIBLE = registerItemBlock(EBlocks.OAK_CRUCIBLE);
    public static final RegistryObject<BlockItem> SPRUCE_CRUCIBLE = registerItemBlock(EBlocks.SPRUCE_CRUCIBLE);
    public static final RegistryObject<BlockItem> BIRCH_CRUCIBLE = registerItemBlock(EBlocks.BIRCH_CRUCIBLE);
    public static final RegistryObject<BlockItem> JUNGLE_CRUCIBLE = registerItemBlock(EBlocks.JUNGLE_CRUCIBLE);
    public static final RegistryObject<BlockItem> ACACIA_CRUCIBLE = registerItemBlock(EBlocks.ACACIA_CRUCIBLE);
    public static final RegistryObject<BlockItem> DARK_OAK_CRUCIBLE = registerItemBlock(EBlocks.DARK_OAK_CRUCIBLE);

    static {
        DUST = registerItemBlock(EBlocks.DUST);
        CRUSHED_NETHERRACK = registerItemBlock(EBlocks.CRUSHED_NETHERRACK);
        CRUSHED_END_STONE = registerItemBlock(EBlocks.CRUSHED_END_STONE);
        COMPRESSED_COBBLESTONE = registerItemBlock(EBlocks.COMPRESSED_COBBLESTONE);
        COMPRESSED_DIRT = registerItemBlock(EBlocks.COMPRESSED_DIRT);
        COMPRESSED_SAND = registerItemBlock(EBlocks.COMPRESSED_SAND);
        COMPRESSED_DUST = registerItemBlock(EBlocks.COMPRESSED_DUST);
        COMPRESSED_CRUSHED_NETHERRACK = registerItemBlock(EBlocks.COMPRESSED_CRUSHED_NETHERRACK);
        COMPRESSED_CRUSHED_END_STONE = registerItemBlock(EBlocks.COMPRESSED_CRUSHED_END_STONE);
        OAK_BARREL = registerItemBlock(EBlocks.OAK_BARREL);
        SPRUCE_BARREL = registerItemBlock(EBlocks.SPRUCE_BARREL);
        BIRCH_BARREL = registerItemBlock(EBlocks.BIRCH_BARREL);
        JUNGLE_BARREL = registerItemBlock(EBlocks.JUNGLE_BARREL);
        ACACIA_BARREL = registerItemBlock(EBlocks.ACACIA_BARREL);
        DARK_OAK_BARREL = registerItemBlock(EBlocks.DARK_OAK_BARREL);
        CRIMSON_BARREL = registerItemBlock(EBlocks.CRIMSON_BARREL);
        WARPED_BARREL = registerItemBlock(EBlocks.WARPED_BARREL);
        STONE_BARREL = registerItemBlock(EBlocks.STONE_BARREL);
        OAK_SIEVE = registerItemBlock(EBlocks.OAK_SIEVE);
        SPRUCE_SIEVE = registerItemBlock(EBlocks.SPRUCE_SIEVE);
        BIRCH_SIEVE = registerItemBlock(EBlocks.BIRCH_SIEVE);
        JUNGLE_SIEVE = registerItemBlock(EBlocks.JUNGLE_SIEVE);
        ACACIA_SIEVE = registerItemBlock(EBlocks.ACACIA_SIEVE);
        DARK_OAK_SIEVE = registerItemBlock(EBlocks.DARK_OAK_SIEVE);
        CRIMSON_SIEVE = registerItemBlock(EBlocks.CRIMSON_SIEVE);
        WARPED_SIEVE = registerItemBlock(EBlocks.WARPED_SIEVE);
        //HEAVY_OAK_SIEVE = registerItemBlock(EBlocks.HEAVY_OAK_SIEVE);
        //HEAVY_SPRUCE_SIEVE = registerItemBlock(EBlocks.HEAVY_SPRUCE_SIEVE);
        //HEAVY_BIRCH_SIEVE = registerItemBlock(EBlocks.HEAVY_BIRCH_SIEVE);
        //HEAVY_JUNGLE_SIEVE = registerItemBlock(EBlocks.HEAVY_JUNGLE_SIEVE);
        //HEAVY_ACACIA_SIEVE = registerItemBlock(EBlocks.HEAVY_ACACIA_SIEVE);
        //HEAVY_DARK_OAK_SIEVE = registerItemBlock(EBlocks.HEAVY_DARK_OAK_SIEVE);
        //HEAVY_CRIMSON_SIEVE = registerItemBlock(EBlocks.HEAVY_CRIMSON_SIEVE);
        //HEAVY_WARPED_SIEVE = registerItemBlock(EBlocks.HEAVY_WARPED_SIEVE);
        PORCELAIN_CRUCIBLE = registerItemBlock(EBlocks.PORCELAIN_CRUCIBLE);
        WARPED_CRUCIBLE = registerItemBlock(EBlocks.WARPED_CRUCIBLE);
        CRIMSON_CRUCIBLE = registerItemBlock(EBlocks.CRIMSON_CRUCIBLE);
        UNFIRED_CRUCIBLE = registerItemBlock(EBlocks.UNFIRED_CRUCIBLE);
        OAK_CRUCIBLE = registerItemBlock(EBlocks.OAK_CRUCIBLE);
        SPRUCE_CRUCIBLE = registerItemBlock(EBlocks.SPRUCE_CRUCIBLE);
        BIRCH_CRUCIBLE = registerItemBlock(EBlocks.BIRCH_CRUCIBLE);
        JUNGLE_CRUCIBLE = registerItemBlock(EBlocks.JUNGLE_CRUCIBLE);
        ACACIA_CRUCIBLE = registerItemBlock(EBlocks.ACACIA_CRUCIBLE);
        DARK_OAK_CRUCIBLE = registerItemBlock(EBlocks.DARK_OAK_CRUCIBLE);
    }
}
