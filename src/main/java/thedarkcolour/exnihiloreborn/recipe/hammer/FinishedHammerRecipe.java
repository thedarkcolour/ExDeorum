package thedarkcolour.exnihiloreborn.recipe.hammer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.exnihiloreborn.recipe.CodecUtil;
import thedarkcolour.exnihiloreborn.recipe.Reward;

import javax.annotation.Nullable;

public class FinishedHammerRecipe implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ImmutableList<Reward> rewards;
    private final IRecipeSerializer<?> serializer;

    public FinishedHammerRecipe(IRecipeSerializer<?> serializer, ResourceLocation id, Ingredient ingredient, ImmutableList<Reward> rewards) {
        this.serializer = serializer;
        this.id = id;
        this.ingredient = ingredient;
        this.rewards = rewards;
    }

    @Override
    public void serializeRecipeData(JsonObject object) {
        object.add("ingredient", ingredient.toJson());

        JsonArray rewardsJson = new JsonArray();

        for (Reward reward : rewards) {
            rewardsJson.add(CodecUtil.encode(Reward.CODEC, reward));
        }

        object.add("rewards", rewardsJson);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getType() {
        return serializer;
    }

    // We don't need an achievement cause this is basically a simplified loot table

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
