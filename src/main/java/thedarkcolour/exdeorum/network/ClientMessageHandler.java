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

package thedarkcolour.exdeorum.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;
import thedarkcolour.exdeorum.client.ClientHandler;
import thedarkcolour.exdeorum.menu.AbstractMachineMenu;

public class ClientMessageHandler {

    // Removes the black sky/fog that appears when the player is below y=62
    public static void handleVoidWorldMessage(VoidWorldMessage msg, IPayloadContext ctx) {
        ctx.workHandler().execute(ClientHandler::disableVoidFogRendering);
    }

    static void handleVisualUpdate(VisualUpdateMessage msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && level.getBlockEntity(msg.pos) instanceof EBlockEntity blockEntity) {
                if (msg.payload == null) {
                    if (blockEntity != msg.blockEntity && msg.blockEntity != null) {
                        blockEntity.copyVisualData(msg.blockEntity);
                    } else {
                        ExDeorum.LOGGER.warn("Failed syncing visual data from server for " + msg.pos.toShortString());
                    }
                } else {
                    blockEntity.readVisualData(msg.payload);
                }
            }
        });
    }

    public static void handleMenuProperty(MenuPropertyMessage msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            Player player = Minecraft.getInstance().player;

            if (player != null && player.containerMenu instanceof AbstractMachineMenu<?> menu && menu.containerId == msg.containerId()) {
                menu.setClientProperty(msg.index(), msg.value());
            }
        });
    }
}
