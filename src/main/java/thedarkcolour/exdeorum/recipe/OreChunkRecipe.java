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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;
import net.neoforged.neoforge.common.util.Lazy;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

import java.util.List;
import java.util.Map;

public class OreChunkRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {
    public static final Codec<OreChunkRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ore_chunk").forGetter(OreChunkRecipe::getOreChunk),
            TagKey.codec(Registries.ITEM).fieldOf("ore").forGetter(OreChunkRecipe::getOre)
    ).apply(instance, OreChunkRecipe::new));

    private static final List<String> GRID_2X2 = List.of("CC", "CC");

    private final Ingredient oreChunk;
    private final TagKey<Item> ore;
    private final ShapedRecipePattern pattern;
    private final Lazy<ItemStack> resultItem;

    public OreChunkRecipe(Ingredient oreChunk, TagKey<Item> ore) {
        this.oreChunk = oreChunk;
        this.ore = ore;
        this.pattern = ShapedRecipePattern.of(Map.of('C', oreChunk), GRID_2X2);
        this.resultItem = Lazy.of(() -> {
            return new ItemStack(PreferredOres.getPreferredOre(this.ore));
        });
    }

    public Ingredient getOreChunk() {
        return this.oreChunk;
    }

    public TagKey<Item> getOre() {
        return this.ore;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.resultItem.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.resultItem.get().copy();
    }

    @Override
    public boolean matches(CraftingContainer container, Level pLevel) {
        return this.pattern.matches(container);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public int getRecipeWidth() {
        return 2;
    }

    @Override
    public int getRecipeHeight() {
        return 2;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.BUILDING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.ORE_CHUNK.get();
    }

    public static class Serializer implements RecipeSerializer<OreChunkRecipe> {
        @Override
        public Codec<OreChunkRecipe> codec() {
            return CODEC;
        }

        @Override
        public OreChunkRecipe fromNetwork(FriendlyByteBuf buffer) {
            return new OreChunkRecipe(Ingredient.fromNetwork(buffer), RecipeUtil.readTag(buffer, Registries.ITEM));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, OreChunkRecipe recipe) {
            recipe.oreChunk.toNetwork(buffer);
            RecipeUtil.writeTag(buffer, recipe.ore);
        }
    }
}
