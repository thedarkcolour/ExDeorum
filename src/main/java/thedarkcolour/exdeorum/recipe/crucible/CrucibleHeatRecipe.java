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

package thedarkcolour.exdeorum.recipe.crucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public record CrucibleHeatRecipe(BlockPredicate blockPredicate, int heatValue) implements Recipe<RecipeInput> {
    public static final MapCodec<CrucibleHeatRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPredicate.CODEC.fieldOf("block_predicate").forGetter(CrucibleHeatRecipe::blockPredicate),
            Codec.INT.fieldOf("heat_value").forGetter(CrucibleHeatRecipe::heatValue)
    ).apply(instance, CrucibleHeatRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleHeatRecipe> STREAM_CODEC = StreamCodec.of(CrucibleHeatRecipe::toNetwork, CrucibleHeatRecipe::fromNetwork);

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<CrucibleHeatRecipe> getSerializer() {
        return ERecipeSerializers.CRUCIBLE_HEAT_SOURCE.get();
    }

    @Override
    public RecipeType<CrucibleHeatRecipe> getType() {
        return ERecipeTypes.CRUCIBLE_HEAT_SOURCE.get();
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, CrucibleHeatRecipe recipe) {
        BlockPredicate.STREAM_CODEC.encode(buffer, recipe.blockPredicate);
        buffer.writeVarInt(recipe.heatValue);
    }

    public static CrucibleHeatRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        BlockPredicate blockPredicate = BlockPredicate.STREAM_CODEC.decode(buffer);
        int heatValue = buffer.readVarInt();
        return new CrucibleHeatRecipe(blockPredicate, heatValue);
    }

}
