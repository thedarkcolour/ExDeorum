package thedarkcolour.exdeorum.blockentity.logic;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;

import java.util.List;

public class CompressedSieveLogic extends SieveLogic {
    public CompressedSieveLogic(Owner owner, boolean mechanical) {
        super(owner, mechanical);
    }

    @Override
    protected List<? extends RecipeHolder<? extends SieveRecipe>> getDropsFor(ItemStack contents) {
        return this.owner.getRecipeCaches().getCompressedSieveRecipes(this.mesh.getItem(), contents);
    }
}
