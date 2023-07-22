package thedarkcolour.exnihiloreborn.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.barrel.FinishedBarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.FinishedCrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.FinishedHammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ERecipeProvider extends RecipeProvider {
    public ERecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        createCraftingRecipes(consumer);
        createSieveRecipes(consumer);
        createCrucibleRecipes(consumer);
        createHammerRecipes(consumer);
        createBarrelCompostRecipes(consumer);
    }

    private void createCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        // Crook
        shapedCrook(consumer, EItems.CROOK, Ingredient.of(Tags.Items.RODS_WOODEN));
        shapedCrook(consumer, EItems.COMPRESSED_CROOK, Ingredient.of(EItems.CROOK.get()));
        shapedCrook(consumer, EItems.BONE_CROOK, Ingredient.of(Items.BONE));

        // Hammer
        shapedHammer(consumer, EItems.WOODEN_HAMMER, Ingredient.of(ItemTags.PLANKS));
        shapedHammer(consumer, EItems.STONE_HAMMER, Ingredient.of(ItemTags.STONE_CRAFTING_MATERIALS));
        shapedHammer(consumer, EItems.GOLDEN_HAMMER, Ingredient.of(Tags.Items.INGOTS_GOLD));
        shapedHammer(consumer, EItems.IRON_HAMMER, Ingredient.of(Tags.Items.INGOTS_IRON));
        shapedHammer(consumer, EItems.DIAMOND_HAMMER, Ingredient.of(Tags.Items.GEMS_DIAMOND));
        SmithingRecipeBuilder.smithing(Ingredient.of(EItems.DIAMOND_HAMMER.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), EItems.NETHERITE_HAMMER.get());

        // Barrels
        shapedU(consumer, EItems.OAK_BARREL, Items.OAK_PLANKS, Items.OAK_SLAB);
        shapedU(consumer, EItems.SPRUCE_BARREL, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        shapedU(consumer, EItems.BIRCH_BARREL, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        shapedU(consumer, EItems.JUNGLE_BARREL, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        shapedU(consumer, EItems.ACACIA_BARREL, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        shapedU(consumer, EItems.DARK_OAK_BARREL, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        shapedU(consumer, EItems.CRIMSON_BARREL, Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        shapedU(consumer, EItems.WARPED_BARREL, Items.WARPED_PLANKS, Items.WARPED_SLAB);
        shapedU(consumer, EItems.STONE_BARREL, Items.STONE, Items.STONE_SLAB);

        // Crucible
        shapedU(consumer, EItems.OAK_CRUCIBLE, ItemTags.OAK_LOGS, Items.OAK_SLAB);
        shapedU(consumer, EItems.SPRUCE_CRUCIBLE, ItemTags.SPRUCE_LOGS, Items.SPRUCE_SLAB);
        shapedU(consumer, EItems.BIRCH_CRUCIBLE, ItemTags.BIRCH_LOGS, Items.BIRCH_SLAB);
        shapedU(consumer, EItems.JUNGLE_CRUCIBLE, ItemTags.JUNGLE_LOGS, Items.JUNGLE_SLAB);
        shapedU(consumer, EItems.ACACIA_CRUCIBLE, ItemTags.ACACIA_LOGS, Items.ACACIA_SLAB);
        shapedU(consumer, EItems.DARK_OAK_CRUCIBLE, ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_SLAB);
        shapedU(consumer, EItems.CRIMSON_CRUCIBLE, ItemTags.CRIMSON_STEMS, Items.CRIMSON_SLAB);
        shapedU(consumer, EItems.WARPED_CRUCIBLE, ItemTags.WARPED_STEMS, Items.WARPED_SLAB);
        shapedU(consumer, EItems.UNFIRED_CRUCIBLE, EItems.PORCELAIN_CLAY.get(), EItems.PORCELAIN_CLAY.get());

        // Furnace Recipe
        CookingRecipeBuilder.smelting(Ingredient.of(EItems.UNFIRED_CRUCIBLE.get()), EItems.PORCELAIN_CRUCIBLE.get(), 0.1f, 200).unlockedBy("has_item", has(EItems.UNFIRED_CRUCIBLE.get())).save(consumer, EItems.PORCELAIN_CRUCIBLE.getId());
        CookingRecipeBuilder.smelting(Ingredient.of(EItems.SILK_WORM.get()), EItems.COOKED_SILK_WORM.get(), 0.1f, 200).unlockedBy("has_item", has(EItems.SILK_WORM.get())).save(consumer, EItems.COOKED_SILK_WORM.getId());

        // Smoker Recipe
        CookingRecipeBuilder.cooking(Ingredient.of(EItems.SILK_WORM.get()), EItems.COOKED_SILK_WORM.get(), 0.1f, 200, IRecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(EItems.SILK_WORM.get())).save(consumer, "cooked_silk_worm_from_smoking");
    }

    private void shapedHammer(Consumer<IFinishedRecipe> consumer, RegistryObject<Item> hammer, Ingredient ingredient) {
        shaped(consumer, hammer, 1, builder -> {
            builder.define('x', ingredient);
            builder.define('v', Tags.Items.RODS_WOODEN);
            builder.pattern(" x ");
            builder.pattern("xv ");
            builder.pattern("  v");
            builder.unlockedBy("has_item", has(ItemTags.PLANKS));
        });
    }

    private void shapedCrook(Consumer<IFinishedRecipe> consumer, RegistryObject<Item> crook, Ingredient stick) {
        shaped(consumer, crook, 1, builder -> {
            builder.define('x', stick);
            builder.pattern("xx");
            builder.pattern(" x");
            builder.pattern(" x");
            builder.unlockedBy("has_item", has(Tags.Items.RODS_WOODEN));
        });
    }

    private void shapedU(Consumer<IFinishedRecipe> consumer, RegistryObject<BlockItem> barrel, ITag<Item> log, Item slab) {
        shaped(consumer, barrel, 1, builder -> {
            builder.define('x', log);
            builder.define('z', slab);
            builder.pattern("x x");
            builder.pattern("x x");
            builder.pattern("xzx");
            builder.unlockedBy("has_item", has(ItemTags.PLANKS));
        });
    }

    private void shapedU(Consumer<IFinishedRecipe> consumer, RegistryObject<BlockItem> barrel, Item plank, Item slab) {
        shaped(consumer, barrel, 1, builder -> {
            builder.define('x', plank);
            builder.define('z', slab);
            builder.pattern("x x");
            builder.pattern("x x");
            builder.pattern("xzx");
            builder.unlockedBy("has_item", has(ItemTags.PLANKS));
        });
    }

    private void shaped(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends Item> jungleBarrel, int amount, Consumer<ShapedRecipeBuilder> recipe) {
        Item item = jungleBarrel.get();

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(item, amount);
        recipe.accept(builder);
        builder.save(consumer, item.getRegistryName());
    }

    private void createSieveRecipes(Consumer<IFinishedRecipe> consumer) {
        sieveRecipe(consumer, "stone_pebble", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.withExtraChances(EItems.STONE_PEBBLE, new float[] {1.0f, 1.0f, 0.5f, 0.5f, 0.1f, 0.1f }));
        sieveRecipe(consumer, "wheat_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.WHEAT_SEEDS, 0.7f));
        sieveRecipe(consumer, "beetroot_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.BEETROOT_SEEDS, 0.35f));
        sieveRecipe(consumer, "melon_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.MELON_SEEDS, 0.35f));
        sieveRecipe(consumer, "pumpkin_seeds", Ingredient.of(Items.DIRT), EItems.STRING_MESH, Reward.of(Items.PUMPKIN_SEEDS, 0.35f));

        //sieveRecipe(consumer, "", Ingredient.of());
    }

    private void createCrucibleRecipes(Consumer<IFinishedRecipe> consumer) {
        lavaCrucible(consumer, "cobblestone", Ingredient.of(Blocks.COBBLESTONE), 250);
        lavaCrucible(consumer, "stone", Ingredient.of(Blocks.STONE), 250);
        lavaCrucible(consumer, "gravel", Ingredient.of(Blocks.GRAVEL), 250);
        lavaCrucible(consumer, "netherrack", Ingredient.of(Blocks.NETHERRACK), 1000);

        waterCrucible(consumer, "sweet_berries", Ingredient.of(Items.SWEET_BERRIES), 50);
        waterCrucible(consumer, "melon_slice", Ingredient.of(Items.MELON_SLICE), 50);

        waterCrucible(consumer, "saplings", Ingredient.of(ItemTags.SAPLINGS), 100);
        waterCrucible(consumer, "small_flowers", Ingredient.of(ItemTags.SMALL_FLOWERS), 100);
        waterCrucible(consumer, "apple", Ingredient.of(Items.APPLE), 100);

        waterCrucible(consumer, "tall_flowers", Ingredient.of(ItemTags.TALL_FLOWERS), 200);

        waterCrucible(consumer, "cactus", Ingredient.of(Items.CACTUS), 250);
        waterCrucible(consumer, "pumpkin", Ingredient.of(Items.PUMPKIN), 250);
        waterCrucible(consumer, "melon", Ingredient.of(Items.MELON), 250);
        waterCrucible(consumer, "leaves", Ingredient.of(ItemTags.LEAVES), 250);
    }

    private void createHammerRecipes(Consumer<IFinishedRecipe> consumer) {
        // Cobblestone -> Gravel -> Sand -> Dust
        hammerRecipe(consumer, "gravel", Blocks.COBBLESTONE, new Reward(Blocks.GRAVEL));
        hammerRecipe(consumer, "sand", Blocks.GRAVEL, new Reward(Blocks.SAND));
        hammerRecipe(consumer, "dust", Blocks.SAND, new Reward(EBlocks.DUST.get()));

        hammerRecipe(consumer, "crushed_netherrack", Blocks.NETHERRACK, new Reward(EBlocks.CRUSHED_NETHERRACK.get()));

        hammerRecipe(consumer, "crushing_sandstone", Ingredient.of(Items.SANDSTONE, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, Items.SMOOTH_SANDSTONE), ImmutableList.of(new Reward(Items.SAND)));
        hammerRecipe(consumer, "crushing_red_sandstone", Ingredient.of(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE), ImmutableList.of(new Reward(Items.RED_SAND)));
        hammerRecipe(consumer, "crushing_stone_bricks", Items.STONE_BRICKS, new Reward(Items.CRACKED_STONE_BRICKS));

        hammerRecipe(consumer, "stone_pebbles",
                Ingredient.of(Items.STONE, Items.CRACKED_STONE_BRICKS),
                Reward.withExtraChances(EItems.STONE_PEBBLE, new float[] { 0.75f, 0.75f, 0.5f, 0.25f, 0.05f }));
    }

    private void createBarrelCompostRecipes(Consumer<IFinishedRecipe> consumer) {
        barrelCompost(consumer, "melon_slice", Ingredient.of(Items.MELON_SLICE), 40);
        barrelCompost(consumer, "kelp", Ingredient.of(Items.DRIED_KELP, Items.KELP), 40);
        barrelCompost(consumer, "silk_worms", Ingredient.of(EItems.SILK_WORM.get(), EItems.COOKED_SILK_WORM.get()), 40);

        barrelCompost(consumer, "seeds", Ingredient.of(Tags.Items.SEEDS), 80);
        barrelCompost(consumer, "seagrass", Ingredient.of(Items.SEAGRASS), 80);
        barrelCompost(consumer, "sweet_berries", Ingredient.of(Items.SWEET_BERRIES), 80);
        barrelCompost(consumer, "wheat", Ingredient.of(Tags.Items.CROPS_WHEAT), 80);
        barrelCompost(consumer, "spider_eye", Ingredient.of(Items.SPIDER_EYE), 80);

        barrelCompost(consumer, "apple", Ingredient.of(Items.APPLE), 100);
        barrelCompost(consumer, "small_flowers", Ingredient.of(ItemTags.SMALL_FLOWERS), 100);
        barrelCompost(consumer, "cookie", Ingredient.of(Items.COOKIE), 100);
        barrelCompost(consumer, "nether_wart", Ingredient.of(Items.NETHER_WART), 100);
        barrelCompost(consumer, "mushrooms", Ingredient.of(Tags.Items.MUSHROOMS), 100);

        barrelCompost(consumer, "saplings", Ingredient.of(ItemTags.SAPLINGS), 125);
        barrelCompost(consumer, "leaves", Ingredient.of(ItemTags.LEAVES), 125);
        barrelCompost(consumer, "bread", Ingredient.of(Items.BREAD), 125);

        barrelCompost(consumer, "tall_flowers", Ingredient.of(ItemTags.TALL_FLOWERS), 150);
        barrelCompost(consumer, "pumpkin_pie", Ingredient.of(Items.PUMPKIN_PIE), 150);

        barrelCompost(consumer, "melon", Ingredient.of(Items.MELON), 200);
        barrelCompost(consumer, "pumpkin", Ingredient.of(Items.PUMPKIN), 200);
    }

    private void lavaCrucible(Consumer<IFinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExNihiloReborn.ID, "lava_crucible/" + id), ERecipeSerializers.LAVA_CRUCIBLE.get(), ingredient, Fluids.LAVA, volume));
    }

    private void waterCrucible(Consumer<IFinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedCrucibleRecipe(new ResourceLocation(ExNihiloReborn.ID, "water_crucible/" + id), ERecipeSerializers.WATER_CRUCIBLE.get(), ingredient, Fluids.WATER, volume));
    }

    private void barrelCompost(Consumer<IFinishedRecipe> consumer, String id, Ingredient ingredient, int volume) {
        consumer.accept(new FinishedBarrelCompostRecipe(new ResourceLocation(ExNihiloReborn.ID, "barrel_compost/" + id), ingredient, volume));
    }

    private void hammerRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient block, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedHammerRecipe(ERecipeSerializers.HAMMER.get(), new ResourceLocation(ExNihiloReborn.ID, "hammer/" + name), block, rewards));
    }

    private void hammerRecipe(Consumer<IFinishedRecipe> consumer, String name, IItemProvider block, Reward... rewards) {
        hammerRecipe(consumer, name, Ingredient.of(block), ImmutableList.<Reward>builder().add(rewards).build());
    }

    private void hammerRecipe(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> tag, Reward rewards) {
        consumer.accept(new FinishedHammerRecipe(ERecipeSerializers.HAMMER.get(), new ResourceLocation(ExNihiloReborn.ID, tag.getName().getPath() + "_to_" + rewards.getItem().getItem().getRegistryName().getPath()), Ingredient.of(tag), ImmutableList.of(rewards)));
    }

    private void compressedHammerRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient block, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedHammerRecipe(ERecipeSerializers.COMPRESSED_HAMMER.get(), new ResourceLocation(ExNihiloReborn.ID, "compressed_hammer/" + name), block, rewards));
    }

    private void sieveRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient block, Supplier<Item> mesh, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedSieveRecipe(ERecipeSerializers.SIEVE.get(), new ResourceLocation(ExNihiloReborn.ID, "sieve/" + name), mesh.get(), block, rewards));
    }

    private void sieveRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient block, Supplier<Item> mesh, Reward rewards) {
        consumer.accept(new FinishedSieveRecipe(ERecipeSerializers.SIEVE.get(), new ResourceLocation(ExNihiloReborn.ID, "sieve/" + name), mesh.get(), block, ImmutableList.of(rewards)));
    }

    private void compressedSieveRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient block, Supplier<Item> mesh, ImmutableList<Reward> rewards) {
        consumer.accept(new FinishedSieveRecipe(ERecipeSerializers.COMPRESSED_SIEVE.get(), new ResourceLocation(ExNihiloReborn.ID, "compressed_sieve/" + name), mesh.get(), block, rewards));
    }
}
