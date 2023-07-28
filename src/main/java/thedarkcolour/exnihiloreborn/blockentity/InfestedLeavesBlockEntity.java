package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

public class InfestedLeavesBlockEntity extends EBlockEntity {
    public static final float PROGRESS_INTERVAL = 0.005f;
    public static final int SPREAD_INTERVAL = 100;

    // A percentage of how much this leaf is infested
    private float progress;
    // A timer that determines when this block should try to spread
    private int spreadTimer;
    // The state this block should render as
    private BlockState mimic;

    public InfestedLeavesBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.INFESTED_LEAVES.get(), pos, state);
    }

    // Attempt to convert a leaf block within 1 block radius around this block
    private void trySpread() {
        // Get random offset
        int x = level.random.nextInt(3) - 1;
        int y = level.random.nextInt(3) - 1;
        int z = level.random.nextInt(3) - 1;

        // Get the block in the world
        BlockPos targetPos = getBlockPos().offset(x, y, z);
        BlockState state = level.getBlockState(targetPos);

        // Test block at the position
        if (state.is(BlockTags.LEAVES)) {
            // Spread and keep distance/persistent properties
            level.setBlock(targetPos, EBlocks.INFESTED_LEAVES.get().defaultBlockState()
                    .setValue(LeavesBlock.DISTANCE, state.getValue(LeavesBlock.DISTANCE))
                    .setValue(LeavesBlock.PERSISTENT, state.getValue(LeavesBlock.PERSISTENT)),
                    2);
            var te = level.getBlockEntity(targetPos);

            // Set mimic state of other block
            if (te instanceof InfestedLeavesBlockEntity leaves) {
                leaves.setMimic(state);
                leaves.setChanged();
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        // From PistonMovingBlockEntity
        @SuppressWarnings("deprecation")
        var holderLookup = this.level != null ? this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK.asLookup();
        mimic = NbtUtils.readBlockState(holderLookup, nbt.getCompound("mimic"));
        progress = nbt.getFloat("progress");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("mimic", NbtUtils.writeBlockState(mimic));
        nbt.putFloat("progress", progress);
    }

    public float getProgress() {
        return progress;
    }

    public BlockState getMimic() {
        return mimic;
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
    }

    public static class Ticker implements BlockEntityTicker<InfestedLeavesBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, InfestedLeavesBlockEntity leaves) {
            // Do progress
            if (leaves.progress < 1.0f) {
                leaves.progress = Math.min(1.0f, leaves.progress + PROGRESS_INTERVAL);
            }

            // If the leave is infested enough, advance the spread timer
            if (!level.isClientSide && leaves.progress > 0.6f) {
                ++leaves.spreadTimer;

                // Attempt to spread and reset the timer
                if (leaves.spreadTimer >= SPREAD_INTERVAL) {
                    leaves.trySpread();
                    leaves.spreadTimer = level.random.nextInt(10);
                }
            }
        }
    }
}
