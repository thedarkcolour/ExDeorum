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

package thedarkcolour.exdeorum.compat.kubejs;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.LavaCrucibleBlockEntity;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

@SuppressWarnings("unused")
class ExDeorumKubeJsBindings {
    static {
        RecipeFilter.PARSE.register((ctx, filters, map) -> {
            var sieveMesh = map.get("sieve_mesh");
            if (sieveMesh != null) {
                filters.add(new SieveMeshFilter(ReplacementMatch.of(sieveMesh)));
            }
        });
    }

    public void setCrucibleHeatValue(Block block, int value) {
        setCrucibleHeatValueForBlock(block, value);
    }

    // This method previously accepted a BlockState, which made it impossible to call through KubeJS.
    public void setCrucibleHeatValueForState(String stateString, int value) {
        try {
            LavaCrucibleBlockEntity.KUBEJS_HEAT_VALUES.put(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), stateString, false).blockState(), value);
        } catch (CommandSyntaxException exception) {
            // Throw a more appropriate exception.
            throw new IllegalArgumentException("Failed to parse BlockState string \"" + stateString + "\"");
        }
    }

    public void setCrucibleHeatValueForBlock(Block block, int value) {
        for (var state : block.getStateDefinition().getPossibleStates()) {
            LavaCrucibleBlockEntity.KUBEJS_HEAT_VALUES.put(state, value);
        }
    }

    public void removeDefaultSieveRecipes(RecipesEventJS recipesEvent) {
        recipesEvent.remove(r -> {
            return r.kjs$getType().equals(ERecipeTypes.SIEVE.getId()) && r.kjs$getOrCreateId().getNamespace().equals(ExDeorum.ID);
        });
    }

    // not the most elegant solution, but if it works, it works
    public void removeDefaultHeatSources() {
        var map = new Object2IntOpenHashMap<BlockState>();
        LavaCrucibleBlockEntity.putDefaults(map);
        for (var key : map.keySet()) {
            LavaCrucibleBlockEntity.KUBEJS_HEAT_VALUES.put(key, 0);
        }
    }
}
