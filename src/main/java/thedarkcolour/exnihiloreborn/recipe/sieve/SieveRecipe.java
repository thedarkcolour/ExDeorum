package thedarkcolour.exnihiloreborn.recipe.sieve;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class SieveRecipe extends AbstractSieveRecipe {
    public SieveRecipe(ResourceLocation id, Item mesh, Ingredient ingredient, ImmutableList<Reward> rewards) {
        super(id, mesh, ingredient, rewards);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.SIEVE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ERecipeTypes.SIEVE;
    }
}
