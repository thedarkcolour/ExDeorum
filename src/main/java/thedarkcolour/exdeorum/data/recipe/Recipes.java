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

package thedarkcolour.exdeorum.data.recipe;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.MutableObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.data.ModCompatData;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.TagResultRecipe;
import thedarkcolour.exdeorum.recipe.WeightedList;
import thedarkcolour.exdeorum.recipe.barrel.*;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.crook.FinishedCrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.FinishedCrucibleHeatRecipe;
import thedarkcolour.exdeorum.recipe.crucible.FinishedCrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.FinishedHammerRecipe;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.tag.EItemTags;
import thedarkcolour.modkit.data.MKRecipeProvider;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator.binomial;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;
import static thedarkcolour.modkit.data.MKRecipeProvider.ingredient;
import static thedarkcolour.modkit.data.MKRecipeProvider.path;

public class Recipes {
    private static final Ingredient SPORES_AND_SEEDS = ingredient(EItems.GRASS_SEEDS, EItems.MYCELIUM_SPORES, EItems.WARPED_NYLIUM_SPORES, EItems.CRIMSON_NYLIUM_SPORES);

    public static void addRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
        craftingRecipes(writer, recipes);
        smeltingRecipes(recipes);
        SieveRecipes.sieveRecipes(writer);
        crucibleRecipes(writer);
        hammerRecipes(writer);
        crookRecipes(writer);
        crucibleHeatSources(writer);
        barrelCompostRecipes(writer);
        barrelMixingRecipes(writer);
        fluidTransformationRecipes(writer);
    }

    private static void craftingRecipes(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes) {
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
        uShaped(recipes, DefaultMaterials.OAK_CRUCIBLE.getItem(), ingredient(Items.OAK_LOG), ingredient(Items.OAK_SLAB));
        uShaped(recipes, DefaultMaterials.SPRUCE_CRUCIBLE.getItem(), ingredient(Items.SPRUCE_LOG), ingredient(Items.SPRUCE_SLAB));
        uShaped(recipes, DefaultMaterials.BIRCH_CRUCIBLE.getItem(), ingredient(Items.BIRCH_LOG), ingredient(Items.BIRCH_SLAB));
        uShaped(recipes, DefaultMaterials.JUNGLE_CRUCIBLE.getItem(), ingredient(Items.JUNGLE_LOG), ingredient(Items.JUNGLE_SLAB));
        uShaped(recipes, DefaultMaterials.ACACIA_CRUCIBLE.getItem(), ingredient(Items.ACACIA_LOG), ingredient(Items.ACACIA_SLAB));
        uShaped(recipes, DefaultMaterials.DARK_OAK_CRUCIBLE.getItem(), ingredient(Items.DARK_OAK_LOG), ingredient(Items.DARK_OAK_SLAB));
        uShaped(recipes, DefaultMaterials.MANGROVE_CRUCIBLE.getItem(), ingredient(Items.MANGROVE_LOG), ingredient(Items.MANGROVE_SLAB));
        uShaped(recipes, DefaultMaterials.CHERRY_CRUCIBLE.getItem(), ingredient(Items.CHERRY_LOG), ingredient(Items.CHERRY_SLAB));
        uShaped(recipes, DefaultMaterials.BAMBOO_CRUCIBLE.getItem(), ingredient(Items.BAMBOO_BLOCK), ingredient(Items.BAMBOO_SLAB));
        uShaped(recipes, DefaultMaterials.CRIMSON_CRUCIBLE.getItem(), ingredient(Items.CRIMSON_STEM), ingredient(Items.CRIMSON_SLAB));
        uShaped(recipes, DefaultMaterials.WARPED_CRUCIBLE.getItem(), ingredient(Items.WARPED_STEM), ingredient(Items.WARPED_SLAB));
        uShaped(recipes, EItems.UNFIRED_PORCELAIN_CRUCIBLE.get(), ingredient(EItems.PORCELAIN_CLAY_BALL.get()), ingredient(EItems.PORCELAIN_CLAY_BALL.get()));
        // BOP crucibles
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.FIR_LOG_ITEM, ModCompatData.FIR_SLAB, DefaultMaterials.FIR_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.REDWOOD_LOG_ITEM, ModCompatData.REDWOOD_SLAB, DefaultMaterials.REDWOOD_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAHOGANY_LOG_ITEM, ModCompatData.MAHOGANY_SLAB, DefaultMaterials.MAHOGANY_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.JACARANDA_LOG_ITEM, ModCompatData.JACARANDA_SLAB, DefaultMaterials.JACARANDA_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.PALM_LOG_ITEM, ModCompatData.PALM_SLAB, DefaultMaterials.PALM_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.WILLOW_LOG_ITEM, ModCompatData.WILLOW_SLAB, DefaultMaterials.WILLOW_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.DEAD_LOG_ITEM, ModCompatData.DEAD_SLAB, DefaultMaterials.DEAD_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAGIC_LOG_ITEM, ModCompatData.MAGIC_SLAB, DefaultMaterials.MAGIC_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.UMBRAN_LOG_ITEM, ModCompatData.UMBRAN_SLAB, DefaultMaterials.UMBRAN_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.HELLBARK_LOG_ITEM, ModCompatData.HELLBARK_SLAB, DefaultMaterials.HELLBARK_CRUCIBLE.getItem());
        // Ars crucibles
        modUShaped(recipes, ModIds.ARS_NOUVEAU, ModCompatData.CASCADING_ARCHWOOD_LOG_ITEM, ModCompatData.ARCHWOOD_SLAB, DefaultMaterials.CASCADING_ARCHWOOD_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.ARS_NOUVEAU, ModCompatData.BLAZING_ARCHWOOD_LOG_ITEM, ModCompatData.ARCHWOOD_SLAB, DefaultMaterials.BLAZING_ARCHWOOD_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.ARS_NOUVEAU, ModCompatData.VEXING_ARCHWOOD_LOG_ITEM, ModCompatData.ARCHWOOD_SLAB, DefaultMaterials.VEXING_ARCHWOOD_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.ARS_NOUVEAU, ModCompatData.FLOURISHING_ARCHWOOD_LOG_ITEM, ModCompatData.ARCHWOOD_SLAB, DefaultMaterials.FLOURISHING_ARCHWOOD_CRUCIBLE.getItem());
        // Aether crucibles
        modUShaped(recipes, ModIds.AETHER, ModCompatData.GOLDEN_OAK_LOG_ITEM, ModCompatData.SKYROOT_SLAB, DefaultMaterials.GOLDEN_OAK_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.AETHER, ModCompatData.SKYROOT_LOG_ITEM, ModCompatData.SKYROOT_SLAB, DefaultMaterials.SKYROOT_CRUCIBLE.getItem());
        // Blue Skies crucibles
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.BLUEBRIGHT_LOG_ITEM, ModCompatData.BLUEBRIGHT_SLAB, DefaultMaterials.BLUEBRIGHT_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.STARLIT_LOG_ITEM, ModCompatData.STARLIT_SLAB, DefaultMaterials.STARLIT_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.FROSTBRIGHT_LOG_ITEM, ModCompatData.FROSTBRIGHT_SLAB, DefaultMaterials.FROSTBRIGHT_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.COMET_LOG_ITEM, ModCompatData.COMET_SLAB, DefaultMaterials.COMET_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.LUNAR_LOG_ITEM, ModCompatData.LUNAR_SLAB, DefaultMaterials.LUNAR_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.DUSK_LOG_ITEM, ModCompatData.DUSK_SLAB, DefaultMaterials.DUSK_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.MAPLE_LOG_ITEM, ModCompatData.MAPLE_SLAB, DefaultMaterials.MAPLE_CRUCIBLE.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.CRYSTALLIZED_LOG_ITEM, ModCompatData.CRYSTALLIZED_SLAB, DefaultMaterials.CRYSTALLIZED_CRUCIBLE.getItem());

        // Barrels
        uShaped(recipes, DefaultMaterials.OAK_BARREL.getItem(), ingredient(Items.OAK_PLANKS), ingredient(Items.OAK_SLAB));
        uShaped(recipes, DefaultMaterials.SPRUCE_BARREL.getItem(), ingredient(Items.SPRUCE_PLANKS), ingredient(Items.SPRUCE_SLAB));
        uShaped(recipes, DefaultMaterials.BIRCH_BARREL.getItem(), ingredient(Items.BIRCH_PLANKS), ingredient(Items.BIRCH_SLAB));
        uShaped(recipes, DefaultMaterials.JUNGLE_BARREL.getItem(), ingredient(Items.JUNGLE_PLANKS), ingredient(Items.JUNGLE_SLAB));
        uShaped(recipes, DefaultMaterials.ACACIA_BARREL.getItem(), ingredient(Items.ACACIA_PLANKS), ingredient(Items.ACACIA_SLAB));
        uShaped(recipes, DefaultMaterials.DARK_OAK_BARREL.getItem(), ingredient(Items.DARK_OAK_PLANKS), ingredient(Items.DARK_OAK_SLAB));
        uShaped(recipes, DefaultMaterials.MANGROVE_BARREL.getItem(), ingredient(Items.MANGROVE_PLANKS), ingredient(Items.MANGROVE_SLAB));
        uShaped(recipes, DefaultMaterials.CHERRY_BARREL.getItem(), ingredient(Items.CHERRY_PLANKS), ingredient(Items.CHERRY_SLAB));
        uShaped(recipes, DefaultMaterials.BAMBOO_BARREL.getItem(), ingredient(Items.BAMBOO_PLANKS), ingredient(Items.BAMBOO_SLAB));
        uShaped(recipes, DefaultMaterials.CRIMSON_BARREL.getItem(), ingredient(Items.CRIMSON_PLANKS), ingredient(Items.CRIMSON_SLAB));
        uShaped(recipes, DefaultMaterials.WARPED_BARREL.getItem(), ingredient(Items.WARPED_PLANKS), ingredient(Items.WARPED_SLAB));
        uShaped(recipes, DefaultMaterials.STONE_BARREL.getItem(), ingredient(Items.STONE), ingredient(Items.STONE_SLAB));
        // Modded barrels
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.FIR_PLANKS_ITEM, ModCompatData.FIR_SLAB, DefaultMaterials.FIR_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.REDWOOD_PLANKS_ITEM, ModCompatData.REDWOOD_SLAB, DefaultMaterials.REDWOOD_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAHOGANY_PLANKS_ITEM, ModCompatData.MAHOGANY_SLAB, DefaultMaterials.MAHOGANY_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.JACARANDA_PLANKS_ITEM, ModCompatData.JACARANDA_SLAB, DefaultMaterials.JACARANDA_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.PALM_PLANKS_ITEM, ModCompatData.PALM_SLAB, DefaultMaterials.PALM_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.WILLOW_PLANKS_ITEM, ModCompatData.WILLOW_SLAB, DefaultMaterials.WILLOW_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.DEAD_PLANKS_ITEM, ModCompatData.DEAD_SLAB, DefaultMaterials.DEAD_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAGIC_PLANKS_ITEM, ModCompatData.MAGIC_SLAB, DefaultMaterials.MAGIC_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.UMBRAN_PLANKS_ITEM, ModCompatData.UMBRAN_SLAB, DefaultMaterials.UMBRAN_BARREL.getItem());
        modUShaped(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.HELLBARK_PLANKS_ITEM, ModCompatData.HELLBARK_SLAB, DefaultMaterials.HELLBARK_BARREL.getItem());
        modUShaped(recipes, ModIds.ARS_NOUVEAU, ModCompatData.ARCHWOOD_PLANKS_ITEM, ModCompatData.ARCHWOOD_SLAB, DefaultMaterials.ARCHWOOD_BARREL.getItem());
        modUShaped(recipes, ModIds.AETHER, ModCompatData.SKYROOT_PLANKS_ITEM, ModCompatData.SKYROOT_SLAB, DefaultMaterials.SKYROOT_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.BLUEBRIGHT_PLANKS_ITEM, ModCompatData.BLUEBRIGHT_SLAB, DefaultMaterials.BLUEBRIGHT_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.STARLIT_PLANKS_ITEM, ModCompatData.STARLIT_SLAB, DefaultMaterials.STARLIT_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.FROSTBRIGHT_PLANKS_ITEM, ModCompatData.FROSTBRIGHT_SLAB, DefaultMaterials.FROSTBRIGHT_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.COMET_PLANKS_ITEM, ModCompatData.COMET_SLAB, DefaultMaterials.COMET_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.LUNAR_PLANKS_ITEM, ModCompatData.LUNAR_SLAB, DefaultMaterials.LUNAR_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.DUSK_PLANKS_ITEM, ModCompatData.DUSK_SLAB, DefaultMaterials.DUSK_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.MAPLE_PLANKS_ITEM, ModCompatData.MAPLE_SLAB, DefaultMaterials.MAPLE_BARREL.getItem());
        modUShaped(recipes, ModIds.BLUE_SKIES, ModCompatData.CRYSTALLIZED_PLANKS_ITEM, ModCompatData.CRYSTALLIZED_SLAB, DefaultMaterials.CRYSTALLIZED_BARREL.getItem());

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

        // Compressed blocks
        recipes.storage3x3(EBlocks.COMPRESSED_DIRT.get(), Items.DIRT);
        recipes.storage3x3(EBlocks.COMPRESSED_GRAVEL.get(), Items.GRAVEL);
        recipes.storage3x3(EBlocks.COMPRESSED_SAND.get(), Items.SAND);
        recipes.storage3x3(EBlocks.COMPRESSED_DUST.get(), EItems.DUST.get());
        recipes.storage3x3(EBlocks.COMPRESSED_RED_SAND.get(), Items.RED_SAND);
        recipes.storage3x3(EBlocks.COMPRESSED_CRUSHED_DEEPSLATE.get(), EItems.CRUSHED_DEEPSLATE.get());
        recipes.storage3x3(EBlocks.COMPRESSED_CRUSHED_BLACKSTONE.get(), EItems.CRUSHED_BLACKSTONE.get());
        recipes.storage3x3(EBlocks.COMPRESSED_CRUSHED_NETHERRACK.get(), EItems.CRUSHED_NETHERRACK.get());
        recipes.storage3x3(EBlocks.COMPRESSED_SOUL_SAND.get(), Items.SOUL_SAND);
        recipes.storage3x3(EBlocks.COMPRESSED_CRUSHED_END_STONE.get(), EItems.CRUSHED_END_STONE.get());
        recipes.storage3x3(EBlocks.COMPRESSED_MOSS_BLOCK.get(), Items.MOSS_BLOCK);

        // Compressed sieves
        compressedSieve(recipes, DefaultMaterials.OAK_COMPRESSED_SIEVE, ingredient(Items.OAK_LOG));
        compressedSieve(recipes, DefaultMaterials.SPRUCE_COMPRESSED_SIEVE, ingredient(Items.SPRUCE_LOG));
        compressedSieve(recipes, DefaultMaterials.BIRCH_COMPRESSED_SIEVE, ingredient(Items.BIRCH_LOG));
        compressedSieve(recipes, DefaultMaterials.JUNGLE_COMPRESSED_SIEVE, ingredient(Items.JUNGLE_LOG));
        compressedSieve(recipes, DefaultMaterials.ACACIA_COMPRESSED_SIEVE, ingredient(Items.ACACIA_LOG));
        compressedSieve(recipes, DefaultMaterials.DARK_OAK_COMPRESSED_SIEVE, ingredient(Items.DARK_OAK_LOG));
        compressedSieve(recipes, DefaultMaterials.MANGROVE_COMPRESSED_SIEVE, ingredient(Items.MANGROVE_LOG));
        compressedSieve(recipes, DefaultMaterials.CHERRY_COMPRESSED_SIEVE, ingredient(Items.CHERRY_LOG));
        compressedSieve(recipes, DefaultMaterials.BAMBOO_COMPRESSED_SIEVE, ingredient(Items.BAMBOO_BLOCK));
        compressedSieve(recipes, DefaultMaterials.WARPED_COMPRESSED_SIEVE, ingredient(Items.WARPED_STEM));
        compressedSieve(recipes, DefaultMaterials.CRIMSON_COMPRESSED_SIEVE, ingredient(Items.CRIMSON_STEM));

        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.FIR_COMPRESSED_SIEVE, ingredient(ModCompatData.FIR_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.REDWOOD_COMPRESSED_SIEVE, ingredient(ModCompatData.REDWOOD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.MAHOGANY_COMPRESSED_SIEVE, ingredient(ModCompatData.MAHOGANY_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.JACARANDA_COMPRESSED_SIEVE, ingredient(ModCompatData.JACARANDA_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.PALM_COMPRESSED_SIEVE, ingredient(ModCompatData.PALM_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.WILLOW_COMPRESSED_SIEVE, ingredient(ModCompatData.WILLOW_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.DEAD_COMPRESSED_SIEVE, ingredient(ModCompatData.DEAD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.MAGIC_COMPRESSED_SIEVE, ingredient(ModCompatData.MAGIC_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.UMBRAN_COMPRESSED_SIEVE, ingredient(ModCompatData.UMBRAN_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BIOMES_O_PLENTY, DefaultMaterials.HELLBARK_COMPRESSED_SIEVE, ingredient(ModCompatData.HELLBARK_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.ARS_NOUVEAU, DefaultMaterials.CASCADING_ARCHWOOD_COMPRESSED_SIEVE, ingredient(ModCompatData.CASCADING_ARCHWOOD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.ARS_NOUVEAU, DefaultMaterials.BLAZING_ARCHWOOD_COMPRESSED_SIEVE, ingredient(ModCompatData.BLAZING_ARCHWOOD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.ARS_NOUVEAU, DefaultMaterials.VEXING_ARCHWOOD_COMPRESSED_SIEVE, ingredient(ModCompatData.VEXING_ARCHWOOD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.ARS_NOUVEAU, DefaultMaterials.FLOURISHING_ARCHWOOD_COMPRESSED_SIEVE, ingredient(ModCompatData.FLOURISHING_ARCHWOOD_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.AETHER, DefaultMaterials.SKYROOT_COMPRESSED_SIEVE, ingredient(ModCompatData.SKYROOT_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.AETHER, DefaultMaterials.GOLDEN_OAK_COMPRESSED_SIEVE, ingredient(ModCompatData.GOLDEN_OAK_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.BLUEBRIGHT_COMPRESSED_SIEVE, ingredient(ModCompatData.BLUEBRIGHT_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.STARLIT_COMPRESSED_SIEVE, ingredient(ModCompatData.STARLIT_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.FROSTBRIGHT_COMPRESSED_SIEVE, ingredient(ModCompatData.FROSTBRIGHT_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.COMET_COMPRESSED_SIEVE, ingredient(ModCompatData.COMET_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.LUNAR_COMPRESSED_SIEVE, ingredient(ModCompatData.LUNAR_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.DUSK_COMPRESSED_SIEVE, ingredient(ModCompatData.DUSK_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.MAPLE_COMPRESSED_SIEVE, ingredient(ModCompatData.MAPLE_LOG_ITEM));
        modCompressedSieve(recipes, ModIds.BLUE_SKIES, DefaultMaterials.CRYSTALLIZED_COMPRESSED_SIEVE, ingredient(ModCompatData.CRYSTALLIZED_LOG_ITEM));

        // Modded ores
        grid2x2TagResult(writer, recipes, EItemTags.ORES_ALUMINUM, ingredient(EItems.ALUMINUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_COBALT, ingredient(EItems.COBALT_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_SILVER, ingredient(EItems.SILVER_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_LEAD, ingredient(EItems.LEAD_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_PLATINUM, ingredient(EItems.PLATINUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_NICKEL, ingredient(EItems.NICKEL_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_URANIUM, ingredient(EItems.URANIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_OSMIUM, ingredient(EItems.OSMIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_TIN, ingredient(EItems.TIN_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_ZINC, ingredient(EItems.ZINC_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_IRIDIUM, ingredient(EItems.IRIDIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_THORIUM, ingredient(EItems.THORIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_MAGNESIUM, ingredient(EItems.MAGNESIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_LITHIUM, ingredient(EItems.LITHIUM_ORE_CHUNK));
        grid2x2TagResult(writer, recipes, EItemTags.ORES_BORON, ingredient(EItems.BORON_ORE_CHUNK));

        // Sieves
        sieve(recipes, DefaultMaterials.OAK_SIEVE.getItem(), Items.OAK_PLANKS, Items.OAK_SLAB);
        sieve(recipes, DefaultMaterials.SPRUCE_SIEVE.getItem(), Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        sieve(recipes, DefaultMaterials.BIRCH_SIEVE.getItem(), Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        sieve(recipes, DefaultMaterials.JUNGLE_SIEVE.getItem(), Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        sieve(recipes, DefaultMaterials.ACACIA_SIEVE.getItem(), Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        sieve(recipes, DefaultMaterials.DARK_OAK_SIEVE.getItem(), Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        sieve(recipes, DefaultMaterials.MANGROVE_SIEVE.getItem(), Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        sieve(recipes, DefaultMaterials.CHERRY_SIEVE.getItem(), Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        sieve(recipes, DefaultMaterials.BAMBOO_SIEVE.getItem(), Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        sieve(recipes, DefaultMaterials.CRIMSON_SIEVE.getItem(), Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        sieve(recipes, DefaultMaterials.WARPED_SIEVE.getItem(), Items.WARPED_PLANKS, Items.WARPED_SLAB);
        // Modded sieves
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.FIR_PLANKS_ITEM, ModCompatData.FIR_SLAB, DefaultMaterials.FIR_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.REDWOOD_PLANKS_ITEM, ModCompatData.REDWOOD_SLAB, DefaultMaterials.REDWOOD_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAHOGANY_PLANKS_ITEM, ModCompatData.MAHOGANY_SLAB, DefaultMaterials.MAHOGANY_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.JACARANDA_PLANKS_ITEM, ModCompatData.JACARANDA_SLAB, DefaultMaterials.JACARANDA_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.PALM_PLANKS_ITEM, ModCompatData.PALM_SLAB, DefaultMaterials.PALM_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.WILLOW_PLANKS_ITEM, ModCompatData.WILLOW_SLAB, DefaultMaterials.WILLOW_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.DEAD_PLANKS_ITEM, ModCompatData.DEAD_SLAB, DefaultMaterials.DEAD_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.MAGIC_PLANKS_ITEM, ModCompatData.MAGIC_SLAB, DefaultMaterials.MAGIC_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.UMBRAN_PLANKS_ITEM, ModCompatData.UMBRAN_SLAB, DefaultMaterials.UMBRAN_SIEVE.getItem());
        modSieve(recipes, ModIds.BIOMES_O_PLENTY, ModCompatData.HELLBARK_PLANKS_ITEM, ModCompatData.HELLBARK_SLAB, DefaultMaterials.HELLBARK_SIEVE.getItem());
        modSieve(recipes, ModIds.ARS_NOUVEAU, ModCompatData.ARCHWOOD_PLANKS_ITEM, ModCompatData.ARCHWOOD_SLAB,  DefaultMaterials.ARCHWOOD_SIEVE.getItem());
        modSieve(recipes, ModIds.AETHER, ModCompatData.SKYROOT_PLANKS_ITEM, ModCompatData.SKYROOT_SLAB, DefaultMaterials.SKYROOT_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.BLUEBRIGHT_PLANKS_ITEM, ModCompatData.BLUEBRIGHT_SLAB, DefaultMaterials.BLUEBRIGHT_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.STARLIT_PLANKS_ITEM, ModCompatData.STARLIT_SLAB, DefaultMaterials.STARLIT_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.FROSTBRIGHT_PLANKS_ITEM, ModCompatData.FROSTBRIGHT_SLAB, DefaultMaterials.FROSTBRIGHT_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.COMET_PLANKS_ITEM, ModCompatData.COMET_SLAB, DefaultMaterials.COMET_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.LUNAR_PLANKS_ITEM, ModCompatData.LUNAR_SLAB, DefaultMaterials.LUNAR_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.DUSK_PLANKS_ITEM, ModCompatData.DUSK_SLAB, DefaultMaterials.DUSK_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.MAPLE_PLANKS_ITEM, ModCompatData.MAPLE_SLAB, DefaultMaterials.MAPLE_SIEVE.getItem());
        modSieve(recipes, ModIds.BLUE_SKIES, ModCompatData.CRYSTALLIZED_PLANKS_ITEM, ModCompatData.CRYSTALLIZED_SLAB, DefaultMaterials.CRYSTALLIZED_SIEVE.getItem());

        // Meshes
        recipes.grid3x3(EItems.STRING_MESH.get(), ingredient(Tags.Items.STRING));
        mesh(recipes, EItems.FLINT_MESH, ingredient(Items.FLINT));
        mesh(recipes, EItems.IRON_MESH, ingredient(Tags.Items.INGOTS_IRON));
        mesh(recipes, EItems.GOLDEN_MESH, ingredient(Tags.Items.INGOTS_GOLD));
        mesh(recipes, EItems.DIAMOND_MESH, ingredient(Tags.Items.GEMS_DIAMOND));
        meshUpgrade(recipes, EItems.FLINT_MESH, EItems.STRING_MESH, ingredient(Items.FLINT));
        meshUpgrade(recipes, EItems.IRON_MESH, EItems.FLINT_MESH, ingredient(Tags.Items.INGOTS_IRON));
        meshUpgrade(recipes, EItems.GOLDEN_MESH, EItems.IRON_MESH, ingredient(Tags.Items.INGOTS_GOLD));
        meshUpgrade(recipes, EItems.DIAMOND_MESH, EItems.GOLDEN_MESH, ingredient(Tags.Items.GEMS_DIAMOND));
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
        recipes.shapedCrafting(RecipeCategory.BUILDING_BLOCKS, Items.SPONGE, recipe -> {
            recipe.define('S', Blocks.SLIME_BLOCK);
            recipe.define('W', ItemTags.WOOL);
            recipe.define('C', EItems.WOOD_CHIPPINGS);
            recipe.pattern("WCW");
            recipe.pattern("CSC");
            recipe.pattern("WCW");
            MKRecipeProvider.unlockedByHaving(recipe, EItems.WOOD_CHIPPINGS.get());
        });
        recipes.shapedCrafting(RecipeCategory.MISC, EItems.MECHANICAL_SIEVE.get(), recipe -> {
            recipe.define('#', Items.IRON_BLOCK);
            recipe.define('G', Items.GLASS);
            recipe.define('H', Items.HOPPER);
            recipe.define('I', Items.IRON_BARS);
            recipe.pattern("#G#");
            recipe.pattern("IHI");
            recipe.pattern("I I");
            MKRecipeProvider.unlockedByHaving(recipe, Items.HOPPER);
        });
        recipes.shapedCrafting(RecipeCategory.MISC, EItems.MECHANICAL_HAMMER.get(), recipe -> {
            recipe.define('#', Items.IRON_BLOCK);
            recipe.define('H', Items.HOPPER);
            recipe.define('T', EItemTags.HAMMERS);
            recipe.define('I', Items.IRON_INGOT);
            recipe.pattern("III");
            recipe.pattern("ITI");
            recipe.pattern("#H#");
            MKRecipeProvider.unlockedByHaving(recipe, Items.HOPPER);
        });
    }

    private static void modUShaped(MKRecipeProvider recipes, String modid, RegistryObject<? extends Item> sides, RegistryObject<? extends Item> middle, Item result) {
        recipes.conditional(path(result), List.of(modInstalled(modid)), writer1 -> {
            uShaped(recipes, result, ingredient(sides), ingredient(middle));
        });
    }

    private static void modSieve(MKRecipeProvider recipes, String modid, RegistryObject<? extends Item> planks, RegistryObject<? extends Item> slab, Item result) {
        recipes.conditional(path(result), List.of(modInstalled(modid)), writer1 -> {
            sieve(recipes, result, planks.get(), slab.get());
        });
    }

    private static void grid2x2TagResult(Consumer<FinishedRecipe> writer, MKRecipeProvider recipes, TagKey<Item> resultTag, Ingredient ingredient) {
        // capture the generated recipe and wrap it in a TagResultRecipe
        var wrappedRecipe = new MutableObject<FinishedRecipe>();
        recipes.pushWriter(wrappedRecipe::setValue, newWriter -> {
            recipes.shapedCrafting(resultTag.location().getPath() + "_tag", RecipeCategory.MISC, Items.AIR, recipe -> {
                recipe.define('#', ingredient);
                recipe.pattern("##");
                recipe.pattern("##");
            });
        });
        writer.accept(new TagResultRecipe.Finished(resultTag, wrappedRecipe.getValue()));
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

    private static void uShaped(MKRecipeProvider recipes, Item result, Ingredient sides, Ingredient middle) {
        recipes.shapedCrafting(RecipeCategory.MISC, result, recipe -> {
            recipe.define('s', sides);
            recipe.define('m', middle);
            recipe.pattern("s s");
            recipe.pattern("s s");
            recipe.pattern("sms");
        });
    }

    private static void sieve(MKRecipeProvider recipes, Item result, Item planks, Item slab) {
        recipes.shapedCrafting(RecipeCategory.MISC, result, recipe -> {
            recipe.define('O', planks);
            recipe.define('_', slab);
            recipe.define('I', Tags.Items.RODS_WOODEN);
            recipe.pattern("O O");
            recipe.pattern("O_O");
            recipe.pattern("I I");
        });
    }

    private static void compressedSieve(MKRecipeProvider recipes, ItemLike result, Ingredient log) {
        recipes.shapedCrafting(RecipeCategory.MISC, result.asItem(), recipe -> {
            recipe.define('O', log);
            recipe.define('I', Tags.Items.RODS_WOODEN);
            recipe.define('_', Tags.Items.INGOTS_IRON);
            recipe.pattern("O O");
            recipe.pattern("O_O");
            recipe.pattern("I I");
        });
    }

    private static void modCompressedSieve(MKRecipeProvider recipes, String modid, ItemLike result, Ingredient log) {
        recipes.conditional(path(result), List.of(modInstalled(modid)), writer1 -> {
            compressedSieve(recipes, result, log);
        });
    }

    private static void mesh(MKRecipeProvider recipes, Supplier<? extends Item> result, Ingredient ingredient) {
        recipes.shapedCrafting(RecipeCategory.MISC, result.get(), recipe -> {
            recipe.define('#', ingredient);
            recipe.define('S', ingredient(Tags.Items.STRING));
            recipe.pattern("S#S");
            recipe.pattern("#S#");
            recipe.pattern("S#S");
        });
    }

    private static void meshUpgrade(MKRecipeProvider recipes, RegistryObject<? extends Item> newMesh, RegistryObject<? extends Item> previousMesh, Ingredient ingredient) {
        recipes.shapedCrafting(newMesh.getId().getPath() + "_from_" + previousMesh.getId().getPath(), RecipeCategory.MISC, newMesh.get(), recipe -> {
            recipe.define('#', ingredient);
            recipe.define('M', previousMesh.get());
            recipe.pattern(" # ");
            recipe.pattern("#M#");
            recipe.pattern(" # ");
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
        recipes.smelting(ingredient(EItems.UNFIRED_PORCELAIN_CRUCIBLE), DefaultMaterials.PORCELAIN_CRUCIBLE.getItem(), 0.1f);
        recipes.smelting(ingredient(EItems.UNFIRED_PORCELAIN_BUCKET), EItems.PORCELAIN_BUCKET.get(), 0.1f);

        recipes.foodCooking(EItems.SILK_WORM.get(), EItems.COOKED_SILK_WORM.get(), 0.1f);
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
        writer.accept(new FinishedCrucibleRecipe(modLoc("lava_crucible/" + id), ERecipeSerializers.LAVA_CRUCIBLE.get(), ingredient, Fluids.LAVA, volume));
    }

    private static void waterCrucible(Consumer<FinishedRecipe> writer, String id, Ingredient ingredient, int volume) {
        writer.accept(new FinishedCrucibleRecipe(modLoc("water_crucible/" + id), ERecipeSerializers.WATER_CRUCIBLE.get(), ingredient, Fluids.WATER, volume));
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
        hammerRecipe(writer, "red_sand", ingredient(EBlocks.CRUSHED_NETHERRACK), Blocks.RED_SAND);

        hammerRecipe(writer, "crushing_sandstone", ingredient(Items.SANDSTONE, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, Items.SMOOTH_SANDSTONE), Items.SAND);
        hammerRecipe(writer, "crushing_red_sandstone", ingredient(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE), Items.RED_SAND);
        hammerRecipe(writer, "crushing_stone_bricks", ingredient(Items.STONE_BRICKS), Items.CRACKED_STONE_BRICKS);

        hammerRecipe(writer, "stone_pebbles", ingredient(Items.STONE, Items.STONE_BRICKS, Items.CHISELED_STONE_BRICKS, Items.CRACKED_STONE_BRICKS), EItems.STONE_PEBBLE.get(), new UniformGenerator(ConstantValue.exactly(1), ConstantValue.exactly(6)));
        hammerRecipe(writer, "basalt", ingredient(Items.POLISHED_BASALT, Items.SMOOTH_BASALT), Items.BASALT);
        hammerRecipe(writer, "wood_chippings", ingredient(ItemTags.LOGS), EItems.WOOD_CHIPPINGS.get(), new UniformGenerator(ConstantValue.exactly(3), ConstantValue.exactly(8)));

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
        hammerRecipe(writer, "pointed_dripstone", ingredient(Items.DRIPSTONE_BLOCK), Items.POINTED_DRIPSTONE, between(2, 4));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, ItemLike result) {
        hammerRecipe(writer, name, block, result, ConstantValue.exactly(1f));
    }

    private static void hammerRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, ItemLike result, NumberProvider resultAmount) {
        writer.accept(new FinishedHammerRecipe(modLoc("hammer/" + name), block, result.asItem(), resultAmount));
    }

    private static void crookRecipes(Consumer<FinishedRecipe> writer) {
        crookRecipe(writer, "silkworm", BlockPredicate.blockTag(BlockTags.LEAVES), EItems.SILK_WORM.get(), 0.01f);
        var fullyInfestedLeaves = BlockPredicate.blockState(EBlocks.INFESTED_LEAVES.get(), StatePropertiesPredicate.Builder.properties().hasProperty(InfestedLeavesBlock.FULLY_INFESTED, true).build());
        crookRecipe(writer, "silkworm_bonus", fullyInfestedLeaves, EItems.SILK_WORM.get(), 0.01f);
        crookRecipe(writer, "string_roll_1", fullyInfestedLeaves, Items.STRING, 0.4f);
        crookRecipe(writer, "string_roll_2", fullyInfestedLeaves, Items.STRING, 0.1f);
    }

    private static void crookRecipe(Consumer<FinishedRecipe> writer, String name, BlockPredicate blockPredicate, ItemLike result, float chance) {
        writer.accept(new FinishedCrookRecipe(modLoc("crook/" + name), blockPredicate, result.asItem(), chance));
    }

    private static void crucibleHeatSources(Consumer<FinishedRecipe> writer) {
        crucibleHeatSource(writer, Blocks.TORCH, 1);
        crucibleHeatSource(writer, Blocks.WALL_TORCH, 1);
        crucibleHeatSource(writer, Blocks.LANTERN, 1);
        crucibleHeatSource(writer, Blocks.SOUL_TORCH, 2);
        crucibleHeatSource(writer, Blocks.SOUL_WALL_TORCH, 2);
        crucibleHeatSource(writer, Blocks.SOUL_LANTERN, 2);
        crucibleHeatSource(writer, Blocks.LAVA, 3);
        crucibleHeatSource(writer, Blocks.FIRE, 5);
        crucibleHeatSource(writer, Blocks.SOUL_FIRE, 5);

        crucibleHeatSource(writer, "lit_campfire", BlockPredicate.blockState(Blocks.CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true).build()), 2);
        crucibleHeatSource(writer, "lit_soul_campfire", BlockPredicate.blockState(Blocks.SOUL_CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true).build()), 2);
    }

    @SuppressWarnings("deprecation")
    private static void crucibleHeatSource(Consumer<FinishedRecipe> writer, Block block, int heatValue) {
        crucibleHeatSource(writer, BuiltInRegistries.BLOCK.getKey(block).getPath(), BlockPredicate.singleBlock(block), heatValue);
    }

    private static void crucibleHeatSource(Consumer<FinishedRecipe> writer, String name, BlockPredicate blockPredicate, int heatValue) {
        writer.accept(new FinishedCrucibleHeatRecipe(modLoc("crucible_heat_source/" + name), blockPredicate, heatValue));
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
        barrelCompost(writer, "wood_chippings", ingredient(EItems.WOOD_CHIPPINGS), 125);
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
        writer.accept(new FinishedBarrelCompostRecipe(modLoc("barrel_compost/" + id), ingredient, volume));
    }

    private static void barrelMixingRecipes(Consumer<FinishedRecipe> writer) {
        // water
        barrelMixing(writer, ingredient(EItems.DUST.get()), Fluids.WATER, Items.CLAY);
        barrelMixing(writer, ingredient(Items.MILK_BUCKET), Fluids.WATER, Items.SLIME_BLOCK);
        barrelMixing(writer, "_from_porcelain_bucket", ingredient(EItems.PORCELAIN_MILK_BUCKET.get()), Fluids.WATER, Items.SLIME_BLOCK);
        barrelFluidMixing(writer, Fluids.WATER, ForgeMod.MILK.get(), Items.SLIME_BLOCK, true);
        barrelMixing(writer, ingredient(Items.SNOWBALL), Fluids.WATER, Items.ICE);
        barrelFluidMixing(writer, Fluids.WATER, Fluids.LAVA, Items.STONE, false);
        // lava
        barrelFluidMixing(writer, EFluids.WITCH_WATER.get(), Fluids.LAVA, Items.NETHERRACK, true);
        barrelFluidMixing(writer, Fluids.LAVA, EFluids.WITCH_WATER.get(), Items.BLACKSTONE, true);
        barrelMixing(writer, ingredient(Items.GLOWSTONE_DUST), Fluids.LAVA, Items.END_STONE);
        barrelFluidMixing(writer, Fluids.LAVA, Fluids.WATER, Items.OBSIDIAN, false);
        barrelMixing(writer, ingredient(Items.SLIME_BALL), Fluids.LAVA, Items.MAGMA_CREAM);
        barrelMixing(writer, ingredient(Items.SOUL_SAND), Fluids.LAVA, Items.SOUL_SOIL);
        // witch water
        barrelMixing(writer, ingredient(Items.SAND), EFluids.WITCH_WATER.get(), Items.SOUL_SAND);
    }

    private static void barrelMixing(Consumer<FinishedRecipe> writer, Ingredient ingredient, Fluid fluidType, Item result) {
        barrelMixing(writer, "", ingredient, fluidType, result);
    }

    private static void barrelMixing(Consumer<FinishedRecipe> writer, String suffix, Ingredient ingredient, Fluid fluidType, Item result) {
        writer.accept(new FinishedBarrelMixingRecipe(modLoc("barrel_mixing/" + path(result) + suffix), ingredient, fluidType, 1000, result));
    }

    private static void barrelFluidMixing(Consumer<FinishedRecipe> writer, Fluid base, Fluid additive, Item result, boolean consumesAdditive) {
        writer.accept(new FinishedBarrelFluidMixingRecipe(modLoc("barrel_fluid_mixing/" + path(result)), base, 1000, additive, result, consumesAdditive));
    }

    private static void fluidTransformationRecipes(Consumer<FinishedRecipe> writer) {
        writer.accept(new FinishedFluidTransformationRecipe(modLoc("barrel_fluid_transformation/witch_water"), Fluids.WATER, EFluids.WITCH_WATER.get(), 0x2B1057, BlockPredicate.singleBlock(Blocks.MYCELIUM), WeightedList.<BlockState>builder().add(50, Blocks.RED_MUSHROOM.defaultBlockState()).add(50, Blocks.BROWN_MUSHROOM.defaultBlockState()).build(), 1700));
    }

    static ResourceLocation modLoc(String path) {
        return new ResourceLocation(ExDeorum.ID, path);
    }

    static ICondition tagNotEmpty(TagKey<Item> tag) {
        return new NotCondition(new TagEmptyCondition(tag.location()));
    }

    static ICondition modInstalled(String modid) {
        return new ModLoadedCondition(modid);
    }

    static final ICondition AE2 = modInstalled(ModIds.APPLIED_ENERGISTICS_2);
    static final ICondition ENDERIO = modInstalled(ModIds.ENDERIO);
    static final ICondition EXTREME_REACTORS = modInstalled(ModIds.EXTREME_REACTORS);

    static NumberProvider compressedMultiplier(NumberProvider resultAmount) {
        if (resultAmount instanceof BinomialDistributionGenerator binomial) {
            return binomial((int) ((ConstantValue) binomial.n).value * 7, ((ConstantValue) binomial.p).value);
        } else {
            throw new IllegalArgumentException("Unable to multiply type " + resultAmount.getClass());
        }
    }
}
