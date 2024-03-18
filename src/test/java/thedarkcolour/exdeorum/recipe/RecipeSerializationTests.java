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

package thedarkcolour.exdeorum.recipe;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thedarkcolour.exdeorum.recipe.barrel.FluidTransformationRecipe;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleHeatRecipe;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

// Tests JSON and network serialization/deserialization methods of all recipes to ensure everything works
public class RecipeSerializationTests {
    @BeforeEach
    void setUp() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void crucibleHeatSourceNetwork() {
        testNetwork(new CrucibleHeatRecipe(null, BlockPredicate.blockTag(BlockTags.DIRT), 3), new CrucibleHeatRecipe.Serializer());
    }

    @Test
    void barrelFluidTransformationNetwork() {
        testNetwork(new FluidTransformationRecipe(null, Fluids.WATER, Fluids.LAVA, 1, BlockPredicate.blockTag(BlockTags.DIRT), WeightedList.<BlockState>builder().build(), 1000), new FluidTransformationRecipe.Serializer());
    }

    @Test
    void crookNetwork() {
        testNetwork(new CrookRecipe(null, BlockPredicate.blockTag(BlockTags.DIRT), Items.DIAMOND, 0.0025f), new CrookRecipe.Serializer());
    }

    // Takes a recipe, uses its serializer to write it to a buffer, then reads it
    // back and checks if the recipe it got is the same as the one originally written
    private static <T extends Recipe<?>> void testNetwork(T recipe, RecipeSerializer<T> serializer) {
        var id = recipe.getId();
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        serializer.toNetwork(buffer, recipe);
        assertEquals(recipe, serializer.fromNetwork(id, buffer));
    }
}
