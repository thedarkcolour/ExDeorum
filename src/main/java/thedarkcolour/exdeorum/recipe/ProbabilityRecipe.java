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
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public abstract class ProbabilityRecipe extends SingleIngredientRecipe {
    public final Item result;
    public final NumberProvider resultAmount;

    public ProbabilityRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(ingredient);
        this.result = result;
        this.resultAmount = resultAmount;
    }

    protected static <T extends ProbabilityRecipe> Products.P3<RecordCodecBuilder.Mu<T>, Ingredient, Item, NumberProvider> commonFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                CodecUtil.ingredientField(),
                CodecUtil.itemField("result", ProbabilityRecipe::getResult),
                NumberProviders.CODEC.fieldOf("result_amount").forGetter(ProbabilityRecipe::getResultAmount)
        );
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(this.result);
    }

    public Item getResult() {
        return this.result;
    }

    public NumberProvider getResultAmount() {
        return this.resultAmount;
    }
}
