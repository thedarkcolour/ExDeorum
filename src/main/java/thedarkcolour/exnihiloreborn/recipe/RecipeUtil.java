package thedarkcolour.exnihiloreborn.recipe;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.SieveRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class RecipeUtil {
    private static final int CONSTANT_TYPE = 1;
    private static final int UNIFORM_TYPE = 2;
    private static final int BINOMIAL_TYPE = 3;
    private static final int UNKNOWN_TYPE = 99;

    private static final Cache<SieveCacheKey, ImmutableList<SieveRecipe>> SIEVE_RECIPE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(3)).build();
    private static Lazy<Map<Item, BarrelCompostRecipe>> barrelCompostRecipeCache;
    private static Lazy<Map<Item, CrucibleRecipe>> lavaCrucibleRecipeCache;
    private static Lazy<Map<Item, CrucibleRecipe>> waterCrucibleRecipeCache;

    public static void reload(RecipeManager recipes) {
        SIEVE_RECIPE_CACHE.invalidateAll();

        barrelCompostRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.BARREL_COMPOST));
        lavaCrucibleRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.LAVA_CRUCIBLE));
        waterCrucibleRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.WATER_CRUCIBLE));
    }

    private static <T extends SingleIngredientRecipe> ImmutableMap<Item, T> loadSimpleRecipeCache(RecipeManager recipes, Supplier<RecipeType<T>> recipeType) {
        var builder = new ImmutableMap.Builder<Item, T>();
        for (var recipe : recipes.byType(recipeType.get()).values()) {
            for (var item : recipe.getIngredient().getItems()) {
                builder.put(item.getItem(), recipe);
            }
        }

        return builder.buildKeepingLast();
    }

    public static List<SieveRecipe> getSieveRecipes(RecipeManager manager, Item mesh, ItemStack item) {
        var cacheKey = new SieveCacheKey(mesh, item.getItem());
        var cacheVal = SIEVE_RECIPE_CACHE.getIfPresent(cacheKey);
        if (cacheVal != null) return cacheVal;

        var builder = new ImmutableList.Builder<SieveRecipe>();
        var cache = true;

        for (var recipe : byType(manager, ERecipeTypes.SIEVE.get())) {
            if (recipe.test(mesh, item)) {
                builder.add(recipe);

                if (recipe.dependsOnNbt) {
                    cache = false;
                }
            }
        }

        var recipes = builder.build();
        if (cache) {
            SIEVE_RECIPE_CACHE.put(cacheKey, recipes);
        }

        return recipes;
    }

    public static CrucibleRecipe getLavaCrucibleRecipe(ItemStack item) {
        return lavaCrucibleRecipeCache.get().get(item.getItem());
    }

    public static CrucibleRecipe getWaterCrucibleRecipe(ItemStack item) {
        return waterCrucibleRecipeCache.get().get(item.getItem());
    }

    public static BarrelCompostRecipe getBarrelCompostRecipe(ItemStack playerItem) {
        return barrelCompostRecipeCache.get().get(playerItem.getItem());
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
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, key)));
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
            case UNIFORM_TYPE -> new UniformGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            case BINOMIAL_TYPE -> new BinomialDistributionGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            default -> ConstantValue.exactly(1f);
        };
    }

    public static boolean areIngredientsEqual(Ingredient first, Ingredient second) {
        // although unlikely, we should check this anyway
        if (first == second) return true;

        if (first.isVanilla() && second.isVanilla()) {
            Ingredient.Value[] firstValues = first.values;
            Ingredient.Value[] secondValues = second.values;

            // if arrays are same size, check if their contents are equal (order matters)
            if (firstValues.length == secondValues.length) {
                for (int i = 0; i < firstValues.length; i++) {
                    Ingredient.Value firstValue = firstValues[i];
                    Ingredient.Value secondValue = secondValues[i];
                    Class<?> firstKlass = firstValue.getClass();
                    Class<?> secondKlass = secondValue.getClass();

                    // if values are the same type of class
                    if (firstKlass == secondKlass) {
                        if (firstKlass == Ingredient.ItemValue.class) {
                            // if items are different, return false
                            if (!ItemStack.matches(((Ingredient.ItemValue) firstValue).item, ((Ingredient.ItemValue) secondValue).item)) {
                                return false;
                            }
                        } else if (firstKlass == Ingredient.TagValue.class) {
                            // if tags are different, return false
                            // identity comparison is okay because tags are always interned in vanilla
                            if (((Ingredient.TagValue) firstValue).tag != ((Ingredient.TagValue) secondValue).tag) {
                                return false;
                            }
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
                        }
                    } else {
                        // if the values are different types, return false
                        return false;
                    }
                }

                // return true if everything was equal
                return true;
            }
        }

        return false;
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
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.get().containsKey(stack.getItem());
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

    private record SieveCacheKey(Item mesh, Item ingredient) {
    }
}
