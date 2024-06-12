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

package thedarkcolour.exdeorum.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.LeavesBlock;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.ESounds;

public class SilkWormItem extends Item {
    public SilkWormItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos();
        var level = context.getLevel();
        var state = level.getBlockState(pos);

        if (!state.isAir()) {
            if (state.is(BlockTags.LEAVES) && state.getBlock() != EBlocks.INFESTED_LEAVES.get()) {
                if (!level.isClientSide) {
                    // Replace with infested block
                    level.setBlock(pos, EBlocks.INFESTED_LEAVES.get().defaultBlockState()
                            .setValue(LeavesBlock.DISTANCE, state.getValue(LeavesBlock.DISTANCE))
                            .setValue(LeavesBlock.PERSISTENT, state.getValue(LeavesBlock.PERSISTENT)), 2);

                    level.playSound(null, pos, ESounds.SILK_WORM_INFEST.get(), SoundSource.BLOCKS);

                    // Set mimic
                    if (level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity leaves) {
                        leaves.setMimic(state);
                    }
                    context.getItemInHand().shrink(1);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        // play a gross noise when you discover a silk worm
        if (entity.tickCount == 1 && entity.getOwner() == null) {
            entity.level().playSound(null, entity, ESounds.SILK_WORM_DROP.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return false;
    }
}
