package thedarkcolour.exnihiloreborn.recipe.sieve;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function4;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.recipe.CodecUtil;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;

import javax.annotation.Nullable;

public abstract class AbstractSieveRecipe extends RewardRecipe {
    public final Item mesh;

    public AbstractSieveRecipe(ResourceLocation id, Item mesh, Ingredient ingredient, ImmutableList<Reward> rewards) {
        super(id, ingredient, rewards);
        this.mesh = mesh;
    }

    public boolean test(Item mesh, ItemStack item) {
        return this.mesh == mesh && getIngredient().test(item);
    }

    public static class Serializer<T extends AbstractSieveRecipe> implements RecipeSerializer<T> {
        private final Function4<ResourceLocation, Item, Ingredient, ImmutableList<Reward>, T> factory;

        public Serializer(Function4<ResourceLocation, Item, Ingredient, ImmutableList<Reward>, T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation name, JsonObject json) {
            Item mesh = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "mesh")));
            Ingredient ingredient = readIngredient(json, "ingredient");

            // Rewards must be a list
            JsonArray rewardsJson = GsonHelper.getAsJsonArray(json, "rewards");
            ImmutableList.Builder<Reward> rewards = ImmutableList.builder();

            for (JsonElement element : rewardsJson) {
                rewards.add(CodecUtil.decode(Reward.CODEC, element));
            }

            return factory.apply(name, mesh, ingredient, rewards.build());
        }

        @Nullable
        @Override
        public T fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            var mesh = buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
            var ingredient = Ingredient.fromNetwork(buffer);
            var rewards = ImmutableList.<Reward>builder();

            for (int i = 0; i < buffer.readVarInt(); i++) {
                rewards.add(Reward.fromNetwork(buffer));
            }

            return factory.apply(name, mesh, ingredient, rewards.build());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, recipe.mesh);
            recipe.getIngredient().toNetwork(buffer);

            ImmutableList<Reward> rewards = recipe.getRewards();
            buffer.writeVarInt(rewards.size());

            for (Reward reward : rewards) {
                reward.toNetwork(buffer);
            }
        }
    }
}
