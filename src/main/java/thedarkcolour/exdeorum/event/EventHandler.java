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

package thedarkcolour.exdeorum.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.item.WateringCanItem;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.voidworld.VoidChunkGenerator;
import thedarkcolour.exdeorum.compat.top.ExDeorumTopCompat;
import thedarkcolour.exdeorum.item.HammerItem;
import thedarkcolour.exdeorum.network.NetworkHandler;

public final class EventHandler {
    public static void register() {
        var fmlBus = MinecraftForge.EVENT_BUS;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        fmlBus.addListener(EventHandler::onPlayerLogin);
        fmlBus.addListener(EventHandler::addReloadListeners);
        modBus.addListener(EventHandler::interModEnqueue);
        fmlBus.addListener(EventHandler::createSpawnTree);
        modBus.addListener(EventHandler::onCommonSetup);

        if (ExDeorum.DEBUG) {
            fmlBus.addListener(EventHandler::handleDebugCommands);
        }
    }

    private static void handleDebugCommands(ClientChatEvent event) {
        if (event.getMessage().equals(".compost_colors")) {
            event.setCanceled(true);

            try {
                CompostColors.loadColors();
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Reloaded " + CompostColors.COLORS.size() + " compost colors!"), false);
            } catch (Exception e) {
                ExDeorum.LOGGER.error("Failed to load vanilla compost colors", e);
            }
        }
    }

    private static void createSpawnTree(LevelEvent.CreateSpawnPosition event) {
        if (event.getLevel() instanceof ServerLevel level && level.getChunkSource().getGenerator() instanceof VoidChunkGenerator) {
            // todo have config option for more kinds of platforms
            var rand = new XoroshiroRandomSource(level.getSeed());
            var pos = new BlockPos.MutableBlockPos(rand.nextIntBetweenInclusive(-200, 200), 64, rand.nextIntBetweenInclusive(-200, 200));
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            pos.move(0, 1, 0);

            // grow tree, has 5% chance to spawn bees based on world seed
            var feature = TreeFeatures.OAK_BEES_005;
            Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(feature).orElse(null);
            if (holder == null || !holder.value().place(level, level.getChunkSource().getGenerator(), rand, pos)) {
                ExDeorum.LOGGER.error("Failed to generate spawn tree :(");
            } else {
                ExDeorum.LOGGER.info("Generated spawn tree at {}", pos);
            }
            event.setCanceled(true);
            event.getSettings().setSpawn(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos), 90.0F);
            ((ServerLevel) event.getLevel()).getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, level.getServer());
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                    EFluids.WITCH_WATER_TYPE.get(),
                    fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : (EConfig.SERVER.witchWaterNetherrackGenerator.get() ? Blocks.NETHERRACK.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState())
            ));
        });
    }

    private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.serverLevel().getChunkSource().getGenerator() instanceof VoidChunkGenerator) {
                NetworkHandler.sendVoidWorld(player);
                var advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation(ExDeorum.ID, "core/root"));

                if (advancement != null && !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    player.getAdvancements().award(advancement, "in_void_world");
                    if (EConfig.SERVER.startingTorch.get()) {
                        player.getInventory().add(new ItemStack(Items.TORCH));
                    }
                    if (EConfig.SERVER.startingWateringCan.get()) {
                        player.getInventory().add(WateringCanItem.getFull(EItems.WOODEN_WATERING_CAN));
                    }
                } else {
                    ExDeorum.LOGGER.error("Unable to grant player the Void World advancement. Ex Nihilo Reborn advancements will not show");
                }
            }
        }
    }

    // Send messages to other mods
    public static void interModEnqueue(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIds.THE_ONE_PROBE)) {
            InterModComms.sendTo(ModIds.THE_ONE_PROBE, "getTheOneProbe", ExDeorumTopCompat::new);
        }
    }

    private static void addReloadListeners(AddReloadListenerEvent event) {
        var recipes = event.getServerResources().getRecipeManager();
        event.addListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
                HammerItem.refreshValidBlocks(recipes);
                RecipeUtil.reload(recipes);
            }, gameExecutor);
        });
    }
}
