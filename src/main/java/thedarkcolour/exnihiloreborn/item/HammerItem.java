package thedarkcolour.exnihiloreborn.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import thedarkcolour.exnihiloreborn.registry.EItems;

import java.util.Set;

public class HammerItem extends ToolItem {
    public static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(
            Blocks.COBBLESTONE,
            Blocks.GRAVEL,
            Blocks.SAND,
            Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.SMOOTH_SANDSTONE,
            Blocks.RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE,
            Blocks.STONE_BRICKS,
            Blocks.CRACKED_STONE_BRICKS, Blocks.STONE
    );

    public HammerItem(IItemTier tier, Properties properties) {
        super(1.0f, -2.8f, tier, EFFECTIVE_ON, properties); // set is ignored in getDestroySpeed
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return this == EItems.WOODEN_HAMMER.get() ? 200 : 0;
    }
}
