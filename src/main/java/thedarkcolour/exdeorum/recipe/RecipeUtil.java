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

package thedarkcolour.exdeorum.recipe;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.*;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.item.HammerItem;
import thedarkcolour.exdeorum.loot.SummationGenerator;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.recipe.cache.*;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ENumberProviders;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.*;

public final class RecipeUtil {
    private static final int CONSTANT_TYPE = 1;
    private static final int UNIFORM_TYPE = 2;
    private static final int BINOMIAL_TYPE = 3;
    private static final int SUMMATION_TYPE = 4;
    private static final int UNKNOWN_TYPE = 99;

    private static SingleIngredientRecipeCache<BarrelCompostRecipe> barrelCompostRecipeCache;
    private static SingleIngredientRecipeCache<CrucibleRecipe> lavaCrucibleRecipeCache;
    private static SingleIngredientRecipeCache<CrucibleRecipe> waterCrucibleRecipeCache;
    private static SingleIngredientRecipeCache<HammerRecipe> hammerRecipeCache;
    private static SieveRecipeCache sieveRecipeCache;
    private static BarrelFluidMixingRecipeCache barrelFluidMixingRecipeCache;
    private static FluidTransformationRecipeCache fluidTransformationRecipeCache;
    private static CrookRecipeCache crookRecipeCache;
    private static CrucibleHeatRecipeCache crucibleHeatRecipeCache;

