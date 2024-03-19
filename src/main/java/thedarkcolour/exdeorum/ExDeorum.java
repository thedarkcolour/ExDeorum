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

package thedarkcolour.exdeorum;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thedarkcolour.exdeorum.client.ClientHandler;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.Data;
import thedarkcolour.exdeorum.data.ModCompatData;
import thedarkcolour.exdeorum.event.EventHandler;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.registry.*;

import java.util.Calendar;

@Mod(ExDeorum.ID)
public class ExDeorum {
    public static final String ID = "exdeorum";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final boolean DEBUG = ModList.get().isLoaded("modkit");
    public static final boolean IS_JUNE = Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE;

    public ExDeorum(IEventBus modBus) {
        createRegistries(modBus);

        // Enable by default to avoid invisible milk in JEI
        NeoForgeMod.enableMilkFluid();

        if (DatagenModLoader.isRunningDataGen()) {
            ModCompatData.registerModData(modBus);
            modBus.addListener(Data::generateData);
        }

        // Game Events
        EventHandler.register(modBus);
        // Client init (todo test that this doesn't crash servers)
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientHandler.register(modBus);
        }
        // Config init
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EConfig.CLIENT_SPEC);
    }

    private static void createRegistries(IEventBus modBus) {
        EBlocks.BLOCKS.register(modBus);
        EBlockEntities.BLOCK_ENTITIES.register(modBus);
        EChunkGenerators.CHUNK_GENERATORS.register(modBus);
        ECreativeTabs.CREATIVE_TABS.register(modBus);
        EFluids.FLUID_TYPES.register(modBus);
        EFluids.FLUIDS.register(modBus);
        EGlobalLootModifiers.GLOBAL_LOOT_MODIFIERS.register(modBus);
        EItems.ITEMS.register(modBus);
        ELootFunctions.LOOT_FUNCTIONS.register(modBus);
        EMenus.MENUS.register(modBus);
        ERecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        ERecipeTypes.RECIPE_TYPES.register(modBus);
        ENumberProviders.NUMBER_PROVIDERS.register(modBus);
        DefaultMaterials.registerMaterials();
    }
}
