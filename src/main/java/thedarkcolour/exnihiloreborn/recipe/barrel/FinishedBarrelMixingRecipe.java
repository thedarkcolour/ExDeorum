package thedarkcolour.exnihiloreborn.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.recipe.EFinishedRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;

public class FinishedBarrelMixingRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final FluidType fluidType;
    private final int fluidAmount;
    private final Item result;

    public FinishedBarrelMixingRecipe(ResourceLocation id, Ingredient ingredient, FluidType fluidType, int fluidAmount, Item result) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluidType = fluidType;
        this.fluidAmount = fluidAmount;
        this.result = result;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", this.ingredient.toJson());
        json.addProperty("fluid_type", ForgeRegistries.FLUID_TYPES.get().getKey(this.fluidType).toString());
        json.addProperty("fluid_amount", this.fluidAmount);
        json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }
}
