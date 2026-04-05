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
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.*;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.loot.SummationGenerator;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.recipe.cache.*;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.CompressedHammerRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.CompressedSieveRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
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
    private static SingleIngredientRecipeCache<CompressedHammerRecipe> compressedHammerRecipeCache;
    private static SieveRecipeCache<SieveRecipe> sieveRecipeCache;
    private static SieveRecipeCache<CompressedSieveRecipe> compressedSieveRecipeCache;
    private static BarrelFluidMixingRecipeCache barrelFluidMixingRecipeCache;
    private static FluidTransformationRecipeCache fluidTransformationRecipeCache;
    private static CrookRecipeCache crookRecipeCache;
    private static CrucibleHeatRecipeCache crucibleHeatRecipeCache;
    private static List<BarrelMixingRecipe> barrelMixingRecipes;
    private static RecipeMap currentRecipeMap;

    public static void reload(RecipeMap recipes) {
        currentRecipeMap = recipes;
        barrelMixingRecipes = recipes.byType(ERecipeTypes.BARREL_MIXING.get()).stream().map(RecipeHolder::value).toList();
        barrelCompostRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.BARREL_COMPOST);
        lavaCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.LAVA_CRUCIBLE);
        waterCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.WATER_CRUCIBLE);
        hammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.HAMMER).trackAllRecipes();
        compressedHammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.COMPRESSED_HAMMER).trackAllRecipes();
        sieveRecipeCache = new SieveRecipeCache<>(recipes, ERecipeTypes.SIEVE);
        compressedSieveRecipeCache = new SieveRecipeCache<>(recipes, ERecipeTypes.COMPRESSED_SIEVE);
        barrelFluidMixingRecipeCache = new BarrelFluidMixingRecipeCache(recipes);
        fluidTransformationRecipeCache = new FluidTransformationRecipeCache(recipes);
        crookRecipeCache = new CrookRecipeCache(recipes);
        crucibleHeatRecipeCache = new CrucibleHeatRecipeCache(recipes);
    }

    public static void unload() {
        barrelCompostRecipeCache = null;
        lavaCrucibleRecipeCache = null;
        waterCrucibleRecipeCache = null;
        hammerRecipeCache = null;
        compressedHammerRecipeCache = null;
        sieveRecipeCache = null;
        compressedSieveRecipeCache = null;
        barrelFluidMixingRecipeCache = null;
        fluidTransformationRecipeCache = null;
        crookRecipeCache = null;
        crucibleHeatRecipeCache = null;
        barrelMixingRecipes = null;
        currentRecipeMap = null;
    }

    public static List<SieveRecipe> getSieveRecipes(Item mesh, ItemStack item) {
        return sieveRecipeCache.getRecipe(mesh, item);
    }

    public static List<CompressedSieveRecipe> getCompressedSieveRecipes(Item mesh, ItemStack item) {
        return compressedSieveRecipeCache.getRecipe(mesh, item);
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

    public static Collection<RecipeHolder<HammerRecipe>> getCachedHammerRecipes() {
        return hammerRecipeCache.getAllRecipes();
    }

    @Nullable
    public static CompressedHammerRecipe getCompressedHammerRecipe(Item item) {
        return compressedHammerRecipeCache.getRecipe(item);
    }

    public static Collection<RecipeHolder<CompressedHammerRecipe>> getCachedCompressedHammerRecipes() {
        return compressedHammerRecipeCache.getAllRecipes();
    }

    public static void toNetworkNumberProvider(FriendlyByteBuf buffer, NumberProvider provider) {
        if (provider instanceof ConstantValue constant) {
            buffer.writeByte(CONSTANT_TYPE);
            buffer.writeFloat(constant.value());
        } else if (provider instanceof UniformGenerator uniform) {
            buffer.writeByte(UNIFORM_TYPE);
            toNetworkNumberProvider(buffer, uniform.min());
            toNetworkNumberProvider(buffer, uniform.max());
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            buffer.writeByte(BINOMIAL_TYPE);
            toNetworkNumberProvider(buffer, binomial.n());
            toNetworkNumberProvider(buffer, binomial.p());
        } else if (provider instanceof SummationGenerator summation) {
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

    public static boolean areIngredientsEqual(Ingredient first, Ingredient second) {
        if (first == second) return true;
        return first.equals(second);
    }

    public static boolean isCompostable(ItemStack stack) {
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.getRecipe(stack) != null;
    }

    @Nullable
    public static BarrelMixingRecipe getBarrelMixingRecipe(ItemStack stack, FluidStack fluid) {
        if (barrelMixingRecipes == null) return null;
        for (var recipe : barrelMixingRecipes) {
            if (recipe.matches(stack, fluid)) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static BarrelFluidMixingRecipe getFluidMixingRecipe(FluidStack base, Fluid additive) {
        var recipe = barrelFluidMixingRecipeCache.getRecipe(base.getFluid(), additive);
        if (recipe != null && base.getAmount() >= recipe.baseFluid().amount()) {
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

    @SuppressWarnings("IfCanBeSwitch")
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

    public static boolean isTagEmpty(TagKey<Item> tag) {
        return !BuiltInRegistries.ITEM.getTagOrEmpty(tag).iterator().hasNext();
    }

    public static LootContext emptyLootContext(ServerLevel level) {
        return new LootContext.Builder(new LootParams(level, ContextMap.EMPTY, Map.of(), 0f)).create(Optional.empty());
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
            return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK, stateString, false).blockState();
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Failed to parse BlockState string \"" + stateString + "\"");
        }
    }

    public static void writeTag(FriendlyByteBuf buffer, TagKey<?> ore) {
        buffer.writeIdentifier(ore.location());
    }

    public static <T> TagKey<T> readTag(FriendlyByteBuf buffer, ResourceKey<Registry<T>> registry) {
        return TagKey.create(registry, buffer.readIdentifier());
    }

    public static boolean isValidResourceLocation(String string) {
        return Identifier.tryParse(string) != null;
    }

    /**
     * @return The global recipe map. {@code null} if recipes have not been loaded yet.
     */
    @Nullable
    public static RecipeMap getRecipeMap() {
        return currentRecipeMap;
    }
}
