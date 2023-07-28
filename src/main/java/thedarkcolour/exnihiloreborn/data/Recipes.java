package thedarkcolour.exnihiloreborn.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.barrel.FinishedBarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.FinishedCrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.FinishedHammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.modkit.data.MKRecipeProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.*;
import static net.minecraft.data.recipes.SmithingTransformRecipeBuilder.smithing;

class Recipes {
    public static void addRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        craftingRecipes(writer, recipes);
        smeltingRecipes(writer, recipes);
        sieveRecipes(writer, recipes);
        crucibleRecipes(writer);
        hammerRecipes(writer);
        barrelCompostRecipes(writer);
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
        MKRecipeProvider.unlockedByHaving(smithing(
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

    // todo add support in modkit
    private static void smeltingRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        MKRecipeProvider.unlockedByHaving(
                smelting(Ingredient.of(EItems.UNFIRED_CRUCIBLE.get()), RecipeCategory.MISC, EItems.PORCELAIN_CRUCIBLE.get(), 0.1f, 200),
                EItems.UNFIRED_CRUCIBLE.get()
        ).save(writer, EItems.PORCELAIN_CRUCIBLE.getId());
        MKRecipeProvider.unlockedByHaving(
                smelting(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 200),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.COOKED_SILK_WORM.getId());
        MKRecipeProvider.unlockedByHaving(
                smoking(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 100),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.COOKED_SILK_WORM.getId().withSuffix("_from_smoking"));
        MKRecipeProvider.unlockedByHaving(
                campfireCooking(Ingredient.of(EItems.SILK_WORM.get()), RecipeCategory.FOOD, EItems.COOKED_SILK_WORM.get(), 0.1f, 600),
                EItems.SILK_WORM.get()
        ).save(writer, EItems.PORCELAIN_CRUCIBLE.getId().withSuffix("_from_campfire_cooking"));
    }

    private static void sieveRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        sieveRecipe(writer, "stone_pebble", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.withExtraChances(EItems.STONE_PEBBLE, new float[] {1.0f, 1.0f, 0.5f, 0.5f, 0.1f, 0.1f }));
        sieveRecipe(writer, "wheat_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.WHEAT_SEEDS, 0.7f));
        sieveRecipe(writer, "beetroot_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.BEETROOT_SEEDS, 0.35f));
        sieveRecipe(writer, "melon_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.MELON_SEEDS, 0.35f));
        sieveRecipe(writer, "pumpkin_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.PUMPKIN_SEEDS, 0.35f));
    }

    private static void sieveRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, Supplier<Item> mesh, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedSieveRecipe(ERecipeSerializers.SIEVE.get(), new ResourceLocation(ExNihiloReborn.ID, "sieve/" + name), mesh.get(), block, rewards));
    }

    private static void sieveRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, Supplier<Item> mesh, Reward rewards) {
        consumer.accept(new FinishedSieveRecipe(ERecipeSerializers.SIEVE.get(), new ResourceLocation(ExNihiloReborn.ID, "sieve/" + name), mesh.get(), block, ImmutableList.of(rewards)));
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
        hammerRecipe(writer, "gravel", Blocks.COBBLESTONE, new Reward(Blocks.GRAVEL));
        hammerRecipe(writer, "sand", Blocks.GRAVEL, new Reward(Blocks.SAND));
        hammerRecipe(writer, "dust", Blocks.SAND, new Reward(EBlocks.DUST.get()));

        hammerRecipe(writer, "crushed_netherrack", Blocks.NETHERRACK, new Reward(EBlocks.CRUSHED_NETHERRACK.get()));

        hammerRecipe(writer, "crushing_sandstone", Ingredient.of(Items.SANDSTONE, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, Items.SMOOTH_SANDSTONE), ImmutableList.of(new Reward(Items.SAND)));
        hammerRecipe(writer, "crushing_red_sandstone", Ingredient.of(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE), ImmutableList.of(new Reward(Items.RED_SAND)));
        hammerRecipe(writer, "crushing_stone_bricks", Items.STONE_BRICKS, new Reward(Items.CRACKED_STONE_BRICKS));

        hammerRecipe(writer, "stone_pebbles",
                Ingredient.of(Items.STONE, Items.CRACKED_STONE_BRICKS),
                Reward.withExtraChances(EItems.STONE_PEBBLE, new float[] { 0.75f, 0.75f, 0.5f, 0.25f, 0.05f }));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedHammerRecipe(ERecipeSerializers.HAMMER.get(), new ResourceLocation(ExNihiloReborn.ID, "hammer/" + name), block, rewards));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> consumer, String name, ItemLike block, Reward... rewards) {
        hammerRecipe(consumer, name, Ingredient.of(block), ImmutableList.<Reward>builder().add(rewards).build());
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> consumer, TagKey<Item> tag, Reward rewards) {
        consumer.accept(new FinishedHammerRecipe(ERecipeSerializers.HAMMER.get(), new ResourceLocation(ExNihiloReborn.ID, tag.location().getPath() + "_to_" + ForgeRegistries.ITEMS.getKey(rewards.getItem().getItem()).getPath()), Ingredient.of(tag), ImmutableList.of(rewards)));
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

    private static void barrelCompost(Consumer<FinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedBarrelCompostRecipe(new ResourceLocation(ExNihiloReborn.ID, "barrel_compost/" + id), ingredient, volume));
    }
}
