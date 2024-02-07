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

package thedarkcolour.exdeorum.compat;

import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

public class CompatHelper {
    public static List<Item> getAvailableBarrels(boolean registered) {
        // Vanilla barrels
        List<Item> barrels = registered ? Lists.newArrayList(EItems.OAK_BARREL.get(), EItems.SPRUCE_BARREL.get(), EItems.BIRCH_BARREL.get(), EItems.JUNGLE_BARREL.get(), EItems.ACACIA_BARREL.get(), EItems.DARK_OAK_BARREL.get(), EItems.MANGROVE_BARREL.get(), EItems.CHERRY_BARREL.get(), EItems.BAMBOO_BARREL.get(), EItems.CRIMSON_BARREL.get(), EItems.WARPED_BARREL.get(), EItems.STONE_BARREL.get()) : new ArrayList<>();
        ModList mods = ModList.get();

        if (mods.isLoaded(ModIds.BIOMES_O_PLENTY) == registered) {
            barrels.add(EItems.FIR_BARREL.get());
            barrels.add(EItems.REDWOOD_BARREL.get());
            barrels.add(EItems.MAHOGANY_BARREL.get());
            barrels.add(EItems.JACARANDA_BARREL.get());
            barrels.add(EItems.PALM_BARREL.get());
            barrels.add(EItems.WILLOW_BARREL.get());
            barrels.add(EItems.DEAD_BARREL.get());
            barrels.add(EItems.MAGIC_BARREL.get());
            barrels.add(EItems.UMBRAN_BARREL.get());
            barrels.add(EItems.HELLBARK_BARREL.get());
        }
        if (mods.isLoaded(ModIds.ARS_NOUVEAU) == registered) {
            barrels.add(EItems.ARCHWOOD_BARREL.get());
        }
        if (mods.isLoaded(ModIds.AETHER) == registered) {
            barrels.add(EItems.SKYROOT_BARREL.get());
        }
        if (mods.isLoaded(ModIds.BLUE_SKIES) == registered) {
            barrels.add(EItems.BLUEBRIGHT_BARREL.get());
            barrels.add(EItems.STARLIT_BARREL.get());
            barrels.add(EItems.FROSTBRIGHT_BARREL.get());
            barrels.add(EItems.COMET_BARREL.get());
            barrels.add(EItems.LUNAR_BARREL.get());
            barrels.add(EItems.DUSK_BARREL.get());
            barrels.add(EItems.MAPLE_BARREL.get());
            barrels.add(EItems.CRYSTALLIZED_BARREL.get());
        }

        return barrels;
    }

    public static List<Item> getAvailableSieves(boolean registered) {
        List<Item> sieves = registered ? Lists.newArrayList(
                EItems.OAK_SIEVE.get(),
                EItems.SPRUCE_SIEVE.get(),
                EItems.BIRCH_SIEVE.get(),
                EItems.JUNGLE_SIEVE.get(),
                EItems.ACACIA_SIEVE.get(),
                EItems.DARK_OAK_SIEVE.get(),
                EItems.MANGROVE_SIEVE.get(),
                EItems.CHERRY_SIEVE.get(),
                EItems.BAMBOO_SIEVE.get(),
                EItems.CRIMSON_SIEVE.get(),
                EItems.WARPED_SIEVE.get(),
                EItems.MECHANICAL_SIEVE.get()
        ) : new ArrayList<>();

        if (ModList.get().isLoaded(ModIds.BIOMES_O_PLENTY) == registered) {
            sieves.add(EItems.FIR_SIEVE.get());
            sieves.add(EItems.REDWOOD_SIEVE.get());
            sieves.add(EItems.MAHOGANY_SIEVE.get());
            sieves.add(EItems.JACARANDA_SIEVE.get());
            sieves.add(EItems.PALM_SIEVE.get());
            sieves.add(EItems.WILLOW_SIEVE.get());
            sieves.add(EItems.DEAD_SIEVE.get());
            sieves.add(EItems.MAGIC_SIEVE.get());
            sieves.add(EItems.UMBRAN_SIEVE.get());
            sieves.add(EItems.HELLBARK_SIEVE.get());
        }
        if (ModList.get().isLoaded(ModIds.ARS_NOUVEAU) == registered) {
            sieves.add(EItems.ARCHWOOD_SIEVE.get());
        }
        if (ModList.get().isLoaded(ModIds.AETHER) == registered) {
            sieves.add(EItems.SKYROOT_SIEVE.get());
        }
        if (ModList.get().isLoaded(ModIds.BLUE_SKIES) == registered) {
            sieves.add(EItems.BLUEBRIGHT_SIEVE.get());
            sieves.add(EItems.STARLIT_SIEVE.get());
            sieves.add(EItems.FROSTBRIGHT_SIEVE.get());
            sieves.add(EItems.COMET_SIEVE.get());
            sieves.add(EItems.LUNAR_SIEVE.get());
            sieves.add(EItems.DUSK_SIEVE.get());
            sieves.add(EItems.MAPLE_SIEVE.get());
            sieves.add(EItems.CRYSTALLIZED_SIEVE.get());
        }

        return sieves;
    }

