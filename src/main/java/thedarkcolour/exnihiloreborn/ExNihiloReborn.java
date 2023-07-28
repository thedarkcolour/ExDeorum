package thedarkcolour.exnihiloreborn;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thedarkcolour.exnihiloreborn.client.ClientHandler;
import thedarkcolour.exnihiloreborn.config.Config;
import thedarkcolour.exnihiloreborn.event.EventHandler;
import thedarkcolour.exnihiloreborn.network.NetworkHandler;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.ECreativeTabs;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EGlobalLootModifiers;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ELootFunctions;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;
import thedarkcolour.exnihiloreborn.registry.EChunkGenerators;

import java.util.Calendar;

@Mod(ExNihiloReborn.ID)
public class ExNihiloReborn {
    public static final String ID = "exnihiloreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final boolean DEBUG = ModList.get().isLoaded("modkit");
    public static final boolean IS_JUNE = Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE;

    public ExNihiloReborn() {
        createRegistries();
        NetworkHandler.register();

        // Game Events
        EventHandler.register();
        // Client init
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientHandler::register);
        // Config init
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private static void createRegistries() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        EBlockEntities.BLOCK_ENTITIES.register(modBus);
        EBlocks.BLOCKS.register(modBus);
        EChunkGenerators.CHUNK_GENERATORS.register(modBus);
        ECreativeTabs.CREATIVE_TABS.register(modBus);
        EFluids.FLUID_TYPES.register(modBus);
        EFluids.FLUIDS.register(modBus);
        EGlobalLootModifiers.GLOBAL_LOOT_MODIFIERS.register(modBus);
        EItems.ITEMS.register(modBus);
        ELootFunctions.LOOT_FUNCTIONS.register(modBus);
        ERecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        ERecipeTypes.RECIPE_TYPES.register(modBus);
    }
}
