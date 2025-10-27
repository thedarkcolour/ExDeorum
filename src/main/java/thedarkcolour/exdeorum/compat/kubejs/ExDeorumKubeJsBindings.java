/*
 * Ex Deorum
 * Copyright (c) 2025 thedarkcolour
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

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.plugin.builtin.event.ServerEvents;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilterParseEvent;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.DeferredHolder;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.function.Consumer;

@SuppressWarnings("unused")
class ExDeorumKubeJsBindings {
    public void setCrucibleHeatValue(Block block, int value) {
        setCrucibleHeatValueForBlock(block, value);
    }

    // This method previously accepted a BlockState, which made it impossible to call through KubeJS.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setCrucibleHeatValueForState(String stateString, int value) {
        onRecipesEvent(event -> {
            var state = RecipeUtil.parseBlockState(stateString);
            var properties = StatePropertiesPredicate.Builder.properties();
            for (Property prop : state.getProperties()) {
                bypassTypeChecking(properties, prop, state);
            }
            event.custom(null, (JsonObject) CrucibleHeatRecipe.CODEC.encoder().encodeStart(JsonOps.INSTANCE, new CrucibleHeatRecipe(BlockPredicate.blockState(state.getBlock(), properties.build().get()), value)).result().get());
        });
    }

    @HideFromJS
    private static <T extends Comparable<T>> void bypassTypeChecking(StatePropertiesPredicate.Builder properties, Property<T> prop, BlockState state) {
        properties.hasProperty(prop, prop.getName(state.getValue(prop)));
    }

    public void setCrucibleHeatValueForBlock(Block block, int value) {
        onRecipesEvent(event -> {
            event.custom(null, (JsonObject) CrucibleHeatRecipe.CODEC.encoder().encodeStart(JsonOps.INSTANCE, new CrucibleHeatRecipe(BlockPredicate.singleBlock(block), value)).result().get());
        });
    }

    public void removeDefaultSieveRecipes(RecipesKubeEvent recipesEvent) {
        removeDefaultRecipes(recipesEvent, ERecipeTypes.SIEVE);
    }

    public void removeDefaultHeatSources() {
        onRecipesEvent(event -> removeDefaultRecipes(event, ERecipeTypes.CRUCIBLE_HEAT_SOURCE));
    }

    @HideFromJS
    private static void removeDefaultRecipes(RecipesKubeEvent event, DeferredHolder<? extends RecipeType<?>, ? extends RecipeType<?>> recipeType) {
        event.remove(null, (ctx) -> {
            var r = ctx.recipe();
            return r.kjs$getType().equals(recipeType.getId()) && r.kjs$getOrCreateId().getNamespace().equals(ExDeorum.ID);
        });
    }

    @HideFromJS
    private static void onRecipesEvent(Consumer<RecipesKubeEvent> action) {
        ServerEvents.RECIPES.listenJava(ScriptType.SERVER, null, jsEvent -> {
            action.accept((RecipesKubeEvent) jsEvent);
            return null;
        });
    }

    public static void onRecipeFilterParse(RecipeFilterParseEvent event) {
        var sieveMesh = event.map.get("sieve_mesh");
        if (sieveMesh != null) {
            ReplacementMatchInfo m = ReplacementMatchInfo.wrap(event.cx, sieveMesh, ReplacementMatchInfo.TYPE_INFO);
            if (m == ReplacementMatchInfo.NONE) {
                throw Context.reportRuntimeError("Unable to parse recipe output filter `" + sieveMesh + "`", event.cx);
            }
            event.filters.add(new SieveMeshFilter(m));
        }
    }
}
