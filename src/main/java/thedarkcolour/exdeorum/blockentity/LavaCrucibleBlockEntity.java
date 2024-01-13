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

        putDefaults(HEAT_REGISTRY);

        for (var entry : KUBEJS_HEAT_VALUES.object2IntEntrySet()) {
            if (entry.getIntValue() <= 0) {
                HEAT_REGISTRY.removeInt(entry.getKey());
            } else {
                HEAT_REGISTRY.put(entry.getKey(), entry.getIntValue());
            }
        }

        KUBEJS_HEAT_VALUES.clear();
    }

    public static void putDefaults(Object2IntMap<BlockState> heatMap) {
        putAllStates(Blocks.TORCH, 1, heatMap);
        putAllStates(Blocks.WALL_TORCH, 1, heatMap);
        putAllStates(Blocks.LANTERN, 1, heatMap);
        putAllStates(Blocks.SOUL_TORCH, 2, heatMap);
        putAllStates(Blocks.SOUL_WALL_TORCH, 2, heatMap);
        putAllStates(Blocks.SOUL_LANTERN, 2, heatMap);
        putAllStates(Blocks.LAVA, 3, heatMap);
        putAllStates(Blocks.FIRE, 5, heatMap);
        putAllStates(Blocks.SOUL_FIRE, 5, heatMap);

        putStates(Blocks.CAMPFIRE, 2, state -> state.getValue(CampfireBlock.LIT), heatMap);
        putStates(Blocks.SOUL_CAMPFIRE, 2, state -> state.getValue(CampfireBlock.LIT), heatMap);
    }

    public static void putAllStates(Block block, int heat, Object2IntMap<BlockState> heatMap) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            heatMap.put(state, heat);
        }
    }

    public static void putStates(Block block, int heat, Predicate<BlockState> predicate, Object2IntMap<BlockState> heatMap) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            if (predicate.test(state)) {
                heatMap.put(state, heat);
            }
        }
    }

    public LavaCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.LAVA_CRUCIBLE.get(), pos, state);
    }

    @Override
    public int getMeltingRate() {
        BlockState state = this.level.getBlockState(getBlockPos().below());

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
