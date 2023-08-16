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

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

// A recipe whose result is an item tag. Tag can be empty.
@SuppressWarnings({"rawtypes", "unchecked"})
public class TagResultRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Recipe<Container> originalRecipe;

    public TagResultRecipe(ResourceLocation id, Recipe originalRecipe) {
        this.id = id;
        this.originalRecipe = originalRecipe;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.originalRecipe.matches(container, level);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        return this.originalRecipe.assemble(container, access);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return this.originalRecipe.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.getResultItem(access);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.TAG_RESULT.get();
    }

    @Override
    public RecipeType<?> getType() {
        return this.originalRecipe.getType();
    }

    public static class Serializer implements RecipeSerializer<TagResultRecipe> {
        @Override
        public TagResultRecipe fromJson(ResourceLocation id, JsonObject json) {
            var tag = TagKey.create(Registries.ITEM, new ResourceLocation(GsonHelper.getAsString(json, "result_tag")));
            var newResult = RecipeUtil.getPreferredItem(tag);
            var originalRecipeJson = GsonHelper.getAsJsonObject(json, "original_recipe");

            if (json.has("result")) {
                var resultElement = json.get("result");
                if (resultElement.isJsonObject()) {

                }
            }
            return null;//return new TagResultRecipe(id, );
        }

        @Override
        public @Nullable TagResultRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TagResultRecipe recipe) {
            ((RecipeSerializer) recipe.originalRecipe.getSerializer()).toNetwork(buffer, recipe.originalRecipe);
            //buffer.writeResourceLocation(recipe.tag.location());
        }
    }
}
