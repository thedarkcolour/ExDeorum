package thedarkcolour.exnihiloreborn.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;

import javax.annotation.Nullable;

public class FinishedBarrelCompostRecipe implements IFinishedRecipe {
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
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
    public IRecipeSerializer<?> getType() {
        return ERecipeSerializers.BARREL_COMPOST.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
