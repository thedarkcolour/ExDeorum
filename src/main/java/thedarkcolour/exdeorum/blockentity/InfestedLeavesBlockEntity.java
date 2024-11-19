/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EBlocks;

public class InfestedLeavesBlockEntity extends EBlockEntity {
    public static final float PROGRESS_INTERVAL = 0.005f;
    public static final int SPREAD_INTERVAL = 40;

    // A percentage of how much this leaf is infested
    private float progress;
    // A timer that determines when this block should try to spread
    private int spreadTimer;
    // The state this block should render as
    private BlockState mimic = Blocks.OAK_LEAVES.defaultBlockState();

    public InfestedLeavesBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.INFESTED_LEAVES.get(), pos, state);
    }

    @Override
    public void writeVisualData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.progress);
    }

    @Override
    public void readVisualData(FriendlyByteBuf buffer) {
        buffer.readFloat();
    }

    // Attempt to convert a leaf block within 1 block radius around this block
    private void trySpread(Level level) {
        // Get random offset
        int x = level.random.nextInt(3) - 1;
        int y = level.random.nextInt(3) - 1;
        int z = level.random.nextInt(3) - 1;

        // Get the block in the world
        BlockPos targetPos = getBlockPos().offset(x, y, z);
        BlockState state = level.getBlockState(targetPos);

        // DO NOT SPREAD TO ALREADY INFESTED LEAVES
        if (state.is(BlockTags.LEAVES) && state.getBlock() != EBlocks.INFESTED_LEAVES.get()) {
            // Spread and keep distance/persistent properties
            InfestedLeavesBlock.setBlock(level, targetPos, state);
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
        this.mimic = NbtUtils.readBlockState(holderLookup, nbt.getCompound("mimic"));
        this.progress = nbt.getFloat("progress");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        if (this.mimic == null || this.mimic.getBlock() == EBlocks.INFESTED_LEAVES.get()) {
            this.mimic = Blocks.OAK_LEAVES.defaultBlockState();
        }
        nbt.put("mimic", NbtUtils.writeBlockState(this.mimic));
        nbt.putFloat("progress", this.progress);
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public BlockState getMimic() {
        return this.mimic;
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

                if (leaves.progress == 1.0f) {
                    level.setBlock(pos, state.setValue(InfestedLeavesBlock.FULLY_INFESTED, true), 1);
                }
            }

            // If the leave is infested enough, advance the spread timer
            if (!level.isClientSide && leaves.progress > 0.6f) {
                ++leaves.spreadTimer;

                // Attempt to spread and reset the timer
                if (leaves.spreadTimer >= SPREAD_INTERVAL) {
                    leaves.trySpread(level);
                    leaves.spreadTimer = level.random.nextInt(10);
                }
            }
        }
    }
}
