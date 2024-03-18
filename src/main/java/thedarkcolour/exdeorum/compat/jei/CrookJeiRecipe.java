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

package thedarkcolour.exdeorum.compat.jei;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public sealed abstract class CrookJeiRecipe {
    public final List<BlockState> states;
    public Item result;
    public float chance;

    public CrookJeiRecipe(List<BlockState> states, Item result, float chance) {
        this.states = states;
        this.result = result;
        this.chance = chance;
    }

    public abstract void addIngredients(IRecipeLayoutBuilder builder);

    static CrookJeiRecipe create(CrookRecipe recipe) {
        if (recipe.blockPredicate() instanceof BlockPredicate.BlockStatePredicate state) {
            return new StatesRecipe(state, state.possibleStates().filter(blockState -> !blockState.hasProperty(BlockStateProperties.WATERLOGGED) || !blockState.getValue(BlockStateProperties.WATERLOGGED)).toList(), recipe.result(), recipe.chance());
        } else if (recipe.blockPredicate() instanceof BlockPredicate.SingleBlockPredicate block) {
            return new BlockRecipe(block.block(), recipe.result(), recipe.chance());
        } else if (recipe.blockPredicate() instanceof BlockPredicate.TagPredicate tag) {
            var list = new ArrayList<BlockState>();

            for (var holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag.tag())) {
                if (holder.isBound()) {
                    list.add(holder.value().defaultBlockState());
                }
            }

            return new TagRecipe(tag.tag(), List.copyOf(list), recipe.result(), recipe.chance());
        }

        throw new IllegalArgumentException("Invalid crook recipe??  ->  " + recipe);
    }

    sealed static class StatesRecipe extends CrookJeiRecipe {
        private final List<ItemStack> itemIngredients;
        public final List<Component> requirements;

        StatesRecipe(@Nullable BlockPredicate.BlockStatePredicate predicate, List<BlockState> states, Item result, float chance) {
            super(states, result, chance);
            ImmutableList.Builder<ItemStack> itemIngredients = ImmutableList.builder();

            var blocks = new HashSet<Block>();

            for (var state : this.states) {
                var block = state.getBlock();

                if (blocks.add(block)) {
                    var item = block.asItem();

                    if (item != Items.AIR) {
                        itemIngredients.add(new ItemStack(item));
                    }
                }
            }

            this.itemIngredients = itemIngredients.build();

            ImmutableList.Builder<Component> requirements = ImmutableList.builder();
            if (predicate != null) {
                var json = CodecUtil.encode(StatePropertiesPredicate.CODEC, predicate.properties());
                if (json instanceof JsonObject obj) {
                    for (var entry : obj.entrySet()) {
                        requirements.add(Component.literal("  " + entry.getKey() + "=" + entry.getValue().toString()).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
            this.requirements = requirements.build();
        }

        @Override
        public void addIngredients(IRecipeLayoutBuilder builder) {
            if (!this.itemIngredients.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(this.itemIngredients);
            }
        }
    }

    static final class TagRecipe extends StatesRecipe {
        public final TagKey<Block> tag;

        public TagRecipe(TagKey<Block> tag, List<BlockState> states, Item result, float chance) {
            super(null, states, result, chance);
            this.tag = tag;
        }
    }

    static final class BlockRecipe extends CrookJeiRecipe {
        private final ItemStack itemIngredient;

        BlockRecipe(Block block, Item result, float chance) {
            super(List.of(block.defaultBlockState()), result, chance);

            var item = block.asItem();
            if (item == Items.AIR) {
                this.itemIngredient = ItemStack.EMPTY;
            } else {
                this.itemIngredient = new ItemStack(item);
            }
        }

        @Override
        public void addIngredients(IRecipeLayoutBuilder builder) {
            if (!this.itemIngredient.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(this.itemIngredient);
            }
        }
    }
}
