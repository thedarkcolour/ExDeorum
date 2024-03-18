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

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import thedarkcolour.exdeorum.registry.EItems;

// Silk worms have a 1 in 100 chance to drop from regular leaves, 1 in 15 if the block is infested.
// Infested leaves have a 1 in 4 * progress to drop 1 string
// Infested leaves have a 1 in 16 * progress to drop another string
public class CrookItem extends Item {
    private final float speed;

    public CrookItem(Properties properties, float speed) {
        super(properties);
        this.speed = speed;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(BlockTags.LEAVES) ? this.speed : 1.0f;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity living) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, living, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_FORTUNE || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.BLOCK_EFFICIENCY;
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack material) {
        if (this == EItems.BONE_CROOK.get()) {
            return material.is(Tags.Items.BONES);
        } else {
            return material.is(ItemTags.PLANKS);
        }
    }

    // Pulls the entity towards the player like in a cartoon
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity living, InteractionHand hand) {
        var difference = playerIn.position().subtract(living.position());
        var distance = difference.horizontalDistance();

        var scalarX = difference.x / distance;
        var scalarZ = difference.z / distance;

        var dx = scalarX * 1.5;
        var dz = scalarZ * 1.5;

        living.setDeltaMovement(living.getDeltaMovement().add(dx, 0.0, dz));

        return InteractionResult.SUCCESS;
    }
}
