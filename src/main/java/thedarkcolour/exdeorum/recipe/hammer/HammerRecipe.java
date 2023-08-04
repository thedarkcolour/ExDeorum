package thedarkcolour.exdeorum.recipe.hammer;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class HammerRecipe extends ProbabilityRecipe {
    public HammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(id, ingredient, result, resultAmount);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.HAMMER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.HAMMER.get();
    }

    public static class Serializer implements RecipeSerializer<HammerRecipe> {
        @Override
        public HammerRecipe fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Item result = RecipeUtil.readItem(json, "result");
            NumberProvider resultAmount = RecipeUtil.readNumberProvider(json, "result_amount");
            return new HammerRecipe(name, ingredient, result, resultAmount);
        }

        @Override
        public @Nullable HammerRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item result = buffer.readById(BuiltInRegistries.ITEM);
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            return new HammerRecipe(name, ingredient, result, resultAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, HammerRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
        }
    }
}
