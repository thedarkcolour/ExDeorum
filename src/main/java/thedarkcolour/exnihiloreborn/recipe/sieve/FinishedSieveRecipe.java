package thedarkcolour.exnihiloreborn.recipe.sieve;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.recipe.CodecUtil;
import thedarkcolour.exnihiloreborn.recipe.Reward;

import javax.annotation.Nullable;

public class FinishedSieveRecipe implements FinishedRecipe {
    private final ResourceLocation id;
    private final Item mesh;
    private final Ingredient ingredient;
    private final ImmutableList<Reward> rewards;
    private final RecipeSerializer<?> serializer;

    public FinishedSieveRecipe(RecipeSerializer<?> serializer, ResourceLocation id, Item mesh, Ingredient ingredient, ImmutableList<Reward> rewards) {
        this.serializer = serializer;
        this.id = id;
        this.mesh = mesh;
        this.ingredient = ingredient;
        this.rewards = rewards;
    }

    @Override
    public void serializeRecipeData(JsonObject object) {
        object.addProperty("mesh", ForgeRegistries.ITEMS.getKey(mesh).toString());
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
    public RecipeSerializer<?> getType() {
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
