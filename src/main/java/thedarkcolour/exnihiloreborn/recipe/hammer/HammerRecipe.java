package thedarkcolour.exnihiloreborn.recipe.hammer;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class HammerRecipe extends RewardRecipe {
    public HammerRecipe(ResourceLocation id, Ingredient ingredient, ImmutableList<Reward> rewards) {
        super(id, ingredient, rewards);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.HAMMER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.HAMMER.get();
    }
}