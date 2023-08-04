package thedarkcolour.exdeorum.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

public class ERecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExDeorum.ID);

    public static final RegistryObject<RecipeSerializer<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_SERIALIZERS.register("barrel_compost", BarrelCompostRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BarrelMixingRecipe>> BARREL_MIXING = RECIPE_SERIALIZERS.register("barrel_mixing", BarrelMixingRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<HammerRecipe>> HAMMER = RECIPE_SERIALIZERS.register("hammer", HammerRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_SERIALIZERS.register("lava_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.LAVA_CRUCIBLE.get()));
    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_SERIALIZERS.register("water_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.WATER_CRUCIBLE.get()));

    public static final RegistryObject<RecipeSerializer<SieveRecipe>> SIEVE = RECIPE_SERIALIZERS.register("sieve", SieveRecipe.Serializer::new);
}
