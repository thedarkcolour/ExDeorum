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

package thedarkcolour.exdeorum.data;

import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.modkit.data.MKItemModelProvider;

class ItemModels {
    public static void addItemModels(MKItemModelProvider models) {
        models.handheld(EItems.WOODEN_HAMMER);
        models.handheld(EItems.STONE_HAMMER);
        models.handheld(EItems.GOLDEN_HAMMER);
        models.handheld(EItems.IRON_HAMMER);
        models.handheld(EItems.DIAMOND_HAMMER);
        models.handheld(EItems.NETHERITE_HAMMER);
        models.handheld(EItems.COMPRESSED_WOODEN_HAMMER);
        models.handheld(EItems.COMPRESSED_STONE_HAMMER);
        models.handheld(EItems.COMPRESSED_GOLDEN_HAMMER);
        models.handheld(EItems.COMPRESSED_IRON_HAMMER);
        models.handheld(EItems.COMPRESSED_DIAMOND_HAMMER);
        models.handheld(EItems.COMPRESSED_NETHERITE_HAMMER);

        models.handheld(EItems.CROOK);
        models.handheld(EItems.BONE_CROOK);

        models.handheld(EItems.WOODEN_WATERING_CAN);
        models.handheld(EItems.STONE_WATERING_CAN);
        models.handheld(EItems.IRON_WATERING_CAN);
        models.handheld(EItems.GOLDEN_WATERING_CAN);
        models.handheld(EItems.DIAMOND_WATERING_CAN);
        models.handheld(EItems.NETHERITE_WATERING_CAN);

        models.generic2d(EItems.END_CAKE);
    }
}
