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

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * Represents a recipe that does not take place in any screen or container.
 * <p>
 * Has one ingredient by default and just tests off of that. Only the 1st slot
 * of any container will be checked, so only one slot should be present.
 */
public abstract class SingleIngredientRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    protected final Ingredient ingredient;
    public final boolean dependsOnNbt;

    public SingleIngredientRecipe(ResourceLocation id, Ingredient ingredient) {
        this.id = id;
        this.ingredient = ingredient;
        this.dependsOnNbt = !ingredient.isSimple();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(Container inventory, Level level) {
        return ingredient.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    /**
     * @deprecated Only used in Vanilla recipe books, and my blocks do not use the recipe book!
     */
    @Deprecated
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}
