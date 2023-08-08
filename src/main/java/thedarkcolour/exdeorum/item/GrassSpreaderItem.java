/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GrassSpreaderItem extends Item {
    private final Supplier<BlockState> grassState;

    public GrassSpreaderItem(Properties properties, Supplier<BlockState> grassState) {
        super(properties);
        this.grassState = grassState;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        var level = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var player = ctx.getPlayer();
        var state = level.getBlockState(pos);
        var grass = grassState.get();

        if (canSpread(state) && grass != state) {
            if (!level.isClientSide) {
                level.setBlock(pos, grass, 3);
                level.playSound(null, pos, SoundEvents.ROOTED_DIRT_PLACE, SoundSource.BLOCKS);

                if (player == null || !player.getAbilities().instabuild) {
                    ctx.getItemInHand().shrink(1);
                }

                return InteractionResult.CONSUME;
            } else {
                level.addDestroyBlockEffect(pos, grass);
            }


            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public boolean canSpread(BlockState state) {
        return state.is(BlockTags.DIRT);
    }
}
