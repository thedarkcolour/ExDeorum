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

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.CompressedHammerRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.CompressedSieveRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public class ERecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ExDeorum.ID);

    public static final RegistryObject<RecipeType<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_TYPES.register("barrel_compost", () -> RecipeType.simple(ERecipeTypes.BARREL_COMPOST.getId()));
    public static final RegistryObject<RecipeType<BarrelMixingRecipe>> BARREL_MIXING = RECIPE_TYPES.register("barrel_mixing", () -> RecipeType.simple(ERecipeTypes.BARREL_MIXING.getId()));
    public static final RegistryObject<RecipeType<BarrelFluidMixingRecipe>> BARREL_FLUID_MIXING = RECIPE_TYPES.register("barrel_fluid_mixing", () -> RecipeType.simple(ERecipeTypes.BARREL_FLUID_MIXING.getId()));
    public static final RegistryObject<RecipeType<FluidTransformationRecipe>> BARREL_FLUID_TRANSFORMATION = RECIPE_TYPES.register("barrel_fluid_transformation", () -> RecipeType.simple(ERecipeTypes.BARREL_FLUID_TRANSFORMATION.getId()));

    public static final RegistryObject<RecipeType<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_TYPES.register("lava_crucible", () -> RecipeType.simple(ERecipeTypes.LAVA_CRUCIBLE.getId()));
    public static final RegistryObject<RecipeType<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_TYPES.register("water_crucible", () -> RecipeType.simple(ERecipeTypes.WATER_CRUCIBLE.getId()));

    public static final RegistryObject<RecipeType<HammerRecipe>> HAMMER = RECIPE_TYPES.register("hammer", () -> RecipeType.simple(ERecipeTypes.HAMMER.getId()));
    public static final RegistryObject<RecipeType<CompressedHammerRecipe>> COMPRESSED_HAMMER = RECIPE_TYPES.register("compressed_hammer", () -> RecipeType.simple(ERecipeTypes.COMPRESSED_SIEVE.getId()));
    public static final RegistryObject<RecipeType<CrookRecipe>> CROOK = RECIPE_TYPES.register("crook", () -> RecipeType.simple(ERecipeTypes.CROOK.getId()));
    public static final RegistryObject<RecipeType<CrucibleHeatRecipe>> CRUCIBLE_HEAT_SOURCE = RECIPE_TYPES.register("crucible_heat_source", () -> RecipeType.simple(ERecipeTypes.CRUCIBLE_HEAT_SOURCE.getId()));

    public static final RegistryObject<RecipeType<SieveRecipe>> SIEVE = RECIPE_TYPES.register("sieve", () -> RecipeType.simple(ERecipeTypes.SIEVE.getId()));
    public static final RegistryObject<RecipeType<CompressedSieveRecipe>> COMPRESSED_SIEVE = RECIPE_TYPES.register("compressed_sieve", () -> RecipeType.simple(ERecipeTypes.COMPRESSED_SIEVE.getId()));
}
