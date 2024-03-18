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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.OreChunkRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public class ERecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExDeorum.ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_SERIALIZERS.register("barrel_compost", BarrelCompostRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BarrelMixingRecipe>> BARREL_MIXING = RECIPE_SERIALIZERS.register("barrel_mixing", BarrelMixingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BarrelFluidMixingRecipe>> BARREL_FLUID_MIXING = RECIPE_SERIALIZERS.register("barrel_fluid_mixing", BarrelFluidMixingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidTransformationRecipe>> BARREL_FLUID_TRANSFORMATION = RECIPE_SERIALIZERS.register("barrel_fluid_transformation", FluidTransformationRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HammerRecipe>> HAMMER = RECIPE_SERIALIZERS.register("hammer", HammerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrookRecipe>> CROOK = RECIPE_SERIALIZERS.register("crook", CrookRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrucibleHeatRecipe>> CRUCIBLE_HEAT_SOURCE = RECIPE_SERIALIZERS.register("crucible_heat_source", CrucibleHeatRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrucibleRecipe.Lava>> LAVA_CRUCIBLE = RECIPE_SERIALIZERS.register("lava_crucible", () -> new CrucibleRecipe.Serializer<>(CrucibleRecipe.Lava.CODEC, CrucibleRecipe.Lava::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrucibleRecipe.Water>> WATER_CRUCIBLE = RECIPE_SERIALIZERS.register("water_crucible", () -> new CrucibleRecipe.Serializer<>(CrucibleRecipe.Water.CODEC, CrucibleRecipe.Water::new));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SieveRecipe>> SIEVE = RECIPE_SERIALIZERS.register("sieve", SieveRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<OreChunkRecipe>> ORE_CHUNK = RECIPE_SERIALIZERS.register("ore_chunk", OreChunkRecipe.Serializer::new);
}
