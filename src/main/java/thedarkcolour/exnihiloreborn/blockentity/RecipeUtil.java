package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import thedarkcolour.exnihiloreborn.recipe.SingleIngredientRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.AbstractSieveRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class RecipeUtil {
    public static <T extends AbstractSieveRecipe> List<T> getSieveResults(MinecraftServer server, RecipeType<T> type, ItemStack mesh, ItemStack item) {
        ArrayList<T> recipes = new ArrayList<>();

        for (T recipe : byType(server, type)) {
            if (recipe.test(mesh.getItem(), item)) {
                recipes.add(recipe);
            }
        }

        return recipes.isEmpty() ? Collections.emptyList() : recipes;
    }

    public static <T extends SingleIngredientRecipe> T getRecipe(MinecraftServer server, RecipeType<T> type, ItemStack item) {
        for (T recipe : byType(server, type)) {
            if (recipe.getIngredient().test(item)) {
                return recipe;
            }
        }

        return null;
    }

    public static <C extends Container, T extends Recipe<C>> Collection<T> byType(MinecraftServer server, RecipeType<T> type) {
        return server.getRecipeManager().byType(type).values();
    }
}
