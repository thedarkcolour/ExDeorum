package thedarkcolour.exdeorum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

// reused logic between Silkworm, Sculk Core, and Creaking Core, which transform blocks they're used on
public abstract class BlockTransformingItem extends Item {
    public BlockTransformingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var state = level.getBlockState(pos);

        if (canTransformState(state)) {
            doTransformState(level, pos, state);

            if (!level.isClientSide()) {
                context.getItemInHand().shrink(1);
            }

            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.PASS;
    }

    protected abstract boolean canTransformState(BlockState state);

    protected abstract void doTransformState(Level level, BlockPos pos, BlockState state);
}
