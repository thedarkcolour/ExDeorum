package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.HammerRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@JeiPlugin
public class ExNihiloRebornJeiPlugin implements IModPlugin {
    public static final ResourceLocation ENH_JEI_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "textures/gui/jei/enr_jei.png");

    public static final RecipeType<BarrelCompostRecipe> BARREL_COMPOST = RecipeType.create(ExNihiloReborn.ID, "barrel_compost", BarrelCompostRecipe.class);
    public static final RecipeType<?> BARREL_MIXING = RecipeType.create(ExNihiloReborn.ID, "barrel_compost", Object.class);
    public static final RecipeType<CrucibleRecipe> LAVA_CRUCIBLE = RecipeType.create(ExNihiloReborn.ID, "lava_crucible", CrucibleRecipe.class);
    public static final RecipeType<CrucibleRecipe> WATER_CRUCIBLE = RecipeType.create(ExNihiloReborn.ID, "water_crucible", CrucibleRecipe.class);
    public static final RecipeType<JeiSieveRecipeGroup> SIEVE = RecipeType.create(ExNihiloReborn.ID, "sieve", JeiSieveRecipeGroup.class);
    public static final RecipeType<HammerRecipe> HAMMER = RecipeType.create(ExNihiloReborn.ID, "hammer", HammerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExNihiloReborn.ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        IDrawable arrow = helper.createDrawable(ExNihiloRebornJeiPlugin.ENH_JEI_TEXTURE, 0, 18, 22, 15);

        registration.addRecipeCategories(new BarrelCompostCategory(helper));
        registration.addRecipeCategories(new CrucibleCategory.LavaCrucible(helper, arrow));
        registration.addRecipeCategories(new CrucibleCategory.WaterCrucible(helper, arrow));
        registration.addRecipeCategories(new SieveCategory(helper));
        registration.addRecipeCategories(new HammerCategory(helper, arrow));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var barrels = new ItemStack[]{
                new ItemStack(EItems.OAK_BARREL.get()),
                new ItemStack(EItems.SPRUCE_BARREL.get()),
                new ItemStack(EItems.BIRCH_BARREL.get()),
                new ItemStack(EItems.JUNGLE_BARREL.get()),
                new ItemStack(EItems.ACACIA_BARREL.get()),
                new ItemStack(EItems.DARK_OAK_BARREL.get()),
                new ItemStack(EItems.MANGROVE_BARREL.get()),
                new ItemStack(EItems.CHERRY_BARREL.get()),
                new ItemStack(EItems.BAMBOO_BARREL.get()),
                new ItemStack(EItems.CRIMSON_BARREL.get()),
                new ItemStack(EItems.WARPED_BARREL.get()),
                new ItemStack(EItems.STONE_BARREL.get()),
        };
        for (var barrel : barrels) {
            registration.addRecipeCatalyst(barrel, BARREL_COMPOST);
            registration.addRecipeCatalyst(barrel, BARREL_MIXING);
        }

        registration.addRecipeCatalyst(new ItemStack(EItems.PORCELAIN_CRUCIBLE.get()), LAVA_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.WARPED_CRUCIBLE.get()), LAVA_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CRIMSON_CRUCIBLE.get()), LAVA_CRUCIBLE);

        registration.addRecipeCatalyst(new ItemStack(EItems.OAK_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.SPRUCE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BIRCH_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.JUNGLE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.ACACIA_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.DARK_OAK_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.MANGROVE_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CHERRY_CRUCIBLE.get()), WATER_CRUCIBLE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BAMBOO_CRUCIBLE.get()), WATER_CRUCIBLE);

        registration.addRecipeCatalyst(new ItemStack(EItems.OAK_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.SPRUCE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BIRCH_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.JUNGLE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.ACACIA_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.DARK_OAK_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.MANGROVE_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CHERRY_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.BAMBOO_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.CRIMSON_SIEVE.get()), SIEVE);
        registration.addRecipeCatalyst(new ItemStack(EItems.WARPED_SIEVE.get()), SIEVE);

        registration.addRecipeCatalyst(new ItemStack(EItems.WOODEN_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.STONE_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.GOLDEN_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.IRON_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.DIAMOND_HAMMER.get()), HAMMER);
        registration.addRecipeCatalyst(new ItemStack(EItems.NETHERITE_HAMMER.get()), HAMMER);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(EItems.SILK_WORM.get()), VanillaTypes.ITEM_STACK, Component.translatable(TranslationKeys.SILK_WORM_JEI_INFO));
        registration.addIngredientInfo(List.of(new ItemStack(EBlocks.OAK_SIEVE.get()), new ItemStack(EBlocks.SPRUCE_SIEVE.get()), new ItemStack(EBlocks.BIRCH_SIEVE.get()), new ItemStack(EBlocks.JUNGLE_SIEVE.get()), new ItemStack(EBlocks.ACACIA_SIEVE.get()), new ItemStack(EBlocks.DARK_OAK_SIEVE.get()), new ItemStack(EBlocks.MANGROVE_SIEVE.get()), new ItemStack(EBlocks.CHERRY_SIEVE.get()), new ItemStack(EBlocks.BAMBOO_SIEVE.get()), new ItemStack(EBlocks.CRIMSON_SIEVE.get()), new ItemStack(EBlocks.WARPED_SIEVE.get())), VanillaTypes.ITEM_STACK, Component.translatable(TranslationKeys.SIEVE_JEI_INFO));
        addRecipes(registration, BARREL_COMPOST, ERecipeTypes.BARREL_COMPOST);
        addRecipes(registration, LAVA_CRUCIBLE, ERecipeTypes.LAVA_CRUCIBLE);
        addRecipes(registration, WATER_CRUCIBLE, ERecipeTypes.WATER_CRUCIBLE);
        addRecipes(registration, HAMMER, ERecipeTypes.HAMMER);

        JeiSieveRecipeGroup.addRecipes(registration, SIEVE);
    }

    private static <C extends Container, T extends Recipe<C>> void addRecipes(IRecipeRegistration registration, RecipeType<T> category, Supplier<net.minecraft.world.item.crafting.RecipeType<T>> type) {
        registration.addRecipes(category, Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor(type.get()));
    }
}
