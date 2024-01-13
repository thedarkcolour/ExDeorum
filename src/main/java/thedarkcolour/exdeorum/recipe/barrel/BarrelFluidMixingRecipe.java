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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

// A recipe where two fluids are mixed together, rather than a fluid and an item.
// The additive must be 1000mB or a source block worth of liquid.
// The additive is not consumed, however. Additives placed in the world are not consumed,
// so it would be unfair to consume the handheld additive.
public class BarrelFluidMixingRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public final Fluid baseFluid;
    public final int baseFluidAmount;
    public final Fluid additiveFluid;
    public final Item result;

    public BarrelFluidMixingRecipe(ResourceLocation id, Fluid baseFluid, int baseFluidAmount, Fluid additiveFluid, Item result) {
        this.id = id;
        this.baseFluid = baseFluid;
        this.baseFluidAmount = baseFluidAmount;
        this.additiveFluid = additiveFluid;
        this.result = result;
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
        return ERecipeSerializers.BARREL_FLUID_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_FLUID_MIXING.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelFluidMixingRecipe> {
        @Override
        public BarrelFluidMixingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Fluid baseFluid = RecipeUtil.readFluid(json, "base_fluid");
            int baseFluidAmount = GsonHelper.getAsInt(json, "base_fluid_amount");
            Fluid additiveFluid = RecipeUtil.readFluid(json, "additive_fluid");
            Item result = RecipeUtil.readItem(json, "result");

            return new BarrelFluidMixingRecipe(id, baseFluid, baseFluidAmount, additiveFluid, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelFluidMixingRecipe recipe) {
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, recipe.baseFluid);
            buffer.writeVarInt(recipe.baseFluidAmount);
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, recipe.additiveFluid);
            buffer.writeRegistryId(ForgeRegistries.ITEMS, recipe.result);
        }

        @Override
        public @Nullable BarrelFluidMixingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Fluid baseFluid = buffer.readRegistryId();
            int baseFluidAmount = buffer.readVarInt();
            Fluid additiveFluid = buffer.readRegistryId();
            Item result = buffer.readRegistryId();

            return new BarrelFluidMixingRecipe(id, baseFluid, baseFluidAmount, additiveFluid, result);
        }
    }
}
