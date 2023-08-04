package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exnihiloreborn.blockentity.SieveBlockEntity;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;

public class SieveBlock extends EBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            box(0, 11, 0, 16, 16, 16),
            box(1, 0, 1, 2, 11, 2),
            box(14, 0, 1, 15, 11, 2),
            box(1, 0, 14, 2, 11, 15),
            box(14, 0, 14, 15, 11, 15)
    );

    public SieveBlock(Properties properties) {
        super(properties, EBlockEntities.SIEVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if (!level.isClientSide) {
            if (!state.is(newState.getBlock())) {
                if (level.getBlockEntity(pos) instanceof SieveBlockEntity sieve) {
                    var mesh = sieve.getMesh();

                    if (!mesh.isEmpty()) {
                        dropItem(level, pos, mesh);
                    }
                }
            }
        }

        super.onRemove(state, level, pos, newState, pIsMoving);
    }
}
