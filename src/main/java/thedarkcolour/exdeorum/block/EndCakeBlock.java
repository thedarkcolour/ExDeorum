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

package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import thedarkcolour.exdeorum.tag.EItemTags;

public class EndCakeBlock extends CakeBlock {
    public EndCakeBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(BITES, 6));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(EItemTags.END_CAKE_MATERIAL)) {
            int bites = state.getValue(BITES);

            if (bites == 0) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                if (!level.isClientSide()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    level.setBlock(pos, state.setValue(BITES, bites - 1), 3);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            int bites = state.getValue(BITES);

            if (!level.isClientSide()) {
                if (tryTeleport((ServerLevel) level, player)) {
                    player.awardStat(Stats.EAT_CAKE_SLICE);
                    player.getFoodData().eat(2, 0.1f);
                    level.gameEvent(player, GameEvent.EAT, pos);

                    if (bites < 6) {
                        level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
                    } else {
                        level.removeBlock(pos, false);
                        level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    // todo test
    private static boolean tryTeleport(ServerLevel level, Player player) {
        if (level.dimension() != Level.END) {
            var endLevel = level.getServer().getLevel(Level.END);

            if (endLevel != null) {
                if (player.canChangeDimensions(level, endLevel)) {
                    player.changeDimension(((EndPortalBlock) Blocks.END_PORTAL).getPortalDestination(level, player, player.getOnPos()));
                    return true;
                }
            }
        }

        return false;
    }
}
