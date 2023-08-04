package thedarkcolour.exdeorum.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedBarrelCompostRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int volume;

    public FinishedBarrelCompostRecipe(ResourceLocation id, Ingredient ingredient, int volume) {
        this.id = id;
        this.ingredient = ingredient;
        this.volume = volume;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", ingredient.toJson());
        json.addProperty("volume", volume);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_COMPOST.get();
    }
}
