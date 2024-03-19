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

package thedarkcolour.exdeorum.event;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ClientChatEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.helper.ItemHelper;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.compat.top.ExDeorumTopCompat;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.item.PorcelainBucket;
import thedarkcolour.exdeorum.item.WateringCanItem;
import thedarkcolour.exdeorum.material.BarrelMaterial;
import thedarkcolour.exdeorum.network.NetworkHandler;
import thedarkcolour.exdeorum.network.VisualUpdateTracker;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EBiomeTags;
import thedarkcolour.exdeorum.voidworld.VoidChunkGenerator;

import java.util.Locale;

public final class EventHandler {
    public static void register(IEventBus modBus) {
        var fmlBus = NeoForge.EVENT_BUS;

        fmlBus.addListener(EventHandler::onPlayerLogin);
        fmlBus.addListener(EventHandler::addReloadListeners);
        fmlBus.addListener(EventHandler::createSpawnTree);
        modBus.addListener(EventHandler::interModEnqueue);
        modBus.addListener(EventHandler::onCommonSetup);
        modBus.addListener(EventHandler::registerPayloadHandler);
        fmlBus.addListener(EventHandler::serverShutdown);
        fmlBus.addListener(EventHandler::serverTick);
        modBus.addListener(EventHandler::registerCapabilities);
        //fmlBus.addListener(EventHandler::onDatapackChanged);

        if (ExDeorum.DEBUG) {
            fmlBus.addListener(EventHandler::handleDebugCommands);
        }
    }

    private static void serverShutdown(ServerStoppingEvent event) {
        RecipeUtil.unload();
    }

    private static void handleDebugCommands(ClientChatEvent event) {
        if (event.getMessage().equals(".compost_colors")) {
            event.setCanceled(true);

            try {
                CompostColors.loadColors();
                var player = Minecraft.getInstance().player;
                if (player != null) {
                    player.displayClientMessage(Component.literal("Reloaded " + CompostColors.COLORS.size() + " compost colors!"), false);
                }
            } catch (Exception e) {
                ExDeorum.LOGGER.error("Failed to load vanilla compost colors", e);
            }
        } else if (event.getMessage().equals(".breakpoint")) {
            event.setCanceled(true);
        }
    }

