package thedarkcolour.exdeorum.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * Represents a recipe that does not take place in any screen or container.
 * <p>
 * Has one ingredient by default and just tests off of that. Only the 1st slot
 * of any container will be checked, so only one slot should be present.
 */
public abstract class SingleIngredientRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    protected final Ingredient ingredient;
    public final boolean dependsOnNbt;

    public SingleIngredientRecipe(ResourceLocation id, Ingredient ingredient) {
        this.id = id;
        this.ingredient = ingredient;
        this.dependsOnNbt = !ingredient.isSimple();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(Container inventory, Level level) {
        return ingredient.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    /**
     * @deprecated Only used in Vanilla recipe books, and my blocks do not use the recipe book!
     */
    @Deprecated
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}
