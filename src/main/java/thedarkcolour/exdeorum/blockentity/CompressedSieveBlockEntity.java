package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.blockentity.logic.CompressedSieveLogic;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class CompressedSieveBlockEntity extends AbstractSieveBlockEntity {
    private static final float COMPRESSED_SIEVE_INTERVAl = 0.075f;

    public CompressedSieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.COMPRESSED_SIEVE.get(), pos, state, COMPRESSED_SIEVE_INTERVAl, owner -> new CompressedSieveLogic(owner, false));
    }
}
