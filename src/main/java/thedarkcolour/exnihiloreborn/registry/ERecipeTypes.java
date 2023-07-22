package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.item.crafting.RecipeType;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.CompressedHammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.HammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.CompressedSieveRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.SieveRecipe;

public class ERecipeTypes {
    public static final RecipeType<BarrelCompostRecipe> BARREL_COMPOST = RecipeType.register(ExNihiloReborn.ID + ":barrel_compost");

    public static final RecipeType<CrucibleRecipe> LAVA_CRUCIBLE = RecipeType.register(ExNihiloReborn.ID + ":lava_crucible");
    public static final RecipeType<CrucibleRecipe> WATER_CRUCIBLE = RecipeType.register(ExNihiloReborn.ID + ":water_crucible");

    public static final RecipeType<HammerRecipe> HAMMER = RecipeType.register(ExNihiloReborn.ID + ":hammer");
    public static final RecipeType<CompressedHammerRecipe> COMPRESSED_HAMMER = RecipeType.register(ExNihiloReborn.ID + ":compressed_hammer");


    public static final RecipeType<SieveRecipe> SIEVE = RecipeType.register(ExNihiloReborn.ID + ":sieve");
    public static final RecipeType<CompressedSieveRecipe> COMPRESSED_SIEVE = RecipeType.register(ExNihiloReborn.ID + ":compressed_sieve");

    // Trigger classloading
    public static void init() {}
}
