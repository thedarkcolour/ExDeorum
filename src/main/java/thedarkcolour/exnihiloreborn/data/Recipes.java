package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.barrel.FinishedBarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.barrel.FinishedBarrelMixingRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.FinishedCrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.FinishedHammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.modkit.data.MKRecipeProvider;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.*;
import static net.minecraft.data.recipes.SmithingTransformRecipeBuilder.smithing;
import static net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator.binomial;
import static thedarkcolour.modkit.data.MKRecipeProvider.unlockedByHaving;

class Recipes {
    public static void addRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        craftingRecipes(writer, recipes);
        smeltingRecipes(writer);
        sieveRecipes(writer);
        crucibleRecipes(writer);
        hammerRecipes(writer);
        barrelCompostRecipes(writer);
        barrelMixingRecipes(writer);
    }

    private static void craftingRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        // Crooks
        shapedCrook(recipes, EItems.CROOK, Ingredient.of(Tags.Items.RODS_WOODEN));
        shapedCrook(recipes, EItems.BONE_CROOK, Ingredient.of(Items.BONE));

        // Hammers
        shapedHammer(recipes, EItems.WOODEN_HAMMER, Ingredient.of(ItemTags.PLANKS));
        shapedHammer(recipes, EItems.STONE_HAMMER, Ingredient.of(ItemTags.STONE_CRAFTING_MATERIALS));
        shapedHammer(recipes, EItems.GOLDEN_HAMMER, Ingredient.of(Tags.Items.INGOTS_GOLD));
        shapedHammer(recipes, EItems.IRON_HAMMER, Ingredient.of(Tags.Items.INGOTS_IRON));
        shapedHammer(recipes, EItems.DIAMOND_HAMMER, Ingredient.of(Tags.Items.GEMS_DIAMOND));
        unlockedByHaving(smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(EItems.DIAMOND_HAMMER.get()),
                        Ingredient.of(Tags.Items.INGOTS_NETHERITE),
                        RecipeCategory.TOOLS,
                        EItems.NETHERITE_HAMMER.get()
                ),
                Items.NETHERITE_INGOT
        ).save(writer, RecipeBuilder.getDefaultRecipeId(EItems.NETHERITE_HAMMER.get()));

        // Crucibles
        uShaped(recipes, EItems.OAK_CRUCIBLE, Ingredient.of(Items.OAK_LOG), Ingredient.of(Items.OAK_SLAB));
        uShaped(recipes, EItems.SPRUCE_CRUCIBLE, Ingredient.of(Items.SPRUCE_LOG), Ingredient.of(Items.SPRUCE_SLAB));
        uShaped(recipes, EItems.BIRCH_CRUCIBLE, Ingredient.of(Items.BIRCH_LOG), Ingredient.of(Items.BIRCH_SLAB));
        uShaped(recipes, EItems.JUNGLE_CRUCIBLE, Ingredient.of(Items.JUNGLE_LOG), Ingredient.of(Items.JUNGLE_SLAB));
        uShaped(recipes, EItems.ACACIA_CRUCIBLE, Ingredient.of(Items.ACACIA_LOG), Ingredient.of(Items.ACACIA_SLAB));
        uShaped(recipes, EItems.DARK_OAK_CRUCIBLE, Ingredient.of(Items.DARK_OAK_LOG), Ingredient.of(Items.DARK_OAK_SLAB));
        uShaped(recipes, EItems.MANGROVE_CRUCIBLE, Ingredient.of(Items.MANGROVE_LOG), Ingredient.of(Items.MANGROVE_SLAB));
        uShaped(recipes, EItems.CHERRY_CRUCIBLE, Ingredient.of(Items.CHERRY_LOG), Ingredient.of(Items.CHERRY_SLAB));
        uShaped(recipes, EItems.BAMBOO_CRUCIBLE, Ingredient.of(Items.BAMBOO_BLOCK), Ingredient.of(Items.BAMBOO_SLAB));
        uShaped(recipes, EItems.CRIMSON_CRUCIBLE, Ingredient.of(Items.CRIMSON_STEM), Ingredient.of(Items.CRIMSON_SLAB));
        uShaped(recipes, EItems.WARPED_CRUCIBLE, Ingredient.of(Items.WARPED_STEM), Ingredient.of(Items.WARPED_SLAB));
        uShaped(recipes, EItems.UNFIRED_CRUCIBLE, Ingredient.of(EItems.PORCELAIN_CLAY.get()), Ingredient.of(EItems.PORCELAIN_CLAY.get()));

        // Barrels
        uShaped(recipes, EItems.OAK_BARREL, Ingredient.of(Items.OAK_PLANKS), Ingredient.of(Items.OAK_SLAB));
        uShaped(recipes, EItems.SPRUCE_BARREL, Ingredient.of(Items.SPRUCE_PLANKS), Ingredient.of(Items.SPRUCE_SLAB));
        uShaped(recipes, EItems.BIRCH_BARREL, Ingredient.of(Items.BIRCH_PLANKS), Ingredient.of(Items.BIRCH_SLAB));
        uShaped(recipes, EItems.JUNGLE_BARREL, Ingredient.of(Items.JUNGLE_PLANKS), Ingredient.of(Items.JUNGLE_SLAB));
        uShaped(recipes, EItems.ACACIA_BARREL, Ingredient.of(Items.ACACIA_PLANKS), Ingredient.of(Items.ACACIA_SLAB));
        uShaped(recipes, EItems.DARK_OAK_BARREL, Ingredient.of(Items.DARK_OAK_PLANKS), Ingredient.of(Items.DARK_OAK_SLAB));
        uShaped(recipes, EItems.MANGROVE_BARREL, Ingredient.of(Items.MANGROVE_PLANKS), Ingredient.of(Items.MANGROVE_SLAB));
        uShaped(recipes, EItems.CHERRY_BARREL, Ingredient.of(Items.CHERRY_PLANKS), Ingredient.of(Items.CHERRY_SLAB));
        uShaped(recipes, EItems.BAMBOO_BARREL, Ingredient.of(Items.BAMBOO_PLANKS), Ingredient.of(Items.BAMBOO_SLAB));
        uShaped(recipes, EItems.CRIMSON_BARREL, Ingredient.of(Items.CRIMSON_PLANKS), Ingredient.of(Items.CRIMSON_SLAB));
        uShaped(recipes, EItems.WARPED_BARREL, Ingredient.of(Items.WARPED_PLANKS), Ingredient.of(Items.WARPED_SLAB));
        uShaped(recipes, EItems.STONE_BARREL, Ingredient.of(Items.STONE), Ingredient.of(Items.STONE_SLAB));

        twoByTwo(recipes, Items.COBBLESTONE, Ingredient.of(EItems.STONE_PEBBLE.get()));
        twoByTwo(recipes, Items.ANDESITE, Ingredient.of(EItems.ANDESITE_PEBBLE.get()));
        twoByTwo(recipes, Items.DIORITE, Ingredient.of(EItems.DIORITE_PEBBLE.get()));
        twoByTwo(recipes, Items.GRANITE, Ingredient.of(EItems.GRANITE_PEBBLE.get()));
        twoByTwo(recipes, Items.IRON_ORE, Ingredient.of(EItems.IRON_ORE_CHUNK.get()));
        twoByTwo(recipes, Items.GOLD_ORE, Ingredient.of(EItems.GOLD_ORE_CHUNK.get()));
        twoByTwo(recipes, Items.COPPER_ORE, Ingredient.of(EItems.COPPER_ORE_CHUNK.get()));
        recipes.shapedCrafting(RecipeCategory.MISC, EItems.STRING_MESH.get(), recipe -> {
            recipe.define('s', Tags.Items.STRING);
            recipe.pattern("sss");
            recipe.pattern("sss");
            recipe.pattern("sss");
        });
    }

    private static void shapedCrook(MKRecipeProvider recipes, RegistryObject<? extends Item> crook, Ingredient stick) {
        recipes.shapedCrafting(RecipeCategory.TOOLS, crook.get(), recipe -> {
            recipe.define('x', stick);
            recipe.pattern("xx");
            recipe.pattern(" x");
            recipe.pattern(" x");
        });
    }

    private static void shapedHammer(MKRecipeProvider recipes, RegistryObject<? extends Item> hammer, Ingredient material) {
        recipes.shapedCrafting(RecipeCategory.TOOLS, hammer.get(), recipe -> {
            recipe.define('m', material);
            recipe.define('s', Tags.Items.RODS_WOODEN);
            recipe.pattern(" m ");
            recipe.pattern(" sm");
            recipe.pattern("s  ");
        });
    }

    private static void uShaped(MKRecipeProvider recipes, RegistryObject<? extends Item> result, Ingredient sides, Ingredient middle) {
        recipes.shapedCrafting(RecipeCategory.MISC, result.get(), recipe -> {
            recipe.define('s', sides);
            recipe.define('m', middle);
            recipe.pattern("s s");
            recipe.pattern("s s");
            recipe.pattern("sms");
        });
    }

    private static void twoByTwo(MKRecipeProvider recipes, Item result, Ingredient ingredient) {
        recipes.shapedCrafting(RecipeCategory.MISC, result, recipe -> {
            recipe.define('#', ingredient);
            recipe.pattern("##");
            recipe.pattern("##");
        });
    }

    // todo add support in modkit
    private static void smeltingRecipes(Consumer<FinishedRecipe> writer) {
        unlockedByHaving(
                smelting(Ingredient.of(EItems.UNFIRED_CRUCIBLE.get()), RecipeCategory.MISC, EItems.PORCELAIN_CRUCIBLE.get(), 0.1f, 200),
                EItems.UNFIRED_CRUCIBLE.get()
        ).save(writer, EItems.PORCELAIN_CRUCIBLE.getId());
        unlockedByHaving(
                smelting(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 200),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.COOKED_SILK_WORM.getId());
        unlockedByHaving(
                smoking(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 100),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.COOKED_SILK_WORM.getId().withSuffix("_from_smoking"));
        unlockedByHaving(
                campfireCooking(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 600),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.PORCELAIN_CRUCIBLE.getId().withSuffix("_from_campfire_cooking"));
    }

    private static void sieveRecipes(Consumer<FinishedRecipe> writer) {
        // Dirt -> String mesh
        forMesh(writer, Ingredient.of(Items.DIRT), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.7f));
            addDrop.accept(Items.BEETROOT_SEEDS, chance(0.35f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.35f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.35f));
            addDrop.accept(Items.FLINT, chance(0.25f));
        });

        // Flint mesh will be used to get a larger variety of outputs from dirt, and
        // is the lowest mesh tier where ore will start to drop.
        // Gravel -> String mesh
        forMesh(writer, Ingredient.of(Items.DIRT), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            addDrop.accept(EItems.ANDESITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(EItems.GRANITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(EItems.DIORITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.5f));
            addDrop.accept(Items.BEETROOT_SEEDS, chance(0.1f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.1f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.1f));
            addDrop.accept(Items.FLINT, chance(0.2f));
        });
    }

    private static BinomialDistributionGenerator chance(float p) {
        return binomial(1, p);
    }

    private static void forMesh(Consumer<FinishedRecipe> writer, Ingredient block, RegistryObject<? extends Item> mesh, Consumer<BiConsumer<Item, NumberProvider>> addDrops) {
        var folder = mesh.getId().getPath().replace("_mesh", "/");
        addDrops.accept((result, resultAmount) -> sieveRecipe(writer, folder + path(result), block, mesh, result, resultAmount));
    }

    private static void sieveRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, Supplier<? extends Item> mesh, Item result, NumberProvider chance) {
        consumer.accept(new FinishedSieveRecipe(new ResourceLocation(ExNihiloReborn.ID, "sieve/" + name), mesh.get(), block, result, chance));
    }

    private static void crucibleRecipes(Consumer<FinishedRecipe> writer) {
        lavaCrucible(writer, "cobblestone", Ingredient.of(Blocks.COBBLESTONE), 250);
        lavaCrucible(writer, "stone", Ingredient.of(Blocks.STONE), 250);
        lavaCrucible(writer, "gravel", Ingredient.of(Blocks.GRAVEL), 250);
        lavaCrucible(writer, "netherrack", Ingredient.of(Blocks.NETHERRACK), 1000);

        waterCrucible(writer, "sweet_berries", Ingredient.of(Items.SWEET_BERRIES), 50);
        waterCrucible(writer, "melon_slice", Ingredient.of(Items.MELON_SLICE), 50);

        waterCrucible(writer, "saplings", Ingredient.of(ItemTags.SAPLINGS), 100);
        waterCrucible(writer, "small_flowers", Ingredient.of(ItemTags.SMALL_FLOWERS), 100);
        waterCrucible(writer, "apple", Ingredient.of(Items.APPLE), 100);

        waterCrucible(writer, "tall_flowers", Ingredient.of(ItemTags.TALL_FLOWERS), 200);

        waterCrucible(writer, "cactus", Ingredient.of(Items.CACTUS), 250);
        waterCrucible(writer, "pumpkin", Ingredient.of(Items.PUMPKIN), 250);
        waterCrucible(writer, "melon", Ingredient.of(Items.MELON), 250);
        waterCrucible(writer, "leaves", Ingredient.of(ItemTags.LEAVES), 250);
        waterCrucible(writer, "lily_pad", Ingredient.of(Items.LILY_PAD), 100);
    }

    private static void lavaCrucible(Consumer<FinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExNihiloReborn.ID, "lava_crucible/" + id), ERecipeSerializers.LAVA_CRUCIBLE.get(), ingredient, Fluids.LAVA, volume));
    }

    private static void waterCrucible(Consumer<FinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExNihiloReborn.ID, "water_crucible/" + id), ERecipeSerializers.WATER_CRUCIBLE.get(), ingredient, Fluids.WATER, volume));
    }

    private static void hammerRecipes(Consumer<FinishedRecipe> writer) {
        // Cobblestone -> Gravel -> Sand -> Dust
        hammerRecipe(writer, "gravel", Ingredient.of(Items.COBBLESTONE), Blocks.GRAVEL);
        hammerRecipe(writer, "sand", Ingredient.of(Items.GRAVEL), Blocks.SAND);
        hammerRecipe(writer, "dust", Ingredient.of(Items.SAND), EBlocks.DUST.get());

        hammerRecipe(writer, "crushed_netherrack", Ingredient.of(Blocks.NETHERRACK), EBlocks.CRUSHED_NETHERRACK.get());

        hammerRecipe(writer, "crushing_sandstone", Ingredient.of(Items.SANDSTONE, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, Items.SMOOTH_SANDSTONE), Items.SAND);
        hammerRecipe(writer, "crushing_red_sandstone", Ingredient.of(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE), Items.RED_SAND);
        hammerRecipe(writer, "crushing_stone_bricks", Ingredient.of(Items.STONE_BRICKS), Items.CRACKED_STONE_BRICKS);

        hammerRecipe(writer, "stone_pebbles", Ingredient.of(Items.STONE, Items.STONE_BRICKS, Items.CHISELED_STONE_BRICKS, Items.CRACKED_STONE_BRICKS), EItems.STONE_PEBBLE.get(), new UniformGenerator(ConstantValue.exactly(1), ConstantValue.exactly(6)));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, ItemLike result) {
        hammerRecipe(writer, name, block, result, ConstantValue.exactly(1f));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, ItemLike result, NumberProvider resultAmount) {
        consumer.accept(new FinishedHammerRecipe(new ResourceLocation(ExNihiloReborn.ID, "hammer/" + name), block, result.asItem(), resultAmount));
    }

    private static void barrelCompostRecipes(Consumer<FinishedRecipe> writer) {
        // plants
        barrelCompost(writer, "saplings", Ingredient.of(ItemTags.SAPLINGS), 125);
        barrelCompost(writer, "leaves", Ingredient.of(ItemTags.LEAVES), 125);
        barrelCompost(writer, "small_flowers", Ingredient.of(ItemTags.SMALL_FLOWERS), 100);
        barrelCompost(writer, "tall_flowers", Ingredient.of(ItemTags.TALL_FLOWERS), 150);
        barrelCompost(writer, "mushrooms", Ingredient.of(Tags.Items.MUSHROOMS), 100);
        barrelCompost(writer, "lily_pad", Ingredient.of(Items.LILY_PAD), 100);
        barrelCompost(writer, "sugar_cane", Ingredient.of(Items.SUGAR_CANE), 80);
        barrelCompost(writer, "vine", Ingredient.of(Items.VINE), 100);
        barrelCompost(writer, "grass", Ingredient.of(Items.GRASS, Items.FERN), 100);
        barrelCompost(writer, "tall_grass", Ingredient.of(Items.TALL_GRASS, Items.LARGE_FERN), 150);
        barrelCompost(writer, "seagrass", Ingredient.of(Items.SEAGRASS), 80);
        barrelCompost(writer, "nether_wart", Ingredient.of(Items.NETHER_WART), 100);
        barrelCompost(writer, "seeds", Ingredient.of(Tags.Items.SEEDS), 80);
        barrelCompost(writer, "wheat", Ingredient.of(Tags.Items.CROPS_WHEAT), 80);
        barrelCompost(writer, "berries", Ingredient.of(Items.SWEET_BERRIES, Items.GLOW_BERRIES), 80);
        barrelCompost(writer, "melon", Ingredient.of(Items.MELON), 200);
        barrelCompost(writer, "cake", Ingredient.of(Items.CAKE), 500);
        barrelCompost(writer, "pumpkin", Ingredient.of(Items.PUMPKIN), 500);
        barrelCompost(writer, "carrots", Ingredient.of(Items.CARROT), 100);
        barrelCompost(writer, "potatoes", Ingredient.of(Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO), 80);
        // flesh
        barrelCompost(writer, "rotten_flesh", Ingredient.of(Items.ROTTEN_FLESH), 100);
        barrelCompost(writer, "spider_eye", Ingredient.of(Items.SPIDER_EYE), 80);
        barrelCompost(writer, "bread", Ingredient.of(Items.BREAD), 125);
        barrelCompost(writer, "string", Ingredient.of(Items.STRING), 40);
        // meats
        barrelCompost(writer, "pork", Ingredient.of(Items.PORKCHOP, Items.COOKED_PORKCHOP), 150);
        barrelCompost(writer, "beef", Ingredient.of(Items.BEEF, Items.COOKED_BEEF), 150);
        barrelCompost(writer, "chicken", Ingredient.of(Items.CHICKEN, Items.COOKED_CHICKEN), 125);
        barrelCompost(writer, "mutton", Ingredient.of(Items.MUTTON, Items.COOKED_MUTTON), 125);
        barrelCompost(writer, "salmon", Ingredient.of(Items.SALMON, Items.COOKED_SALMON), 125);
        barrelCompost(writer, "rabbit", Ingredient.of(Items.RABBIT, Items.COOKED_RABBIT), 100);
        barrelCompost(writer, "cod", Ingredient.of(Items.COD, Items.COOKED_COD), 100);
        barrelCompost(writer, "tropical_fish", Ingredient.of(Items.TROPICAL_FISH), 80);
        barrelCompost(writer, "pufferfish", Ingredient.of(Items.PUFFERFISH), 80);
        barrelCompost(writer, "egg", Ingredient.of(Items.EGG), 100);
        // foods
        barrelCompost(writer, "melon_slice", Ingredient.of(Items.MELON_SLICE), 40);
        barrelCompost(writer, "kelp", Ingredient.of(Items.KELP, Items.DRIED_KELP), 40);
        barrelCompost(writer, "silk_worms", Ingredient.of(EItems.SILK_WORM.get(), EItems.COOKED_SILK_WORM.get()), 40);
        barrelCompost(writer, "apple", Ingredient.of(Items.APPLE), 100);
        barrelCompost(writer, "cookie", Ingredient.of(Items.COOKIE), 100);
        barrelCompost(writer, "pumpkin_pie", Ingredient.of(Items.PUMPKIN_PIE), 150);
    }

    private static void barrelCompost(Consumer<FinishedRecipe> writer, String id, Ingredient ingredient, int volume) {
        writer.accept(new FinishedBarrelCompostRecipe(new ResourceLocation(ExNihiloReborn.ID, "barrel_compost/" + id), ingredient, volume));
    }

    private static void barrelMixingRecipes(Consumer<FinishedRecipe> writer) {
        barrelMixing(writer, Ingredient.of(Items.MILK_BUCKET), ForgeMod.WATER_TYPE, Items.SLIME_BLOCK);
        barrelMixing(writer, Ingredient.of(Items.SNOWBALL), ForgeMod.WATER_TYPE, Items.ICE);
        barrelMixing(writer, Ingredient.of(Items.SAND), EFluids.WITCH_WATER_TYPE, Items.SOUL_SAND);
        barrelMixing(writer, Ingredient.of(Items.REDSTONE), ForgeMod.LAVA_TYPE, Items.NETHERRACK);
        barrelMixing(writer, Ingredient.of(Items.GLOWSTONE_DUST), ForgeMod.LAVA_TYPE, Items.END_STONE);
        barrelMixing(writer, Ingredient.of(Items.WATER_BUCKET), ForgeMod.LAVA_TYPE, Items.OBSIDIAN);
        barrelMixing(writer, Ingredient.of(Items.LAVA_BUCKET), ForgeMod.WATER_TYPE, Items.STONE);
    }

    private static void barrelMixing(Consumer<FinishedRecipe> writer, Ingredient ingredient, Supplier<FluidType> fluidType, Item result) {
        writer.accept(new FinishedBarrelMixingRecipe(new ResourceLocation(ExNihiloReborn.ID, "barrel_mixing/" + path(result)), ingredient, fluidType.get(), 1000, result));
    }

    private static String path(Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item).getPath());
    }
}
