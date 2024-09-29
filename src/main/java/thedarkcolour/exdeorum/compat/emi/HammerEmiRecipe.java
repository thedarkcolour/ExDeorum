package thedarkcolour.exdeorum.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;

import java.util.List;

abstract class HammerEmiRecipe extends EmiOneToOneRecipe {
    private final List<EmiStack> outputs;

    HammerEmiRecipe(HammerRecipe recipe) {
        super(recipe);

        this.outputs = EmiUtil.outputs(recipe.result);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    static class Hammer extends HammerEmiRecipe {
        Hammer(HammerRecipe recipe) {
            super(recipe);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.HAMMER;
        }
    }

    static class CompressedHammer extends HammerEmiRecipe {
        CompressedHammer(HammerRecipe recipe) {
            super(recipe);
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return ExDeorumEmiPlugin.COMPRESSED_HAMMER;
        }
    }
}
