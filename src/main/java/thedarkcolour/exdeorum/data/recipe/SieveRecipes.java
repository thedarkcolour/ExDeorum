package thedarkcolour.exdeorum.data.recipe;

import com.mojang.datafixers.util.Either;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.data.ModCompatData;
import thedarkcolour.exdeorum.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EItemTags;
import thedarkcolour.modkit.data.MKRecipeProvider;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator.binomial;
import static thedarkcolour.modkit.data.MKRecipeProvider.ingredient;
import static thedarkcolour.modkit.data.MKRecipeProvider.path;

class SieveRecipes {
    static void sieveRecipes(Consumer<FinishedRecipe> writer) {
        var allMeshes = List.of(EItems.STRING_MESH, EItems.FLINT_MESH, EItems.IRON_MESH, EItems.GOLDEN_MESH, EItems.DIAMOND_MESH, EItems.NETHERITE_MESH);

        // Dirt -> String mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.DIRT), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.DIRT), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.65f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.45f));
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
        forMesh(writer, ingredient(Items.DIRT), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
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
        forMesh(writer, ingredient(Items.DIRT), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.60f));
            addDrop.accept(Items.FLINT, binomial(3, 0.3f));
            addDrop.accept(Items.POTATO, chance(0.25f));
            addDrop.accept(Items.CARROT, chance(0.25f));
            addDrop.accept(EItems.GRASS_SEEDS.get(), chance(0.15f));
            addDrop.accept(EItems.MYCELIUM_SPORES.get(), chance(0.1f));
            addDrop.accept(Items.BAMBOO, chance(0.06f));
        });
        // Netherite should be the best for all drops (except pebbles)
        // Dirt -> Netherite mesh
        forMesh(writer, ingredient(Items.DIRT), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(5, 0.4f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
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
        forMesh(writer, ingredient(Items.GRAVEL), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.03f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.035f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.03f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });
        // Gravel -> Flint mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.055f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.03f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });
        // Gravel -> Iron mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
            addDrop.accept(Items.FLINT, chance(0.15f));
            addDrop.accept(Items.COAL, chance(0.15f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.08f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.11f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.06f));
            addDrop.accept(Items.DIAMOND, chance(0.05f));
            addDrop.accept(Items.EMERALD, chance(0.04f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.04f));

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.055f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.045f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });
        // Golden mesh has much higher drops for gold and gems, but at the cost of much lower drops for metals
        // Gravel -> Golden mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
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

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });
        // Gravel -> Diamond mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(5, 0.6f));
            addDrop.accept(Items.FLINT, chance(0.05f));
            addDrop.accept(Items.COAL, chance(0.06f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.11f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.07f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(Items.DIAMOND, chance(0.08f));
            addDrop.accept(Items.EMERALD, chance(0.07f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.06f));

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });
        // Gravel -> Netherite mesh
        forMesh(writer, ingredient(Items.GRAVEL), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(6, 0.625f));
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

            addConditionalDrop.accept(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_ALUMINUM));
            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.10f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.ZINC_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_ZINC));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.055f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
        });

        // Sand -> String mesh
        forMesh(writer, ingredient(Items.SAND), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.13f));
            addDrop.accept(Items.FLINT, chance(0.2f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.13f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.13f));
            addDrop.accept(Items.KELP, chance(0.1f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.05f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.SAND), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.SAND), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.SAND), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.FLINT, chance(0.23f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.22f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.22f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.09f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.09f));
        });
        forMesh(writer, ingredient(Items.SAND), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.15f));
            addDrop.accept(Items.FLINT, binomial(2, 0.23f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.23f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.23f));
            addDrop.accept(Items.KELP, chance(0.1f));
            addDrop.accept(Items.SEA_PICKLE, chance(0.07f));
            addDrop.accept(Items.PRISMARINE_SHARD, chance(0.12f));
            addDrop.accept(Items.PRISMARINE_CRYSTALS, chance(0.12f));
        });

        // Red Sand -> String mesh
        forMesh(writer, ingredient(Items.RED_SAND), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.12f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.07f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.09f));
            addDrop.accept(Items.REDSTONE, chance(0.08f));
            addDrop.accept(Items.RAW_GOLD, chance(0.03f));
        });
        forMesh(writer, ingredient(Items.RED_SAND), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.12f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.07f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.12f));
            addDrop.accept(Items.REDSTONE, chance(0.09f));
            addDrop.accept(Items.RAW_GOLD, chance(0.04f));
        });
        forMesh(writer, ingredient(Items.RED_SAND), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.12f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.07f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.09f));
            addDrop.accept(Items.REDSTONE, chance(0.11f));
            addDrop.accept(Items.RAW_GOLD, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.RED_SAND), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.12f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.07f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.19f));
            addDrop.accept(Items.REDSTONE, chance(0.07f));
            addDrop.accept(Items.RAW_GOLD, chance(0.11f));
        });
        forMesh(writer, ingredient(Items.RED_SAND), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.10f));
            addDrop.accept(Items.DEAD_BUSH, chance(0.03f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.14f));
            addDrop.accept(Items.REDSTONE, chance(0.14f));
            addDrop.accept(Items.RAW_GOLD, chance(0.08f));
        });
        forMesh(writer, ingredient(Items.RED_SAND), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.CACTUS, chance(0.12f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.15f));
            addDrop.accept(Items.REDSTONE, chance(0.17f));
            addDrop.accept(Items.RAW_GOLD, chance(0.10f));
        });

        forMesh(writer, ingredient(EItems.DUST.get()), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(Items.BONE_MEAL, chance(0.1f));
            addDrop.accept(Items.REDSTONE, chance(0.06f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.04f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.03f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.06f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.05f), modInstalled(ModIds.EXTREME_REACTORS));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
            addDrop.accept(Items.BONE_MEAL, chance(0.11f));
            addDrop.accept(Items.REDSTONE, chance(0.09f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.07f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.04f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.07f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.055f), modInstalled(ModIds.EXTREME_REACTORS));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.BONE_MEAL, chance(0.12f));
            addDrop.accept(Items.REDSTONE, chance(0.1f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.09f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.05f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.06f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.09f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.08f), modInstalled(ModIds.EXTREME_REACTORS));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.BONE_MEAL, chance(0.11f));
            addDrop.accept(Items.REDSTONE, chance(0.12f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.11f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(2, 0.18f));
            addDrop.accept(Items.RAW_GOLD, chance(0.02f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.11f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.10f), modInstalled(ModIds.EXTREME_REACTORS));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.14f));
            addDrop.accept(Items.BONE_MEAL, chance(0.10f));
            addDrop.accept(Items.REDSTONE, chance(0.12f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.11f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.12f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.12f), modInstalled(ModIds.EXTREME_REACTORS));
        });
        forMesh(writer, ingredient(EItems.DUST.get()), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.GUNPOWDER, chance(0.14f));
            addDrop.accept(Items.BONE_MEAL, chance(0.13f));
            addDrop.accept(Items.REDSTONE, chance(0.14f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.15f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.1f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));
            addDrop.accept(Items.IRON_NUGGET, chance(0.08f));

            addConditionalDrop.accept(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.135f), modInstalled(ModIds.ENDERIO));
            addConditionalDrop.accept(ModCompatData.YELLORITE_DUST.get(), chance(0.14f), modInstalled(ModIds.EXTREME_REACTORS));
        });

        // Crushed Deepslate -> String mesh
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.05f));
            addDrop.accept(Items.DIAMOND, chance(0.04f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.04f));
            addDrop.accept(Items.EMERALD, chance(0.03f));

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.03f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.12f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.06f));
            addDrop.accept(Items.DIAMOND, chance(0.06f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.08f));
            addDrop.accept(Items.EMERALD, chance(0.05f));

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.1f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.1f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.10f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.06f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.065f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.08f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
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

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.15f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.09f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.16f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.13f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.07f));
            addDrop.accept(Items.DIAMOND, chance(0.08f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.12f));
            addDrop.accept(Items.EMERALD, chance(0.08f));

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.13f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.065f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.075f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.1f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_DEEPSLATE.get()), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.7f));
            addDrop.accept(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            addDrop.accept(EItems.IRON_ORE_CHUNK.get(), chance(0.17f));
            addDrop.accept(EItems.GOLD_ORE_CHUNK.get(), chance(0.15f));
            addDrop.accept(Items.AMETHYST_SHARD, chance(0.1f));
            addDrop.accept(Items.DIAMOND, chance(0.1f));
            addDrop.accept(Items.LAPIS_LAZULI, chance(0.14f));
            addDrop.accept(Items.EMERALD, chance(0.1f));

            addConditionalDrop.accept(EItems.SILVER_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_SILVER));
            addConditionalDrop.accept(EItems.LEAD_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_LEAD));
            addConditionalDrop.accept(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.14f), tagNotEmpty(EItemTags.ORES_OSMIUM));
            addConditionalDrop.accept(EItems.NICKEL_ORE_CHUNK.get(), chance(0.15f), tagNotEmpty(EItemTags.ORES_NICKEL));
            addConditionalDrop.accept(EItems.TIN_ORE_CHUNK.get(), chance(0.16f), tagNotEmpty(EItemTags.ORES_TIN));
            addConditionalDrop.accept(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.065f), tagNotEmpty(EItemTags.ORES_IRIDIUM));
            addConditionalDrop.accept(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_PLATINUM));
            addConditionalDrop.accept(EItems.URANIUM_ORE_CHUNK.get(), chance(0.12f), tagNotEmpty(EItemTags.ORES_URANIUM));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.5f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.02f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.2f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.08f));
            addDrop.accept(Items.GUNPOWDER, chance(0.07f));
            addDrop.accept(Items.BLACK_DYE, chance(0.07f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.65f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.55f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.03f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.225f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.09f));
            addDrop.accept(Items.GUNPOWDER, chance(0.09f));
            addDrop.accept(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.55f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.04f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.25f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.09f));
            addDrop.accept(Items.GUNPOWDER, chance(0.09f));
            addDrop.accept(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.05f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(8, 0.325f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(Items.BLACK_DYE, chance(0.06f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.06f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.275f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.11f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_BLACKSTONE.get()), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.75f));
            addDrop.accept(Items.ANCIENT_DEBRIS, chance(0.1f));
            addDrop.accept(Items.GOLD_NUGGET, binomial(4, 0.325f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.12f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(3, 0.4f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(3, 0.3f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.08f));
            addDrop.accept(Items.QUARTZ, chance(0.08f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.05f));
            addDrop.accept(Items.GUNPOWDER, chance(0.08f));
            addDrop.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.05f));
            addDrop.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.05f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.07f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.04f), tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.5f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.4f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.09f));
            addDrop.accept(Items.QUARTZ, chance(0.09f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.06f));
            addDrop.accept(Items.GUNPOWDER, chance(0.09f));
            addDrop.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.07f));
            addDrop.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.07f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.08f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.05f), tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.1f));
            addDrop.accept(Items.QUARTZ, chance(0.11f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.07f));
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            addDrop.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.1f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.065f), tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.11f));
            addDrop.accept(Items.QUARTZ, chance(0.13f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.08f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
            addDrop.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            addDrop.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.14f));
            addDrop.accept(Items.RAW_GOLD, chance(0.03f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.07f), tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.14f));
            addDrop.accept(Items.QUARTZ, chance(0.13f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.12f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.09f), tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_NETHERRACK.get()), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            addDrop.accept(Items.BLAZE_POWDER, chance(0.15f));
            addDrop.accept(Items.QUARTZ, chance(0.15f));
            addDrop.accept(Items.MAGMA_CREAM, chance(0.1f));
            addDrop.accept(Items.GUNPOWDER, chance(0.13f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.12f));

            addConditionalDrop.accept(EItems.COBALT_ORE_CHUNK.get(), chance(0.11f), tagNotEmpty(EItemTags.ORES_COBALT));
        });

        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.12f));
            addDrop.accept(Items.GUNPOWDER, chance(0.07f));
            addDrop.accept(Items.BONE, chance(0.08f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.06f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.14f));
            addDrop.accept(Items.GUNPOWDER, chance(0.08f));
            addDrop.accept(Items.BONE, chance(0.1f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.07f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.07f));
            addDrop.accept(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.03f));
            addDrop.accept(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.03f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.15f));
            addDrop.accept(Items.GUNPOWDER, chance(0.07f));
            addDrop.accept(Items.BONE, chance(0.08f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.06f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.06f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.17f));
            addDrop.accept(Items.GUNPOWDER, chance(0.1f));
            addDrop.accept(Items.BONE, chance(0.11f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.08f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.09f));
            addDrop.accept(Items.GOLD_NUGGET, chance(0.15f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.19f));
            addDrop.accept(Items.GUNPOWDER, chance(0.11f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.09f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.11f));
        });
        forMesh(writer, ingredient(Items.SOUL_SAND), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.QUARTZ, chance(0.21f));
            addDrop.accept(Items.GUNPOWDER, chance(0.14f));
            addDrop.accept(Items.GHAST_TEAR, chance(0.11f));
            addDrop.accept(Items.GLOWSTONE_DUST, chance(0.13f));
        });

        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.STRING_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.07f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.09f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrop.accept(Items.ENDER_EYE, chance(0.02f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.08f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.11f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.06f));
            addDrop.accept(Items.ENDER_EYE, chance(0.03f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.IRON_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.10f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.13f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.07f));
            addDrop.accept(Items.ENDER_EYE, chance(0.04f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.GOLDEN_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.12f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.12f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.06f));
            addDrop.accept(Items.ENDER_EYE, chance(0.07f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.DIAMOND_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.15f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.10f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrop.accept(Items.ENDER_EYE, chance(0.09f));
        });
        forMesh(writer, ingredient(EItems.CRUSHED_END_STONE), EItems.NETHERITE_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.ENDER_PEARL, chance(0.17f));
            addDrop.accept(Items.CHORUS_FRUIT, chance(0.10f));
            addDrop.accept(Items.CHORUS_FLOWER, chance(0.04f));
            addDrop.accept(Items.ENDER_EYE, chance(0.09f));
            addDrop.accept(Items.ECHO_SHARD, chance(0.03f));
            addDrop.accept(Items.SCULK_SHRIEKER, chance(0.01f));
        });

        for (int i = 0; i < allMeshes.size(); i++) {
            var mesh = allMeshes.get(i);
            final int j = i;
            forMesh(writer, ingredient(Items.MOSS_BLOCK), mesh, (addDrop, addTagDrop, addConditionalDrop) -> {
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
        forMesh(writer, ingredient(Items.MOSS_BLOCK), EItems.FLINT_MESH, (addDrop, addTagDrop, addConditionalDrop) -> {
            addDrop.accept(Items.SWEET_BERRIES, chance(0.03f));
            addDrop.accept(Items.FLOWERING_AZALEA, chance(0.03f));
            addDrop.accept(Items.GLOW_LICHEN, chance(0.04f));
            addDrop.accept(Items.LILY_PAD, chance(0.04f));
        });
    }

    private static BinomialDistributionGenerator chance(float p) {
        return binomial(1, p);
    }

    private static ICondition tagNotEmpty(TagKey<Item> tag) {
        return new NotCondition(new TagEmptyCondition(tag.location()));
    }

    private static ICondition modInstalled(String modid) {
        return new ModLoadedCondition(modid);
    }

    private static void forMesh(Consumer<FinishedRecipe> writer, Ingredient block, RegistryObject<? extends Item> mesh, ForMeshContext addDrops) {
        var folder = mesh.getId().getPath().replace("_mesh", "/");
        var basePath = path(block.getItems()[0].getItem()) + "/" + folder;

        addDrops.accept(
                (result, resultAmount) -> sieveRecipe(writer, basePath + path(result), block, mesh, result, resultAmount),
                (resultTag, resultAmount) -> sieveRecipeTag(writer, basePath + resultTag.location().getPath().concat("_tag"), block, mesh, resultTag, resultAmount),
                ((result, resultAmount, condition) -> sieveConditional(writer, basePath + result.map(MKRecipeProvider::path, tag -> tag.location().getPath().concat("_tag")), block, mesh, result, resultAmount, condition))
        );
    }

    @FunctionalInterface
    private interface ForMeshContext {
        void accept(BiConsumer<Item, NumberProvider> addDrop, BiConsumer<TagKey<Item>, NumberProvider> addTagDrop, AddConditionalTag addConditionalTag);
    }

    private interface AddConditionalTag {
        void accept(Either<Item, TagKey<Item>> result, NumberProvider resultAmount, ICondition condition);

        default void accept(Item result, NumberProvider resultAmount, ICondition condition) {
            this.accept(Either.left(result), resultAmount, condition);
        }

        default void accept(TagKey<Item> result, NumberProvider resultAmount, ICondition condition) {
            this.accept(Either.right(result), resultAmount, condition);
        }
    }

    private static void sieveRecipe(Consumer<FinishedRecipe> writer, String name, Ingredient block, Supplier<? extends Item> mesh, Item result, NumberProvider resultAmount) {
        writer.accept(new FinishedSieveRecipe(new ResourceLocation(ExDeorum.ID, "sieve/" + name), mesh.get(), block, Either.left(result), resultAmount));
    }

    private static void sieveRecipeTag(Consumer<FinishedRecipe> writer, String name, Ingredient block, Supplier<? extends Item> mesh, TagKey<Item> result, NumberProvider resultAmount) {
        writer.accept(new FinishedSieveRecipe(new ResourceLocation(ExDeorum.ID, "sieve/" + name), mesh.get(), block, Either.right(result), resultAmount));
    }

    private static void sieveConditional(Consumer<FinishedRecipe> writer, String name, Ingredient block, Supplier<? extends Item> mesh, Either<Item, TagKey<Item>> result, NumberProvider resultAmount, ICondition condition) {
        var path = new ResourceLocation(ExDeorum.ID, "sieve/" + name);
        ConditionalRecipe.builder()
                .addCondition(condition)
                .addRecipe(new FinishedSieveRecipe(path, mesh.get(), block, result, resultAmount))
                .build(writer, path);
    }
}
