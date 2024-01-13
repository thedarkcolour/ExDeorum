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

package thedarkcolour.exdeorum.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class BarrelCompostRecipe extends SingleIngredientRecipe {
    private final int volume;

    public BarrelCompostRecipe(ResourceLocation id, Ingredient ingredient, int volume) {
        super(id, ingredient);

        this.volume = volume;
    }

    public int getVolume() {
        return this.volume;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_COMPOST.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_COMPOST.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelCompostRecipe> {
        @Override // Creates the recipe object from a JSON file
        public BarrelCompostRecipe fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            int volume = GsonHelper.getAsInt(json, "volume");

            return new BarrelCompostRecipe(name, ingredient, volume);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelCompostRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.getVolume());
        }

        @Override
        public BarrelCompostRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int volume = buffer.readVarInt();

            return new BarrelCompostRecipe(name, ingredient, volume);
        }
    }
}
