package thedarkcolour.exdeorum.compat.jei;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

class JeiUtil {
    public static IRecipeSlotBuilder addFluidIngredient(IRecipeSlotBuilder builder, SizedFluidIngredient fluid) {
        return addFluidIngredient(builder, fluid.ingredient(), fluid.amount());
    }

    public static IRecipeSlotBuilder addFluidIngredient(IRecipeSlotBuilder builder, FluidIngredient ingredient, int amount) {
        for (var stack : ingredient.getStacks()) {
            builder.addFluidStack(stack.getFluid(), amount);
        }
        return builder;
    }
}
