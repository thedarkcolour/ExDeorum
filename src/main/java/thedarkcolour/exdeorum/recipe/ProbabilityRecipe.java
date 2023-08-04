package thedarkcolour.exdeorum.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public abstract class ProbabilityRecipe extends SingleIngredientRecipe {
    public final Item result;
    public final NumberProvider resultAmount;

    public ProbabilityRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(id, ingredient);
        this.result = result;
        this.resultAmount = resultAmount;
    }
}
