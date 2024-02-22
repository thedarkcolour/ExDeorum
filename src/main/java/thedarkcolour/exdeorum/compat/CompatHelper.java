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
        List<Item> lavaCrucibles = new ArrayList<>();

        for (var material : DefaultMaterials.LAVA_CRUCIBLES) {
            if (registered == ModList.get().isLoaded(material.requiredModId)) {
                lavaCrucibles.add(material.getItem());
            }
        }

        return lavaCrucibles;
    }

    public static List<Item> getAvailableWaterCrucibles(boolean registered) {
        List<Item> waterCrucibles = new ArrayList<>();

        for (var material : DefaultMaterials.WATER_CRUCIBLES) {
            if (registered == ModList.get().isLoaded(material.requiredModId)) {
                waterCrucibles.add(material.getItem());
            }
        }


        return waterCrucibles;
    }
}
