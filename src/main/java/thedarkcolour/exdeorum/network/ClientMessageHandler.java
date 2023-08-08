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

package thedarkcolour.exdeorum.network;

import net.minecraft.client.Minecraft;

public class ClientMessageHandler {
    public static boolean isInVoidWorld;

    // Removes the black sky/fog that appears when the player is below y=62
    public static void disableVoidFogRendering() {
        isInVoidWorld = true;

        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.clientLevelData.isFlat = true;
        }
    }
}
