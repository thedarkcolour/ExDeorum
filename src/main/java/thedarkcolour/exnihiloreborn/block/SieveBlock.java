package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exnihiloreborn.blockentity.EBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.SieveBlockEntity;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;

public class SieveBlock extends EBlock {
    public SieveBlock(Properties properties) {
        super(properties, EBlockEntities.SIEVE);
    }

    // todo
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }
}
