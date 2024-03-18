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
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.item.*;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;

public class EItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExDeorum.ID);

    // Silk Worm
    public static final DeferredItem<SilkWormItem> SILK_WORM = ITEMS.register("silk_worm", () -> new SilkWormItem(props()));
    public static final DeferredItem<Item> COOKED_SILK_WORM = ITEMS.register("cooked_silk_worm", () -> new CookedSilkWormItem(props().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6f).build())));

    // Crooks
    public static final DeferredItem<Item> CROOK = ITEMS.register("crook", () -> new CrookItem(props().durability(128), 2.0f));
    public static final DeferredItem<Item> BONE_CROOK = ITEMS.register("bone_crook", () -> new CrookItem(props().durability(312), 4.0f));

    // Watering cans
    public static final DeferredItem<Item> WOODEN_WATERING_CAN = ITEMS.register("wooden_watering_can", () -> new WateringCanItem(300, props().stacksTo(1)));
    public static final DeferredItem<Item> STONE_WATERING_CAN = ITEMS.register("stone_watering_can", () -> new WateringCanItem(1000, props().stacksTo(1)));
    public static final DeferredItem<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can", () -> new WateringCanItem(2000, props().stacksTo(1)));
    public static final DeferredItem<Item> GOLDEN_WATERING_CAN = ITEMS.register("golden_watering_can", () -> new WateringCanItem(4000, props().stacksTo(1)));
    public static final DeferredItem<Item> DIAMOND_WATERING_CAN = ITEMS.register("diamond_watering_can", () -> new WideWateringCanItem(false, props().stacksTo(1)));
    public static final DeferredItem<Item> NETHERITE_WATERING_CAN = ITEMS.register("netherite_watering_can", () -> new WideWateringCanItem(true, props().stacksTo(1)));

    // Sieve Meshes
    public static final DeferredItem<Item> STRING_MESH = ITEMS.register("string_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final DeferredItem<Item> FLINT_MESH = ITEMS.register("flint_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final DeferredItem<Item> IRON_MESH = ITEMS.register("iron_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final DeferredItem<Item> GOLDEN_MESH = ITEMS.register("golden_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final DeferredItem<Item> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new MeshItem(props().stacksTo(16)));
    public static final DeferredItem<Item> NETHERITE_MESH = ITEMS.register("netherite_mesh", () -> new MeshItem(props().stacksTo(16)));

    // Hammers
    public static final DeferredItem<Item> WOODEN_HAMMER = ITEMS.register("wooden_hammer", () -> new HammerItem(Tiers.WOOD, props()));
    public static final DeferredItem<Item> STONE_HAMMER = ITEMS.register("stone_hammer", () -> new HammerItem(Tiers.STONE, props()));
    public static final DeferredItem<Item> GOLDEN_HAMMER = ITEMS.register("golden_hammer", () -> new HammerItem(Tiers.GOLD, props()));
    public static final DeferredItem<Item> IRON_HAMMER = ITEMS.register("iron_hammer", () -> new HammerItem(Tiers.IRON, props()));
    public static final DeferredItem<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer", () -> new HammerItem(Tiers.DIAMOND, props()));
    public static final DeferredItem<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer", () -> new HammerItem(Tiers.NETHERITE, props()));

    // Ore Chunks
    public static final DeferredItem<Item> IRON_ORE_CHUNK = registerSimpleItem("iron_ore_chunk");
    public static final DeferredItem<Item> COPPER_ORE_CHUNK = registerSimpleItem("copper_ore_chunk");
    public static final DeferredItem<Item> GOLD_ORE_CHUNK = registerSimpleItem("gold_ore_chunk");
    // Modded Ore Chunks
    public static final DeferredItem<Item> ALUMINUM_ORE_CHUNK = registerSimpleItem("aluminum_ore_chunk");
    public static final DeferredItem<Item> COBALT_ORE_CHUNK = registerSimpleItem("cobalt_ore_chunk");
    public static final DeferredItem<Item> SILVER_ORE_CHUNK = registerSimpleItem("silver_ore_chunk");
    public static final DeferredItem<Item> LEAD_ORE_CHUNK = registerSimpleItem("lead_ore_chunk");
    public static final DeferredItem<Item> PLATINUM_ORE_CHUNK = registerSimpleItem("platinum_ore_chunk");
    public static final DeferredItem<Item> NICKEL_ORE_CHUNK = registerSimpleItem("nickel_ore_chunk");
    public static final DeferredItem<Item> URANIUM_ORE_CHUNK = registerSimpleItem("uranium_ore_chunk");
    public static final DeferredItem<Item> OSMIUM_ORE_CHUNK = registerSimpleItem("osmium_ore_chunk");
    public static final DeferredItem<Item> TIN_ORE_CHUNK = registerSimpleItem("tin_ore_chunk");
    public static final DeferredItem<Item> ZINC_ORE_CHUNK = registerSimpleItem("zinc_ore_chunk");
    public static final DeferredItem<Item> IRIDIUM_ORE_CHUNK = registerSimpleItem("iridium_ore_chunk");
    public static final DeferredItem<Item> THORIUM_ORE_CHUNK = registerSimpleItem("thorium_ore_chunk");
    public static final DeferredItem<Item> MAGNESIUM_ORE_CHUNK = registerSimpleItem("magnesium_ore_chunk");
    public static final DeferredItem<Item> LITHIUM_ORE_CHUNK = registerSimpleItem("lithium_ore_chunk");
    public static final DeferredItem<Item> BORON_ORE_CHUNK = registerSimpleItem("boron_ore_chunk");

    // Pebbles
    public static final DeferredItem<Item> STONE_PEBBLE = registerSimpleItem("stone_pebble");
    public static final DeferredItem<Item> DIORITE_PEBBLE = registerSimpleItem("diorite_pebble");
    public static final DeferredItem<Item> GRANITE_PEBBLE = registerSimpleItem("granite_pebble");
    public static final DeferredItem<Item> ANDESITE_PEBBLE = registerSimpleItem("andesite_pebble");
    public static final DeferredItem<Item> DEEPSLATE_PEBBLE = registerSimpleItem("deepslate_pebble");
    public static final DeferredItem<Item> TUFF_PEBBLE = registerSimpleItem("tuff_pebble");
    public static final DeferredItem<Item> CALCITE_PEBBLE = registerSimpleItem("calcite_pebble");
    public static final DeferredItem<Item> BLACKSTONE_PEBBLE = registerSimpleItem("blackstone_pebble");
    public static final DeferredItem<Item> BASALT_PEBBLE = registerSimpleItem("basalt_pebble");

    // Misc
    public static final DeferredItem<Item> PORCELAIN_CLAY_BALL = registerSimpleItem("porcelain_clay_ball");
    public static final DeferredItem<Item> GRASS_SEEDS = ITEMS.register("grass_seeds", () -> new GrassSpreaderItem(props(), Blocks.GRASS_BLOCK::defaultBlockState));
    public static final DeferredItem<Item> MYCELIUM_SPORES = ITEMS.register("mycelium_spores", () -> new GrassSpreaderItem(props(), Blocks.MYCELIUM::defaultBlockState));
    public static final DeferredItem<Item> WARPED_NYLIUM_SPORES = ITEMS.register("warped_nylium_spores", () -> new NyliumSpreaderItem(props(), Blocks.WARPED_NYLIUM::defaultBlockState));
    public static final DeferredItem<Item> CRIMSON_NYLIUM_SPORES = ITEMS.register("crimson_nylium_spores", () -> new NyliumSpreaderItem(props(), Blocks.CRIMSON_NYLIUM::defaultBlockState));
    public static final DeferredItem<Item> SCULK_CORE = ITEMS.register("sculk_core", () -> new SculkCoreItem(props().stacksTo(1)));
    public static final DeferredItem<Item> RANDOM_POTTERY_SHERD = ITEMS.register("random_pottery_sherd", () -> new RandomResultItem.RandomSherd(props()));
    public static final DeferredItem<Item> RANDOM_ARMOR_TRIM = ITEMS.register("random_armor_trim", () -> new RandomResultItem.RandomSandyArmorTrim(props()));
    public static final DeferredItem<Item> WOOD_CHIPPINGS = registerSimpleItem("wood_chippings");

    // Buckets
    public static final DeferredItem<Item> UNFIRED_PORCELAIN_BUCKET = registerSimpleItem("unfired_porcelain_bucket");
    public static final DeferredItem<Item> PORCELAIN_BUCKET = ITEMS.register("porcelain_bucket", () -> new PorcelainBucket(() -> Fluids.EMPTY, props().stacksTo(16)));
    public static final DeferredItem<Item> PORCELAIN_WATER_BUCKET = ITEMS.register("porcelain_water_bucket", () -> new PorcelainBucket(() -> Fluids.WATER, props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));
    public static final DeferredItem<Item> PORCELAIN_LAVA_BUCKET = ITEMS.register("porcelain_lava_bucket", () -> new PorcelainBucket(() -> Fluids.LAVA, props().stacksTo(1)));
    public static final DeferredItem<Item> PORCELAIN_MILK_BUCKET = ITEMS.register("porcelain_milk_bucket", () -> new PorcelainMilkBucket(props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));
    public static final DeferredItem<Item> PORCELAIN_WITCH_WATER_BUCKET = ITEMS.register("porcelain_witch_water_bucket", () -> new PorcelainBucket(EFluids.WITCH_WATER, props().craftRemainder(PORCELAIN_BUCKET.get()).stacksTo(1)));

    // Fluids
    public static final DeferredItem<Item> WITCH_WATER_BUCKET = ITEMS.register("witch_water_bucket", () -> new WitchWaterBucketItem(props().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static DeferredItem<Item> registerSimpleItem(String name) {
        return ITEMS.register(name, () -> new Item(props()));
    }

    // Returns new properties with creative tab set
    public static Item.Properties props() {
        return new Item.Properties();
    }

    // Register a block item
    public static DeferredItem<BlockItem> registerItemBlock(DeferredBlock<? extends Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), props()));
    }

    // BlockItems
    public static final DeferredItem<BlockItem> DUST = registerItemBlock(EBlocks.DUST);
    public static final DeferredItem<BlockItem> CRUSHED_NETHERRACK = registerItemBlock(EBlocks.CRUSHED_NETHERRACK);
    public static final DeferredItem<BlockItem> CRUSHED_END_STONE = registerItemBlock(EBlocks.CRUSHED_END_STONE);
    public static final DeferredItem<BlockItem> CRUSHED_DEEPSLATE = registerItemBlock(EBlocks.CRUSHED_DEEPSLATE);
    public static final DeferredItem<BlockItem> CRUSHED_BLACKSTONE = registerItemBlock(EBlocks.CRUSHED_BLACKSTONE);

    // Mechanical Sieves
    public static final DeferredItem<BlockItem> MECHANICAL_SIEVE = registerItemBlock(EBlocks.MECHANICAL_SIEVE);
    public static final DeferredItem<BlockItem> MECHANICAL_HAMMER = registerItemBlock(EBlocks.MECHANICAL_HAMMER);

    public static final DeferredItem<BlockItem> UNFIRED_PORCELAIN_CRUCIBLE = registerItemBlock(EBlocks.UNFIRED_PORCELAIN_CRUCIBLE);

    public static final DeferredItem<BlockItem> END_CAKE = registerItemBlock(EBlocks.END_CAKE);
    public static final DeferredItem<BlockItem> INFESTED_LEAVES = registerItemBlock(EBlocks.INFESTED_LEAVES);


    public static void addItemsToMainTab(CreativeModeTab.Output output) {
        for (var material : DefaultMaterials.BARRELS) {
            if (ModList.get().isLoaded(material.requiredModId)) {
                output.accept(material.getItem());
            }
        }

        for (var material : DefaultMaterials.SIEVES) {
            if (ModList.get().isLoaded(material.requiredModId)) {
                output.accept(material.getItem());
            }
        }

        output.accept(MECHANICAL_SIEVE.get());
        output.accept(MECHANICAL_HAMMER.get());

        output.accept(UNFIRED_PORCELAIN_CRUCIBLE.get());
        for (var material : DefaultMaterials.LAVA_CRUCIBLES) {
            if (ModList.get().isLoaded(material.requiredModId)) {
                output.accept(material.getItem());
            }
        }
        for (var material : DefaultMaterials.WATER_CRUCIBLES) {
            if (ModList.get().isLoaded(material.requiredModId)) {
                output.accept(material.getItem());
            }
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
