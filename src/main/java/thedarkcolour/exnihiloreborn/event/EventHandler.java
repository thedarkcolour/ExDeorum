package thedarkcolour.exnihiloreborn.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.blockentity.LavaCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.blockentity.WaterCrucibleBlockEntity;
import thedarkcolour.exnihiloreborn.client.CompostColors;
import thedarkcolour.exnihiloreborn.voidworld.VoidChunkGenerator;
import thedarkcolour.exnihiloreborn.compat.top.TopCompatExNihiloReborn;
import thedarkcolour.exnihiloreborn.item.HammerItem;
import thedarkcolour.exnihiloreborn.network.NetworkHandler;

import java.util.concurrent.CompletableFuture;

public final class EventHandler {
    public static void register() {
        var fmlBus = MinecraftForge.EVENT_BUS;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        fmlBus.addListener(EventHandler::onPlayerLogin);
        fmlBus.addListener(EventHandler::addReloadListeners);
        modBus.addListener(EventHandler::interModEnqueue);
        fmlBus.addListener(EventHandler::createSpawnTree);

        if (ExNihiloReborn.DEBUG) {
            fmlBus.addListener(EventHandler::handleDebugCommands);
        }
    }

    private static void handleDebugCommands(ClientChatEvent event) {
        if (event.getMessage().equals(".compost_colors")) {
            event.setCanceled(true);

            try {
                CompostColors.loadColors();
            } catch (Exception e) {
                ExNihiloReborn.LOGGER.error("Failed to load vanilla compost colors", e);
            }
        }
    }

    private static void createSpawnTree(LevelEvent.CreateSpawnPosition event) {
        if (event.getLevel() instanceof ServerLevel level) {
            // todo have config option for more kinds of platforms
            var rand = new XoroshiroRandomSource(level.getSeed());
            var pos = new BlockPos.MutableBlockPos(rand.nextIntBetweenInclusive(-200, 200), 64, rand.nextIntBetweenInclusive(-200, 200));
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            pos.move(0, 1, 0);

            // grow tree, has 5% chance to spawn bees based on world seed
            var feature = TreeFeatures.OAK_BEES_005;
            Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(feature).orElse(null);
            if (holder == null || !holder.value().place(level, level.getChunkSource().getGenerator(), rand, pos)) {
                ExNihiloReborn.LOGGER.error("Failed to generate spawn tree :(");
            } else {
                ExNihiloReborn.LOGGER.info("Generated spawn tree at {}", pos);
            }
            event.setCanceled(true);
            event.getSettings().setSpawn(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos), 90.0F);
            ((ServerLevel) event.getLevel()).getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, level.getServer());
        }
    }

    private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.serverLevel().getChunkSource().getGenerator() instanceof VoidChunkGenerator) {
                NetworkHandler.sendVoidWorld(player);
                var advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation(ExNihiloReborn.ID, "core/root"));
                if (advancement != null) {
                    player.getAdvancements().award(advancement, "in_void_world");
                } else {
                    ExNihiloReborn.LOGGER.error("Unable to grant player the Void World advancement. Ex Nihilo Reborn advancements will not show");
                }
            }
        }
    }

    // Send messages to other mods
    public static void interModEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopCompatExNihiloReborn::new);
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        var recipes = event.getServerResources().getRecipeManager();
        event.addListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            return CompletableFuture.allOf().thenCompose(prepBarrier::wait).thenRunAsync(() -> {
                LavaCrucibleBlockEntity.RECIPES_CACHE.invalidateAll();
                WaterCrucibleBlockEntity.RECIPES_CACHE.invalidateAll();
                HammerItem.refreshValidBlocks(recipes);
            }, gameExecutor);
        });
    }
}
