package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.EBlockEntity;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EItems;

import java.util.function.Supplier;

public abstract class AbstractCrucibleBlock extends EBlock {
    public AbstractCrucibleBlock(Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
        super(properties, blockEntityType);
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> type) {
        return !level.isClientSide && (type == EBlockEntities.WATER_CRUCIBLE.get() || type == EBlockEntities.LAVA_CRUCIBLE.get()) ? (BlockEntityTicker<T>) new AbstractCrucibleBlockEntity.Ticker() : null;
    }
}
