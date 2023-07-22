package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

@JeiPlugin
public class ExNihiloRebornPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExNihiloReborn.ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

    }
}
