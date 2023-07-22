package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import thedarkcolour.exnihiloreborn.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.EBlockEntity;

public abstract class AbstractCrucibleBlock extends EBlock {
    public AbstractCrucibleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return UnfiredCrucibleBlock.SHAPE;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractCrucibleBlockEntity crucible) {
            return crucible.getTank().getFluid().getFluid().getFluidType().getLightLevel();
        }
        return 0;
    }
}
