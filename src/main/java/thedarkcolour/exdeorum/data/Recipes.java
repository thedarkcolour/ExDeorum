/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.barrel.FinishedBarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.FinishedBarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.FinishedCrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.FinishedHammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.modkit.data.MKRecipeProvider;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator.binomial;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;
import static thedarkcolour.modkit.data.MKRecipeProvider.ingredient;
import static thedarkcolour.modkit.data.MKRecipeProvider.path;

class Recipes {
    private static final Ingredient SPORES_AND_SEEDS = ingredient(EItems.GRASS_SEEDS, EItems.MYCELIUM_SPORES, EItems.WARPED_NYLIUM_SPORES, EItems.CRIMSON_NYLIUM_SPORES);
    public static void addRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        craftingRecipes(recipes);
        smeltingRecipes(recipes);
        sieveRecipes(writer);
        crucibleRecipes(writer);
        hammerRecipes(writer);
        barrelCompostRecipes(writer);
        barrelMixingRecipes(writer);
    }

    private static void craftingRecipes(MKRecipeProvider recipes) {
        // Crooks
        shapedCrook(recipes, EItems.CROOK, ingredient(Tags.Items.RODS_WOODEN));
        shapedCrook(recipes, EItems.BONE_CROOK, ingredient(Items.BONE));

        // Hammers
        shapedHammer(recipes, EItems.WOODEN_HAMMER, ingredient(ItemTags.PLANKS));
        shapedHammer(recipes, EItems.STONE_HAMMER, ingredient(ItemTags.STONE_CRAFTING_MATERIALS));
        shapedHammer(recipes, EItems.GOLDEN_HAMMER, ingredient(Tags.Items.INGOTS_GOLD));
        shapedHammer(recipes, EItems.IRON_HAMMER, ingredient(Tags.Items.INGOTS_IRON));
        shapedHammer(recipes, EItems.DIAMOND_HAMMER, ingredient(Tags.Items.GEMS_DIAMOND));
        recipes.netheriteUpgrade(RecipeCategory.TOOLS, ingredient(EItems.DIAMOND_HAMMER.get()), EItems.NETHERITE_HAMMER.get());

        // Crucibles
        uShaped(recipes, EItems.OAK_CRUCIBLE, ingredient(Items.OAK_LOG), ingredient(Items.OAK_SLAB));
        uShaped(recipes, EItems.SPRUCE_CRUCIBLE, ingredient(Items.SPRUCE_LOG), ingredient(Items.SPRUCE_SLAB));
        uShaped(recipes, EItems.BIRCH_CRUCIBLE, ingredient(Items.BIRCH_LOG), ingredient(Items.BIRCH_SLAB));
        uShaped(recipes, EItems.JUNGLE_CRUCIBLE, ingredient(Items.JUNGLE_LOG), ingredient(Items.JUNGLE_SLAB));
        uShaped(recipes, EItems.ACACIA_CRUCIBLE, ingredient(Items.ACACIA_LOG), ingredient(Items.ACACIA_SLAB));
        uShaped(recipes, EItems.DARK_OAK_CRUCIBLE, ingredient(Items.DARK_OAK_LOG), ingredient(Items.DARK_OAK_SLAB));
        uShaped(recipes, EItems.MANGROVE_CRUCIBLE, ingredient(Items.MANGROVE_LOG), ingredient(Items.MANGROVE_SLAB));
        uShaped(recipes, EItems.CHERRY_CRUCIBLE, ingredient(Items.CHERRY_LOG), ingredient(Items.CHERRY_SLAB));
        uShaped(recipes, EItems.BAMBOO_CRUCIBLE, ingredient(Items.BAMBOO_BLOCK), ingredient(Items.BAMBOO_SLAB));
        uShaped(recipes, EItems.CRIMSON_CRUCIBLE, ingredient(Items.CRIMSON_STEM), ingredient(Items.CRIMSON_SLAB));
        uShaped(recipes, EItems.WARPED_CRUCIBLE, ingredient(Items.WARPED_STEM), ingredient(Items.WARPED_SLAB));
        uShaped(recipes, EItems.UNFIRED_PORCELAIN_CRUCIBLE, ingredient(EItems.PORCELAIN_CLAY_BALL.get()), ingredient(EItems.PORCELAIN_CLAY_BALL.get()));

        // Barrels
        uShaped(recipes, EItems.OAK_BARREL, ingredient(Items.OAK_PLANKS), ingredient(Items.OAK_SLAB));
        uShaped(recipes, EItems.SPRUCE_BARREL, ingredient(Items.SPRUCE_PLANKS), ingredient(Items.SPRUCE_SLAB));
        uShaped(recipes, EItems.BIRCH_BARREL, ingredient(Items.BIRCH_PLANKS), ingredient(Items.BIRCH_SLAB));
        uShaped(recipes, EItems.JUNGLE_BARREL, ingredient(Items.JUNGLE_PLANKS), ingredient(Items.JUNGLE_SLAB));
        uShaped(recipes, EItems.ACACIA_BARREL, ingredient(Items.ACACIA_PLANKS), ingredient(Items.ACACIA_SLAB));
        uShaped(recipes, EItems.DARK_OAK_BARREL, ingredient(Items.DARK_OAK_PLANKS), ingredient(Items.DARK_OAK_SLAB));
        uShaped(recipes, EItems.MANGROVE_BARREL, ingredient(Items.MANGROVE_PLANKS), ingredient(Items.MANGROVE_SLAB));
        uShaped(recipes, EItems.CHERRY_BARREL, ingredient(Items.CHERRY_PLANKS), ingredient(Items.CHERRY_SLAB));
        uShaped(recipes, EItems.BAMBOO_BARREL, ingredient(Items.BAMBOO_PLANKS), ingredient(Items.BAMBOO_SLAB));
        uShaped(recipes, EItems.CRIMSON_BARREL, ingredient(Items.CRIMSON_PLANKS), ingredient(Items.CRIMSON_SLAB));
        uShaped(recipes, EItems.WARPED_BARREL, ingredient(Items.WARPED_PLANKS), ingredient(Items.WARPED_SLAB));
        uShaped(recipes, EItems.STONE_BARREL, ingredient(Items.STONE), ingredient(Items.STONE_SLAB));

        // Pebbles and ore chunks
        recipes.grid2x2(Items.COBBLESTONE, ingredient(EItems.STONE_PEBBLE));
        recipes.grid2x2(Items.ANDESITE, ingredient(EItems.ANDESITE_PEBBLE));
        recipes.grid2x2(Items.DIORITE, ingredient(EItems.DIORITE_PEBBLE));
        recipes.grid2x2(Items.GRANITE, ingredient(EItems.GRANITE_PEBBLE));
        recipes.grid2x2(Items.COBBLED_DEEPSLATE, ingredient(EItems.DEEPSLATE_PEBBLE));
        recipes.grid2x2(Items.TUFF, ingredient(EItems.TUFF_PEBBLE));
        recipes.grid2x2(Items.CALCITE, ingredient(EItems.CALCITE_PEBBLE));
        recipes.grid2x2(Items.BLACKSTONE, ingredient(EItems.BLACKSTONE_PEBBLE));
        recipes.grid2x2(Items.BASALT, ingredient(EItems.BASALT_PEBBLE));
        recipes.grid2x2(Items.IRON_ORE, ingredient(EItems.IRON_ORE_CHUNK));
        recipes.grid2x2(Items.GOLD_ORE, ingredient(EItems.GOLD_ORE_CHUNK));
        recipes.grid2x2(Items.COPPER_ORE, ingredient(EItems.COPPER_ORE_CHUNK));
        recipes.grid2x2(Items.MOSS_BLOCK, ingredient(EItems.GRASS_SEEDS));

        // Sieves
        sieve(recipes, EItems.OAK_SIEVE, Items.OAK_PLANKS, Items.OAK_SLAB);
        sieve(recipes, EItems.SPRUCE_SIEVE, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        sieve(recipes, EItems.BIRCH_SIEVE, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        sieve(recipes, EItems.JUNGLE_SIEVE, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        sieve(recipes, EItems.ACACIA_SIEVE, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        sieve(recipes, EItems.DARK_OAK_SIEVE, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        sieve(recipes, EItems.MANGROVE_SIEVE, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        sieve(recipes, EItems.CHERRY_SIEVE, Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        sieve(recipes, EItems.BAMBOO_SIEVE, Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        sieve(recipes, EItems.CRIMSON_SIEVE, Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        sieve(recipes, EItems.WARPED_SIEVE, Items.WARPED_PLANKS, Items.WARPED_SLAB);

        // Meshes
        recipes.grid3x3(EItems.STRING_MESH.get(), ingredient(Tags.Items.STRING));
        mesh(recipes, EItems.FLINT_MESH.get(), ingredient(Items.FLINT));
        mesh(recipes, EItems.IRON_MESH.get(), ingredient(Tags.Items.INGOTS_IRON));
        mesh(recipes, EItems.GOLDEN_MESH.get(), ingredient(Tags.Items.INGOTS_GOLD));
        mesh(recipes, EItems.DIAMOND_MESH.get(), ingredient(Tags.Items.GEMS_DIAMOND));
        recipes.netheriteUpgrade(RecipeCategory.MISC, ingredient(EItems.DIAMOND_MESH), EItems.NETHERITE_MESH.get());

        // Watering cans
        wateringCan(recipes, EItems.WOODEN_WATERING_CAN, ingredient(ItemTags.PLANKS));
        wateringCan(recipes, EItems.STONE_WATERING_CAN, ingredient(ItemTags.STONE_TOOL_MATERIALS));
        wateringCan(recipes, EItems.IRON_WATERING_CAN, ingredient(Tags.Items.INGOTS_IRON));
        wateringCan(recipes, EItems.GOLDEN_WATERING_CAN, ingredient(Tags.Items.INGOTS_GOLD));
        wateringCan(recipes, EItems.DIAMOND_WATERING_CAN, ingredient(Tags.Items.GEMS_DIAMOND));
        recipes.netheriteUpgrade(RecipeCategory.TOOLS, ingredient(EItems.DIAMOND_WATERING_CAN), EItems.NETHERITE_WATERING_CAN.get());

        // misc
        recipes.shapelessCrafting(RecipeCategory.MISC, new ItemStack(EItems.PORCELAIN_CLAY_BALL.get()), ingredient(Items.CLAY_BALL), ingredient(Items.BONE_MEAL));
        recipes.shapedCrafting(RecipeCategory.MISC, EItems.UNFIRED_PORCELAIN_BUCKET.get(), recipe -> {
            recipe.define('#', EItems.PORCELAIN_CLAY_BALL);
            recipe.pattern("# #");
            recipe.pattern(" # ");
        });
        recipes.shapedCrafting(RecipeCategory.MISC, EItems.SCULK_CORE.get(), recipe -> {
            recipe.define('#', Items.ECHO_SHARD);
            recipe.define('O', Items.ENDER_PEARL);
            recipe.pattern(" # ");
            recipe.pattern("#O#");
            recipe.pattern(" # ");
        });
        recipes.shapedCrafting(RecipeCategory.FOOD, EItems.END_CAKE.get(), recipe -> {
            recipe.define('P', Items.ENDER_EYE);
            recipe.define('S', Items.SUGAR);
            recipe.define('E', Tags.Items.EGGS);
            recipe.define('C', EItems.CRUSHED_END_STONE);
            recipe.pattern("PPP");
            recipe.pattern("SES");
            recipe.pattern("CCC");
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

    private static void sieve(MKRecipeProvider recipes, Supplier<? extends Item> result, Item planks, Item slab) {
        recipes.shapedCrafting(RecipeCategory.MISC, result.get(), recipe -> {
            recipe.define('O', planks);
            recipe.define('_', slab);
            recipe.define('I', Tags.Items.RODS_WOODEN);
            recipe.pattern("O O");
            recipe.pattern("O_O");
            recipe.pattern("I I");
        });
    }

    private static void mesh(MKRecipeProvider recipes, Item result, Ingredient ingredient) {
        recipes.shapedCrafting(RecipeCategory.MISC, result, recipe -> {
            recipe.define('#', ingredient);
            recipe.define('S', ingredient(Tags.Items.STRING));
            recipe.pattern("S#S");
            recipe.pattern("#S#");
            recipe.pattern("S#S");
        });
    }

    private static void wateringCan(MKRecipeProvider recipes, Supplier<? extends Item> result, Ingredient shell) {
        recipes.shapedCrafting(RecipeCategory.TOOLS, result.get(), recipe -> {
            recipe.define('#', shell);
            recipe.define('B', Items.BOWL);
            recipe.pattern("#  ");
            recipe.pattern("#B#");
            recipe.pattern(" # ");
        });
    }

    private static void smeltingRecipes(MKRecipeProvider recipes) {
        recipes.smelting(ingredient(EItems.UNFIRED_PORCELAIN_CRUCIBLE), EItems.PORCELAIN_CRUCIBLE.get(), 0.1f);
        recipes.smelting(ingredient(EItems.UNFIRED_PORCELAIN_BUCKET), EItems.PORCELAIN_BUCKET.get(), 0.1f);

        recipes.foodCooking(EItems.SILK_WORM.get(), EItems.COOKED_SILK_WORM.get(), 0.1f);
    }

    private static void sieveRecipes(Consumer<FinishedRecipe> writer) {
        var allMeshes = List.of(EItems.STRING_MESH, EItems.FLINT_MESH, EItems.IRON_MESH, EItems.GOLDEN_MESH, EItems.DIAMOND_MESH, EItems.NETHERITE_MESH);

        // Dirt -> String mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            addDrop.accept(Items.FLINT, chance(0.25f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.125f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.1f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.1f));
            addDrop.accept(Items.BEETROOT_SEEDS, chance(0.1f));
            addDrop.accept(Items.POTATO, chance(0.1f));
            addDrop.accept(Items.CARROT, chance(0.1f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.1f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.03f));
            addDrop.accept(Items.SUGAR_CANE, chance(0.1f));
            addDrop.accept(Items.POISONOUS_POTATO, chance(0.05f));
            addDrop.accept(Items.BAMBOO, chance(0.04f));
        });
        // Flint mesh will be used to get a larger variety of outputs from dirt, just so people don't always
        // have the inventory spam that are the -ite pebbles.
        // Dirt -> Flint mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            addDrop.accept(Items.FLINT, chance(0.3f));
            addDrop.accept(EItems.ANDESITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(EItems.GRANITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(EItems.DIORITE_PEBBLE.get(), binomial(7, 0.4f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.15f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.12f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.12f));
            addDrop.accept(Items.POTATO, chance(0.13f));
            addDrop.accept(Items.CARROT, chance(0.13f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.15f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.05f));
            addDrop.accept(Items.SUGAR_CANE, chance(0.15f));
            addDrop.accept(Items.POISONOUS_POTATO, chance(0.03f));
            addDrop.accept(Items.BAMBOO, chance(0.04f));
            addDrop.accept(Items.PINK_PETALS, chance(0.03f));
            addDrop.accept(Items.SWEET_BERRIES, chance(0.05f));
        });
        // Dirt -> Iron mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.65f));
            addDrop.accept(Items.FLINT, chance(0.3f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.175f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.15f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.15f));
            addDrop.accept(Items.POTATO, chance(0.15f));
            addDrop.accept(Items.CARROT, chance(0.15f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.175f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.1f));
            addDrop.accept(Items.SUGAR_CANE, chance(0.15f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.05f));
            addDrop.accept(Items.BAMBOO, chance(0.06f));
        });
        // Gold tends to spread its luster to whatever passes through it...
        // Dirt -> Gold mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            addDrop.accept(Items.FLINT, chance(0.2f));
            addDrop.accept(Items.WHEAT_SEEDS, chance(0.2f));
            addDrop.accept(Items.MELON_SEEDS, chance(0.165f));
            addDrop.accept(Items.PUMPKIN_SEEDS, chance(0.165f));
            addDrop.accept(Items.POTATO, chance(0.175f));
            addDrop.accept(Items.CARROT, chance(0.175f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.25f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.13f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.05f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.05f));
            addDrop.accept(Items.GOLDEN_CARROT, chance(0.02f));
            addDrop.accept(Items.BAMBOO, chance(0.05f));
        });
        // Diamond tables have less junk items in them. Maybe you want those items? Use other meshes!
        // Dirt -> Diamond mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            addDrop.accept(Items.FLINT, binomial(3, 0.3f));
            addDrop.accept(Items.POTATO, chance(0.25f));
            addDrop.accept(Items.CARROT, chance(0.25f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.15f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.1f));
            addDrop.accept(Items.BAMBOO, chance(0.06f));
        });
        // Netherite should be the best for all drops (except pebbles)
        // Dirt -> Netherite mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(5, 0.4f));
            addDrop.accept(Items.FLINT, binomial(3, 0.4f));
            addDrop.accept(Items.POTATO, chance(0.3f));
            addDrop.accept(Items.CARROT, chance(0.3f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.2f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.2f));
            addDrop.accept(Items.GOLDEN_CARROT, chance(0.01f));
            addDrop.accept(Items.GOLDEN_APPLE, chance(0.0025f));
            addDrop.accept(Items.BAMBOO, chance(0.06f));
        });

        // Gravel -> String mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(Items.FLINT, chance(0.2f));
            addDrop.accept(Items.COAL, chance(0.1f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.03f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.05f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.03f));
            addDrop.accept(Items.DIAMOND, chance(0.02f));
            addDrop.accept(Items.EMERALD, chance(0.01f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.01f));
        });
        // Gravel -> Flint mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.ANDESITE_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(EItems.GRANITE_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(EItems.DIORITE_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(Items.POINTED_DRIPSTONE, chance(0.15f));
            addDrop.accept(Items.FLINT, chance(0.25f));
            addDrop.accept(Items.COAL, chance(0.125f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.05f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.1f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.07f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.04f));
            addDrop.accept(Items.DIAMOND, chance(0.03f));
            addDrop.accept(Items.EMERALD, chance(0.015f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.015f));
        });
        // Gravel -> Iron mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(Items.FLINT, chance(0.15f));
            addDrop.accept(Items.COAL, chance(0.15f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.08f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.11f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.06f));
            addDrop.accept(Items.DIAMOND, chance(0.05f));
            addDrop.accept(Items.EMERALD, chance(0.04f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.04f));
        });
        // Golden mesh has much higher drops for gold and gems, but at the cost of much lower drops for metals
        // Gravel -> Golden mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(Items.FLINT, chance(0.13f));
            addDrop.accept(Items.COAL, chance(0.2f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.1f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.07f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.04f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.1f));
            addDrop.accept(Items.DIAMOND, chance(0.1f));
            addDrop.accept(Items.EMERALD, chance(0.09f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));
            addDrop.accept(Items.RAW_GOLD, chance(0.02f));
        });
        // Gravel -> Diamond mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(Items.FLINT, chance(0.05f));
            addDrop.accept(Items.COAL, chance(0.06f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.11f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.07f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(Items.DIAMOND, chance(0.08f));
            addDrop.accept(Items.EMERALD, chance(0.07f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.06f));
        });
        // Gravel -> Netherite mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(Items.COAL, chance(0.06f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.11f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.1f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(Items.DIAMOND, chance(0.1f));
            addDrop.accept(Items.EMERALD, chance(0.09f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.04f));
            addDrop.accept(Items.RAW_GOLD, chance(0.01f));
        });

        // Sand -> String mesh
        forMesh(writer, ingredient(Items.SAND), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(Items.CACTUS, chance(0.13f));
            addDrop.accept(Items.FLINT, chance(0.2f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.13f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.13f));
            addDrop.accept(Items.KELP, chance(0.1f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.05f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(Items.FLINT, binomial(2, 0.2f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.03f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.16f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.16f));
            addDrop.accept(Items.BURN_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.DANGER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.FRIEND_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.HEART_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.HEARTBREAK_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.HOWL_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.SHEAF_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.BLADE_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.EXPLORER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.MOURNER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.PLENTY_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.ANGLER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.SHELTER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.SNORT_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.ARCHER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.MINER_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.PRIZE_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.SKULL_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.ARMS_UP_POTTERY_SHERD, chance(0.03f));
            addDrop.accept(Items.BREWER_POTTERY_SHERD, chance(0.03f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(Items.CACTUS, chance(0.13f));
            addDrop.accept(Items.FLINT, chance(0.23f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.18f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.18f));
            addDrop.accept(Items.KELP, chance(0.07f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.03f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.06f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(Items.CACTUS, chance(0.10f));
            addDrop.accept(Items.FLINT, chance(0.18f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(3, 0.28f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.16f));
            addDrop.accept(Items.KELP, chance(0.05f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.03f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.08f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.08f));
            addDrop.accept(Items.RAW_GOLD, chance(0.04f));
            addDrop.accept(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, chance(0.01f));
            addDrop.accept(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, chance(0.01f));
            addDrop.accept(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, chance(0.01f));
            addDrop.accept(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, chance(0.01f));
            addDrop.accept(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, chance(0.01f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(Items.FLINT, chance(0.23f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.22f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.22f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.09f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.09f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(Items.CACTUS, chance(0.15f));
            addDrop.accept(Items.FLINT, binomial(2, 0.23f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.23f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.23f));
            addDrop.accept(Items.KELP, chance(0.1f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.07f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.12f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.12f));
        });

        forMesh(writer, ingredient(EItems.DUST.get()), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(Items.BONE_MEAL, chance(0.1f));
            addDrop.accept(Items.REDSTONE, chance(0.06f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.04f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.03f));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
            addDrop.accept(Items.BONE_MEAL, chance(0.11f));
            addDrop.accept(Items.REDSTONE, chance(0.09f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.07f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.04f));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.BONE_MEAL, chance(0.12f));
            addDrop.accept(Items.REDSTONE, chance(0.1f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.09f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.05f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.06f));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.BONE_MEAL, chance(0.11f));
            addDrop.accept(Items.REDSTONE, chance(0.12f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.11f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(2, 0.18f));
            addDrop.accept(Items.RAW_GOLD, chance(0.02f));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.14f));
            addDrop.accept(Items.BONE_MEAL, chance(0.10f));
            addDrop.accept(Items.REDSTONE, chance(0.12f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.11f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.14f));
            addDrop.accept(Items.BONE_MEAL, chance(0.13f));
            addDrop.accept(Items.REDSTONE, chance(0.14f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.15f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.1f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.08f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.05f));
            addDrop.accept(Items.DIAMOND, chance(0.04f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.04f));
            addDrop.accept(Items.EMERALD, chance(0.03f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.TUFF_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(EItems.CALCITE_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.11f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.11f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.06f));
            addDrop.accept(Items.DIAMOND, chance(0.05f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.05f));
            addDrop.accept(Items.EMERALD, chance(0.04f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.06f));
            addDrop.accept(Items.DIAMOND, chance(0.06f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.08f));
            addDrop.accept(Items.EMERALD, chance(0.05f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.15f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.08f));
            addDrop.accept(Items.DIAMOND, chance(0.08f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.07f));
            addDrop.accept(Items.EMERALD, chance(0.07f));
            addDrop.accept(Items.RAW_GOLD, chance(0.05f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(3, 0.1f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.16f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.07f));
            addDrop.accept(Items.DIAMOND, chance(0.08f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.12f));
            addDrop.accept(Items.EMERALD, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.7f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.17f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.15f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.1f));
            addDrop.accept(Items.DIAMOND, chance(0.1f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.14f));
            addDrop.accept(Items.EMERALD, chance(0.1f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.STRING_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.5f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.02f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.2f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.08f));
            addDrop.accept(Items.GUNPOWDER, chance(0.07f));
            addDrop.accept(Items.BLACK_DYE, chance(0.07f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.65f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.55f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.03f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.225f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.09f));
            addDrop.accept(Items.GUNPOWDER, chance(0.09f));
            addDrop.accept(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.IRON_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.55f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.04f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.25f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.09f));
            addDrop.accept(Items.GUNPOWDER, chance(0.09f));
            addDrop.accept(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.GOLDEN_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.05f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(8, 0.325f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(Items.BLACK_DYE, chance(0.06f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.DIAMOND_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.275f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.11f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.NETHERITE_MESH, addDrop -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.75f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.1f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.325f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.12f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.STRING_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(3, 0.4f));
            addDrops.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.3f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.08f));
            addDrops.accept(Items.QUARTZ, chance(0.08f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.05f));
            addDrops.accept(Items.GUNPOWDER, chance(0.08f));
            addDrops.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.05f));
            addDrops.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.05f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.07f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.FLINT_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrops.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.4f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.09f));
            addDrops.accept(Items.QUARTZ, chance(0.09f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.06f));
            addDrops.accept(Items.GUNPOWDER, chance(0.09f));
            addDrops.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.07f));
            addDrops.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.07f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.IRON_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrops.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.1f));
            addDrops.accept(Items.QUARTZ, chance(0.11f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.07f));
            addDrops.accept(Items.GUNPOWDER, chance(0.1f));
            addDrops.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            addDrops.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.1f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.GOLDEN_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrops.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.11f));
            addDrops.accept(Items.QUARTZ, chance(0.13f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.08f));
            addDrops.accept(Items.GUNPOWDER, chance(0.11f));
            addDrops.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            addDrops.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.14f));
            addDrops.accept(Items.RAW_GOLD, chance(0.03f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.DIAMOND_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.14f));
            addDrops.accept(Items.QUARTZ, chance(0.13f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrops.accept(Items.GUNPOWDER, chance(0.13f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.12f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.NETHERITE_MESH, addDrops -> {
            addDrops.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            addDrops.accept(Items.BLAZE_POWDER, chance(0.15f));
            addDrops.accept(Items.QUARTZ, chance(0.15f));
            addDrops.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrops.accept(Items.GUNPOWDER, chance(0.13f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.12f));
        });

        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.STRING_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.12f));
            addDrops.accept(Items.GUNPOWDER, chance(0.07f));
            addDrops.accept(Items.BONE, chance(0.08f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.06f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.FLINT_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.14f));
            addDrops.accept(Items.GUNPOWDER, chance(0.08f));
            addDrops.accept(Items.BONE, chance(0.1f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.07f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.07f));
            addDrops.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.03f));
            addDrops.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.03f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.IRON_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.15f));
            addDrops.accept(Items.GUNPOWDER, chance(0.07f));
            addDrops.accept(Items.BONE, chance(0.08f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.06f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.GOLDEN_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.17f));
            addDrops.accept(Items.GUNPOWDER, chance(0.1f));
            addDrops.accept(Items.BONE, chance(0.11f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.08f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.09f));
            addDrops.accept(Items.GOLD_NUGGET, chance(0.15f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.DIAMOND_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.19f));
            addDrops.accept(Items.GUNPOWDER, chance(0.11f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.09f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.11f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.NETHERITE_MESH, addDrops -> {
            addDrops.accept(Items.QUARTZ, chance(0.21f));
            addDrops.accept(Items.GUNPOWDER, chance(0.14f));
            addDrops.accept(Items.GHAST_TEAR, chance(0.11f));
            addDrops.accept(Items.GLOWSTONE_DUST, chance(0.13f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.STRING_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.07f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.09f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrops.accept(Items.ENDER_EYE, chance(0.02f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.FLINT_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.08f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.11f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.06f));
            addDrops.accept(Items.ENDER_EYE, chance(0.03f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.IRON_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.10f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.13f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.07f));
            addDrops.accept(Items.ENDER_EYE, chance(0.04f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.GOLDEN_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.12f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.12f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.06f));
            addDrops.accept(Items.ENDER_EYE, chance(0.07f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.DIAMOND_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.15f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.10f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrops.accept(Items.ENDER_EYE, chance(0.09f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.NETHERITE_MESH, addDrops -> {
            addDrops.accept(Items.ENDER_PEARL, chance(0.17f));
            addDrops.accept(Items.CHORUS_FRUIT, chance(0.10f));
            addDrops.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrops.accept(Items.ENDER_EYE, chance(0.09f));
            addDrops.accept(Items.ECHO_SHARD, chance(0.03f));
            addDrops.accept(Items.SCULK_SHRIEKER, chance(0.01f));
        });

        for (int i = 0; i < allMeshes.size(); i++) {
            var mesh = allMeshes.get(i);
            final int j = i;
            forMesh(writer, ingredient(Items.MOSS_BLOCK), mesh, addDrop -> {
                addDrop.accept(Items.OAK_SAPLING, chance(0.13f));
                addDrop.accept(Items.SPRUCE_SAPLING, chance(0.11f));
                addDrop.accept(Items.BIRCH_SAPLING, chance(0.11f));
                addDrop.accept(Items.ACACIA_SAPLING, chance(0.11f));
                addDrop.accept(Items.DARK_OAK_SAPLING, chance(0.11f));
                addDrop.accept(Items.JUNGLE_SAPLING, chance(0.11f));
                addDrop.accept(Items.CHERRY_SAPLING, chance(0.11f));
                addDrop.accept(Items.MANGROVE_PROPAGULE, chance(0.11f));
                addDrop.accept(Items.AZALEA, chance(0.08f + j * 0.01f));
                addDrop.accept(Items.GLOW_BERRIES, chance(0.04f + j * 0.075f));
                addDrop.accept(Items.SMALL_DRIPLEAF, chance(0.07f + j * 0.025f));
                addDrop.accept(Items.BIG_DRIPLEAF, chance(0.05f + j * 0.02f));
                addDrop.accept(Items.SPORE_BLOSSOM, chance(0.03f + j * 0.015f));
            });
        }
        forMesh(writer, ingredient(Items.MOSS_BLOCK), EItems.FLINT_MESH, addDrop -> {
            addDrop.accept(Items.SWEET_BERRIES, chance(0.03f));
            addDrop.accept(Items.FLOWERING_AZALEA, chance(0.03f));
            addDrop.accept(Items.GLOW_LICHEN, chance(0.04f));
            addDrop.accept(Items.LILY_PAD, chance(0.04f));
        });
    }

    private static BinomialDistributionGenerator chance(float p) {
        return binomial(1, p);
    }

    private static void forMesh(Consumer<FinishedRecipe> writer, Ingredient block, RegistryObject<? extends Item> mesh, Consumer<BiConsumer<Item, NumberProvider>> addDrops) {
        var folder = mesh.getId().getPath().replace("_mesh", "/");
        addDrops.accept((result, resultAmount) -> sieveRecipe(writer, path(block.getItems()[0].getItem()) + "/" + folder + path(result), block, mesh, result, resultAmount));
    }

    private static void sieveRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient block, Supplier<? extends Item> mesh, Item result, NumberProvider chance) {
        consumer.accept(new FinishedSieveRecipe(new ResourceLocation(ExDeorum.ID, "sieve/" + name), mesh.get(), block, result, chance));
    }

    private static void crucibleRecipes(Consumer<FinishedRecipe> writer) {
        lavaCrucible(writer, "cobblestone", ingredient(Tags.Items.COBBLESTONE), 250);
        lavaCrucible(writer, "stone", ingredient(Tags.Items.STONE), 250);
        lavaCrucible(writer, "gravel", ingredient(Tags.Items.GRAVEL), 250);
        lavaCrucible(writer, "netherrack", ingredient(Tags.Items.NETHERRACK), 500);

        waterCrucible(writer, "saplings", ingredient(ItemTags.SAPLINGS), 100);
        waterCrucible(writer, "leaves", ingredient(ItemTags.LEAVES), 250);
        waterCrucible(writer, "small_flowers", ingredient(ItemTags.SMALL_FLOWERS), 100);
        waterCrucible(writer, "tall_flowers", ingredient(ItemTags.TALL_FLOWERS), 200);
        waterCrucible(writer, "mushrooms", ingredient(Tags.Items.MUSHROOMS), 100);
        waterCrucible(writer, "lily_pad", ingredient(Items.LILY_PAD), 150);
        waterCrucible(writer, "sugar_cane", ingredient(Items.SUGAR_CANE), 100);
        waterCrucible(writer, "vine", ingredient(Items.VINE), 100);
        waterCrucible(writer, "seeds_and_spores", SPORES_AND_SEEDS, 50);
        waterCrucible(writer, "seeds", ingredient(Tags.Items.SEEDS), 50);
        waterCrucible(writer, "grass", ingredient(Items.GRASS, Items.TALL_GRASS), 100);
        waterCrucible(writer, "grass_block", ingredient(Items.GRASS_BLOCK), 150);
        waterCrucible(writer, "sweet_berries", ingredient(Items.SWEET_BERRIES, Items.GLOW_BERRIES), 50);
        waterCrucible(writer, "melon_slice", ingredient(Items.MELON_SLICE), 50);
        waterCrucible(writer, "potato", ingredient(Items.POTATO), 100);
        waterCrucible(writer, "carrot", ingredient(Items.CARROT), 100);
        waterCrucible(writer, "beetroot", ingredient(Items.BEETROOT), 100);
        waterCrucible(writer, "apple", ingredient(Items.APPLE), 100);
        waterCrucible(writer, "cactus", ingredient(Items.CACTUS), 250);
        waterCrucible(writer, "pumpkin", ingredient(Items.PUMPKIN), 250);
        waterCrucible(writer, "melon", ingredient(Items.MELON), 250);
        waterCrucible(writer, "seagrass", ingredient(Items.SEAGRASS), 100);
        waterCrucible(writer, "sea_pickle", ingredient(Items.SEA_PICKLE), 200);
        waterCrucible(writer, "moss", ingredient(Items.MOSS_BLOCK), 150);
        waterCrucible(writer, "moss_carpet", ingredient(Items.MOSS_CARPET), 100);
        waterCrucible(writer, "spore_blossom", ingredient(Items.SPORE_BLOSSOM), 150);
    }

    private static void lavaCrucible(Consumer<FinishedRecipe> writer, String id, Ingredient ingredient, int volume) {
        writer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExDeorum.ID, "lava_crucible/" + id), ERecipeSerializers.LAVA_CRUCIBLE.get(), ingredient, Fluids.LAVA, volume));
    }

    private static void waterCrucible(Consumer<FinishedRecipe> writer, String id, Ingredient ingredient, int volume) {
        writer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExDeorum.ID, "water_crucible/" + id), ERecipeSerializers.WATER_CRUCIBLE.get(), ingredient, Fluids.WATER, volume));
    }

    private static void hammerRecipes(Consumer<FinishedRecipe> writer) {
        // Cobblestone -> Gravel -> Sand -> Dust
        hammerRecipe(writer, "gravel", ingredient(Items.COBBLESTONE, Items.DIORITE, Items.GRANITE, Items.ANDESITE), Blocks.GRAVEL);
        hammerRecipe(writer, "sand", ingredient(Items.GRAVEL), Blocks.SAND);
        hammerRecipe(writer, "dust", ingredient(Items.SAND, Items.RED_SAND), EBlocks.DUST.get());

        hammerRecipe(writer, "crushed_deepslate", ingredient(Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE), EBlocks.CRUSHED_DEEPSLATE.get());
        hammerRecipe(writer, "crushed_netherrack", ingredient(Blocks.NETHERRACK), EBlocks.CRUSHED_NETHERRACK.get());
        hammerRecipe(writer, "crushed_blackstone", ingredient(Blocks.BLACKSTONE), EBlocks.CRUSHED_BLACKSTONE.get());
        hammerRecipe(writer, "crushed_end_stone", ingredient(Blocks.END_STONE), EBlocks.CRUSHED_END_STONE.get());

        hammerRecipe(writer, "crushing_sandstone", ingredient(Items.SANDSTONE, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, Items.SMOOTH_SANDSTONE), Items.SAND);
        hammerRecipe(writer, "crushing_red_sandstone", ingredient(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE), Items.RED_SAND);
        hammerRecipe(writer, "crushing_stone_bricks", ingredient(Items.STONE_BRICKS), Items.CRACKED_STONE_BRICKS);

        hammerRecipe(writer, "stone_pebbles", ingredient(Items.STONE, Items.STONE_BRICKS, Items.CHISELED_STONE_BRICKS, Items.CRACKED_STONE_BRICKS), EItems.STONE_PEBBLE.get(), new UniformGenerator(ConstantValue.exactly(1), ConstantValue.exactly(6)));
        hammerRecipe(writer, "basalt", ingredient(Items.POLISHED_BASALT, Items.SMOOTH_BASALT), Items.BASALT);

        hammerRecipe(writer, "tube_coral", ingredient(Items.TUBE_CORAL_BLOCK), Items.TUBE_CORAL);
        hammerRecipe(writer, "brain_coral", ingredient(Items.BRAIN_CORAL_BLOCK), Items.BRAIN_CORAL);
        hammerRecipe(writer, "bubble_coral", ingredient(Items.BUBBLE_CORAL_BLOCK), Items.BUBBLE_CORAL);
        hammerRecipe(writer, "fire_coral", ingredient(Items.FIRE_CORAL_BLOCK), Items.FIRE_CORAL);
        hammerRecipe(writer, "horn_coral", ingredient(Items.HORN_CORAL_BLOCK), Items.HORN_CORAL);
        hammerRecipe(writer, "tube_coral_fan", ingredient(Items.TUBE_CORAL), Items.TUBE_CORAL_FAN);
        hammerRecipe(writer, "brain_coral_fan", ingredient(Items.BRAIN_CORAL), Items.BRAIN_CORAL_FAN);
        hammerRecipe(writer, "bubble_coral_fan", ingredient(Items.BUBBLE_CORAL), Items.BUBBLE_CORAL_FAN);
        hammerRecipe(writer, "fire_coral_fan", ingredient(Items.FIRE_CORAL), Items.FIRE_CORAL_FAN);
        hammerRecipe(writer, "horn_coral_fan", ingredient(Items.HORN_CORAL), Items.HORN_CORAL_FAN);

        hammerRecipe(writer, "prismarine", ingredient(Items.PRISMARINE, Items.PRISMARINE_BRICKS, Items.DARK_PRISMARINE), Items.PRISMARINE_SHARD, between(1, 4));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, ItemLike result) {
        hammerRecipe(writer, name, block, result, ConstantValue.exactly(1f));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, ItemLike result, NumberProvider resultAmount) {
        writer.accept(new FinishedHammerRecipe(new ResourceLocation(ExDeorum.ID, "hammer/" + name), block, result.asItem(), resultAmount));
    }

    private static void barrelCompostRecipes(Consumer<FinishedRecipe> writer) {
        // plants
        barrelCompost(writer, "saplings", ingredient(ItemTags.SAPLINGS), 125);
        barrelCompost(writer, "leaves", ingredient(ItemTags.LEAVES), 125);
        barrelCompost(writer, "small_flowers", ingredient(ItemTags.SMALL_FLOWERS), 100);
        barrelCompost(writer, "tall_flowers", ingredient(ItemTags.TALL_FLOWERS), 150);
        barrelCompost(writer, "mushrooms", ingredient(Tags.Items.MUSHROOMS), 100);
        barrelCompost(writer, "lily_pad", ingredient(Items.LILY_PAD), 100);
        barrelCompost(writer, "sugar_cane", ingredient(Items.SUGAR_CANE), 80);
        barrelCompost(writer, "vine", ingredient(Items.VINE), 100);
        barrelCompost(writer, "grass", ingredient(Items.GRASS, Items.FERN), 100);
        barrelCompost(writer, "tall_grass", ingredient(Items.TALL_GRASS, Items.LARGE_FERN), 150);
        barrelCompost(writer, "seagrass", ingredient(Items.SEAGRASS), 80);
        barrelCompost(writer, "nether_wart", ingredient(Items.NETHER_WART), 100);
        barrelCompost(writer, "seeds", ingredient(Tags.Items.SEEDS), 80);
        barrelCompost(writer, "wheat", ingredient(Tags.Items.CROPS_WHEAT), 80);
        barrelCompost(writer, "berries", ingredient(Items.SWEET_BERRIES, Items.GLOW_BERRIES), 80);
        barrelCompost(writer, "melon", ingredient(Items.MELON), 200);
        barrelCompost(writer, "cake", ingredient(Items.CAKE), 500);
        barrelCompost(writer, "pumpkin", ingredient(Items.PUMPKIN), 500);
        barrelCompost(writer, "carrots", ingredient(Items.CARROT), 100);
        barrelCompost(writer, "potatoes", ingredient(Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO), 80);
        barrelCompost(writer, "beetroot", ingredient(Items.BEETROOT), 80);
        barrelCompost(writer, "moss_block", ingredient(Items.MOSS_BLOCK), 150);
        barrelCompost(writer, "moss_carpet", ingredient(Items.MOSS_CARPET), 100);
        barrelCompost(writer, "spores_and_seeds", SPORES_AND_SEEDS, 80);
        barrelCompost(writer, "bamboo", ingredient(Items.BAMBOO), 100);
        barrelCompost(writer, "cactus", ingredient(Items.CACTUS), 125);
        barrelCompost(writer, "dead_bush", ingredient(Items.DEAD_BUSH), 80);
        barrelCompost(writer, "chorus_flower", ingredient(Items.CHORUS_FLOWER), 150);
        barrelCompost(writer, "chorus_fruit", ingredient(Items.CHORUS_FRUIT), 80);
        barrelCompost(writer, "chorus_plant", ingredient(Items.CHORUS_PLANT), 150);
        barrelCompost(writer, "kelp", ingredient(Items.KELP, Items.DRIED_KELP), 80);
        barrelCompost(writer, "sea_pickle", ingredient(Items.SEA_PICKLE), 80);
        barrelCompost(writer, "spore_blossom", ingredient(Items.SPORE_BLOSSOM), 125);
        barrelCompost(writer, "weeping_vines", ingredient(Items.WEEPING_VINES), 100);
        barrelCompost(writer, "twisting_vines", ingredient(Items.TWISTING_VINES), 100);
        // flesh
        barrelCompost(writer, "rotten_flesh", ingredient(Items.ROTTEN_FLESH), 100);
        barrelCompost(writer, "spider_eye", ingredient(Items.SPIDER_EYE), 80);
        barrelCompost(writer, "fermented_spider_eye", ingredient(Items.FERMENTED_SPIDER_EYE), 100);
        barrelCompost(writer, "string", ingredient(Items.STRING), 40);
        barrelCompost(writer, "rabbit_foot", ingredient(Items.RABBIT_FOOT), 100);
        // meats
        barrelCompost(writer, "pork", ingredient(Items.PORKCHOP, Items.COOKED_PORKCHOP), 150);
        barrelCompost(writer, "beef", ingredient(Items.BEEF, Items.COOKED_BEEF), 150);
        barrelCompost(writer, "chicken", ingredient(Items.CHICKEN, Items.COOKED_CHICKEN), 125);
        barrelCompost(writer, "mutton", ingredient(Items.MUTTON, Items.COOKED_MUTTON), 125);
        barrelCompost(writer, "salmon", ingredient(Items.SALMON, Items.COOKED_SALMON), 125);
        barrelCompost(writer, "rabbit", ingredient(Items.RABBIT, Items.COOKED_RABBIT), 100);
        barrelCompost(writer, "cod", ingredient(Items.COD, Items.COOKED_COD), 100);
        barrelCompost(writer, "tropical_fish", ingredient(Items.TROPICAL_FISH), 80);
        barrelCompost(writer, "pufferfish", ingredient(Items.PUFFERFISH), 80);
        barrelCompost(writer, "egg", ingredient(Items.EGG), 100);
        // foods
        barrelCompost(writer, "melon_slice", ingredient(Items.MELON_SLICE), 40);
        barrelCompost(writer, "silk_worms", ingredient(EItems.SILK_WORM.get(), EItems.COOKED_SILK_WORM.get()), 40);
        barrelCompost(writer, "apple", ingredient(Items.APPLE), 100);
        barrelCompost(writer, "cookie", ingredient(Items.COOKIE), 100);
        barrelCompost(writer, "pumpkin_pie", ingredient(Items.PUMPKIN_PIE), 150);
        barrelCompost(writer, "bread", ingredient(Items.BREAD), 125);
        barrelCompost(writer, "mushroom_stew", ingredient(Items.MUSHROOM_STEW), 200);
        barrelCompost(writer, "suspicious_stew", ingredient(Items.SUSPICIOUS_STEW), 200);
        barrelCompost(writer, "beetroot_soup", ingredient(Items.BEETROOT_SOUP), 150);
        barrelCompost(writer, "rabbit_stew", ingredient(Items.RABBIT_STEW), 200);

        // lol
        barrelCompost(writer, "golden_apples", ingredient(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE), 1000);
    }

    private static void barrelCompost(Consumer<FinishedRecipe> writer, String id, Ingredient ingredient, int volume) {
        writer.accept(new FinishedBarrelCompostRecipe(new ResourceLocation(ExDeorum.ID, "barrel_compost/" + id), ingredient, volume));
    }

    private static void barrelMixingRecipes(Consumer<FinishedRecipe> writer) {
        // water
        barrelMixing(writer, ingredient(Items.MILK_BUCKET), Fluids.WATER, Items.SLIME_BLOCK);
        barrelMixing(writer, "_from_porcelain_bucket", ingredient(EItems.PORCELAIN_MILK_BUCKET.get()), Fluids.WATER, Items.SLIME_BLOCK);
        barrelMixing(writer, ingredient(Items.SNOWBALL), Fluids.WATER, Items.ICE);
        barrelMixing(writer, ingredient(Items.LAVA_BUCKET), Fluids.WATER, Items.STONE);
        barrelMixing(writer, "_from_porcelain_bucket", ingredient(EItems.PORCELAIN_LAVA_BUCKET), Fluids.WATER, Items.STONE);
        // lava
        barrelMixing(writer, ingredient(EItems.WITCH_WATER_BUCKET), Fluids.LAVA, Items.NETHERRACK);
        barrelMixing(writer, "_from_porcelain_bucket", ingredient(EItems.PORCELAIN_WITCH_WATER_BUCKET), Fluids.LAVA, Items.NETHERRACK);
        barrelMixing(writer, ingredient(Items.GLOWSTONE_DUST), Fluids.LAVA, Items.END_STONE);
        barrelMixing(writer, ingredient(Items.WATER_BUCKET), Fluids.LAVA, Items.OBSIDIAN);
        barrelMixing(writer, "_from_porcelain_bucket", ingredient(EItems.PORCELAIN_WATER_BUCKET), Fluids.LAVA, Items.OBSIDIAN);
        barrelMixing(writer, ingredient(Items.SLIME_BALL), Fluids.LAVA, Items.MAGMA_CREAM);
        barrelMixing(writer, ingredient(Items.SOUL_SAND), Fluids.LAVA, Items.SOUL_SOIL);
        // witch water
        barrelMixing(writer, ingredient(Items.SAND), EFluids.WITCH_WATER.get(), Items.SOUL_SAND);
    }

    private static void barrelMixing(Consumer<FinishedRecipe> writer, Ingredient ingredient, Fluid fluidType, Item result) {
        writer.accept(new FinishedBarrelMixingRecipe(new ResourceLocation(ExDeorum.ID, "barrel_mixing/" + path(result)), ingredient, fluidType, 1000, result));
    }

    // todo replace with fluid mixing recipe type
    private static void barrelMixing(Consumer<FinishedRecipe> writer, String suffix, Ingredient ingredient, Fluid fluidType, Item result) {
        writer.accept(new FinishedBarrelMixingRecipe(new ResourceLocation(ExDeorum.ID, "barrel_mixing/" + path(result) + suffix), ingredient, fluidType, 1000, result));
    }
}
