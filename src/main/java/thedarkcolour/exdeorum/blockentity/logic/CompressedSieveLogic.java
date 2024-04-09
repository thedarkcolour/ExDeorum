package thedarkcolour.exdeorum.blockentity.logic;

import net.minecraft.world.item.ItemStack;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

import java.util.List;

public class CompressedSieveLogic extends SieveLogic {
    public CompressedSieveLogic(Owner owner, boolean mechanical) {
        super(owner, mechanical);
    }

    @Override
    protected List<? extends SieveRecipe> getDropsFor(ItemStack contents) {
        return RecipeUtil.getCompressedSieveRecipes(this.mesh.getItem(), contents);
    }
}
