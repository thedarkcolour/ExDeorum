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

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;

import javax.annotation.Nullable;

public class LavaCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    public LavaCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.LAVA_CRUCIBLE.get(), pos, state);
    }

    @Override
    public int getMeltingRate() {
        return RecipeUtil.getHeatValue(this.level.getBlockState(getBlockPos().below()));
    }

    @Override
    protected @Nullable CrucibleRecipe getRecipe(ItemStack item) {
        return RecipeUtil.getLavaCrucibleRecipe(item);
    }

    @Override
    public Block getDefaultMeltBlock() {
        return Blocks.COBBLESTONE;
    }
}