    public static void reload(RecipeManager recipes) {
        barrelCompostRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.BARREL_COMPOST);
        lavaCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.LAVA_CRUCIBLE);
        waterCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.WATER_CRUCIBLE);
        hammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.HAMMER);
        sieveRecipeCache = new SieveRecipeCache(recipes);
        barrelFluidMixingRecipeCache = new BarrelFluidMixingRecipeCache(recipes);
        fluidTransformationRecipeCache = new FluidTransformationRecipeCache(recipes);
        crookRecipeCache = new CrookRecipeCache(recipes);
        crucibleHeatRecipeCache = new CrucibleHeatRecipeCache(recipes);
        HammerItem.refreshValidBlocks();
    }

    public static void unload() {
        barrelCompostRecipeCache = null;
        lavaCrucibleRecipeCache = null;
        waterCrucibleRecipeCache = null;
        hammerRecipeCache = null;
        sieveRecipeCache = null;
        barrelFluidMixingRecipeCache = null;
        fluidTransformationRecipeCache = null;
        crookRecipeCache = null;
        crucibleHeatRecipeCache = null;
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

    public static void toNetworkNumberProvider(FriendlyByteBuf buffer, NumberProvider provider) {
        if (provider.getType() == NumberProviders.CONSTANT) {
            buffer.writeByte(CONSTANT_TYPE);
            buffer.writeFloat(((ConstantValue) provider).value());
        } else if (provider.getType() == NumberProviders.UNIFORM) {
            var uniform = (UniformGenerator) provider;
            buffer.writeByte(UNIFORM_TYPE);
            toNetworkNumberProvider(buffer, uniform.min());
            toNetworkNumberProvider(buffer, uniform.max());
        } else if (provider.getType() == NumberProviders.BINOMIAL) {
            var binomial = (BinomialDistributionGenerator) provider;
            buffer.writeByte(BINOMIAL_TYPE);
            toNetworkNumberProvider(buffer, binomial.n());
            toNetworkNumberProvider(buffer, binomial.p());
        } else if (provider.getType() == ENumberProviders.SUMMATION.get()) {
            var summation = (SummationGenerator) provider;
            var providers = summation.providers();
            int length = providers.size();
            buffer.writeByte(SUMMATION_TYPE);
            buffer.writeByte(length);
            for (int i = 0; i < length; i++) {
                toNetworkNumberProvider(buffer, providers.get(i));
            }
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
            case SUMMATION_TYPE -> {
                var length = buffer.readByte();
                var providers = new NumberProvider[length];
                for (int i = 0; i < length; i++) {
                    providers[i] = fromNetworkNumberProvider(buffer);
                }
                yield new SummationGenerator(List.of(providers));
            }
            default -> ConstantValue.exactly(1f);
        };
    }

    // todo support Forge's ingredient types
    public static boolean areIngredientsEqual(Ingredient first, Ingredient second) {
        // although unlikely, we should check this anyway
        if (first == second) return true;

        if (first.getClass() == Ingredient.class && second.getClass() == Ingredient.class) {
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
                return ItemStack.matches(((Ingredient.ItemValue) firstValue).item(), ((Ingredient.ItemValue) secondValue).item());
            } else if (firstKlass == Ingredient.TagValue.class) {
                // if tags are different, return false
                // identity comparison is okay because tags are always interned in vanilla
                return ((Ingredient.TagValue) firstValue).tag() == ((Ingredient.TagValue) secondValue).tag();
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

    public static boolean isCompostable(ItemStack stack) {
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.getRecipe(stack) != null;
    }

    // todo stop using the RecipeManager
    @Nullable
    public static BarrelMixingRecipe getBarrelMixingRecipe(RecipeManager recipes, ItemStack stack, FluidStack fluid) {
        for (var recipe : recipes.byType(ERecipeTypes.BARREL_MIXING.get()).values()) {
            if (recipe.value().matches(stack, fluid)) {
                return recipe.value();
            }
        }

        return null;
    }

    @Nullable
    public static BarrelFluidMixingRecipe getFluidMixingRecipe(FluidStack base, Fluid additive) {
        var recipe = barrelFluidMixingRecipeCache.getRecipe(base.getFluid(), additive);
        if (recipe != null && base.getAmount() >= recipe.baseFluidAmount()) {
            return recipe;
        } else {
            return null;
        }
    }

    @Nullable
    public static FluidTransformationRecipe getFluidTransformationRecipe(Fluid baseFluid, BlockState catalystState) {
        if (baseFluid != Fluids.EMPTY) {
            return fluidTransformationRecipeCache.getRecipe(baseFluid, catalystState);
        } else {
            return null;
        }
    }

    public static double getExpectedValue(NumberProvider provider) {
        if (provider instanceof ConstantValue constant) {
            return constant.value();
        } else if (provider instanceof UniformGenerator uniform) {
            return getExpectedValue(uniform.min()) + getExpectedValue(uniform.max()) / 2.0;
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            return getExpectedValue(binomial.n()) * getExpectedValue(binomial.p());
        } else if (provider instanceof SummationGenerator summation) {
            double avgSum = 0.0;

            for (var child : summation.providers()) {
                double expectedValue = getExpectedValue(child);

                if (expectedValue == -1.0f) {
                    return -1.0f;
                } else {
                    avgSum += expectedValue;
                }
            }

            return avgSum;
        } else {
            // no way of knowing beforehand so just put them last
            return -1.0;
        }
    }

    public static BlockPredicate readBlockPredicateNetwork(FriendlyByteBuf buffer) {
        BlockPredicate blockPredicate = BlockPredicate.fromNetwork(buffer);

        if (blockPredicate == null) {
            throw new IllegalStateException("Failed to read block predicate from network");
        }
        return blockPredicate;
    }

    public static boolean isTagEmpty(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.getTag(tag).map(set -> !set.iterator().hasNext()).orElse(PreferredOres.getPreferredOre(tag) == Items.AIR);
    }

    public static LootContext emptyLootContext(ServerLevel level) {
        return new LootContext.Builder(new LootParams(level, Map.of(), Map.of(), 0)).create(Optional.empty());
    }

    public static List<CrookRecipe> getCrookRecipes(BlockState state) {
        return crookRecipeCache.getRecipes(state);
    }

    public static int getHeatValue(BlockState state) {
        return crucibleHeatRecipeCache.getValue(state);
    }

    public static ObjectSet<Object2IntMap.Entry<BlockState>> getHeatSources() {
        return crucibleHeatRecipeCache.getEntries();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String writeBlockState(BlockState state) {
        var registryKey = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        Collection<Property> properties = (Collection<Property>) ((Collection)state.getProperties());

        if (properties.isEmpty()) {
            return registryKey.toString();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(registryKey);
            builder.append('[');
            for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext(); ) {
                var property = iterator.next();
                builder.append(property.getName());
                builder.append('=');
                builder.append(property.getName(state.getValue(property)));
                if (iterator.hasNext()) {
                    builder.append(',');
                }
            }
            builder.append(']');
            return builder.toString();
        }
    }

    public static BlockState parseBlockState(String stateString) {
        try {
            return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), stateString, false).blockState();
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Failed to parse BlockState string \"" + stateString + "\"");
        }
    }

    public static void writeTag(FriendlyByteBuf buffer, TagKey<?> ore) {
        buffer.writeResourceLocation(ore.location());
    }

    public static <T> TagKey<T> readTag(FriendlyByteBuf buffer, ResourceKey<Registry<T>> registry) {
        return TagKey.create(registry, buffer.readResourceLocation());
    }
}
