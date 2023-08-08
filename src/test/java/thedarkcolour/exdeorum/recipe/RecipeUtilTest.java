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

package thedarkcolour.exdeorum.recipe;

import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RecipeUtilTest {
    @BeforeEach
    void setUp() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void areIngredientsEqual() {
        assertTrue(RecipeUtil.areIngredientsEqual(Ingredient.of(Items.OAK_SLAB), Ingredient.of(Items.OAK_SLAB)));
        assertFalse(RecipeUtil.areIngredientsEqual(Ingredient.of(Items.BIRCH_SLAB), Ingredient.of(Items.OAK_SLAB)));
        assertTrue(RecipeUtil.areIngredientsEqual(Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND)));
        assertFalse(RecipeUtil.areIngredientsEqual(Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.GEMS_DIAMOND)));
        assertTrue(RecipeUtil.areIngredientsEqual(Ingredient.of(Items.OAK_SLAB, Items.SPRUCE_SLAB), Ingredient.of(Items.OAK_SLAB, Items.SPRUCE_SLAB)));
        assertTrue(RecipeUtil.areIngredientsEqual(Ingredient.of(Items.OAK_SLAB, Items.SPRUCE_SLAB), Ingredient.of(Items.SPRUCE_SLAB, Items.OAK_SLAB)));
    }
}