    private static void createSpawnTree(LevelEvent.CreateSpawnPosition event) {
        if (event.getLevel() instanceof ServerLevel level && level.getChunkSource().getGenerator() instanceof VoidChunkGenerator) {
            var rand = new XoroshiroRandomSource(level.getSeed());
            var pos = new BlockPos.MutableBlockPos(rand.nextIntBetweenInclusive(-200, 200), 64, rand.nextIntBetweenInclusive(-200, 200));
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            pos.move(0, 1, 0);

            // grow tree, has 5% chance to spawn bees based on world seed
            var configuredFeatureRegistry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
            var defaultTreeFeature = TreeFeatures.OAK_BEES_005;
            var defaultTreeFeatureLoc = ResourceLocation.tryParse(EConfig.SERVER.defaultSpawnTreeFeature.get());

            Holder<ConfiguredFeature<?, ?>> holder = configuredFeatureRegistry.getHolder(defaultTreeFeature).orElse(null);

            if (defaultTreeFeatureLoc != null) {
                var value = configuredFeatureRegistry.getHolder(ResourceKey.create(Registries.CONFIGURED_FEATURE, defaultTreeFeatureLoc)).orElse(null);
                if (value != null) {
                    holder = value;
                }
            }

            if (EConfig.SERVER.useBiomeAppropriateTree.get()) {
                var biome = level.getBiome(pos);

                for (var entry : EBiomeTags.TREE_TAGS.entrySet()) {
                    if (biome.is(entry.getKey())) {
                        var optional = entry.getValue();

                        if (optional.isBound()) {
                            holder = optional;
                            break;
                        }
                    }
                }
            }

            if (holder == null || !holder.value().place(level, level.getChunkSource().getGenerator(), rand, pos)) {
                ExDeorum.LOGGER.error("Failed to generate spawn tree :(");
            } else {
                ExDeorum.LOGGER.debug("Generated spawn tree at {}", pos);
            }

            event.setCanceled(true);
            event.getSettings().setSpawn(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos), 90.0F);
            level.getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, level.getServer());
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                    EFluids.WITCH_WATER_TYPE.get(),
                    fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : (EConfig.SERVER.witchWaterNetherrackGenerator.get() ? Blocks.NETHERRACK.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState())
            ));
            var dirtVariants = new BlockState[]{Blocks.DIRT.defaultBlockState(), Blocks.PODZOL.defaultBlockState(), Blocks.COARSE_DIRT.defaultBlockState()};
            var rng = RandomSource.create();
            FluidInteractionRegistry.addInteraction(EFluids.WITCH_WATER_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                    (level, pos, relative, state) -> level.getFluidState(relative).getFluidType() == NeoForgeMod.WATER_TYPE.value() && EConfig.SERVER.witchWaterDirtGenerator.get(),
                    fluidState -> Util.getRandom(dirtVariants, rng)
            ));

            BarrelMaterial.loadTransparentBlocks();
        });
    }

    private static void registerPayloadHandler(RegisterPayloadHandlerEvent event) {
        NetworkHandler.register(event.registrar(ExDeorum.ID));
    }
    private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var generator = player.serverLevel().getChunkSource().getGenerator();

            // tries to account for other SkyBlock generator mods like SkyBlockBuilder
            if (generator instanceof VoidChunkGenerator || generator.getClass().getName().toLowerCase(Locale.ROOT).contains("skyblock")) {
                NetworkHandler.sendVoidWorld(player);
                var advancement = player.server.getAdvancements().get(new ResourceLocation(ExDeorum.ID, "core/root"));

                if (advancement != null) {
                    if (!player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                        player.getAdvancements().award(advancement, "in_void_world");
                        if (EConfig.SERVER.startingTorch.get()) {
                            player.getInventory().add(new ItemStack(Items.TORCH));
                        }
                        if (EConfig.SERVER.startingWateringCan.get()) {
                            player.getInventory().add(WateringCanItem.getFull(EItems.WOODEN_WATERING_CAN));
                        }
                    }
                } else {
                    ExDeorum.LOGGER.error("Unable to grant player the Void World advancement. Ex Deorum advancements will not show");
                }
            }
        } else {
            if (Minecraft.getInstance().getConnection() != null) {
                RecipeUtil.reload(Minecraft.getInstance().getConnection().getRecipeManager());
            }
        }
    }

    // Send messages to other mods
    private static void interModEnqueue(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIds.THE_ONE_PROBE)) {
            InterModComms.sendTo(ModIds.THE_ONE_PROBE, "getTheOneProbe", ExDeorumTopCompat::new);
        }
        if (ModList.get().isLoaded(ModIds.INVENTORY_SORTER)) {
            InterModComms.sendTo(ModIds.INVENTORY_SORTER, "slotblacklist", ItemHelper.Slot.class::getName);
        }
    }

    private static void addReloadListeners(AddReloadListenerEvent event) {
        var recipes = event.getServerResources().getRecipeManager();
        event.addListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
                RecipeUtil.reload(recipes);
            }, gameExecutor);
        });
    }

    private static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            VisualUpdateTracker.syncVisualUpdates();
        }
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntities.BARREL.get(), (barrel, direction) -> barrel.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, EBlockEntities.BARREL.get(), (barrel, direction) -> barrel.getTank());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntities.MECHANICAL_SIEVE.get(), (sieve, direction) -> sieve.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, EBlockEntities.MECHANICAL_SIEVE.get(), (sieve, direction) -> sieve.getEnergyStorage());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntities.MECHANICAL_HAMMER.get(), (hammer, direction) -> hammer.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, EBlockEntities.MECHANICAL_HAMMER.get(), (hammer, direction) -> hammer.getEnergyStorage());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntities.LAVA_CRUCIBLE.get(), (hammer, direction) -> hammer.getItem());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, EBlockEntities.LAVA_CRUCIBLE.get(), (hammer, direction) -> hammer.getTank());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntities.WATER_CRUCIBLE.get(), (hammer, direction) -> hammer.getItem());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, EBlockEntities.WATER_CRUCIBLE.get(), (hammer, direction) -> hammer.getTank());

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new PorcelainBucket.ItemHandler(stack),
                EItems.PORCELAIN_BUCKET,
                EItems.PORCELAIN_WATER_BUCKET,
                EItems.PORCELAIN_LAVA_BUCKET,
                EItems.PORCELAIN_MILK_BUCKET,
                EItems.PORCELAIN_WITCH_WATER_BUCKET);
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), EItems.WITCH_WATER_BUCKET);
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new WateringCanItem.FluidHandler(stack),
                EItems.WOODEN_WATERING_CAN,
                EItems.STONE_WATERING_CAN,
                EItems.IRON_WATERING_CAN,
                EItems.GOLDEN_WATERING_CAN,
                EItems.DIAMOND_WATERING_CAN,
                EItems.NETHERITE_WATERING_CAN);
    }
}
