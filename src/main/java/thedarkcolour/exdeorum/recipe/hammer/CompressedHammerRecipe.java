package thedarkcolour.exdeorum.recipe.hammer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class CompressedHammerRecipe extends HammerRecipe {
    private static final Codec<CompressedHammerRecipe> CODEC = RecordCodecBuilder.create(instance -> ProbabilityRecipe.commonFields(instance).apply(instance, CompressedHammerRecipe::new));

    public CompressedHammerRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount) {
        super(ingredient, result, resultAmount);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.COMPRESSED_HAMMER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.COMPRESSED_HAMMER.get();
    }

    public static class Serializer extends HammerRecipe.AbstractSerializer<CompressedHammerRecipe> {
        @Override
        protected CompressedHammerRecipe createHammerRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount) {
            return new CompressedHammerRecipe(ingredient, result, resultAmount);
        }

        @Override
        public Codec<CompressedHammerRecipe> codec() {
            return CODEC;
        }
    }
}
