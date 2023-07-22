package thedarkcolour.exnihiloreborn.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.exnihiloreborn.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

public class SilkWormItem extends Item {
    public SilkWormItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        BlockState state = level.getBlockState(pos);

        if (!state.isAir()) {
            if (state.is(BlockTags.LEAVES)) {
                // Replace with infested block
                level.setBlock(pos, EBlocks.INFESTED_LEAVES.get().defaultBlockState()
                        .setValue(LeavesBlock.DISTANCE, state.getValue(LeavesBlock.DISTANCE))
                        .setValue(LeavesBlock.PERSISTENT, state.getValue(LeavesBlock.PERSISTENT)), 2);

                // Set mimic
                TileEntity te = level.getBlockEntity(pos);
                if (te instanceof InfestedLeavesBlockEntity) {
                    ((InfestedLeavesBlockEntity) te).setMimic(state);
                } else {
                    return ActionResultType.FAIL;
                }

                // Decrease item
                context.getItemInHand().shrink(1);

                return ActionResultType.sidedSuccess(level.isClientSide);
            }
        }

        return ActionResultType.FAIL;
    }
}
