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

import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.material.MaterialRegistry;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.ArrayList;
import java.util.List;

public class CompatHelper {
    public static List<ItemLike> getAvailableBarrels(boolean registered) {
        return getAvailableMaterials(DefaultMaterials.BARRELS, registered);
    }

    public static List<ItemLike> getAvailableSieves(boolean registered, boolean includeMechanical) {
        List<ItemLike> sieves = getAvailableMaterials(DefaultMaterials.SIEVES, registered);
        if (includeMechanical) {
            sieves.add(EItems.MECHANICAL_SIEVE.get());
        }
        return sieves;
    }

    public static List<ItemLike> getAvailableLavaCrucibles(boolean registered) {
        return getAvailableMaterials(DefaultMaterials.LAVA_CRUCIBLES, registered);
    }

    public static List<ItemLike> getAvailableWaterCrucibles(boolean registered) {
        return getAvailableMaterials(DefaultMaterials.WATER_CRUCIBLES, registered);
    }

    public static List<ItemLike> getAvailableCompressedSieves(boolean registered) {
        return getAvailableMaterials(DefaultMaterials.COMPRESSED_SIEVES, registered);
    }

    private static List<ItemLike> getAvailableMaterials(MaterialRegistry<?> registry, boolean registered) {
        List<ItemLike> materials = new ArrayList<>();

        for (var material : registry) {
            if (registered == ModList.get().isLoaded(material.requiredModId)) {
                materials.add(material);
            }
        }

        return materials;
    }
}
