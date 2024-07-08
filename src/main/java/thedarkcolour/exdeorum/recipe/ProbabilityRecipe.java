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

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public abstract class ProbabilityRecipe extends SingleIngredientRecipe {
    public final ItemStack result;
    public final NumberProvider resultAmount;

    public ProbabilityRecipe(Ingredient ingredient, ItemStack result, NumberProvider resultAmount) {
        super(ingredient);
        this.result = result;
        this.resultAmount = resultAmount;
    }

    protected static <T extends ProbabilityRecipe> Products.P3<RecordCodecBuilder.Mu<T>, Ingredient, ItemStack, NumberProvider> commonFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                CodecUtil.ingredientField(),
                ItemStack.CODEC.fieldOf("result").forGetter(ProbabilityRecipe::result),
                NumberProviders.CODEC.fieldOf("result_amount").forGetter(ProbabilityRecipe::resultAmount)
        );
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider access) {
        return this.result;
    }

    public ItemStack result() {
        return this.result;
    }

    public NumberProvider resultAmount() {
        return this.resultAmount;
    }
}
