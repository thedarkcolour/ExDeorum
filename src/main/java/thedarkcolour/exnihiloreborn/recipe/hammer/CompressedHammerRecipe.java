package thedarkcolour.exnihiloreborn.recipe.hammer;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class CompressedHammerRecipe extends RewardRecipe {
    public CompressedHammerRecipe(ResourceLocation id, Ingredient ingredient, ImmutableList<Reward> rewards) {
        super(id, ingredient, rewards);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.COMPRESSED_HAMMER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ERecipeTypes.COMPRESSED_HAMMER;
    }
}

