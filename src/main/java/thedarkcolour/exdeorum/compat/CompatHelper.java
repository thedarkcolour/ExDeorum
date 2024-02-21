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
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

public class CompatHelper {
    public static List<Item> getAvailableBarrels(boolean registered) {
        List<Item> barrels = new ArrayList<>();
        for (var material : DefaultMaterials.BARRELS) {
            if (registered == ModList.get().isLoaded(material.requiredModId)) {
                barrels.add(material.getItem());
            }
        }
        return barrels;
    }

    public static List<Item> getAvailableSieves(boolean registered, boolean includeMechanical) {
        List<Item> sieves = new ArrayList<>();
        for (var material : DefaultMaterials.SIEVES) {
            if (registered == ModList.get().isLoaded(material.requiredModId)) {
                sieves.add(material.getItem());
            }
        }
        if (includeMechanical) {
            sieves.add(EItems.MECHANICAL_SIEVE.get());
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
