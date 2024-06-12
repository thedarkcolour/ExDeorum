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

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.data.TranslationKeys;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomResultItem extends Item {
    public RandomResultItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            var possibilities = getPossibilities();

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            var newItem = new ItemStack(Util.getRandom(possibilities, level.random));
            player.getInventory().placeItemBackInInventory(newItem);

            return InteractionResultHolder.consume(stack.isEmpty() ? player.getItemInHand(hand) : stack);
        }
        return InteractionResultHolder.success(stack);
    }

    protected abstract List<Item> getPossibilities();

    public static class RandomSherd extends RandomResultItem {
        public RandomSherd(Properties properties) {
            super(properties);
        }

        @Override
        protected List<Item> getPossibilities() {
            var list = new ArrayList<Item>();
            for (var holder : BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.DECORATED_POT_SHERDS)) {
                list.add(holder.value());
            }
            return list;
        }
    }

    public static class RandomArmorTrim extends RandomResultItem {
        public static final List<Item> POSSIBLE_TRIMS = Lists.newArrayList(
                Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
                Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
        );

        public RandomArmorTrim(Properties properties) {
            super(properties);
        }

        @Override
        protected List<Item> getPossibilities() {
            return POSSIBLE_TRIMS;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
            tooltip.add(Component.translatable(TranslationKeys.RANDOM_TRIM_DOES_NOT_CONTAIN_UPGRADE).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
