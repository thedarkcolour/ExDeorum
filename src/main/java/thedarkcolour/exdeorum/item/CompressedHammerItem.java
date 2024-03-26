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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.Set;

// todo implement
public class CompressedHammerItem extends HammerItem {
    public static Lazy<Set<Block>> validBlocks = Lazy.of(() -> HammerItem.computeValidBlocks(RecipeUtil.getCachedCompressedHammerRecipes()));

    public static void refreshValidBlocks() {
        validBlocks = Lazy.of(() -> HammerItem.computeValidBlocks(RecipeUtil.getCachedCompressedHammerRecipes()));
    }

    public CompressedHammerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    protected Set<Block> getValidBlocks() {
        return validBlocks.get();
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return this == EItems.WOODEN_COMPRESSED_HAMMER.get() ? 200 : 0;
    }
}
