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

package thedarkcolour.exdeorum.recipe.sieve;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public class SieveRecipe extends ProbabilityRecipe {
    public final Item mesh;
    public final boolean byHandOnly;

    public SieveRecipe(ResourceLocation id, Ingredient ingredient, Item mesh, Item result, NumberProvider resultAmount, boolean byHandOnly) {
        super(id, ingredient, result, resultAmount);

        this.mesh = mesh;
        this.byHandOnly = byHandOnly;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.SIEVE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.SIEVE.get();
    }

    public static abstract class AbstractSerializer<T extends SieveRecipe> implements RecipeSerializer<T> {
        protected abstract T createSieveRecipe(ResourceLocation id, Ingredient ingredient, Item mesh, Item result, NumberProvider resultAmount, boolean byHandOnly);

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Item mesh = RecipeUtil.readItem(json, "mesh");
            Item result;

            if (json.has("result")) {
                result = RecipeUtil.readItem(json, "result");
            } else if (json.has("result_tag")) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, new ResourceLocation(GsonHelper.getAsString(json, "result_tag")));
                result = PreferredOres.getPreferredOre(tag);

                if (result == Items.AIR) {
                    ExDeorum.LOGGER.info("Skipped loading recipe {} as result_tag {} was empty", id, tag);
                    return null;
                }
            } else {
                throw new JsonSyntaxException("missing \"result\" item location or \"result_tag\" tag location");
            }

            NumberProvider resultAmount = RecipeUtil.readNumberProvider(json, "result_amount");
            boolean byHandOnly = json.has("by_hand_only") && json.get("by_hand_only").getAsBoolean();
            return createSieveRecipe(id, ingredient, mesh, result, resultAmount, byHandOnly);
        }

        @SuppressWarnings("deprecation")
        @Override
        public @Nullable T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item mesh = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return createSieveRecipe(id, ingredient, mesh, result, resultAmount, buffer.readBoolean());
        }

        @SuppressWarnings("deprecation")
        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.mesh);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
            buffer.writeBoolean(recipe.byHandOnly);
        }
    }

    public static class Serializer extends AbstractSerializer<SieveRecipe> {
        @Override
        protected SieveRecipe createSieveRecipe(ResourceLocation id, Ingredient ingredient, Item mesh, Item result, NumberProvider resultAmount, boolean byHandOnly) {
            return new SieveRecipe(id, ingredient, mesh, result, resultAmount, byHandOnly);
        }
    }
}
