package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
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

    @Override
    public void tick() {
        // Do progress
        if (progress < 1.0f) {
            progress = Math.min(1.0f, progress + PROGRESS_INTERVAL);
        }

        // If the leave is infested enough, advance the spread timer
        if (!level.isClientSide && progress > 0.6f) {
            ++spreadTimer;

            // Attempt to spread and reset the timer
            if (spreadTimer >= SPREAD_INTERVAL) {
                trySpread();
                spreadTimer = level.random.nextInt(10);
            }
        }
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
            TileEntity te = level.getBlockEntity(targetPos);

            // Set mimic state of other block
            if (te instanceof InfestedLeavesBlockEntity) {
                ((InfestedLeavesBlockEntity) te).setMimic(state);
                te.setChanged();
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        mimic = NBTUtil.readBlockState(nbt.getCompound("Mimic"));
        progress = nbt.getFloat("Progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("Mimic", NBTUtil.writeBlockState(mimic));
        nbt.putFloat("Progress", progress);
        return super.save(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        // Used in getUpdatePacket
        return save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Sends a packet with updated NBT whenever setChanged is called
        return new SUpdateTileEntityPacket(getBlockPos(), 244, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        // load properties from server's NBT
        load(null, pkt.getTag());
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
}
