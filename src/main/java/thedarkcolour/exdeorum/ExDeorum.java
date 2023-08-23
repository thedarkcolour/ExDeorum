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
import thedarkcolour.exdeorum.config.EConfig;
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
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientHandlerRegistrar::register);
        // Config init
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EConfig.CLIENT_SPEC);
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

    private interface ClientHandlerRegistrar {
        private static void register() {
            ClientHandler.register();
        }
    }
}
