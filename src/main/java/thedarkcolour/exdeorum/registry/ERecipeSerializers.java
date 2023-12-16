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

package thedarkcolour.exdeorum.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.TagResultRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public class ERecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExDeorum.ID);

    public static final RegistryObject<RecipeSerializer<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_SERIALIZERS.register("barrel_compost", BarrelCompostRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BarrelMixingRecipe>> BARREL_MIXING = RECIPE_SERIALIZERS.register("barrel_mixing", BarrelMixingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BarrelFluidMixingRecipe>> BARREL_FLUID_MIXING = RECIPE_SERIALIZERS.register("barrel_fluid_mixing", BarrelFluidMixingRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<HammerRecipe>> HAMMER = RECIPE_SERIALIZERS.register("hammer", HammerRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_SERIALIZERS.register("lava_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.LAVA_CRUCIBLE.get()));
    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_SERIALIZERS.register("water_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.WATER_CRUCIBLE.get()));

    public static final RegistryObject<RecipeSerializer<SieveRecipe>> SIEVE = RECIPE_SERIALIZERS.register("sieve", SieveRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> TAG_RESULT = RECIPE_SERIALIZERS.register("tag_result", TagResultRecipe.Serializer::new);
}
