package thedarkcolour.exnihiloreborn.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function3;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

public abstract class RewardRecipe extends SingleIngredientRecipe {
    private final ImmutableList<Reward> rewards;

    public RewardRecipe(ResourceLocation id, Ingredient ingredient, ImmutableList<Reward> rewards) {
        super(id, ingredient);
        this.rewards = rewards;
    }

    public ImmutableList<Reward> getRewards() {
        return rewards;
    }

    public static class Serializer<T extends RewardRecipe> implements RecipeSerializer<T> {
        private final Function3<ResourceLocation, Ingredient, ImmutableList<Reward>, T> factory;

        public Serializer(Function3<ResourceLocation, Ingredient, ImmutableList<Reward>, T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = readIngredient(json, "ingredient");

            // Rewards must be a list
            JsonArray rewardsJson = GsonHelper.getAsJsonArray(json, "rewards");
            ImmutableList.Builder<Reward> rewards = ImmutableList.builder();

            for (JsonElement element : rewardsJson) {
                rewards.add(CodecUtil.decode(Reward.CODEC, element));
            }

            return factory.apply(name, ingredient, rewards.build());
        }

        @Nullable
        @Override
        public T fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ImmutableList.Builder<Reward> rewards = ImmutableList.builder();

            for (int i = 0; i < buffer.readVarInt(); i++) {
                rewards.add(Reward.fromNetwork(buffer));
            }

            return factory.apply(name, ingredient, rewards.build());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.getIngredient().toNetwork(buffer);

            ImmutableList<Reward> rewards = recipe.getRewards();
            buffer.writeVarInt(rewards.size());

            for (Reward reward : rewards) {
                reward.toNetwork(buffer);
            }
        }
    }
}
