package thedarkcolour.exdeorum;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thedarkcolour.exdeorum.client.ClientHandler;
import thedarkcolour.exdeorum.config.Config;
import thedarkcolour.exdeorum.event.EventHandler;
import thedarkcolour.exdeorum.network.NetworkHandler;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.ECreativeTabs;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EGlobalLootModifiers;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ELootFunctions;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;
import thedarkcolour.exdeorum.registry.EChunkGenerators;

import java.util.Calendar;

@Mod(ExDeorum.ID)
public class ExDeorum {
    public static final String ID = "exdeorum";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final boolean DEBUG = ModList.get().isLoaded("modkit");
    public static final boolean IS_JUNE = Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE;

    public ExDeorum() {
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
