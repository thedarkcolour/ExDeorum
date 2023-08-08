/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.ter.BarrelRenderer;
import thedarkcolour.exdeorum.client.ter.CrucibleRenderer;
import thedarkcolour.exdeorum.client.ter.InfestedLeavesRenderer;
import thedarkcolour.exdeorum.client.ter.SieveRenderer;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.network.ClientMessageHandler;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;

import java.io.IOException;
import java.io.ObjectInputFilter;

public class ClientHandler {
    public static void register() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var fmlBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(ClientHandler::clientSetup);
        modBus.addListener(ClientHandler::registerRenderers);
        modBus.addListener(ClientHandler::registerShaders);
        modBus.addListener(ClientHandler::addClientReloadListeners);
        fmlBus.addListener(ClientHandler::onPlayerRespawn);
        fmlBus.addListener(ClientHandler::onPlayerLogout);
        modBus.addListener(ClientHandler::onConfigChanged);
    }

    private static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(RenderUtil::reload, gameExecutor);
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

    private static void onConfigChanged(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == EConfig.CLIENT_SPEC) {
            RenderSystem.recordRenderCall(() -> {
                Minecraft.getInstance().levelRenderer.allChanged();
            });
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBlockEntities.INFESTED_LEAVES.get(), ctx -> new InfestedLeavesRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.BARREL.get(), BarrelRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.LAVA_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.WATER_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), SieveRenderer::new);
    }

    private static void registerShaders(RegisterShadersEvent event) {
        try {
            // NEW_ENTITY is BLOCK except it also uses UV1 (overlay coordinates)
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(ExDeorum.ID, "rendertype_tinted_cutout_mipped"), DefaultVertexFormat.NEW_ENTITY), (instance) -> {
                RenderUtil.renderTypeTintedCutoutMippedShader = instance;
            });
        } catch (IOException e) {
            ExDeorum.LOGGER.error("Unable to load tinted shader", e);
        }
    }

    private static void setRenderLayers() {
        // Fluids
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER_FLOWING.get(), RenderType.translucent());
    }
}
