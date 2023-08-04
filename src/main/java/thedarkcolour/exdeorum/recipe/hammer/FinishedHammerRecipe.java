package thedarkcolour.exdeorum.recipe.hammer;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.recipe.EFinishedRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;

public class FinishedHammerRecipe implements EFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Item result;
    private final NumberProvider resultAmount;

    public FinishedHammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.resultAmount = resultAmount;
    }

    @Override
    public void serializeRecipeData(JsonObject object) {
        object.add("ingredient", ingredient.toJson());
        object.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
        object.add("result_amount", LootDataType.PREDICATE.parser().toJsonTree(resultAmount));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ERecipeSerializers.HAMMER.get();
    }
}
