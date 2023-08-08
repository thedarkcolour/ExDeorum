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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import thedarkcolour.exdeorum.ExDeorum;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExDeorum.ID, "channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, VoidWorldMessage.class, VoidWorldMessage::encode, VoidWorldMessage::decode, VoidWorldMessage::handle);
    }

    public static void sendVoidWorld(ServerPlayer pPlayer) {
        CHANNEL.sendTo(new VoidWorldMessage(), pPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    static void handle(Supplier<NetworkEvent.Context> ctxSupplier, Consumer<NetworkEvent.Context> handler) {
        var ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> handler.accept(ctx));
        ctx.setPacketHandled(true);
    }
}
