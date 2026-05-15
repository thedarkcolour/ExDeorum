package thedarkcolour.exdeorum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CreakingCoreItem extends BlockTransformingItem {
    public CreakingCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canTransformState(BlockState state) {
        return state.is(Blocks.PALE_OAK_LOG);
    }

    @Override
    protected void doTransformState(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        var newState = Blocks.CREAKING_HEART.defaultBlockState()
                .setValue(CreakingHeartBlock.AXIS, state.getValue(BlockStateProperties.AXIS))
                .setValue(CreakingHeartBlock.NATURAL, false);
        level.setBlock(pos, newState, 3);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.CREAKING_HEART.defaultBlockState()));
    }
}
