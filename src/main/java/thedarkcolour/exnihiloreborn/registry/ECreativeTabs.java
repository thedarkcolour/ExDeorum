package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;

import java.util.function.Consumer;

public class ECreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExNihiloReborn.ID);

    public static final RegistryObject<CreativeModeTab> MAIN = register("main", ECreativeTabs::mainTab);

    private static RegistryObject<CreativeModeTab> register(String id, Consumer<CreativeModeTab.Builder> create) {
        return CREATIVE_TABS.register(id, () -> Util.make(new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0), create).build());
    }

    private static void mainTab(CreativeModeTab.Builder builder) {
        builder.icon(() -> new ItemStack(EItems.CROOK.get()));
        builder.title(Component.translatable(TranslationKeys.MAIN_CREATIVE_TAB));
        builder.withTabsBefore(CreativeModeTabs.SPAWN_EGGS);
        builder.displayItems((enabledFeatures, output) -> EItems.addItemsToMainTab(output));
    }
}
