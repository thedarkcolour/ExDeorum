package thedarkcolour.exnihiloreborn.recipe.sieve;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.recipe.ProbabilityRecipe;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class SieveRecipe extends ProbabilityRecipe {
    public final Item mesh;

    public SieveRecipe(ResourceLocation id, Ingredient ingredient, Item mesh, Item result, NumberProvider resultAmount) {
        super(id, ingredient, result, resultAmount);

        this.mesh = mesh;
    }

    public boolean test(Item mesh, ItemStack item) {
        return this.mesh == mesh && getIngredient().test(item);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.SIEVE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.SIEVE.get();
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        @Override
        public SieveRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Item mesh = RecipeUtil.readItem(json, "mesh");
            Item result = RecipeUtil.readItem(json, "result");
            NumberProvider resultAmount = RecipeUtil.readNumberProvider(json, "result_amount");
            return new SieveRecipe(id, ingredient, mesh, result, resultAmount);
        }

        @Override
        public @Nullable SieveRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item mesh = buffer.readById(BuiltInRegistries.ITEM);
            Item result = buffer.readById(BuiltInRegistries.ITEM);
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return new SieveRecipe(id, ingredient, mesh, result, resultAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SieveRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.mesh);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
        }
    }
}
