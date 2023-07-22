package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.exnihiloreborn.blockentity.WaterCrucibleBlockEntity;

public class WaterCrucibleBlock extends AbstractCrucibleBlock {
    public WaterCrucibleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterCrucibleBlockEntity(pos, state);
    }
}
