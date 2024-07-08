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

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkHandler {
    // DO NOT CONVERT the lambdas to method reference. The server will crash loading client code otherwise.
    @SuppressWarnings("Convert2MethodRef")
    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(MenuPropertyMessage.TYPE, MenuPropertyMessage.STREAM_CODEC, (msg, ctx) -> ClientMessageHandler.handleMenuProperty(msg, ctx));
        registrar.playToClient(VisualUpdateMessage.TYPE, VisualUpdateMessage.STREAM_CODEC, (msg, ctx) -> ClientMessageHandler.handleVisualUpdate(msg, ctx));
        // not sure if this stops working if they're in the wrong phase, so I'll put it in both
        registrar.commonToClient(VoidWorldMessage.TYPE, VoidWorldMessage.STREAM_CODEC, (msg, ctx) -> ClientMessageHandler.handleVoidWorldMessage(ctx));
    }

    public static void sendVoidWorld(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, VoidWorldMessage.INSTANCE);
    }

    public static void sendMenuProperty(ServerPlayer player, int containerId, int index, int prevSieveEnergy) {
        PacketDistributor.sendToPlayer(player, new MenuPropertyMessage(containerId, index, prevSieveEnergy));
    }
}
