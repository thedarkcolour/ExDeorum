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

package thedarkcolour.exdeorum.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.item.*;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;

public class EItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExDeorum.ID);

    // Silk Worm
    public static final RegistryObject<SilkWormItem> SILK_WORM = ITEMS.register("silk_worm", () -> new SilkWormItem(props()));
    public static final RegistryObject<Item> COOKED_SILK_WORM = ITEMS.register("cooked_silk_worm", () -> new CookedSilkWormItem(props().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6f).build())));

    // Crooks
    public static final RegistryObject<Item> CROOK = ITEMS.register("crook", () -> new CrookItem(props().durability(128), 2.0f));
    public static final RegistryObject<Item> BONE_CROOK = ITEMS.register("bone_crook", () -> new CrookItem(props().durability(312), 4.0f));

    // Watering cans
    public static final RegistryObject<Item> WOODEN_WATERING_CAN = ITEMS.register("wooden_watering_can", () -> new WateringCanItem(300, props().stacksTo(1)));
    public static final RegistryObject<Item> STONE_WATERING_CAN = ITEMS.register("stone_watering_can", () -> new WateringCanItem(1000, props().stacksTo(1)));
    public static final RegistryObject<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can", () -> new WateringCanItem(2000, props().stacksTo(1)));
    public static final RegistryObject<Item> GOLDEN_WATERING_CAN = ITEMS.register("golden_watering_can", () -> new WateringCanItem(4000, props().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_WATERING_CAN = ITEMS.register("diamond_watering_can", () -> new WideWateringCanItem(false, props().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_WATERING_CAN = ITEMS.register("netherite_watering_can", () -> new WideWateringCanItem(true, props().stacksTo(1)));

    // Sieve Meshes
    public static final RegistryObject<Item> STRING_MESH = ITEMS.register("string_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final RegistryObject<Item> FLINT_MESH = ITEMS.register("flint_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final RegistryObject<Item> IRON_MESH = ITEMS.register("iron_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final RegistryObject<Item> GOLDEN_MESH = ITEMS.register("golden_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final RegistryObject<Item> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final RegistryObject<Item> NETHERITE_MESH = ITEMS.register("netherite_mesh", () -> new MeshItem(props().stacksTo(16)));

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
    // Modded Ore Chunks
    public static final RegistryObject<Item> ALUMINUM_ORE_CHUNK = registerSimpleItem("aluminum_ore_chunk");
    public static final RegistryObject<Item> COBALT_ORE_CHUNK = registerSimpleItem("cobalt_ore_chunk");
    public static final RegistryObject<Item> SILVER_ORE_CHUNK = registerSimpleItem("silver_ore_chunk");
    public static final RegistryObject<Item> LEAD_ORE_CHUNK = registerSimpleItem("lead_ore_chunk");
    public static final RegistryObject<Item> PLATINUM_ORE_CHUNK = registerSimpleItem("platinum_ore_chunk");
    public static final RegistryObject<Item> NICKEL_ORE_CHUNK = registerSimpleItem("nickel_ore_chunk");
    public static final RegistryObject<Item> URANIUM_ORE_CHUNK = registerSimpleItem("uranium_ore_chunk");
    public static final RegistryObject<Item> OSMIUM_ORE_CHUNK = registerSimpleItem("osmium_ore_chunk");
    public static final RegistryObject<Item> TIN_ORE_CHUNK = registerSimpleItem("tin_ore_chunk");
    public static final RegistryObject<Item> ZINC_ORE_CHUNK = registerSimpleItem("zinc_ore_chunk");
    public static final RegistryObject<Item> IRIDIUM_ORE_CHUNK = registerSimpleItem("iridium_ore_chunk");
    public static final RegistryObject<Item> THORIUM_ORE_CHUNK = registerSimpleItem("thorium_ore_chunk");
    public static final RegistryObject<Item> MAGNESIUM_ORE_CHUNK = registerSimpleItem("magnesium_ore_chunk");
    public static final RegistryObject<Item> LITHIUM_ORE_CHUNK = registerSimpleItem("lithium_ore_chunk");
    public static final RegistryObject<Item> BORON_ORE_CHUNK = registerSimpleItem("boron_ore_chunk");

    // Pebbles
    public static final RegistryObject<Item> STONE_PEBBLE = registerSimpleItem("stone_pebble");
    public static final RegistryObject<Item> DIORITE_PEBBLE = registerSimpleItem("diorite_pebble");
    public static final RegistryObject<Item> GRANITE_PEBBLE = registerSimpleItem("granite_pebble");
    public static final RegistryObject<Item> ANDESITE_PEBBLE = registerSimpleItem("andesite_pebble");
    public static final RegistryObject<Item> DEEPSLATE_PEBBLE = registerSimpleItem("deepslate_pebble");
    public static final RegistryObject<Item> TUFF_PEBBLE = registerSimpleItem("tuff_pebble");
    public static final RegistryObject<Item> CALCITE_PEBBLE = registerSimpleItem("calcite_pebble");
    public static final RegistryObject<Item> BLACKSTONE_PEBBLE = registerSimpleItem("blackstone_pebble");
    public static final RegistryObject<Item> BASALT_PEBBLE = registerSimpleItem("basalt_pebble");

    // Misc
    public static final RegistryObject<Item> PORCELAIN_CLAY_BALL = registerSimpleItem("porcelain_clay_ball");
    public static final RegistryObject<Item> GRASS_SEEDS = ITEMS.register("grass_seeds", () -> new GrassSpreaderItem(props(), Blocks.GRASS_BLOCK::defaultBlockState));
    public static final RegistryObject<Item> MYCELIUM_SPORES = ITEMS.register("mycelium_spores", () -> new GrassSpreaderItem(props(), Blocks.MYCELIUM::defaultBlockState));
    public static final RegistryObject<Item> WARPED_NYLIUM_SPORES = ITEMS.register("warped_nylium_spores", () -> new NyliumSpreaderItem(props(), Blocks.WARPED_NYLIUM::defaultBlockState));
    public static final RegistryObject<Item> CRIMSON_NYLIUM_SPORES = ITEMS.register("crimson_nylium_spores", () -> new NyliumSpreaderItem(props(), Blocks.CRIMSON_NYLIUM::defaultBlockState));
    public static final RegistryObject<Item> SCULK_CORE = ITEMS.register("sculk_core", () -> new SculkCoreItem(props().stacksTo(1)));
    public static final RegistryObject<Item> RANDOM_POTTERY_SHERD = ITEMS.register("random_pottery_sherd", () -> new RandomResultItem.RandomSherd(props()));
    public static final RegistryObject<Item> RANDOM_ARMOR_TRIM = ITEMS.register("random_armor_trim", () -> new RandomResultItem.RandomSandyArmorTrim(props()));
    public static final RegistryObject<Item> WOOD_CHIPPINGS = registerSimpleItem("wood_chippings");

    // Buckets
    public static final RegistryObject<Item> UNFIRED_PORCELAIN_BUCKET = registerSimpleItem("unfired_porcelain_bucket");
    public static final RegistryObject<Item> PORCELAIN_BUCKET = ITEMS.register("porcelain_bucket", () -> new PorcelainBucket(() -> Fluids.EMPTY, props().stacksTo(16)));
    public static final RegistryObject<Item> PORCELAIN_WATER_BUCKET = ITEMS.register("porcelain_water_bucket", () -> new PorcelainBucket(() -> Fluids.WATER, props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));
    public static final RegistryObject<Item> PORCELAIN_LAVA_BUCKET = ITEMS.register("porcelain_lava_bucket", () -> new PorcelainBucket(() -> Fluids.LAVA, props().stacksTo(1)));
    public static final RegistryObject<Item> PORCELAIN_MILK_BUCKET = ITEMS.register("porcelain_milk_bucket", () -> new PorcelainMilkBucket(props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));
    public static final RegistryObject<Item> PORCELAIN_WITCH_WATER_BUCKET = ITEMS.register("porcelain_witch_water_bucket", () -> new PorcelainBucket(EFluids.WITCH_WATER, props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));

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
    public static final RegistryObject<BlockItem> DUST = registerItemBlock(EBlocks.DUST);
    public static final RegistryObject<BlockItem> CRUSHED_NETHERRACK = registerItemBlock(EBlocks.CRUSHED_NETHERRACK);
    public static final RegistryObject<BlockItem> CRUSHED_END_STONE = registerItemBlock(EBlocks.CRUSHED_END_STONE);
    public static final RegistryObject<BlockItem> CRUSHED_DEEPSLATE = registerItemBlock(EBlocks.CRUSHED_DEEPSLATE);
    public static final RegistryObject<BlockItem> CRUSHED_BLACKSTONE = registerItemBlock(EBlocks.CRUSHED_BLACKSTONE);

    // Barrels
    public static final RegistryObject<BlockItem> OAK_BARREL = registerItemBlock(EBlocks.OAK_BARREL);
    public static final RegistryObject<BlockItem> SPRUCE_BARREL = registerItemBlock(EBlocks.SPRUCE_BARREL);
    public static final RegistryObject<BlockItem> BIRCH_BARREL = registerItemBlock(EBlocks.BIRCH_BARREL);
    public static final RegistryObject<BlockItem> JUNGLE_BARREL = registerItemBlock(EBlocks.JUNGLE_BARREL);
    public static final RegistryObject<BlockItem> ACACIA_BARREL = registerItemBlock(EBlocks.ACACIA_BARREL);
    public static final RegistryObject<BlockItem> DARK_OAK_BARREL = registerItemBlock(EBlocks.DARK_OAK_BARREL);
    public static final RegistryObject<BlockItem> MANGROVE_BARREL = registerItemBlock(EBlocks.MANGROVE_BARREL);
    public static final RegistryObject<BlockItem> CHERRY_BARREL = registerItemBlock(EBlocks.CHERRY_BARREL);
    public static final RegistryObject<BlockItem> BAMBOO_BARREL = registerItemBlock(EBlocks.BAMBOO_BARREL);
    public static final RegistryObject<BlockItem> CRIMSON_BARREL = registerItemBlock(EBlocks.CRIMSON_BARREL);
    public static final RegistryObject<BlockItem> WARPED_BARREL = registerItemBlock(EBlocks.WARPED_BARREL);
    public static final RegistryObject<BlockItem> STONE_BARREL = registerItemBlock(EBlocks.STONE_BARREL);
    // BOP Barrels
    public static final RegistryObject<BlockItem> FIR_BARREL = registerItemBlock(EBlocks.FIR_BARREL);
    public static final RegistryObject<BlockItem> REDWOOD_BARREL = registerItemBlock(EBlocks.REDWOOD_BARREL);
    public static final RegistryObject<BlockItem> MAHOGANY_BARREL = registerItemBlock(EBlocks.MAHOGANY_BARREL);
    public static final RegistryObject<BlockItem> JACARANDA_BARREL = registerItemBlock(EBlocks.JACARANDA_BARREL);
    public static final RegistryObject<BlockItem> PALM_BARREL = registerItemBlock(EBlocks.PALM_BARREL);
    public static final RegistryObject<BlockItem> WILLOW_BARREL = registerItemBlock(EBlocks.WILLOW_BARREL);
    public static final RegistryObject<BlockItem> DEAD_BARREL = registerItemBlock(EBlocks.DEAD_BARREL);
    public static final RegistryObject<BlockItem> MAGIC_BARREL = registerItemBlock(EBlocks.MAGIC_BARREL);
    public static final RegistryObject<BlockItem> UMBRAN_BARREL = registerItemBlock(EBlocks.UMBRAN_BARREL);
    public static final RegistryObject<BlockItem> HELLBARK_BARREL = registerItemBlock(EBlocks.HELLBARK_BARREL);
    // Ars Nouveau Barrels
    public static final RegistryObject<BlockItem> ARCHWOOD_BARREL = registerItemBlock(EBlocks.ARCHWOOD_BARREL);
    // Aether Barrels
    public static final RegistryObject<BlockItem> SKYROOT_BARREL = registerItemBlock(EBlocks.SKYROOT_BARREL);
    // Blue Skies Barrels
    public static final RegistryObject<BlockItem> BLUEBRIGHT_BARREL = registerItemBlock(EBlocks.BLUEBRIGHT_BARREL);
    public static final RegistryObject<BlockItem> STARLIT_BARREL = registerItemBlock(EBlocks.STARLIT_BARREL);
    public static final RegistryObject<BlockItem> FROSTBRIGHT_BARREL = registerItemBlock(EBlocks.FROSTBRIGHT_BARREL);
    public static final RegistryObject<BlockItem> COMET_BARREL = registerItemBlock(EBlocks.COMET_BARREL);
    public static final RegistryObject<BlockItem> LUNAR_BARREL = registerItemBlock(EBlocks.LUNAR_BARREL);
    public static final RegistryObject<BlockItem> DUSK_BARREL = registerItemBlock(EBlocks.DUSK_BARREL);
    public static final RegistryObject<BlockItem> MAPLE_BARREL = registerItemBlock(EBlocks.MAPLE_BARREL);
    public static final RegistryObject<BlockItem> CRYSTALLIZED_BARREL = registerItemBlock(EBlocks.CRYSTALLIZED_BARREL);

    // Sieves
    public static final RegistryObject<BlockItem> OAK_SIEVE = registerItemBlock(EBlocks.OAK_SIEVE);
    public static final RegistryObject<BlockItem> SPRUCE_SIEVE = registerItemBlock(EBlocks.SPRUCE_SIEVE);
    public static final RegistryObject<BlockItem> BIRCH_SIEVE = registerItemBlock(EBlocks.BIRCH_SIEVE);
    public static final RegistryObject<BlockItem> JUNGLE_SIEVE = registerItemBlock(EBlocks.JUNGLE_SIEVE);
    public static final RegistryObject<BlockItem> ACACIA_SIEVE = registerItemBlock(EBlocks.ACACIA_SIEVE);
    public static final RegistryObject<BlockItem> DARK_OAK_SIEVE = registerItemBlock(EBlocks.DARK_OAK_SIEVE);
    public static final RegistryObject<BlockItem> MANGROVE_SIEVE = registerItemBlock(EBlocks.MANGROVE_SIEVE);
    public static final RegistryObject<BlockItem> CHERRY_SIEVE = registerItemBlock(EBlocks.CHERRY_SIEVE);
    public static final RegistryObject<BlockItem> BAMBOO_SIEVE = registerItemBlock(EBlocks.BAMBOO_SIEVE);
    public static final RegistryObject<BlockItem> CRIMSON_SIEVE = registerItemBlock(EBlocks.CRIMSON_SIEVE);
    public static final RegistryObject<BlockItem> WARPED_SIEVE = registerItemBlock(EBlocks.WARPED_SIEVE);
    // BOP Sieves
    public static final RegistryObject<BlockItem> FIR_SIEVE = registerItemBlock(EBlocks.FIR_SIEVE);
    public static final RegistryObject<BlockItem> REDWOOD_SIEVE = registerItemBlock(EBlocks.REDWOOD_SIEVE);
    public static final RegistryObject<BlockItem> MAHOGANY_SIEVE = registerItemBlock(EBlocks.MAHOGANY_SIEVE);
    public static final RegistryObject<BlockItem> JACARANDA_SIEVE = registerItemBlock(EBlocks.JACARANDA_SIEVE);
    public static final RegistryObject<BlockItem> PALM_SIEVE = registerItemBlock(EBlocks.PALM_SIEVE);
    public static final RegistryObject<BlockItem> WILLOW_SIEVE = registerItemBlock(EBlocks.WILLOW_SIEVE);
    public static final RegistryObject<BlockItem> DEAD_SIEVE = registerItemBlock(EBlocks.DEAD_SIEVE);
    public static final RegistryObject<BlockItem> MAGIC_SIEVE = registerItemBlock(EBlocks.MAGIC_SIEVE);
    public static final RegistryObject<BlockItem> UMBRAN_SIEVE = registerItemBlock(EBlocks.UMBRAN_SIEVE);
    public static final RegistryObject<BlockItem> HELLBARK_SIEVE = registerItemBlock(EBlocks.HELLBARK_SIEVE);
    // Ars Nouveau Sieves
    public static final RegistryObject<BlockItem> ARCHWOOD_SIEVE = registerItemBlock(EBlocks.ARCHWOOD_SIEVE);
    // Aether Sieves
    public static final RegistryObject<BlockItem> SKYROOT_SIEVE = registerItemBlock(EBlocks.SKYROOT_SIEVE);
    // Blue Skies Sieves
    public static final RegistryObject<BlockItem> BLUEBRIGHT_SIEVE = registerItemBlock(EBlocks.BLUEBRIGHT_SIEVE);
    public static final RegistryObject<BlockItem> STARLIT_SIEVE = registerItemBlock(EBlocks.STARLIT_SIEVE);
    public static final RegistryObject<BlockItem> FROSTBRIGHT_SIEVE = registerItemBlock(EBlocks.FROSTBRIGHT_SIEVE);
    public static final RegistryObject<BlockItem> COMET_SIEVE = registerItemBlock(EBlocks.COMET_SIEVE);
    public static final RegistryObject<BlockItem> LUNAR_SIEVE = registerItemBlock(EBlocks.LUNAR_SIEVE);
    public static final RegistryObject<BlockItem> DUSK_SIEVE = registerItemBlock(EBlocks.DUSK_SIEVE);
    public static final RegistryObject<BlockItem> MAPLE_SIEVE = registerItemBlock(EBlocks.MAPLE_SIEVE);
    public static final RegistryObject<BlockItem> CRYSTALLIZED_SIEVE = registerItemBlock(EBlocks.CRYSTALLIZED_SIEVE);
    // Mechanical Sieves
    public static final RegistryObject<BlockItem> MECHANICAL_SIEVE = registerItemBlock(EBlocks.MECHANICAL_SIEVE);

    // Lava Crucibles
    public static final RegistryObject<BlockItem> PORCELAIN_CRUCIBLE = registerItemBlock(EBlocks.PORCELAIN_CRUCIBLE);
    public static final RegistryObject<BlockItem> WARPED_CRUCIBLE = registerItemBlock(EBlocks.WARPED_CRUCIBLE);
    public static final RegistryObject<BlockItem> CRIMSON_CRUCIBLE = registerItemBlock(EBlocks.CRIMSON_CRUCIBLE);
    public static final RegistryObject<BlockItem> UNFIRED_PORCELAIN_CRUCIBLE = registerItemBlock(EBlocks.UNFIRED_PORCELAIN_CRUCIBLE);
    // BOP Lava Crucibles
    public static final RegistryObject<BlockItem> HELLBARK_CRUCIBLE = registerItemBlock(EBlocks.HELLBARK_CRUCIBLE);
    // Blue Skies Lava Crucibles
    public static final RegistryObject<BlockItem> CRYSTALLIZED_CRUCIBLE = registerItemBlock(EBlocks.CRYSTALLIZED_CRUCIBLE);

    // Water Crucibles
    public static final RegistryObject<BlockItem> OAK_CRUCIBLE = registerItemBlock(EBlocks.OAK_CRUCIBLE);
    public static final RegistryObject<BlockItem> SPRUCE_CRUCIBLE = registerItemBlock(EBlocks.SPRUCE_CRUCIBLE);
    public static final RegistryObject<BlockItem> BIRCH_CRUCIBLE = registerItemBlock(EBlocks.BIRCH_CRUCIBLE);
    public static final RegistryObject<BlockItem> JUNGLE_CRUCIBLE = registerItemBlock(EBlocks.JUNGLE_CRUCIBLE);
    public static final RegistryObject<BlockItem> ACACIA_CRUCIBLE = registerItemBlock(EBlocks.ACACIA_CRUCIBLE);
    public static final RegistryObject<BlockItem> DARK_OAK_CRUCIBLE = registerItemBlock(EBlocks.DARK_OAK_CRUCIBLE);
    public static final RegistryObject<BlockItem> MANGROVE_CRUCIBLE = registerItemBlock(EBlocks.MANGROVE_CRUCIBLE);
    public static final RegistryObject<BlockItem> CHERRY_CRUCIBLE = registerItemBlock(EBlocks.CHERRY_CRUCIBLE);
    public static final RegistryObject<BlockItem> BAMBOO_CRUCIBLE = registerItemBlock(EBlocks.BAMBOO_CRUCIBLE);
    // BOP Water Crucibles
    public static final RegistryObject<BlockItem> FIR_CRUCIBLE = registerItemBlock(EBlocks.FIR_CRUCIBLE);
    public static final RegistryObject<BlockItem> REDWOOD_CRUCIBLE = registerItemBlock(EBlocks.REDWOOD_CRUCIBLE);
    public static final RegistryObject<BlockItem> MAHOGANY_CRUCIBLE = registerItemBlock(EBlocks.MAHOGANY_CRUCIBLE);
    public static final RegistryObject<BlockItem> JACARANDA_CRUCIBLE = registerItemBlock(EBlocks.JACARANDA_CRUCIBLE);
    public static final RegistryObject<BlockItem> PALM_CRUCIBLE = registerItemBlock(EBlocks.PALM_CRUCIBLE);
    public static final RegistryObject<BlockItem> WILLOW_CRUCIBLE = registerItemBlock(EBlocks.WILLOW_CRUCIBLE);
    public static final RegistryObject<BlockItem> DEAD_CRUCIBLE = registerItemBlock(EBlocks.DEAD_CRUCIBLE);
    public static final RegistryObject<BlockItem> MAGIC_CRUCIBLE = registerItemBlock(EBlocks.MAGIC_CRUCIBLE);
    public static final RegistryObject<BlockItem> UMBRAN_CRUCIBLE = registerItemBlock(EBlocks.UMBRAN_CRUCIBLE);
    // Ars Nouveau Water Crucibles
    public static final RegistryObject<BlockItem> CASCADING_ARCHWOOD_CRUCIBLE = registerItemBlock(EBlocks.CASCADING_ARCHWOOD_CRUCIBLE);
    public static final RegistryObject<BlockItem> BLAZING_ARCHWOOD_CRUCIBLE = registerItemBlock(EBlocks.BLAZING_ARCHWOOD_CRUCIBLE);
    public static final RegistryObject<BlockItem> VEXING_ARCHWOOD_CRUCIBLE = registerItemBlock(EBlocks.VEXING_ARCHWOOD_CRUCIBLE);
    public static final RegistryObject<BlockItem> FLOURISHING_ARCHWOOD_CRUCIBLE = registerItemBlock(EBlocks.FLOURISHING_ARCHWOOD_CRUCIBLE);
    // Aether Water Crucibles
    public static final RegistryObject<BlockItem> SKYROOT_CRUCIBLE = registerItemBlock(EBlocks.SKYROOT_CRUCIBLE);
    public static final RegistryObject<BlockItem> GOLDEN_OAK_CRUCIBLE = registerItemBlock(EBlocks.GOLDEN_OAK_CRUCIBLE);
    // Blue Skies Water Crucibles
    public static final RegistryObject<BlockItem> BLUEBRIGHT_CRUCIBLE = registerItemBlock(EBlocks.BLUEBRIGHT_CRUCIBLE);
    public static final RegistryObject<BlockItem> STARLIT_CRUCIBLE = registerItemBlock(EBlocks.STARLIT_CRUCIBLE);
    public static final RegistryObject<BlockItem> FROSTBRIGHT_CRUCIBLE = registerItemBlock(EBlocks.FROSTBRIGHT_CRUCIBLE);
    public static final RegistryObject<BlockItem> COMET_CRUCIBLE = registerItemBlock(EBlocks.COMET_CRUCIBLE);
    public static final RegistryObject<BlockItem> LUNAR_CRUCIBLE = registerItemBlock(EBlocks.LUNAR_CRUCIBLE);
    public static final RegistryObject<BlockItem> DUSK_CRUCIBLE = registerItemBlock(EBlocks.DUSK_CRUCIBLE);
    public static final RegistryObject<BlockItem> MAPLE_CRUCIBLE = registerItemBlock(EBlocks.MAPLE_CRUCIBLE);

    public static final RegistryObject<BlockItem> END_CAKE = registerItemBlock(EBlocks.END_CAKE);

    public static void addItemsToMainTab(CreativeModeTab.Output output) {
        boolean biomesOPlenty = ModList.get().isLoaded(ModIds.BIOMES_O_PLENTY);
        boolean arsNouveau = ModList.get().isLoaded(ModIds.ARS_NOUVEAU);
        boolean aether = ModList.get().isLoaded(ModIds.AETHER);
        boolean blueSkies = ModList.get().isLoaded(ModIds.BLUE_SKIES);

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

        if (biomesOPlenty) {
            output.accept(FIR_BARREL.get());
            output.accept(REDWOOD_BARREL.get());
            output.accept(MAHOGANY_BARREL.get());
            output.accept(JACARANDA_BARREL.get());
            output.accept(PALM_BARREL.get());
            output.accept(WILLOW_BARREL.get());
            output.accept(DEAD_BARREL.get());
            output.accept(MAGIC_BARREL.get());
            output.accept(UMBRAN_BARREL.get());
            output.accept(HELLBARK_BARREL.get());
        }
        if (arsNouveau) {
            output.accept(ARCHWOOD_BARREL.get());
        }
        if (aether) {
            output.accept(SKYROOT_BARREL.get());
        }
        if (blueSkies) {
            output.accept(BLUEBRIGHT_BARREL.get());
            output.accept(STARLIT_BARREL.get());
            output.accept(FROSTBRIGHT_BARREL.get());
            output.accept(COMET_BARREL.get());
            output.accept(LUNAR_BARREL.get());
            output.accept(DUSK_BARREL.get());
            output.accept(MAPLE_BARREL.get());
            output.accept(CRYSTALLIZED_BARREL.get());
        }

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

        if (biomesOPlenty) {
            output.accept(FIR_SIEVE.get());
            output.accept(REDWOOD_SIEVE.get());
            output.accept(MAHOGANY_SIEVE.get());
            output.accept(JACARANDA_SIEVE.get());
            output.accept(PALM_SIEVE.get());
            output.accept(WILLOW_SIEVE.get());
            output.accept(DEAD_SIEVE.get());
            output.accept(MAGIC_SIEVE.get());
            output.accept(UMBRAN_SIEVE.get());
            output.accept(HELLBARK_SIEVE.get());
        }
        if (arsNouveau) {
            output.accept(ARCHWOOD_SIEVE.get());
        }
        if (aether) {
            output.accept(SKYROOT_SIEVE.get());
        }
        if (blueSkies) {
            output.accept(BLUEBRIGHT_SIEVE.get());
            output.accept(STARLIT_SIEVE.get());
            output.accept(FROSTBRIGHT_SIEVE.get());
            output.accept(COMET_SIEVE.get());
            output.accept(LUNAR_SIEVE.get());
            output.accept(DUSK_SIEVE.get());
            output.accept(MAPLE_SIEVE.get());
            output.accept(CRYSTALLIZED_SIEVE.get());
        }
        output.accept(MECHANICAL_SIEVE.get());

        output.accept(PORCELAIN_CRUCIBLE.get());
        output.accept(WARPED_CRUCIBLE.get());
        output.accept(CRIMSON_CRUCIBLE.get());
        output.accept(UNFIRED_PORCELAIN_CRUCIBLE.get());
        if (biomesOPlenty) {
            output.accept(HELLBARK_CRUCIBLE.get());
        }
        if (blueSkies) {
            output.accept(CRYSTALLIZED_CRUCIBLE.get());
        }
        output.accept(OAK_CRUCIBLE.get());
        output.accept(SPRUCE_CRUCIBLE.get());
        output.accept(BIRCH_CRUCIBLE.get());
        output.accept(JUNGLE_CRUCIBLE.get());
        output.accept(ACACIA_CRUCIBLE.get());
        output.accept(DARK_OAK_CRUCIBLE.get());
        output.accept(MANGROVE_CRUCIBLE.get());
        output.accept(CHERRY_CRUCIBLE.get());
        output.accept(BAMBOO_CRUCIBLE.get());

        if (biomesOPlenty) {
            output.accept(FIR_CRUCIBLE.get());
            output.accept(REDWOOD_CRUCIBLE.get());
            output.accept(MAHOGANY_CRUCIBLE.get());
            output.accept(JACARANDA_CRUCIBLE.get());
            output.accept(PALM_CRUCIBLE.get());
            output.accept(WILLOW_CRUCIBLE.get());
            output.accept(DEAD_CRUCIBLE.get());
            output.accept(MAGIC_CRUCIBLE.get());
            output.accept(UMBRAN_CRUCIBLE.get());
        }
        if (arsNouveau) {
            output.accept(CASCADING_ARCHWOOD_CRUCIBLE.get());
            output.accept(BLAZING_ARCHWOOD_CRUCIBLE.get());
            output.accept(VEXING_ARCHWOOD_CRUCIBLE.get());
            output.accept(FLOURISHING_ARCHWOOD_CRUCIBLE.get());
        }
        if (aether) {
            output.accept(SKYROOT_CRUCIBLE.get());
            output.accept(GOLDEN_OAK_CRUCIBLE.get());
        }
        if (blueSkies) {
            output.accept(BLUEBRIGHT_CRUCIBLE.get());
            output.accept(STARLIT_CRUCIBLE.get());
            output.accept(FROSTBRIGHT_CRUCIBLE.get());
            output.accept(COMET_CRUCIBLE.get());
            output.accept(LUNAR_CRUCIBLE.get());
            output.accept(DUSK_CRUCIBLE.get());
            output.accept(MAPLE_CRUCIBLE.get());
        }

        output.accept(DUST.get());
        output.accept(CRUSHED_NETHERRACK.get());
        output.accept(CRUSHED_END_STONE.get());
        output.accept(CRUSHED_DEEPSLATE.get());
        output.accept(CRUSHED_BLACKSTONE.get());
        output.accept(END_CAKE.get());
        output.accept(RANDOM_ARMOR_TRIM.get());
        output.accept(RANDOM_POTTERY_SHERD.get());

        output.accept(SILK_WORM.get());
        output.accept(COOKED_SILK_WORM.get());
        output.accept(CROOK.get());
        output.accept(BONE_CROOK.get());
        var wateringCans = List.of(WOODEN_WATERING_CAN, STONE_WATERING_CAN, IRON_WATERING_CAN, GOLDEN_WATERING_CAN, DIAMOND_WATERING_CAN, NETHERITE_WATERING_CAN);
        for (var wateringCan : wateringCans) {
            var full = WateringCanItem.getFull(wateringCan);
            output.accept(wateringCan.get());
            output.accept(full);
        }
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

        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_ALUMINUM)) output.accept(ALUMINUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_COBALT)) output.accept(COBALT_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_SILVER)) output.accept(SILVER_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_LEAD)) output.accept(LEAD_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_PLATINUM)) output.accept(PLATINUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_NICKEL)) output.accept(NICKEL_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_URANIUM)) output.accept(URANIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_OSMIUM)) output.accept(OSMIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_TIN)) output.accept(TIN_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_ZINC)) output.accept(ZINC_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_IRIDIUM)) output.accept(IRIDIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_THORIUM)) output.accept(THORIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_MAGNESIUM)) output.accept(MAGNESIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_LITHIUM)) output.accept(LITHIUM_ORE_CHUNK.get());
        if (!RecipeUtil.isTagEmpty(EItemTags.ORES_BORON)) output.accept(BORON_ORE_CHUNK.get());

        output.accept(STONE_PEBBLE.get());
        output.accept(DIORITE_PEBBLE.get());
        output.accept(GRANITE_PEBBLE.get());
        output.accept(ANDESITE_PEBBLE.get());
        output.accept(DEEPSLATE_PEBBLE.get());
        output.accept(TUFF_PEBBLE.get());
        output.accept(CALCITE_PEBBLE.get());
        output.accept(BLACKSTONE_PEBBLE.get());
        output.accept(BASALT_PEBBLE.get());
        output.accept(GRASS_SEEDS.get());
        output.accept(MYCELIUM_SPORES.get());
        output.accept(WARPED_NYLIUM_SPORES.get());
        output.accept(CRIMSON_NYLIUM_SPORES.get());
        output.accept(SCULK_CORE.get());
        output.accept(WOOD_CHIPPINGS.get());
        output.accept(PORCELAIN_CLAY_BALL.get());
        output.accept(UNFIRED_PORCELAIN_BUCKET.get());
        output.accept(PORCELAIN_BUCKET.get());
        output.accept(PORCELAIN_WATER_BUCKET.get());
        output.accept(PORCELAIN_LAVA_BUCKET.get());
        output.accept(PORCELAIN_MILK_BUCKET.get());
        output.accept(PORCELAIN_WITCH_WATER_BUCKET.get());
        output.accept(WITCH_WATER_BUCKET.get());
    }
}
