package thedarkcolour.exdeorum.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public class ERecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ExDeorum.ID);

    public static final RegistryObject<RecipeType<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_TYPES.register("barrel_compost", () -> RecipeType.simple(ERecipeTypes.BARREL_COMPOST.getId()));
    public static final RegistryObject<RecipeType<BarrelMixingRecipe>> BARREL_MIXING = RECIPE_TYPES.register("barrel_mixing", () -> RecipeType.simple(ERecipeTypes.BARREL_MIXING.getId()));

    public static final RegistryObject<RecipeType<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_TYPES.register("lava_crucible", () -> RecipeType.simple(ERecipeTypes.LAVA_CRUCIBLE.getId()));
    public static final RegistryObject<RecipeType<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_TYPES.register("water_crucible", () -> RecipeType.simple(ERecipeTypes.WATER_CRUCIBLE.getId()));

    public static final RegistryObject<RecipeType<HammerRecipe>> HAMMER = RECIPE_TYPES.register("hammer", () -> RecipeType.simple(ERecipeTypes.HAMMER.getId()));

    public static final RegistryObject<RecipeType<SieveRecipe>> SIEVE = RECIPE_TYPES.register("sieve", () -> RecipeType.simple(ERecipeTypes.SIEVE.getId()));
}
