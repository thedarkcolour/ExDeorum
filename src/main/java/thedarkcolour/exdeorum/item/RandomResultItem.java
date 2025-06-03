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

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

public class RandomResultItem extends Item {
    private final TagKey<Item> possibilities;

    public RandomResultItem(Properties properties, TagKey<Item> possibilities) {
        super(properties);

        this.possibilities = possibilities;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            var possibleResults = new ArrayList<Item>();
            for (var holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.possibilities)) {
                possibleResults.add(holder.value());
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            var newItem = new ItemStack(Util.getRandom(possibleResults, level.random));
            player.getInventory().placeItemBackInInventory(newItem);

            return InteractionResultHolder.consume(stack.isEmpty() ? player.getItemInHand(hand) : stack);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (this == EItems.RANDOM_ARMOR_TRIM.value()) {
            tooltip.add(Component.translatable(TranslationKeys.RANDOM_TRIM_DOES_NOT_CONTAIN_UPGRADE).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
