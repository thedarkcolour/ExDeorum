package thedarkcolour.exnihiloreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.WaterCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;

public class WaterCrucibleBlock extends AbstractCrucibleBlock {
    public WaterCrucibleBlock(Properties properties) {
        super(properties, EBlockEntities.WATER_CRUCIBLE);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterCrucibleBlockEntity(pos, state);
    }
}
