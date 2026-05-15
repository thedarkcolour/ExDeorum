package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import thedarkcolour.exdeorum.compat.XeiSieveRecipe;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

class CompressedSieveCategory extends SieveCategory {
    CompressedSieveCategory(IGuiHelper helper) {
        super(helper, DefaultMaterials.OAK_COMPRESSED_SIEVE, Component.translatable(TranslationKeys.COMPRESSED_SIEVE_CATEGORY_TITLE), XeiSieveRecipe.COMPRESSED_SIEVE_ROWS);
    }

    @Override
    public IRecipeType<XeiSieveRecipe> getRecipeType() {
        return ExDeorumJeiPlugin.COMPRESSED_SIEVE;
    }
}
