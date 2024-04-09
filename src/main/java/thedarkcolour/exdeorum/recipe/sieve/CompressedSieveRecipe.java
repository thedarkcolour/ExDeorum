package thedarkcolour.exdeorum.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class CompressedSieveRecipe extends SieveRecipe {
    private static final Codec<CompressedSieveRecipe> CODEC = RecordCodecBuilder.create(instance -> commonSieveFields(instance).apply(instance, CompressedSieveRecipe::new));

    public CompressedSieveRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount, Item mesh, boolean byHandOnly) {
        super(ingredient, result, resultAmount, mesh, byHandOnly);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.COMPRESSED_SIEVE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.COMPRESSED_SIEVE.get();
    }

    public static class Serializer extends SieveRecipe.AbstractSerializer<CompressedSieveRecipe> {
        @Override
        protected CompressedSieveRecipe createSieveRecipe(Ingredient ingredient, Item result, NumberProvider resultAmount, Item mesh, boolean byHandOnly) {
            return new CompressedSieveRecipe(ingredient, result, resultAmount, mesh, byHandOnly);
        }

        @Override
        public Codec<CompressedSieveRecipe> codec() {
            return CODEC;
        }
    }
}
