package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import thedarkcolour.exdeorum.compat.GroupedSieveRecipe;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

class CompressedSieveCategory extends SieveCategory {
    CompressedSieveCategory(IGuiHelper helper) {
        super(helper, DefaultMaterials.OAK_COMPRESSED_SIEVE, Component.translatable(TranslationKeys.COMPRESSED_SIEVE_CATEGORY_TITLE));
    }

    @Override
    public RecipeType<GroupedSieveRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.COMPRESSED_SIEVE;
    }
}
