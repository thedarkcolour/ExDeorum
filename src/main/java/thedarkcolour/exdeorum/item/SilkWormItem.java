package thedarkcolour.exdeorum.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.LeavesBlock;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.registry.EBlocks;

public class SilkWormItem extends Item {
    public SilkWormItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos();
        var level = context.getLevel();
        var state = level.getBlockState(pos);

        if (!state.isAir()) {
            if (state.is(BlockTags.LEAVES)) {
                if (!level.isClientSide) {
                    // Replace with infested block
                    level.setBlock(pos, EBlocks.INFESTED_LEAVES.get().defaultBlockState()
                            .setValue(LeavesBlock.DISTANCE, state.getValue(LeavesBlock.DISTANCE))
                            .setValue(LeavesBlock.PERSISTENT, state.getValue(LeavesBlock.PERSISTENT)), 2);

                    // Set mimic
                    if (level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity leaves) {
                        leaves.setMimic(state);
                    }
                    context.getItemInHand().shrink(1);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}
