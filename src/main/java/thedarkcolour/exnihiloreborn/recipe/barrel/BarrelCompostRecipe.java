package thedarkcolour.exnihiloreborn.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thedarkcolour.exnihiloreborn.recipe.SingleIngredientRecipe;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class BarrelCompostRecipe extends SingleIngredientRecipe {
    private final int volume;

    public BarrelCompostRecipe(ResourceLocation id, Ingredient ingredient, int volume) {
        super(id, ingredient);

        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_COMPOST.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ERecipeTypes.BARREL_COMPOST;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements RecipeSerializer<BarrelCompostRecipe> {
        @Override // Creates the recipe object from a JSON file
        public BarrelCompostRecipe fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient;

            if (JSONUtils.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
            }

            int volume = JSONUtils.getAsInt(json, "volume");

            return new BarrelCompostRecipe(name, ingredient, volume);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, BarrelCompostRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeVarInt(recipe.getVolume());
        }

        @Override
        public BarrelCompostRecipe fromNetwork(ResourceLocation name, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int volume = buffer.readVarInt();

            return new BarrelCompostRecipe(name, ingredient, volume);
        }
    }
}
