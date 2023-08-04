package thedarkcolour.exdeorum.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class BarrelMixingRecipe extends SingleIngredientRecipe {
    public final FluidType fluidType;
    public final int fluidAmount;
    public final Item result;

    public BarrelMixingRecipe(ResourceLocation id, Ingredient ingredient, FluidType fluidType, int fluidAmount, Item result) {
        super(id, ingredient);
        this.fluidType = fluidType;
        this.fluidAmount = fluidAmount;
        this.result = result;
    }

    // Do not use
    @Override
    @Deprecated
    public boolean matches(Container inventory, Level level) {
        return false;
    }

    public boolean matches(ItemStack item, FluidStack fluid) {
        return ingredient.test(item) && fluid.getFluid().getFluidType() == fluidType && fluid.getAmount() >= fluidAmount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_MIXING.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelMixingRecipe> {
        @Override
        public BarrelMixingRecipe fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            FluidType fluidType = ForgeRegistries.FLUID_TYPES.get().getValue(new ResourceLocation(GsonHelper.getAsString(json, "fluid_type")));
            int fluidAmount = GsonHelper.getAsInt(json, "fluid_amount");
            Item result = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "result")));

            return new BarrelMixingRecipe(name, ingredient, fluidType, fluidAmount, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelMixingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeRegistryId(ForgeRegistries.FLUID_TYPES.get(), recipe.fluidType);
            buffer.writeVarInt(recipe.fluidAmount);
            buffer.writeRegistryId(ForgeRegistries.ITEMS, recipe.result);
        }

        @Override
        public @Nullable BarrelMixingRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            FluidType fluidType = buffer.readRegistryId();
            int fluidAmount = buffer.readVarInt();
            Item result = buffer.readRegistryId();

            return new BarrelMixingRecipe(name, ingredient, fluidType, fluidAmount, result);
        }
    }
}
