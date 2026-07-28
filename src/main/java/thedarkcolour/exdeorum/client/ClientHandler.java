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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.asm.ASMHooks;
import thedarkcolour.exdeorum.client.screen.MechanicalHammerScreen;
import thedarkcolour.exdeorum.client.screen.MechanicalSieveScreen;
import thedarkcolour.exdeorum.client.ter.*;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.fluid.WitchWaterFluid;
import thedarkcolour.exdeorum.item.WateringCanItem;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.EMenus;

public class ClientHandler {
    public static boolean isInVoidWorld;
    // This is used to prevent Ex Deorum from resetting world type when trying to configure Superflat, Single Biome, etc.
    public static Holder<WorldPreset> originalDefaultWorldPreset;

    public static void register(IEventBus modBus) {
        var fmlBus = NeoForge.EVENT_BUS;

        modBus.addListener(ClientHandler::registerMenuScreens);
        modBus.addListener(ClientHandler::registerRenderers);
        modBus.addListener(ClientHandler::registerClientExtensions);
        modBus.addListener(ClientHandler::registerFluidModels);
        modBus.addListener(ClientHandler::addClientReloadListeners);
        modBus.addListener(ClientHandler::onConfigChanged);
        fmlBus.addListener(ClientHandler::onPlayerRespawn);
        fmlBus.addListener(ClientHandler::onPlayerLogout);
        fmlBus.addListener(ClientHandler::onScreenOpen);
        fmlBus.addListener(ClientHandler::onRecipesReceived);

        if (ExDeorum.DEBUG) {
            fmlBus.addListener(ClientHandler::handleDebugCommands);
        }
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new WitchWaterFluid.ClientExtensions(), EFluids.WITCH_WATER_TYPE.get());
        event.registerItem(new WateringCanItem.ClientExtensions(), EItems.WATERING_CANS.stream().map(DeferredItem::asItem).toArray(Item[]::new));
    }

    private static void registerFluidModels(RegisterFluidModelsEvent event) {
        event.register(
                new FluidModel.Unbaked(
                        new Material(WitchWaterFluid.STILL_TEXTURE),
                        new Material(WitchWaterFluid.FLOWING_TEXTURE),
                        new Material(WitchWaterFluid.OVERLAY_TEXTURE),
                        null
                ),
                EFluids.WITCH_WATER,
                EFluids.WITCH_WATER_FLOWING
        );
    }

    private static void addClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(ExDeorum.loc("render_util"), (ResourceManagerReloadListener) _ -> RenderUtil.reload());
    }

    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(EMenus.MECHANICAL_SIEVE.get(), MechanicalSieveScreen::new);
        event.register(EMenus.MECHANICAL_HAMMER.get(), MechanicalHammerScreen::new);
    }

    private static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        if (isInVoidWorld) {
            disableVoidFogRendering();
        }
    }

    private static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        isInVoidWorld = false;
    }

    private static void onConfigChanged(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == EConfig.CLIENT_SPEC) {
            Minecraft.getInstance().levelExtractor.allChanged();
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBlockEntities.INFESTED_LEAVES.get(), InfestedLeavesRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.BARREL.get(), BarrelRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.LAVA_CRUCIBLE.get(), _ -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.WATER_CRUCIBLE.get(), _ -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), _ -> new SieveRenderer<>(0.75f, 15f));
        event.registerBlockEntityRenderer(EBlockEntities.MECHANICAL_SIEVE.get(), _ -> new SieveRenderer<>(0.75f, 15f));
        event.registerBlockEntityRenderer(EBlockEntities.COMPRESSED_SIEVE.get(), _ -> new CompressedSieveRenderer<>(0.5625f, 16f));
    }

    // Sets Ex Deorum world type as default
    private static void onScreenOpen(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof CreateWorldScreen screen && EConfig.COMMON.setVoidWorldAsDefault.get()) {
            var uiState = screen.getUiState();
            var originalPreset = uiState.getWorldType().preset();

            if (originalPreset != null) {
                if (originalDefaultWorldPreset == null) {
                    originalDefaultWorldPreset = originalPreset;
                }
                if (originalDefaultWorldPreset.unwrapKey().equals(originalPreset.unwrapKey())) {
                    var voidWorldPreset = uiState.getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).get(ASMHooks.overrideDefaultWorldPreset()).orElse(null);
                    uiState.setWorldType(new WorldCreationUiState.WorldTypeEntry(voidWorldPreset));
                }
            }
        }
    }

    private static void onRecipesReceived(RecipesReceivedEvent event) {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            RecipeUtil.reload(event.getRecipeMap());
        }
    }

    public static void disableVoidFogRendering() {
        isInVoidWorld = true;

        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.clientLevelData.isFlat = true;
        }
    }

    public static void handleDebugCommands(ClientChatEvent event) {
        if (event.getMessage().equals(".compost_colors")) {
            event.setCanceled(true);

            ClientsideCode.debugCompute();
            CompostColors.export(ModIds.MINECRAFT);

            CompostColors.loadColors();
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("Reloaded " + CompostColors.COLORS.size() + " compost colors!"));
            }
        } else if (event.getMessage().equals(".breakpoint")) {
            event.setCanceled(true);
        }
    }
}
