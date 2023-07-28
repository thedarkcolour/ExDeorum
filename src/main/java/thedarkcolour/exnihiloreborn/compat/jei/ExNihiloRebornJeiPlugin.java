package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.util.Objects;

@JeiPlugin
public class ExNihiloRebornJeiPlugin implements IModPlugin {
    public static final RecipeType<BarrelCompostRecipe> BARREL_COMPOST = RecipeType.create(ExNihiloReborn.ID, "barrel_compost", BarrelCompostRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExNihiloReborn.ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BarrelCompostCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(EItems.OAK_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.SPRUCE_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.BIRCH_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.JUNGLE_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.ACACIA_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.DARK_OAK_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.MANGROVE_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.CHERRY_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.BAMBOO_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.CRIMSON_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.WARPED_BARREL.get()), BARREL_COMPOST);
        registration.addRecipeCatalyst(new ItemStack(EItems.STONE_BARREL.get()), BARREL_COMPOST);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(EItems.SILK_WORM.get()), VanillaTypes.ITEM_STACK, Component.translatable(TranslationKeys.SILK_WORM_JEI_INFO));
        registration.addRecipes(BARREL_COMPOST, Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor(ERecipeTypes.BARREL_COMPOST.get()));
    }
}
