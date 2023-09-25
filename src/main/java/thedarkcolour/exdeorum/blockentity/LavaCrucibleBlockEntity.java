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

package thedarkcolour.exdeorum.blockentity;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LavaCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    public static final Object2IntMap<BlockState> HEAT_REGISTRY = new Object2IntOpenHashMap<>();
    public static final Object2IntMap<BlockState> KUBEJS_HEAT_VALUES = new Object2IntLinkedOpenHashMap<>();

    static {
        putDefaultHeatValues();
    }

    public static void putDefaultHeatValues() {
        HEAT_REGISTRY.clear();

        putAllStates(Blocks.TORCH, 1);
        putAllStates(Blocks.WALL_TORCH, 1);
        putAllStates(Blocks.LANTERN, 1);
        putAllStates(Blocks.SOUL_TORCH, 2);
        putAllStates(Blocks.SOUL_WALL_TORCH, 2);
        putAllStates(Blocks.SOUL_LANTERN, 2);
        putAllStates(Blocks.LAVA, 3);
        putAllStates(Blocks.FIRE, 5);
        putAllStates(Blocks.SOUL_FIRE, 5);

        putStates(Blocks.CAMPFIRE, 2, state -> state.getValue(CampfireBlock.LIT));
        putStates(Blocks.SOUL_CAMPFIRE, 2, state -> state.getValue(CampfireBlock.LIT));

        HEAT_REGISTRY.putAll(KUBEJS_HEAT_VALUES);
        KUBEJS_HEAT_VALUES.clear();
    }

    public static void putAllStates(Block block, int heat) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            HEAT_REGISTRY.put(state, heat);
        }
    }

    public static void putStates(Block block, int heat, Predicate<BlockState> predicate) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            if (predicate.test(state)) {
                HEAT_REGISTRY.put(state, heat);
            }
        }
    }

    public LavaCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.LAVA_CRUCIBLE.get(), pos, state);
    }

    @Override
    public int getMeltingRate() {
        BlockState state = level.getBlockState(getBlockPos().below());

        return HEAT_REGISTRY.getInt(state);
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
