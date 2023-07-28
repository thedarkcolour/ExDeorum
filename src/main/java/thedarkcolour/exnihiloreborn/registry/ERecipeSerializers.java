package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.HammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.SieveRecipe;

public class ERecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExNihiloReborn.ID);

    public static final RegistryObject<RecipeSerializer<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_SERIALIZERS.register("barrel_compost", BarrelCompostRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<HammerRecipe>> HAMMER = RECIPE_SERIALIZERS.register("hammer", () -> new RewardRecipe.Serializer<>(HammerRecipe::new));

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_SERIALIZERS.register("lava_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.LAVA_CRUCIBLE.get()));
    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_SERIALIZERS.register("water_crucible", () -> new CrucibleRecipe.Serializer(ERecipeTypes.WATER_CRUCIBLE.get()));

    public static final RegistryObject<RecipeSerializer<SieveRecipe>> SIEVE = RECIPE_SERIALIZERS.register("sieve", () -> new SieveRecipe.Serializer<>(SieveRecipe::new));
}
