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

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ESounds;

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
        var grass = this.grassState.get();

        if (canSpread(state) && grass != state) {
            if (!level.isClientSide) {
                level.setBlock(pos, grass, 3);
                level.playSound(null, pos, ESounds.GRASS_SEEDS_PLACE.get(), SoundSource.BLOCKS);

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

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand pUsedHand) {
        if (stack.getItem() == EItems.MYCELIUM_SPORES.get() && target instanceof Cow cow) {
            var mushroomCow = EntityType.MOOSHROOM.create(cow.level());

            if (mushroomCow != null) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                cow.discard();
                mushroomCow.moveTo(cow.getX(), cow.getY(), cow.getZ());
                mushroomCow.setHealth(cow.getHealth());
                mushroomCow.yBodyRot = cow.yBodyRot;

                if (cow.hasCustomName()) {
                    mushroomCow.setCustomName(cow.getCustomName());
                    mushroomCow.setCustomNameVisible(cow.isCustomNameVisible());
                }
                if (cow.isPersistenceRequired()) {
                    mushroomCow.setPersistenceRequired();
                }
                mushroomCow.setInvulnerable(cow.isInvulnerable());
                cow.level().addFreshEntity(mushroomCow);

                if (!cow.level().isClientSide) {
                    ((ServerLevel)cow.level()).sendParticles(ParticleTypes.EXPLOSION, cow.getX(), cow.getY(0.5D), cow.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
                cow.playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
            }

        }

        return InteractionResult.PASS;
    }

    public boolean canSpread(BlockState state) {
        return state.is(BlockTags.DIRT);
    }
}
