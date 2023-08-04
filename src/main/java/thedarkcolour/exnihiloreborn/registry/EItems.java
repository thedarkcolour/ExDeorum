package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.item.CrookItem;
import thedarkcolour.exnihiloreborn.item.HammerItem;
import thedarkcolour.exnihiloreborn.item.MeshItem;
import thedarkcolour.exnihiloreborn.item.PorcelainBucketItem;
import thedarkcolour.exnihiloreborn.item.SilkWormItem;
import thedarkcolour.exnihiloreborn.item.WitchWaterBucketItem;

public class EItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExNihiloReborn.ID);

    // Silk Worm
    public static final RegistryObject<SilkWormItem> SILK_WORM = ITEMS.register("silk_worm", () -> new SilkWormItem(props()));
    public static final RegistryObject<Item> COOKED_SILK_WORM = ITEMS.register("cooked_silk_worm", () -> new Item(props().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6f).build())));

    // Crooks
    public static final RegistryObject<Item> CROOK = ITEMS.register("crook", () -> new CrookItem(props().durability(128)));
    public static final RegistryObject<Item> BONE_CROOK = ITEMS.register("bone_crook", () -> new CrookItem(props().durability(312)));

    // Sieve Meshes
    public static final RegistryObject<Item> STRING_MESH = ITEMS.register("string_mesh", () -> new MeshItem(props().stacksTo(1)));
    public static final RegistryObject<Item> FLINT_MESH = ITEMS.register("flint_mesh", () -> new MeshItem(props().stacksTo(1)));
    public static final RegistryObject<Item> IRON_MESH = ITEMS.register("iron_mesh", () -> new MeshItem(props().stacksTo(1)));
    public static final RegistryObject<Item> GOLDEN_MESH = ITEMS.register("golden_mesh", () -> new MeshItem(props().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new MeshItem(props().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_MESH = ITEMS.register("netherite_mesh", () -> new MeshItem(props().stacksTo(1)));

    // Hammers
    public static final RegistryObject<Item> WOODEN_HAMMER = ITEMS.register("wooden_hammer", () -> new HammerItem(Tiers.WOOD, props()));
    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer", () -> new HammerItem(Tiers.STONE, props()));
    public static final RegistryObject<Item> GOLDEN_HAMMER = ITEMS.register("golden_hammer", () -> new HammerItem(Tiers.GOLD, props()));
    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer", () -> new HammerItem(Tiers.IRON, props()));
    public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer", () -> new HammerItem(Tiers.DIAMOND, props()));
    public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer", () -> new HammerItem(Tiers.NETHERITE, props()));

    // Ore Chunks
    public static final RegistryObject<Item> IRON_ORE_CHUNK = registerSimpleItem("iron_ore_chunk");
    public static final RegistryObject<Item> COPPER_ORE_CHUNK = registerSimpleItem("copper_ore_chunk");
    public static final RegistryObject<Item> GOLD_ORE_CHUNK = registerSimpleItem("gold_ore_chunk");

    // Pebbles
    public static final RegistryObject<Item> STONE_PEBBLE = registerSimpleItem("stone_pebble");
    public static final RegistryObject<Item> DIORITE_PEBBLE = registerSimpleItem("diorite_pebble");
    public static final RegistryObject<Item> GRANITE_PEBBLE = registerSimpleItem("granite_pebble");
    public static final RegistryObject<Item> ANDESITE_PEBBLE = registerSimpleItem("andesite_pebble");
    public static final RegistryObject<Item> DEEPSLATE_PEBBLE = registerSimpleItem("deepslate_pebble");
    public static final RegistryObject<Item> TUFF_PEBBLE = registerSimpleItem("tuff_pebble");

    // Misc
    public static final RegistryObject<Item> PORCELAIN_CLAY = registerSimpleItem("porcelain_clay");
    public static final RegistryObject<Item> PORCELAIN_BUCKET = ITEMS.register("porcelain_bucket", () -> new PorcelainBucketItem(props().stacksTo(1)));

    // Fluids
    public static final RegistryObject<Item> WITCH_WATER_BUCKET = ITEMS.register("witch_water_bucket", () -> new WitchWaterBucketItem(props().craftRemainder(Items.BUCKET).stacksTo(1)));

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

    // Barrels
    public static final RegistryObject<BlockItem> OAK_BARREL;
    public static final RegistryObject<BlockItem> SPRUCE_BARREL;
    public static final RegistryObject<BlockItem> BIRCH_BARREL;
    public static final RegistryObject<BlockItem> JUNGLE_BARREL;
    public static final RegistryObject<BlockItem> ACACIA_BARREL;
    public static final RegistryObject<BlockItem> DARK_OAK_BARREL;
    public static final RegistryObject<BlockItem> MANGROVE_BARREL;
    public static final RegistryObject<BlockItem> CHERRY_BARREL;
    public static final RegistryObject<BlockItem> BAMBOO_BARREL;
    public static final RegistryObject<BlockItem> CRIMSON_BARREL;
    public static final RegistryObject<BlockItem> WARPED_BARREL;
    public static final RegistryObject<BlockItem> STONE_BARREL;

    // Sieves
    public static final RegistryObject<BlockItem> OAK_SIEVE;
    public static final RegistryObject<BlockItem> SPRUCE_SIEVE;
    public static final RegistryObject<BlockItem> BIRCH_SIEVE;
    public static final RegistryObject<BlockItem> JUNGLE_SIEVE;
    public static final RegistryObject<BlockItem> ACACIA_SIEVE;
    public static final RegistryObject<BlockItem> DARK_OAK_SIEVE;
    public static final RegistryObject<BlockItem> MANGROVE_SIEVE;
    public static final RegistryObject<BlockItem> CHERRY_SIEVE;
    public static final RegistryObject<BlockItem> BAMBOO_SIEVE;
    public static final RegistryObject<BlockItem> CRIMSON_SIEVE;
    public static final RegistryObject<BlockItem> WARPED_SIEVE;

    // Lava Crucibles
    public static final RegistryObject<BlockItem> PORCELAIN_CRUCIBLE;
    public static final RegistryObject<BlockItem> WARPED_CRUCIBLE;
    public static final RegistryObject<BlockItem> CRIMSON_CRUCIBLE;
    public static final RegistryObject<BlockItem> UNFIRED_CRUCIBLE;

    // Water Crucibles
    public static final RegistryObject<BlockItem> OAK_CRUCIBLE;
    public static final RegistryObject<BlockItem> SPRUCE_CRUCIBLE;
    public static final RegistryObject<BlockItem> BIRCH_CRUCIBLE;
    public static final RegistryObject<BlockItem> JUNGLE_CRUCIBLE;
    public static final RegistryObject<BlockItem> ACACIA_CRUCIBLE;
    public static final RegistryObject<BlockItem> DARK_OAK_CRUCIBLE;
    public static final RegistryObject<BlockItem> MANGROVE_CRUCIBLE;
    public static final RegistryObject<BlockItem> CHERRY_CRUCIBLE;
    public static final RegistryObject<BlockItem> BAMBOO_CRUCIBLE;

    public static void addItemsToMainTab(CreativeModeTab.Output output) {
        output.accept(OAK_BARREL.get());
        output.accept(SPRUCE_BARREL.get());
        output.accept(BIRCH_BARREL.get());
        output.accept(JUNGLE_BARREL.get());
        output.accept(ACACIA_BARREL.get());
        output.accept(DARK_OAK_BARREL.get());
        output.accept(MANGROVE_BARREL.get());
        output.accept(CHERRY_BARREL.get());
        output.accept(BAMBOO_BARREL.get());
        output.accept(CRIMSON_BARREL.get());
        output.accept(WARPED_BARREL.get());
        output.accept(STONE_BARREL.get());
        output.accept(OAK_SIEVE.get());
        output.accept(SPRUCE_SIEVE.get());
        output.accept(BIRCH_SIEVE.get());
        output.accept(JUNGLE_SIEVE.get());
        output.accept(ACACIA_SIEVE.get());
        output.accept(DARK_OAK_SIEVE.get());
        output.accept(MANGROVE_SIEVE.get());
        output.accept(CHERRY_SIEVE.get());
        output.accept(BAMBOO_SIEVE.get());
        output.accept(CRIMSON_SIEVE.get());
        output.accept(WARPED_SIEVE.get());
        output.accept(PORCELAIN_CRUCIBLE.get());
        output.accept(WARPED_CRUCIBLE.get());
        output.accept(CRIMSON_CRUCIBLE.get());
        output.accept(UNFIRED_CRUCIBLE.get());
        output.accept(OAK_CRUCIBLE.get());
        output.accept(SPRUCE_CRUCIBLE.get());
        output.accept(BIRCH_CRUCIBLE.get());
        output.accept(JUNGLE_CRUCIBLE.get());
        output.accept(ACACIA_CRUCIBLE.get());
        output.accept(DARK_OAK_CRUCIBLE.get());
        output.accept(MANGROVE_CRUCIBLE.get());
        output.accept(CHERRY_CRUCIBLE.get());
        output.accept(BAMBOO_CRUCIBLE.get());
        output.accept(DUST.get());
        output.accept(CRUSHED_NETHERRACK.get());
        output.accept(CRUSHED_END_STONE.get());

        output.accept(SILK_WORM.get());
        output.accept(COOKED_SILK_WORM.get());
        output.accept(CROOK.get());
        output.accept(BONE_CROOK.get());
        output.accept(STRING_MESH.get());
        output.accept(FLINT_MESH.get());
        output.accept(IRON_MESH.get());
        output.accept(GOLDEN_MESH.get());
        output.accept(DIAMOND_MESH.get());
        output.accept(NETHERITE_MESH.get());
        output.accept(WOODEN_HAMMER.get());
        output.accept(STONE_HAMMER.get());
        output.accept(GOLDEN_HAMMER.get());
        output.accept(IRON_HAMMER.get());
        output.accept(DIAMOND_HAMMER.get());
        output.accept(NETHERITE_HAMMER.get());
        output.accept(IRON_ORE_CHUNK.get());
        output.accept(COPPER_ORE_CHUNK.get());
        output.accept(GOLD_ORE_CHUNK.get());
        output.accept(STONE_PEBBLE.get());
        output.accept(DIORITE_PEBBLE.get());
        output.accept(GRANITE_PEBBLE.get());
        output.accept(ANDESITE_PEBBLE.get());
        output.accept(TUFF_PEBBLE.get());
        output.accept(DEEPSLATE_PEBBLE.get());
        output.accept(PORCELAIN_CLAY.get());
        output.accept(PORCELAIN_BUCKET.get());
        output.accept(WITCH_WATER_BUCKET.get());
    }

    static {
        DUST = registerItemBlock(EBlocks.DUST);
        CRUSHED_NETHERRACK = registerItemBlock(EBlocks.CRUSHED_NETHERRACK);
        CRUSHED_END_STONE = registerItemBlock(EBlocks.CRUSHED_END_STONE);
        OAK_BARREL = registerItemBlock(EBlocks.OAK_BARREL);
        SPRUCE_BARREL = registerItemBlock(EBlocks.SPRUCE_BARREL);
        BIRCH_BARREL = registerItemBlock(EBlocks.BIRCH_BARREL);
        JUNGLE_BARREL = registerItemBlock(EBlocks.JUNGLE_BARREL);
        ACACIA_BARREL = registerItemBlock(EBlocks.ACACIA_BARREL);
        DARK_OAK_BARREL = registerItemBlock(EBlocks.DARK_OAK_BARREL);
        MANGROVE_BARREL = registerItemBlock(EBlocks.MANGROVE_BARREL);
        CHERRY_BARREL = registerItemBlock(EBlocks.CHERRY_BARREL);
        BAMBOO_BARREL = registerItemBlock(EBlocks.BAMBOO_BARREL);
        CRIMSON_BARREL = registerItemBlock(EBlocks.CRIMSON_BARREL);
        WARPED_BARREL = registerItemBlock(EBlocks.WARPED_BARREL);
        STONE_BARREL = registerItemBlock(EBlocks.STONE_BARREL);
        OAK_SIEVE = registerItemBlock(EBlocks.OAK_SIEVE);
        SPRUCE_SIEVE = registerItemBlock(EBlocks.SPRUCE_SIEVE);
        BIRCH_SIEVE = registerItemBlock(EBlocks.BIRCH_SIEVE);
        JUNGLE_SIEVE = registerItemBlock(EBlocks.JUNGLE_SIEVE);
        ACACIA_SIEVE = registerItemBlock(EBlocks.ACACIA_SIEVE);
        DARK_OAK_SIEVE = registerItemBlock(EBlocks.DARK_OAK_SIEVE);
        MANGROVE_SIEVE = registerItemBlock(EBlocks.MANGROVE_SIEVE);
        CHERRY_SIEVE = registerItemBlock(EBlocks.CHERRY_SIEVE);
        BAMBOO_SIEVE = registerItemBlock(EBlocks.BAMBOO_SIEVE);
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
        MANGROVE_CRUCIBLE = registerItemBlock(EBlocks.MANGROVE_CRUCIBLE);
        CHERRY_CRUCIBLE = registerItemBlock(EBlocks.CHERRY_CRUCIBLE);
        BAMBOO_CRUCIBLE = registerItemBlock(EBlocks.BAMBOO_CRUCIBLE);
    }
}
