package thedarkcolour.exdeorum.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.compat.ModIds;

// registers dummy items so that data generation can reference modded items without needing those mods installed.
public class ModCompatData {
    public static RegistryObject<Item> GRAINS_OF_INFINITY = null;

    public static void registerModData() {
        registerEnderIO();
    }

    private static void registerEnderIO() {
        var deferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, ModIds.ENDERIO);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());

        GRAINS_OF_INFINITY = deferredRegister.register("grains_of_infinity", () -> new Item(new Item.Properties()));
    }
}
