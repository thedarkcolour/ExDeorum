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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class TagResultRecipe {
    public static class Serializer<T extends Recipe<?>> implements RecipeSerializer<T> {
        @SuppressWarnings({"deprecation", "unchecked"})
        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            var resultTag = GsonHelper.getAsString(json, "result_tag");
            if (ResourceLocation.isValidResourceLocation(resultTag)) {
                var tag = TagKey.create(Registries.ITEM, new ResourceLocation(resultTag));
                var preferredItem = PreferredOres.getPreferredOre(tag);
                if (preferredItem != Items.AIR) {
                    var recipeJson = GsonHelper.getAsJsonObject(json, "recipe");

                    if (recipeJson.has("result")) {
                        var resultJson = GsonHelper.getAsJsonObject(recipeJson, "result");
                        var itemId = preferredItem.builtInRegistryHolder().key().location().toString();

                        // replace item id with correct value
                        resultJson.addProperty("item", itemId);

                        return (T) RecipeManager.fromJson(id, recipeJson);
                    } else {
                        ExDeorum.LOGGER.error("Skipping recipe {} with unhandled recipe type, \"{}\". Please report to Ex Deorum GitHub.", id, recipeJson.get("type").getAsString());
                    }
                } else {
                    ExDeorum.LOGGER.info("Skipping recipe {} as ExDeorum could not determine substitute for tag {}", id, tag.location());
                }
            } else {
                ExDeorum.LOGGER.error("Invalid resource location for \"result_tag\" in recipe {}", id);
            }

            return null;
        }

        @Override
        public @Nullable T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        }
    }

    public static class Finished implements FinishedRecipe {
        private final TagKey<Item> resultTag;
        private final FinishedRecipe recipe;

        public Finished(TagKey<Item> resultTag, FinishedRecipe recipe) {
            this.resultTag = resultTag;
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject recipeJson = new JsonObject();
            this.recipe.serializeRecipeData(recipeJson);
            recipeJson.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.recipe.getType()).toString());

            json.addProperty("result_tag", this.resultTag.location().toString());
            json.add("recipe", recipeJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ERecipeSerializers.TAG_RESULT.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.recipe.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.recipe.getAdvancementId();
        }
    }
}
