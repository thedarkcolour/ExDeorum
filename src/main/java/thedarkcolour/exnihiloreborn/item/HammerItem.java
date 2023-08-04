package thedarkcolour.exnihiloreborn.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.util.Set;

public class HammerItem extends DiggerItem {
    public static final Set<Block> VALID_BLOCKS = new ObjectOpenHashSet<>();

    public HammerItem(Tier tier, Properties properties) {
        super(1.0f, -2.8f, tier, null, properties);
    }

    public static void refreshValidBlocks(RecipeManager recipes) {
        VALID_BLOCKS.clear();
        for (var recipe : recipes.byType(ERecipeTypes.HAMMER.get()).values()) {
            for (var item : recipe.getIngredient().getItems()) {
                if (item.getItem() instanceof BlockItem blockItem) {
                    VALID_BLOCKS.add(blockItem.getBlock());
                }
            }
        }
    }

    protected Set<Block> getValidBlocks() {
        return VALID_BLOCKS;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return getValidBlocks().contains(state.getBlock()) ? this.speed : 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (net.minecraftforge.common.TierSortingRegistry.isTierSorted(getTier())) {
            return net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state) && getValidBlocks().contains(state.getBlock());
        }
        int i = this.getTier().getLevel();
        if (i < 3 && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !state.is(BlockTags.NEEDS_STONE_TOOL)) && getValidBlocks().contains(state.getBlock());
        }
    }

    // FORGE START
    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return getValidBlocks().contains(state.getBlock()) && net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return this == EItems.WOODEN_HAMMER.get() ? 200 : 0;
    }
}
