package thedarkcolour.exnihiloreborn.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface EFinishedRecipe extends FinishedRecipe {
    @Nullable
    @Override
    default JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    default ResourceLocation getAdvancementId() {
        return null;
    }
}
