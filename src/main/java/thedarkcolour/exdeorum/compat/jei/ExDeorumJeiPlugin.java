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

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.item.WateringCanItem;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@JeiPlugin
public class ExDeorumJeiPlugin implements IModPlugin {
    public static final ResourceLocation EX_DEORUM_JEI_TEXTURE = new ResourceLocation(ExDeorum.ID, "textures/gui/jei/enr_jei.png");

    public static final RecipeType<BarrelCompostRecipe> BARREL_COMPOST = RecipeType.create(ExDeorum.ID, "barrel_compost", BarrelCompostRecipe.class);
    public static final RecipeType<BarrelMixingRecipe> BARREL_MIXING = RecipeType.create(ExDeorum.ID, "barrel_mixing", BarrelMixingRecipe.class);
    public static final RecipeType<CrucibleRecipe> LAVA_CRUCIBLE = RecipeType.create(ExDeorum.ID, "lava_crucible", CrucibleRecipe.class);
    public static final RecipeType<CrucibleRecipe> WATER_CRUCIBLE = RecipeType.create(ExDeorum.ID, "water_crucible", CrucibleRecipe.class);
    public static final RecipeType<JeiSieveRecipeGroup> SIEVE = RecipeType.create(ExDeorum.ID, "sieve", JeiSieveRecipeGroup.class);
    public static final RecipeType<HammerRecipe> HAMMER = RecipeType.create(ExDeorum.ID, "hammer", HammerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExDeorum.ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        var arrow = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 0, 18, 22, 15);
        var plus = helper.createDrawable(ExDeorumJeiPlugin.EX_DEORUM_JEI_TEXTURE, 22,18, 8, 8);

        registration.addRecipeCategories(new BarrelCompostCategory(helper));
        registration.addRecipeCategories(new BarrelMixingCategory(helper, plus, arrow));
        registration.addRecipeCategories(new CrucibleCategory.LavaCrucible(helper, arrow));
        registration.addRecipeCategories(new CrucibleCategory.WaterCrucible(helper, arrow));
        registration.addRecipeCategories(new SieveCategory(helper));
        registration.addRecipeCategories(new HammerCategory(helper, arrow));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var barrels = new ItemStack[]{
                new ItemStack(EItems.OAK_BARREL.get()),
                new ItemStack(EItems.SPRUCE_BARREL.get()),
                new ItemStack(EItems.BIRCH_BARREL.get()),
                new ItemStack(EItems.JUNGLE_BARREL.get()),
                new ItemStack(EItems.ACACIA_BARREL.get()),
                new ItemStack(EItems.DARK_OAK_BARREL.get()),
                new ItemStack(EItems.MANGROVE_BARREL.get()),
                new ItemStack(EItems.CHERRY_BARREL.get()),
                new ItemStack(EItems.BAMBOO_BARREL.get()),
                new ItemStack(EItems.CRIMSON_BARREL.get()),
                new ItemStack(EItems.WARPED_BARREL.get()),
                new ItemStack(EItems.STONE_BARREL.get()),
        };
        for (var barrel : barrels) {
            registration.addRecipeCatalyst(barrel, BARREL_COMPOST);
            registration.addRecipeCatalyst(barrel, BARREL_MIXING);
        }

        registration.addRecipeCatalyst(new ItemStack(EItems.PORCELAIN_CRUCIBLE.get()), LAVA_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.WARPED_CRUCIBLE.get()), LAVA_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CRIMSON_CRUCIBLE.get()), LAVA_CRUCIBLE);

        registration.addRecipeCatalyst(new ItemStack(EItems.OAK_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.SPRUCE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BIRCH_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.JUNGLE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.ACACIA_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.DARK_OAK_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.MANGROVE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CHERRY_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BAMBOO_CRUCIBLE.get()), WATER_CRUCIBLE);

        registration.addRecipeCatalyst(new ItemStack(EItems.OAK_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.SPRUCE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BIRCH_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.JUNGLE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.ACACIA_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.DARK_OAK_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.MANGROVE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CHERRY_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BAMBOO_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CRIMSON_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.WARPED_SIEVE.get()), SIEVE);

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
