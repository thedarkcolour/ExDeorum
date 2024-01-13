/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.screen.MechanicalSieveScreen;
import thedarkcolour.exdeorum.client.ter.BarrelRenderer;
import thedarkcolour.exdeorum.client.ter.CrucibleRenderer;
import thedarkcolour.exdeorum.client.ter.InfestedLeavesRenderer;
import thedarkcolour.exdeorum.client.ter.SieveRenderer;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.network.ClientMessageHandler;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EMenus;
import thedarkcolour.exdeorum.registry.EWorldPresets;

import java.io.IOException;

public class ClientHandler {
    // Used for the composting recipe category in JEI
    public static final ResourceLocation OAK_BARREL_COMPOSTING = new ResourceLocation(ExDeorum.ID, "item/oak_barrel_composting");
    // This is set to true whenever the server tells a client to do so, then set back to false after cache is refreshed.
    public static boolean needsRecipeCacheRefresh;

    public static void register() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var fmlBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(ClientHandler::clientSetup);
        modBus.addListener(ClientHandler::registerRenderers);
        modBus.addListener(ClientHandler::registerShaders);
        modBus.addListener(ClientHandler::addClientReloadListeners);
        modBus.addListener(ClientHandler::onConfigChanged);
        fmlBus.addListener(ClientHandler::onPlayerRespawn);
        fmlBus.addListener(ClientHandler::onPlayerLogout);
        fmlBus.addListener(ClientHandler::onScreenOpen);
        fmlBus.addListener(ClientHandler::onTagsUpdated);

        if (ModList.get().isLoaded(ModIds.JEI)) {
            modBus.addListener(ClientHandler::registerAdditionalModels);
        }
    }

    private static void onTagsUpdated(TagsUpdatedEvent event) {
        if (needsRecipeCacheRefresh && Minecraft.getInstance().getConnection() != null) {
            RecipeUtil.reload(Minecraft.getInstance().getConnection().getRecipeManager());
            needsRecipeCacheRefresh = false;
        }
    }

    private static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(RenderUtil::reload, gameExecutor);
        });
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setRenderLayers();
            MenuScreens.register(EMenus.MECHANICAL_SIEVE.get(), MechanicalSieveScreen::new);
        });
    }

    private static void setRenderLayers() {
        // Fluids
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER_FLOWING.get(), RenderType.translucent());
    }

    private static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        if (ClientMessageHandler.isInVoidWorld) {
            ClientMessageHandler.disableVoidFogRendering();
        }
    }

    private static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientMessageHandler.isInVoidWorld = false;
        needsRecipeCacheRefresh = false;
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
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), ctx -> new SieveRenderer<>());
        event.registerBlockEntityRenderer(EBlockEntities.MECHANICAL_SIEVE.get(), ctx -> new SieveRenderer<>());
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

    // Sets Ex Deorum world type as default
    private static void onScreenOpen(ScreenEvent.Opening event) {
        if (EConfig.CLIENT.setVoidWorldAsDefault.get() && EConfig.COMMON.setVoidWorldAsDefault.get()) {
            if (event.getNewScreen() instanceof CreateWorldScreen screen) {
                var ctx = screen.getUiState().getSettings();
                screen.getUiState().setWorldType(new WorldCreationUiState.WorldTypeEntry(ctx.worldgenLoadContext().registryOrThrow(Registries.WORLD_PRESET).getHolder(EWorldPresets.VOID_WORLD).orElse(null)));
            }
        }
    }

    // Only called when JEI is loaded, because this registers the recipe category icon models.
    private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation(ExDeorum.ID, "block/oak_barrel_composting"));
        event.register(OAK_BARREL_COMPOSTING);
    }
}
