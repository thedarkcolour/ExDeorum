package thedarkcolour.exnihiloreborn.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exnihiloreborn.client.ter.BarrelRenderer;
import thedarkcolour.exnihiloreborn.client.ter.CrucibleRenderer;
import thedarkcolour.exnihiloreborn.client.ter.InfestedLeavesRenderer;
import thedarkcolour.exnihiloreborn.client.ter.SieveRenderer;
import thedarkcolour.exnihiloreborn.network.ClientMessageHandler;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EFluids;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public class ClientHandler {
    public static void register() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var fmlBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(ClientHandler::clientSetup);
        modBus.addListener(ClientHandler::registerRenderers);
        modBus.addListener(ClientHandler::addClientReloadListeners);
        fmlBus.addListener(ClientHandler::onPlayerRespawn);
        fmlBus.addListener(ClientHandler::onPlayerLogout);
    }

    private static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return CompletableFuture.allOf().thenCompose(prepBarrier::wait).thenRunAsync(RenderUtil::reload, gameExecutor);
        });
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::setRenderLayers);
    }

    private static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        if (ClientMessageHandler.isInVoidWorld) {
            ClientMessageHandler.disableVoidFogRendering();
        }
    }

    private static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientMessageHandler.isInVoidWorld = false;
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBlockEntities.INFESTED_LEAVES.get(), ctx -> new InfestedLeavesRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.BARREL.get(), BarrelRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.LAVA_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.WATER_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), SieveRenderer::new);
    }

    private static void setRenderLayers() {
        // Fluids
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER_STILL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER_FLOWING.get(), RenderType.translucent());
    }

    public static Color getRainbowColor(long time, float partialTicks) {
        return Color.getHSBColor((180 * Mth.sin((time + partialTicks) / 16.0f) - 180) / 360.0f, 0.7f, 0.8f);
    }
}
