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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.WeightedList;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

// todo consider NBT tag of input fluid?
public class FluidTransformationRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public final Fluid baseFluid;
    public final Fluid resultFluid;
    public final int resultColor;
    public final BlockPredicate catalyst;
    public final WeightedList<BlockState> byproducts;
    public final int duration;

    public FluidTransformationRecipe(ResourceLocation id, Fluid baseFluid, Fluid resultFluid, int resultColor, BlockPredicate catalyst, WeightedList<BlockState> byproducts, int duration) {
        this.id = id;
        this.baseFluid = baseFluid;
        this.resultFluid = resultFluid;
        this.resultColor = resultColor;
        this.catalyst = catalyst;
        this.byproducts = byproducts;
        this.duration = duration;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_FLUID_TRANSFORMATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_FLUID_TRANSFORMATION.get();
    }

    public static class Serializer implements RecipeSerializer<FluidTransformationRecipe> {
        @Override
        public FluidTransformationRecipe fromJson(ResourceLocation id, JsonObject json) {
            Fluid baseFluid = RecipeUtil.readFluid(json, "base_fluid");
            Fluid resultFluid = RecipeUtil.readFluid(json, "result_fluid");
            int resultColor = GsonHelper.getAsInt(json, "result_color");
            int duration = GsonHelper.getAsInt(json, "duration");
            BlockPredicate catalyst = RecipeUtil.readBlockPredicate(id, json, "catalyst");
            var byproducts = WeightedList.fromJson(json.getAsJsonArray("byproducts"), element -> {
                if (element.isJsonPrimitive()) {
                    return RecipeUtil.parseBlockState(element.getAsString());
                } else {
                    return null;
                }
            });
            if (catalyst == null) {
                throw new JsonSyntaxException("Failed to read barrel fluid transformation recipe catalyst");
            }
            if (byproducts == null) {
                throw new JsonSyntaxException("Failed to read barrel fluid transformation recipe byproducts");
            }
            return new FluidTransformationRecipe(id, baseFluid, resultFluid, resultColor, catalyst, byproducts, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FluidTransformationRecipe recipe) {
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, recipe.baseFluid);
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, recipe.resultFluid);
            buffer.writeInt(recipe.resultColor);
            recipe.catalyst.toNetwork(buffer);
            recipe.byproducts.toNetwork(buffer, (buf, state) -> buf.writeId(Block.BLOCK_STATE_REGISTRY, state));
            buffer.writeVarInt(recipe.duration);
        }

        @Override
        public @Nullable FluidTransformationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Fluid baseFluid = buffer.readRegistryId();
            Fluid resultFluid = buffer.readRegistryId();
            int resultColor = buffer.readInt();
            BlockPredicate catalyst = RecipeUtil.readBlockPredicateNetwork(id, buffer);
            if (catalyst == null) {
                return null;
            }
            WeightedList<BlockState> byproducts = WeightedList.fromNetwork(buffer, buf -> buf.readById(Block.BLOCK_STATE_REGISTRY));
            int duration = buffer.readVarInt();
            return new FluidTransformationRecipe(id, baseFluid, resultFluid, resultColor, catalyst, byproducts, duration);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidTransformationRecipe that = (FluidTransformationRecipe) o;
        return this.resultColor == that.resultColor && this.duration == that.duration && Objects.equals(this.id, that.id) && Objects.equals(this.baseFluid, that.baseFluid) && Objects.equals(this.resultFluid, that.resultFluid) && Objects.equals(this.catalyst, that.catalyst) && Objects.equals(this.byproducts, that.byproducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.baseFluid, this.resultFluid, this.resultColor, this.catalyst, this.byproducts, this.duration);
    }
}
