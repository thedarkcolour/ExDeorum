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

package thedarkcolour.exdeorum.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.*;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.item.HammerItem;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class RecipeUtil {
    private static final int CONSTANT_TYPE = 1;
    private static final int UNIFORM_TYPE = 2;
    private static final int BINOMIAL_TYPE = 3;
    private static final int UNKNOWN_TYPE = 99;

    private static SingleIngredientRecipeCache<BarrelCompostRecipe> barrelCompostRecipeCache;
    private static SingleIngredientRecipeCache<CrucibleRecipe> lavaCrucibleRecipeCache;
    private static SingleIngredientRecipeCache<CrucibleRecipe> waterCrucibleRecipeCache;
    private static SingleIngredientRecipeCache<HammerRecipe> hammerRecipeCache;
    private static SieveRecipeCache sieveRecipeCache;
    private static BarrelFluidMixingRecipeCache barrelFluidMixingRecipeCache;

    public static void reload(RecipeManager recipes) {
        barrelCompostRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.BARREL_COMPOST);
        lavaCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.LAVA_CRUCIBLE);
        waterCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.WATER_CRUCIBLE);
        hammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.HAMMER).trackAllRecipes();
        sieveRecipeCache = new SieveRecipeCache(recipes);
        barrelFluidMixingRecipeCache = new BarrelFluidMixingRecipeCache(recipes);
        HammerItem.refreshValidBlocks();
    }

    public static void unload() {
        barrelCompostRecipeCache = null;
        lavaCrucibleRecipeCache = null;
        waterCrucibleRecipeCache = null;
        hammerRecipeCache = null;
        sieveRecipeCache = null;
        barrelFluidMixingRecipeCache = null;
    }

    public static List<SieveRecipe> getSieveRecipes(Item mesh, ItemStack item) {
        return sieveRecipeCache.getRecipe(mesh, item);
    }

    @Nullable
    public static CrucibleRecipe getLavaCrucibleRecipe(ItemStack item) {
        return lavaCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public static CrucibleRecipe getWaterCrucibleRecipe(ItemStack item) {
        return waterCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public static BarrelCompostRecipe getBarrelCompostRecipe(ItemStack item) {
        return barrelCompostRecipeCache.getRecipe(item);
    }

    @Nullable
    public static HammerRecipe getHammerRecipe(Item item) {
        return hammerRecipeCache.getRecipe(item);
    }

    public static Collection<HammerRecipe> getCachedHammerRecipes() {
        return hammerRecipeCache.getAllRecipes();
    }

    public static <C extends Container, T extends Recipe<C>> Collection<T> byType(RecipeManager manager, RecipeType<T> type) {
        return manager.byType(type).values();
    }

    public static Ingredient readIngredient(JsonObject json, String key) {
        if (GsonHelper.isArrayNode(json, key)) {
            return Ingredient.fromJson(GsonHelper.getAsJsonArray(json, key));
        } else {
            return Ingredient.fromJson(GsonHelper.getAsJsonObject(json, key));
        }
    }

    public static Item readItem(JsonObject json, String key) {
        return CraftingHelper.getItem(GsonHelper.getAsString(json, key), true);
    }

    public static Fluid readFluid(JsonObject json, String key) {
        String fluidName = GsonHelper.getAsString(json, key);
        ResourceLocation fluidKey = new ResourceLocation(fluidName);
        if (!ForgeRegistries.FLUIDS.containsKey(fluidKey)) {
            throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidKey);
        if (fluid == Fluids.EMPTY) {
            throw new JsonSyntaxException("Invalid Fluid: " + fluidName);
        }
        return Objects.requireNonNull(fluid);
    }

    public static NumberProvider readNumberProvider(JsonObject json, String key) {
        var obj = json.get(key);
        return LootDataType.PREDICATE.parser().fromJson(obj, NumberProvider.class);
    }

    public static void toNetworkNumberProvider(FriendlyByteBuf buffer, NumberProvider provider) {
        if (provider.getType() == NumberProviders.CONSTANT) {
            buffer.writeByte(CONSTANT_TYPE);
            buffer.writeFloat(((ConstantValue) provider).value);
        } else if (provider.getType() == NumberProviders.UNIFORM) {
            var uniform = (UniformGenerator) provider;
            buffer.writeByte(UNIFORM_TYPE);
            toNetworkNumberProvider(buffer, uniform.min);
            toNetworkNumberProvider(buffer, uniform.max);
        } else if (provider.getType() == NumberProviders.BINOMIAL) {
            var binomial = (BinomialDistributionGenerator) provider;
            buffer.writeByte(BINOMIAL_TYPE);
            toNetworkNumberProvider(buffer, binomial.n);
            toNetworkNumberProvider(buffer, binomial.p);
        } else {
            buffer.writeByte(UNKNOWN_TYPE);
        }
    }

    public static NumberProvider fromNetworkNumberProvider(FriendlyByteBuf buffer) {
        return switch (buffer.readByte()) {
            case CONSTANT_TYPE -> ConstantValue.exactly(buffer.readFloat());
            case UNIFORM_TYPE ->
                    new UniformGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            case BINOMIAL_TYPE ->
                    new BinomialDistributionGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            default -> ConstantValue.exactly(1f);
        };
    }

    // todo support Forge's ingredient types
    public static boolean areIngredientsEqual(Ingredient first, Ingredient second) {
        // although unlikely, we should check this anyway
        if (first == second) return true;

        if (first.isVanilla() && second.isVanilla()) {
            var firstValues = new ObjectArrayList<>(first.values);
            var secondValues = new ObjectArrayList<>(second.values);

            // if arrays are same size, check if their contents are equal (order does not matter)
            if (firstValues.size() == secondValues.size()) {
                outer:
                for (int i = 0; i < firstValues.size(); i++) {
                    var firstValue = firstValues.get(i);

                    for (int j = 0; j < firstValues.size(); j++) {
                        if (areValuesEqual(firstValue, secondValues.get(j))) {
                            firstValues.remove(i);
                            secondValues.remove(j);
                            i--;

                            continue outer;
                        }
                    }

                    return false;
                }

                // return true if everything was equal
                return true;
            }
        }

        return false;
    }

    private static boolean areValuesEqual(Ingredient.Value firstValue, Ingredient.Value secondValue) {
        Class<?> firstKlass = firstValue.getClass();
        Class<?> secondKlass = secondValue.getClass();

        // if values are the same type of class
        if (firstKlass == secondKlass) {
            if (firstKlass == Ingredient.ItemValue.class) {
                // if items are different, return false
                return ItemStack.matches(((Ingredient.ItemValue) firstValue).item, ((Ingredient.ItemValue) secondValue).item);
            } else if (firstKlass == Ingredient.TagValue.class) {
                // if tags are different, return false
                // identity comparison is okay because tags are always interned in vanilla
                return ((Ingredient.TagValue) firstValue).tag == ((Ingredient.TagValue) secondValue).tag;
            } else {
                var firstItems = firstValue.getItems();
                var secondItems = secondValue.getItems();
                var len = firstItems.size();

                if (len == secondItems.size()) {
                    Iterator<ItemStack> firstIter = firstItems.iterator();
                    Iterator<ItemStack> secondIter = secondItems.iterator();

                    while (firstIter.hasNext()) {
                        if (!ItemStack.matches(firstIter.next(), secondIter.next())) {
                            // if one of the items is different, return false
                            return false;
                        }
                    }
                } else {
                    // if values have different amounts of items, return false
                    return false;
                }

                // if all items are the same, return true
                return true;
            }
        } else {
            // if the values are different types, return false
            return false;
        }
    }

    public static boolean hasSieveResult(RecipeManager recipes, Item mesh, ItemStack stack) {
        for (var recipe : byType(recipes, ERecipeTypes.SIEVE.get())) {
            if (recipe.test(mesh, stack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCompostable(ItemStack stack) {
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.getRecipe(stack) != null;
    }

    public static @Nullable BarrelMixingRecipe getBarrelMixingRecipe(RecipeManager recipes, ItemStack stack, FluidStack fluid) {
        for (var recipe : byType(recipes, ERecipeTypes.BARREL_MIXING.get())) {
            if (recipe.matches(stack, fluid)) {
                return recipe;
            }
        }

        return null;
    }

    public static double getExpectedValue(NumberProvider provider) {
        if (provider instanceof ConstantValue constant) {
            return constant.value;
        } else if (provider instanceof UniformGenerator uniform) {
            return getExpectedValue(uniform.min) + getExpectedValue(uniform.max) / 2.0;
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            return getExpectedValue(binomial.n) * getExpectedValue(binomial.p);
        } else {
            // no way of knowing beforehand so just put them last
            return -1.0;
        }
    }

    public static boolean isTagEmpty(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.getTag(tag).map(set -> !set.iterator().hasNext()).orElse(PreferredOres.getPreferredOre(tag) == Items.AIR);
    }

    @Nullable
    public static BarrelFluidMixingRecipe getFluidMixingRecipe(FluidStack base, Fluid additive) {
        var recipe = barrelFluidMixingRecipeCache.getRecipe(base.getFluid(), additive);
        if (recipe != null && base.getAmount() >= recipe.baseFluidAmount) {
            return recipe;
        } else {
            return null;
        }
    }
}
