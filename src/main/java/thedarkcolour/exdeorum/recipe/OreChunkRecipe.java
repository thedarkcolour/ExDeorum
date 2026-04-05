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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import thedarkcolour.exdeorum.compat.PreferredOres;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

import java.util.List;
import java.util.Map;

public class OreChunkRecipe implements CraftingRecipe {
    public static final MapCodec<OreChunkRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ore_chunk").forGetter(OreChunkRecipe::getOreChunk),
            TagKey.codec(Registries.ITEM).fieldOf("ore").forGetter(OreChunkRecipe::getOre)
    ).apply(instance, OreChunkRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OreChunkRecipe> STREAM_CODEC = StreamCodec.of(OreChunkRecipe::toNetwork, OreChunkRecipe::fromNetwork);

    private static final List<String> GRID_2X2 = List.of("CC", "CC");

    private final Ingredient oreChunk;
    private final TagKey<Item> ore;
    private final ShapedRecipePattern pattern;
    private final Lazy<ItemStack> resultItem;

    public OreChunkRecipe(Ingredient oreChunk, TagKey<Item> ore) {
        this.oreChunk = oreChunk;
        this.ore = ore;
        this.pattern = ShapedRecipePattern.of(Map.of('C', oreChunk), GRID_2X2);
        this.resultItem = Lazy.of(() -> new ItemStack(PreferredOres.getPreferredOre(this.ore)));
    }

    public Ingredient getOreChunk() {
        return this.oreChunk;
    }

    public TagKey<Item> getOre() {
        return this.ore;
    }

    public List<Ingredient> getIngredients() {
        return this.pattern.ingredients().stream()
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return this.resultItem.get().copy();
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        return this.pattern.matches(container);
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(getIngredients());
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.BUILDING;
    }

    @Override
    public RecipeSerializer<OreChunkRecipe> getSerializer() {
        return ERecipeSerializers.ORE_CHUNK.get();
    }

    public static OreChunkRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new OreChunkRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), RecipeUtil.readTag(buffer, Registries.ITEM));
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, OreChunkRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.oreChunk);
        RecipeUtil.writeTag(buffer, recipe.ore);
    }

}