    public static List<Item> getAvailableLavaCrucibles(boolean registered) {
        List<Item> lavaCrucibles = registered ? Lists.newArrayList(
                EItems.PORCELAIN_CRUCIBLE.get(),
                EItems.WARPED_CRUCIBLE.get(),
                EItems.CRIMSON_CRUCIBLE.get()
        ) : new ArrayList<>();

        if (ModList.get().isLoaded(ModIds.BIOMES_O_PLENTY) == registered) {
            lavaCrucibles.add(EItems.HELLBARK_CRUCIBLE.get());
        }
        if (ModList.get().isLoaded(ModIds.BLUE_SKIES) == registered) {
            lavaCrucibles.add(EItems.CRYSTALLIZED_CRUCIBLE.get());
        }

        return lavaCrucibles;
    }

    public static List<Item> getAvailableWaterCrucibles(boolean registered) {
        List<Item> waterCrucibles = registered ? Lists.newArrayList(
                EItems.OAK_CRUCIBLE.get(),
                EItems.SPRUCE_CRUCIBLE.get(),
                EItems.BIRCH_CRUCIBLE.get(),
                EItems.JUNGLE_CRUCIBLE.get(),
                EItems.ACACIA_CRUCIBLE.get(),
                EItems.DARK_OAK_CRUCIBLE.get(),
                EItems.MANGROVE_CRUCIBLE.get(),
                EItems.CHERRY_CRUCIBLE.get(),
                EItems.BAMBOO_CRUCIBLE.get()
        ) : new ArrayList<>();

        if (ModList.get().isLoaded(ModIds.BIOMES_O_PLENTY) == registered) {
            waterCrucibles.add(EItems.FIR_CRUCIBLE.get());
            waterCrucibles.add(EItems.REDWOOD_CRUCIBLE.get());
            waterCrucibles.add(EItems.MAHOGANY_CRUCIBLE.get());
            waterCrucibles.add(EItems.JACARANDA_CRUCIBLE.get());
            waterCrucibles.add(EItems.PALM_CRUCIBLE.get());
            waterCrucibles.add(EItems.WILLOW_CRUCIBLE.get());
            waterCrucibles.add(EItems.DEAD_CRUCIBLE.get());
            waterCrucibles.add(EItems.MAGIC_CRUCIBLE.get());
            waterCrucibles.add(EItems.UMBRAN_CRUCIBLE.get());
        }
        if (ModList.get().isLoaded(ModIds.ARS_NOUVEAU) == registered) {
            waterCrucibles.add(EItems.CASCADING_ARCHWOOD_CRUCIBLE.get());
            waterCrucibles.add(EItems.BLAZING_ARCHWOOD_CRUCIBLE.get());
            waterCrucibles.add(EItems.VEXING_ARCHWOOD_CRUCIBLE.get());
            waterCrucibles.add(EItems.FLOURISHING_ARCHWOOD_CRUCIBLE.get());
        }
        if (ModList.get().isLoaded(ModIds.AETHER) == registered) {
            waterCrucibles.add(EItems.SKYROOT_CRUCIBLE.get());
            waterCrucibles.add(EItems.GOLDEN_OAK_CRUCIBLE.get());
        }
        if (ModList.get().isLoaded(ModIds.BLUE_SKIES) == registered) {
            waterCrucibles.add(EItems.BLUEBRIGHT_CRUCIBLE.get());
            waterCrucibles.add(EItems.STARLIT_CRUCIBLE.get());
            waterCrucibles.add(EItems.FROSTBRIGHT_CRUCIBLE.get());
            waterCrucibles.add(EItems.COMET_CRUCIBLE.get());
            waterCrucibles.add(EItems.LUNAR_CRUCIBLE.get());
            waterCrucibles.add(EItems.DUSK_CRUCIBLE.get());
            waterCrucibles.add(EItems.MAPLE_CRUCIBLE.get());
        }

        return waterCrucibles;
    }
}
