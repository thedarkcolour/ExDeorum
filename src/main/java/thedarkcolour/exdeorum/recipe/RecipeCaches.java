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
        return this.sieveRecipeCache.getRecipe(mesh, item);
    }

    public List<CompressedSieveRecipe> getCompressedSieveRecipes(Item mesh, ItemStack item) {
        return this.compressedSieveRecipeCache.getRecipe(mesh, item);
    }

    @Nullable
    public CrucibleRecipe getLavaCrucibleRecipe(ItemStack item) {
        return this.lavaCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public CrucibleRecipe getWaterCrucibleRecipe(ItemStack item) {
        return this.waterCrucibleRecipeCache.getRecipe(item);
    }

    @Nullable
    public BarrelCompostRecipe getBarrelCompostRecipe(ItemStack item) {
        return this.barrelCompostRecipeCache.getRecipe(item);
    }

    @Nullable
    public HammerRecipe getHammerRecipe(Item item) {
        return this.hammerRecipeCache.getRecipe(item);
    }

    public Collection<RecipeHolder<HammerRecipe>> getCachedHammerRecipes() {
        return this.hammerRecipeCache.getAllRecipes();
    }

    @Nullable
    public CompressedHammerRecipe getCompressedHammerRecipe(Item item) {
        return this.compressedHammerRecipeCache.getRecipe(item);
    }

    public Collection<RecipeHolder<CompressedHammerRecipe>> getCachedCompressedHammerRecipes() {
        return this.compressedHammerRecipeCache.getAllRecipes();
    }

    public List<CrookRecipe> getCrookRecipes(BlockState state) {
        return this.crookRecipeCache.getRecipes(state);
    }

    public boolean isCompostable(ItemStack stack) {
        return this.barrelCompostRecipeCache != null && this.barrelCompostRecipeCache.getRecipe(stack) != null;
    }

    public int getHeatValue(BlockState state) {
        return this.crucibleHeatRecipeCache.getValue(state);
    }

    public ObjectSet<Object2IntMap.Entry<BlockState>> getHeatSources() {
        return this.crucibleHeatRecipeCache.getEntries();
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
        var recipe = this.barrelFluidMixingRecipeCache.getRecipe(base.getFluid(), additive);
        if (recipe != null && base.getAmount() >= recipe.baseFluid().amount()) {
            return recipe;
        } else {
            return null;
        }
    }

    @Nullable
    public FluidTransformationRecipe getFluidTransformationRecipe(Fluid baseFluid, BlockState catalystState) {
        if (baseFluid != Fluids.EMPTY) {
            return this.fluidTransformationRecipeCache.getRecipe(baseFluid, catalystState);
        } else {
            return null;
        }
    }

    public void reload(RecipeManager recipes) {
        this.barrelCompostRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.BARREL_COMPOST);
        this.lavaCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.LAVA_CRUCIBLE);
        this.waterCrucibleRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.WATER_CRUCIBLE);
        this.hammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.HAMMER).trackAllRecipes();
        this.compressedHammerRecipeCache = new SingleIngredientRecipeCache<>(recipes, ERecipeTypes.COMPRESSED_HAMMER).trackAllRecipes();
        this.sieveRecipeCache = new SieveRecipeCache<>(recipes, ERecipeTypes.SIEVE);
        this.compressedSieveRecipeCache = new SieveRecipeCache<>(recipes, ERecipeTypes.COMPRESSED_SIEVE);
        this.barrelFluidMixingRecipeCache = new BarrelFluidMixingRecipeCache(recipes);
        this.fluidTransformationRecipeCache = new FluidTransformationRecipeCache(recipes);
        this.crookRecipeCache = new CrookRecipeCache(recipes);
        this.crucibleHeatRecipeCache = new CrucibleHeatRecipeCache(recipes);
    }

    public void unload() {
        this.barrelCompostRecipeCache = null;
        this.lavaCrucibleRecipeCache = null;
        this.waterCrucibleRecipeCache = null;
        this.hammerRecipeCache = null;
        this.compressedHammerRecipeCache = null;
        this.sieveRecipeCache = null;
        this.compressedSieveRecipeCache = null;
        this.barrelFluidMixingRecipeCache = null;
        this.fluidTransformationRecipeCache = null;
        this.crookRecipeCache = null;
        this.crucibleHeatRecipeCache = null;
    }
}
