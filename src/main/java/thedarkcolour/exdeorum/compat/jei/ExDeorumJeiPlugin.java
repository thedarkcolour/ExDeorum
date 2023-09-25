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

package thedarkcolour.exdeorum.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.item.WateringCanItem;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeTypes;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@JeiPlugin
public class ExDeorumJeiPlugin implements IModPlugin {
    public static final ResourceLocation EX_DEORUM_JEI_TEXTURE = new ResourceLocation(ExDeorum.ID, "textures/gui/jei/enr_jei.png");

    static final RecipeType<BarrelCompostRecipe> BARREL_COMPOST = RecipeType.create(ExDeorum.ID, "barrel_compost", BarrelCompostRecipe.class);
    static final RecipeType<BarrelMixingRecipe> BARREL_MIXING = RecipeType.create(ExDeorum.ID, "barrel_mixing", BarrelMixingRecipe.class);
    static final RecipeType<CrucibleRecipe> LAVA_CRUCIBLE = RecipeType.create(ExDeorum.ID, "lava_crucible", CrucibleRecipe.class);
    static final RecipeType<CrucibleRecipe> WATER_CRUCIBLE = RecipeType.create(ExDeorum.ID, "water_crucible", CrucibleRecipe.class);
    static final RecipeType<JeiSieveRecipeGroup> SIEVE = RecipeType.create(ExDeorum.ID, "sieve", JeiSieveRecipeGroup.class);
    static final RecipeType<HammerRecipe> HAMMER = RecipeType.create(ExDeorum.ID, "hammer", HammerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExDeorum.ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        var arrow = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 0, 18, 22, 15);
        var plus = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 22, 18, 8, 8);

        registration.addRecipeCategories(new BarrelCompostCategory(helper));
        registration.addRecipeCategories(new BarrelMixingCategory(helper, plus, arrow));
        registration.addRecipeCategories(new CrucibleCategory.LavaCrucible(helper, arrow));
        registration.addRecipeCategories(new CrucibleCategory.WaterCrucible(helper, arrow));
        registration.addRecipeCategories(new SieveCategory(helper));
        registration.addRecipeCategories(new HammerCategory(helper, arrow));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var barrels = Lists.newArrayList(
                EItems.OAK_BARREL.get(),
                EItems.SPRUCE_BARREL.get(),
                EItems.BIRCH_BARREL.get(),
                EItems.JUNGLE_BARREL.get(),
                EItems.ACACIA_BARREL.get(),
                EItems.DARK_OAK_BARREL.get(),
                EItems.MANGROVE_BARREL.get(),
                EItems.CHERRY_BARREL.get(),
                EItems.BAMBOO_BARREL.get(),
                EItems.CRIMSON_BARREL.get(),
                EItems.WARPED_BARREL.get(),
                EItems.STONE_BARREL.get()
        );
        var sieves = Lists.newArrayList(
                EItems.OAK_SIEVE.get(),
                EItems.SPRUCE_SIEVE.get(),
                EItems.BIRCH_SIEVE.get(),
                EItems.JUNGLE_SIEVE.get(),
                EItems.ACACIA_SIEVE.get(),
                EItems.DARK_OAK_SIEVE.get(),
                EItems.MANGROVE_SIEVE.get(),
                EItems.CHERRY_SIEVE.get(),
                EItems.BAMBOO_SIEVE.get(),
                EItems.CRIMSON_SIEVE.get(),
                EItems.WARPED_SIEVE.get()
        );
        var lavaCrucibles = Lists.newArrayList(
                EItems.PORCELAIN_CRUCIBLE.get(),
                EItems.WARPED_CRUCIBLE.get(),
                EItems.CRIMSON_CRUCIBLE.get()
        );
        var waterCrucibles = Lists.newArrayList(
                EItems.OAK_CRUCIBLE.get(),
                EItems.SPRUCE_CRUCIBLE.get(),
                EItems.BIRCH_CRUCIBLE.get(),
                EItems.JUNGLE_CRUCIBLE.get(),
                EItems.ACACIA_CRUCIBLE.get(),
                EItems.DARK_OAK_CRUCIBLE.get(),
                EItems.MANGROVE_CRUCIBLE.get(),
                EItems.CHERRY_CRUCIBLE.get(),
                EItems.BAMBOO_CRUCIBLE.get()
        );
        if (ModList.get().isLoaded(ModIds.BIOMES_O_PLENTY)) {
            barrels.add(EItems.FIR_BARREL.get());
            barrels.add(EItems.REDWOOD_BARREL.get());
            barrels.add(EItems.MAHOGANY_BARREL.get());
            barrels.add(EItems.JACARANDA_BARREL.get());
            barrels.add(EItems.PALM_BARREL.get());
            barrels.add(EItems.WILLOW_BARREL.get());
            barrels.add(EItems.DEAD_BARREL.get());
            barrels.add(EItems.MAGIC_BARREL.get());
            barrels.add(EItems.UMBRAN_BARREL.get());
            barrels.add(EItems.HELLBARK_BARREL.get());
            sieves.add(EItems.FIR_SIEVE.get());
            sieves.add(EItems.REDWOOD_SIEVE.get());
            sieves.add(EItems.MAHOGANY_SIEVE.get());
            sieves.add(EItems.JACARANDA_SIEVE.get());
            sieves.add(EItems.PALM_SIEVE.get());
            sieves.add(EItems.WILLOW_SIEVE.get());
            sieves.add(EItems.DEAD_SIEVE.get());
            sieves.add(EItems.MAGIC_SIEVE.get());
            sieves.add(EItems.UMBRAN_SIEVE.get());
            sieves.add(EItems.HELLBARK_SIEVE.get());
            lavaCrucibles.add(EItems.HELLBARK_CRUCIBLE.get());
            waterCrucibles.add(EItems.FIR_CRUCIBLE.get());
            waterCrucibles.add(EItems.REDWOOD_CRUCIBLE.get());
            waterCrucibles.add(EItems.MAHOGANY_CRUCIBLE.get());
            waterCrucibles.add(EItems.JACARANDA_CRUCIBLE.get());
            waterCrucibles.add(EItems.PALM_CRUCIBLE.get());
            waterCrucibles.add(EItems.WILLOW_CRUCIBLE.get());
            waterCrucibles.add(EItems.DEAD_CRUCIBLE.get());
            waterCrucibles.add(EItems.MAGIC_CRUCIBLE.get());
            waterCrucibles.add(EItems.UMBRAN_CRUCIBLE.get());
        }
        for (var barrel : barrels) {
            var stack = new ItemStack(barrel);
            registration.addRecipeCatalyst(stack, BARREL_COMPOST);
            registration.addRecipeCatalyst(stack, BARREL_MIXING);
        }
        for (var lavaCrucible : lavaCrucibles) {
            registration.addRecipeCatalyst(new ItemStack(lavaCrucible), LAVA_CRUCIBLE);
        }
        for (var waterCrucible : waterCrucibles) {
            registration.addRecipeCatalyst(new ItemStack(waterCrucible), WATER_CRUCIBLE);
        }
        for (var sieve : sieves) {
            registration.addRecipeCatalyst(new ItemStack(sieve), SIEVE);
        }

        registration.addRecipeCatalyst(new ItemStack(EItems.WOODEN_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.STONE_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.GOLDEN_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.IRON_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.DIAMOND_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.NETHERITE_HAMMER.get()), HAMMER);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addItemStackInfo(new ItemStack(EItems.SILK_WORM.get()), Component.translatable(TranslationKeys.SILK_WORM_JEI_INFO));
        registration.addItemStackInfo(List.of(new ItemStack(EBlocks.OAK_SIEVE.get()), new ItemStack(EBlocks.SPRUCE_SIEVE.get()), new ItemStack(EBlocks.BIRCH_SIEVE.get()), new ItemStack(EBlocks.JUNGLE_SIEVE.get()), new ItemStack(EBlocks.ACACIA_SIEVE.get()), new ItemStack(EBlocks.DARK_OAK_SIEVE.get()), new ItemStack(EBlocks.MANGROVE_SIEVE.get()), new ItemStack(EBlocks.CHERRY_SIEVE.get()), new ItemStack(EBlocks.BAMBOO_SIEVE.get()), new ItemStack(EBlocks.CRIMSON_SIEVE.get()), new ItemStack(EBlocks.WARPED_SIEVE.get())), Component.translatable(TranslationKeys.SIEVE_JEI_INFO));
        registration.addItemStackInfo(List.of(WateringCanItem.getFull(EItems.WOODEN_WATERING_CAN), WateringCanItem.getFull(EItems.STONE_WATERING_CAN), WateringCanItem.getFull(EItems.IRON_WATERING_CAN), WateringCanItem.getFull(EItems.GOLDEN_WATERING_CAN), WateringCanItem.getFull(EItems.DIAMOND_WATERING_CAN), WateringCanItem.getFull(EItems.NETHERITE_WATERING_CAN)), Component.translatable(TranslationKeys.WATERING_CAN_JEI_INFO));
        var witchWaterInfo = Component.translatable(TranslationKeys.WITCH_WATER_JEI_INFO);
        registration.addItemStackInfo(List.of(new ItemStack(EItems.WITCH_WATER_BUCKET.get()), new ItemStack(EItems.PORCELAIN_WITCH_WATER_BUCKET.get())), witchWaterInfo);
        registration.addIngredientInfo(new FluidStack(EFluids.WITCH_WATER.get(), 1000), ForgeTypes.FLUID_STACK, witchWaterInfo);
        registration.addItemStackInfo(new ItemStack(EItems.GRASS_SEEDS.get()), Component.translatable(TranslationKeys.GRASS_SEEDS_JEI_INFO));
        registration.addItemStackInfo(new ItemStack(EItems.MYCELIUM_SPORES.get()), Component.translatable(TranslationKeys.MYCELIUM_SPORES_JEI_INFO));
        registration.addItemStackInfo(new ItemStack(EItems.WARPED_NYLIUM_SPORES.get()), Component.translatable(TranslationKeys.WARPED_NYLIUM_SPORES_JEI_INFO));
        registration.addItemStackInfo(new ItemStack(EItems.CRIMSON_NYLIUM_SPORES.get()), Component.translatable(TranslationKeys.CRIMSON_NYLIUM_SPORES_JEI_INFO));
        registration.addItemStackInfo(new ItemStack(EItems.SCULK_CORE.get()), Component.translatable(TranslationKeys.SCULK_CORE_JEI_INFO));

        var toRemove = new ArrayList<ItemStack>();

        if (RecipeUtil.isTagEmpty(EItemTags.ORES_ALUMINUM))
            toRemove.add(new ItemStack(EItems.ALUMINUM_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_COBALT)) toRemove.add(new ItemStack(EItems.COBALT_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_SILVER)) toRemove.add(new ItemStack(EItems.SILVER_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_LEAD)) toRemove.add(new ItemStack(EItems.LEAD_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_PLATINUM))
            toRemove.add(new ItemStack(EItems.PLATINUM_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_NICKEL)) toRemove.add(new ItemStack(EItems.NICKEL_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_URANIUM)) toRemove.add(new ItemStack(EItems.URANIUM_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_OSMIUM)) toRemove.add(new ItemStack(EItems.OSMIUM_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_TIN)) toRemove.add(new ItemStack(EItems.TIN_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_ZINC)) toRemove.add(new ItemStack(EItems.ZINC_ORE_CHUNK.get()));
        if (RecipeUtil.isTagEmpty(EItemTags.ORES_IRIDIUM)) toRemove.add(new ItemStack(EItems.IRIDIUM_ORE_CHUNK.get()));

        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, toRemove);

        addRecipes(registration, BARREL_COMPOST, ERecipeTypes.BARREL_COMPOST);
        addRecipes(registration, BARREL_MIXING, ERecipeTypes.BARREL_MIXING);
        addRecipes(registration, LAVA_CRUCIBLE, ERecipeTypes.LAVA_CRUCIBLE);
        addRecipes(registration, WATER_CRUCIBLE, ERecipeTypes.WATER_CRUCIBLE);
        addRecipes(registration, HAMMER, ERecipeTypes.HAMMER);
        JeiSieveRecipeGroup.addGroupedRecipes(registration, SIEVE);
    }

    private static <C extends Container, T extends Recipe<C>> void addRecipes(IRecipeRegistration registration, RecipeType<T> category, Supplier<net.minecraft.world.item.crafting.RecipeType<T>> type) {
        registration.addRecipes(category, Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor(type.get()));
    }
}
