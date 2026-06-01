package thedarkcolour.exdeorum.recipe;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
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

import java.util.Collection;
import java.util.List;

public class RecipeCaches {
    private SingleIngredientRecipeCache<BarrelCompostRecipe> barrelCompostRecipeCache;
    private SingleIngredientRecipeCache<CrucibleRecipe> lavaCrucibleRecipeCache;
    private SingleIngredientRecipeCache<CrucibleRecipe> waterCrucibleRecipeCache;
    private SingleIngredientRecipeCache<HammerRecipe> hammerRecipeCache;
    private SingleIngredientRecipeCache<CompressedHammerRecipe> compressedHammerRecipeCache;
    private SieveRecipeCache<SieveRecipe> sieveRecipeCache;
    private SieveRecipeCache<CompressedSieveRecipe> compressedSieveRecipeCache;
    private BarrelFluidMixingRecipeCache barrelFluidMixingRecipeCache;
    private FluidTransformationRecipeCache fluidTransformationRecipeCache;
    private CrookRecipeCache crookRecipeCache;
    private CrucibleHeatRecipeCache crucibleHeatRecipeCache;

    public List<SieveRecipe> getSieveRecipes(Item mesh, ItemStack item) {
        return sieveRecipeCache.getRecipe(mesh, item);
    }

    public List<CompressedSieveRecipe> getCompressedSieveRecipes(Item mesh, ItemStack item) {
        return compressedSieveRecipeCache.getRecipe(mesh, item);
    }

    @Nullable
    public CrucibleRecipe getLavaCrucibleRecipe(ItemStack item) {
        return lavaCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public CrucibleRecipe getWaterCrucibleRecipe(ItemStack item) {
        return waterCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public BarrelCompostRecipe getBarrelCompostRecipe(ItemStack item) {
        return barrelCompostRecipeCache.getRecipe(item);
    }

    @Nullable
    public HammerRecipe getHammerRecipe(Item item) {
        return hammerRecipeCache.getRecipe(item);
    }

    public Collection<RecipeHolder<HammerRecipe>> getCachedHammerRecipes() {
        return hammerRecipeCache.getAllRecipes();
    }

    @Nullable
    public CompressedHammerRecipe getCompressedHammerRecipe(Item item) {
        return compressedHammerRecipeCache.getRecipe(item);
    }

    public Collection<RecipeHolder<CompressedHammerRecipe>> getCachedCompressedHammerRecipes() {
        return compressedHammerRecipeCache.getAllRecipes();
    }

    public List<CrookRecipe> getCrookRecipes(BlockState state) {
        return crookRecipeCache.getRecipes(state);
    }

    public boolean isCompostable(ItemStack stack) {
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.getRecipe(stack) != null;
    }

    public int getHeatValue(BlockState state) {
        return crucibleHeatRecipeCache.getValue(state);
    }

    public ObjectSet<Object2IntMap.Entry<BlockState>> getHeatSources() {
        return crucibleHeatRecipeCache.getEntries();
    }

    // todo stop using the RecipeManager
    @Nullable
    public BarrelMixingRecipe getBarrelMixingRecipe(RecipeManager recipes, ItemStack stack, FluidStack fluid) {
        for (var recipe : recipes.byType(ERecipeTypes.BARREL_MIXING.get())) {
            if (recipe.value().matches(stack, fluid)) {
                return recipe.value();
            }
        }

        return null;
    }

    @Nullable
    public BarrelFluidMixingRecipe getFluidMixingRecipe(FluidStack base, Fluid additive) {
        var recipe = barrelFluidMixingRecipeCache.getRecipe(base.getFluid(), additive);
        if (recipe != null && base.getAmount() >= recipe.baseFluid().amount()) {
            return recipe;
        } else {
            return null;
        }
    }

    @Nullable
    public FluidTransformationRecipe getFluidTransformationRecipe(Fluid baseFluid, BlockState catalystState) {
        if (baseFluid != Fluids.EMPTY) {
            return fluidTransformationRecipeCache.getRecipe(baseFluid, catalystState);
        } else {
            return null;
        }
    }

    public void reload(RecipeManager recipes) {
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

    public void unload() {
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
    }
}
