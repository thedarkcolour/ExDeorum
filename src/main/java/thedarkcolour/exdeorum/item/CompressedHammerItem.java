package thedarkcolour.exdeorum.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.Set;

public class CompressedHammerItem extends HammerItem {
    private static Lazy<Set<Block>> validBlocks = Lazy.of(() -> HammerItem.computeValidBlocks(RecipeUtil.getCachedCompressedHammerRecipes()));

    public static void refreshValidBlocks() {
        validBlocks = Lazy.of(() -> HammerItem.computeValidBlocks(RecipeUtil.getCachedCompressedHammerRecipes()));
    }

    public CompressedHammerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    protected Set<Block> getValidBlocks() {
        return validBlocks.get();
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return this == EItems.COMPRESSED_WOODEN_HAMMER.get() ? 200 : 0;
    }
}
