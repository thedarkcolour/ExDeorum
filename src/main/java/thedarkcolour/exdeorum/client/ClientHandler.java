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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.FastColor;
import net.minecraft.util.Unit;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.asm.ASMHooks;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.model.InfestedLeavesBakedModel;
import thedarkcolour.exdeorum.client.screen.MechanicalHammerScreen;
import thedarkcolour.exdeorum.client.screen.MechanicalSieveScreen;
import thedarkcolour.exdeorum.client.ter.*;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EMenus;

public class ClientHandler {
    // Used for the composting recipe category in JEI
    public static final ModelResourceLocation OAK_BARREL_COMPOSTING = new ModelResourceLocation(ExDeorum.loc("item/oak_barrel_composting"), ModelResourceLocation.STANDALONE_VARIANT);
    public static boolean isInVoidWorld;
    // This is used to prevent Ex Deorum from resetting world type when trying to configure Superflat, Single Biome, etc.
    public static Holder<WorldPreset> originalDefaultWorldPreset;

    public static void register(IEventBus modBus) {
        var fmlBus = NeoForge.EVENT_BUS;

        modBus.addListener(ClientHandler::clientSetup);
        modBus.addListener(ClientHandler::registerMenuScreens);
        modBus.addListener(ClientHandler::registerRenderers);
        modBus.addListener(ClientHandler::addClientReloadListeners);
        modBus.addListener(ClientHandler::onConfigChanged);
        modBus.addListener(ClientHandler::onModelBake);
        modBus.addListener(ClientHandler::addBlockColorHandler);
        fmlBus.addListener(ClientHandler::onPlayerRespawn);
        fmlBus.addListener(ClientHandler::onPlayerLogout);
        fmlBus.addListener(ClientHandler::onScreenOpen);
        fmlBus.addListener(ClientHandler::onRecipesUpdated);

        if (ModList.get().isLoaded(ModIds.JEI) || ModList.get().isLoaded(ModIds.EMI)) {
            modBus.addListener(ClientHandler::registerAdditionalModels);
        }
    }

    private static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(RenderUtil::reload, gameExecutor);
        });
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::setRenderLayers);
    }

    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(EMenus.MECHANICAL_SIEVE.get(), MechanicalSieveScreen::new);
        event.register(EMenus.MECHANICAL_HAMMER.get(), MechanicalHammerScreen::new);
    }

    private static void setRenderLayers() {
        // Fluids
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(EFluids.WITCH_WATER_FLOWING.get(), RenderType.translucent());
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
            RenderSystem.recordRenderCall(() -> Minecraft.getInstance().levelRenderer.allChanged());
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBlockEntities.INFESTED_LEAVES.get(), ctx -> new InfestedLeavesRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.BARREL.get(), BarrelRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.LAVA_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.WATER_CRUCIBLE.get(), ctx -> new CrucibleRenderer());
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), ctx -> new SieveRenderer<>(0.75f, 15f));
        event.registerBlockEntityRenderer(EBlockEntities.MECHANICAL_SIEVE.get(), ctx -> new SieveRenderer<>(0.75f, 15f));
        event.registerBlockEntityRenderer(EBlockEntities.COMPRESSED_SIEVE.get(), ctx -> new CompressedSieveRenderer<>(0.5625f, 16f));
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
                    var voidWorldPreset = uiState.getSettings().worldgenLoadContext().registryOrThrow(Registries.WORLD_PRESET).getHolder(ASMHooks.overrideDefaultWorldPreset()).orElse(null);
                    uiState.setWorldType(new WorldCreationUiState.WorldTypeEntry(voidWorldPreset));
                }
            }
        }
    }

    // Only called when JEI is loaded, because this registers the recipe category icon models.
    private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(ExDeorum.loc("block/oak_barrel_composting"), ModelResourceLocation.STANDALONE_VARIANT));
        event.register(OAK_BARREL_COMPOSTING);
    }

    private static void onModelBake(ModelEvent.ModifyBakingResult event) {
        var model = new InfestedLeavesBakedModel();
        for (var state : EBlocks.INFESTED_LEAVES.get().getStateDefinition().getPossibleStates()) {
            var location = BlockModelShaper.stateToModelLocation(state);
            event.getModels().put(location, model);
        }
    }

    private static void addBlockColorHandler(RegisterColorHandlersEvent.Block event) {
        var blockColors = event.getBlockColors();
        event.register((state, level, pos, tintIndex) -> {
            int innerColor;
            if (level != null && pos != null && level.getBlockEntity(pos) instanceof InfestedLeavesBlockEntity infestedLeavesBlockEntity) {
                var mimicState = infestedLeavesBlockEntity.getMimic();
                try {
                    innerColor = blockColors.getColor(mimicState, level, pos, tintIndex);
                } catch (Exception e) {
                    // The block may be unhappy that the BlockState in the world (infested leaves) does not match
                    // the mimic state that was provided in the argument. In such cases, use the default foliage
                    // color resolver.
                    innerColor = level.getBlockTint(pos, BiomeColors.FOLIAGE_COLOR_RESOLVER);
                }
            } else {
                innerColor = FoliageColor.getDefaultColor();
            }
            int gray = (30 * FastColor.ARGB32.red(innerColor) + 59 * FastColor.ARGB32.green(innerColor) + 11 * FastColor.ARGB32.blue(innerColor)) / 100;
            return FastColor.ARGB32.color(255, gray, gray, gray);
        }, EBlocks.INFESTED_LEAVES.get());
    }

    private static void onRecipesUpdated(RecipesUpdatedEvent event) {
        if (!Minecraft.getInstance().isSingleplayer()) {
            RecipeUtil.reload(event.getRecipeManager());
        }
    }

    public static void disableVoidFogRendering() {
        isInVoidWorld = true;

        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.clientLevelData.isFlat = true;
        }
    }
}
