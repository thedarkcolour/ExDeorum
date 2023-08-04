package thedarkcolour.exnihiloreborn.recipe.crucible;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.recipe.CodecUtil;
import thedarkcolour.exnihiloreborn.recipe.SingleIngredientRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;

import javax.annotation.Nullable;

public class CrucibleRecipe extends SingleIngredientRecipe {
    private final RecipeType<?> type;
    private final FluidStack result;

    public CrucibleRecipe(ResourceLocation id, RecipeType<?> type, Ingredient ingredient, FluidStack result) {
        super(id, ingredient);
        this.type = type;
        this.result = result;

        if (this.dependsOnNbt) {
            throw new IllegalArgumentException("Cannot use NBT to determine Ex Nihilo Reborn Crucible output");
        }
    }

    public FluidStack getResult() {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.LAVA_CRUCIBLE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        private final RecipeType<?> type;

        public Serializer(RecipeType<?> type) {
            this.type = type;
        }

        @Override
        public CrucibleRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            FluidStack stack = CodecUtil.decode(FluidStack.CODEC, json.get("fluid"));

            return new CrucibleRecipe(id, type, ingredient, stack);
        }

        @Nullable
        @Override
        public CrucibleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new CrucibleRecipe(id, type, Ingredient.fromNetwork(buffer), FluidStack.readFromPacket(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrucibleRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            recipe.getResult().writeToPacket(buffer);
        }
    }
}
