package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class CompressedSieveBlock extends SieveBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            box(0, 8, 0, 16, 14, 16),
            box(1, 0, 1, 3, 8, 3),
            box(13, 0, 1, 15, 8, 3),
            box(1, 0, 13, 3, 8, 15),
            box(13, 0, 13, 15, 8, 15)
    );

    public CompressedSieveBlock(Properties properties) {
        super(properties, EBlockEntities.COMPRESSED_SIEVE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
