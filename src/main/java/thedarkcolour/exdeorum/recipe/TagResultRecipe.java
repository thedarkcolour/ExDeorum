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
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TagResultRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Recipe<Container> wrapped;
    private final TagKey<Item> result;

    public TagResultRecipe(ResourceLocation id, Recipe wrapped, TagKey<Item> result) {
        this.id = id;
        this.wrapped = wrapped;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.wrapped.matches(container, level);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        access.registryOrThrow(Registries.ITEM).getTag(result);
        throw new UnsupportedOperationException("TagResultRecipe#assemble");
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return this.wrapped.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        throw new UnsupportedOperationException("TagResultRecipe#getResultItem");
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
        return ERecipeTypes.TAG_RESULT.get();
    }

    public static class Serializer implements RecipeSerializer<TagResultRecipe> {
        @Override
        public TagResultRecipe fromJson(ResourceLocation id, JsonObject json) {
            return null;
        }

        @Override
        public @Nullable TagResultRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TagResultRecipe recipe) {
            ((RecipeSerializer) recipe.wrapped.getSerializer()).toNetwork(buffer, recipe.wrapped);
            //buffer.writeResourceLocation(recipe.tag.location());
        }
    }
}
