package thedarkcolour.exnihiloreborn.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import thedarkcolour.exnihiloreborn.recipe.EFinishedRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;

import javax.annotation.Nullable;

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
