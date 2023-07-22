package thedarkcolour.exnihiloreborn;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exnihiloreborn.blockentity.LavaCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.WaterCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.client.ClientHandler;
import thedarkcolour.exnihiloreborn.compat.top.TopCompatExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ELootFunctions;
import thedarkcolour.exnihiloreborn.registry.ERecipeSerializers;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import java.util.concurrent.CompletableFuture;

@Mod(ExNihiloReborn.ID)
public class ExNihiloReborn {
    public static final String ID = "exnihiloreborn";

    public ExNihiloReborn() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus fmlBus = MinecraftForge.EVENT_BUS;

        // Mod init
        ELootFunctions.init();
        ERecipeTypes.init();
        modBus.addListener(this::interModEnqueue);

        // Client init
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientHandler::register);

        // Registry Classes
        EBlockEntities.BLOCK_ENTITIES.register(modBus);
        EBlocks.BLOCKS.register(modBus);
        EFluids.FLUIDS.register(modBus);
        EItems.ITEMS.register(modBus);
        ERecipeSerializers.RECIPE_SERIALIZERS.register(modBus);

        // Game Events
        fmlBus.addListener(this::serverStart);
    }

    // Send messages to other mods
    public void interModEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopCompatExNihiloReborn::new);
    }

    public void serverStart(AddReloadListenerEvent event) {
        event.addListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return CompletableFuture.allOf().thenCompose(prepBarrier::wait).thenRunAsync(() -> {

                LavaCrucibleBlockEntity.RECIPES_CACHE.invalidateAll();
                WaterCrucibleBlockEntity.RECIPES_CACHE.invalidateAll();
            }, gameExecutor);
        });
    }
}
