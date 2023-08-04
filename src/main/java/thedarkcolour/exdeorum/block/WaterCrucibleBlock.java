package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.exdeorum.blockentity.WaterCrucibleBlockEntity;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class WaterCrucibleBlock extends AbstractCrucibleBlock {
    public WaterCrucibleBlock(Properties properties) {
        super(properties, EBlockEntities.WATER_CRUCIBLE);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterCrucibleBlockEntity(pos, state);
    }
}
