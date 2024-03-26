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

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.Collection;
import java.util.Set;

public class HammerItem extends DiggerItem {
    public static Lazy<Set<Block>> validBlocks = Lazy.of(() -> computeValidBlocks(RecipeUtil.getCachedHammerRecipes()));

    public HammerItem(Tier tier, Properties properties) {
        super(1.0f, -2.8f, tier, null, properties);
    }

    public static Set<Block> computeValidBlocks(Collection<? extends HammerRecipe> hammerRecipes) {
        var validBlocks = new ObjectOpenHashSet<Block>(hammerRecipes.size());

        for (var recipe : hammerRecipes) {
            for (var item : recipe.getIngredient().getItems()) {
                if (item.getItem() instanceof BlockItem blockItem) {
                    validBlocks.add(blockItem.getBlock());
                }
            }
        }

        return validBlocks;
    }

    public static void refreshValidBlocks() {
        validBlocks = Lazy.of(() -> computeValidBlocks(RecipeUtil.getCachedHammerRecipes()));
    }

    protected Set<Block> getValidBlocks() {
        return validBlocks.get();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return getValidBlocks().contains(state.getBlock()) ? this.speed : 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (net.minecraftforge.common.TierSortingRegistry.isTierSorted(getTier())) {
            return net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state) && getValidBlocks().contains(state.getBlock());
        }
        int i = this.getTier().getLevel();
        if (i < 3 && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !state.is(BlockTags.NEEDS_STONE_TOOL)) && getValidBlocks().contains(state.getBlock());
        }
    }

    // FORGE START
    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return getValidBlocks().contains(state.getBlock()) && net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return this == EItems.WOODEN_HAMMER.get() ? 200 : 0;
    }
}
