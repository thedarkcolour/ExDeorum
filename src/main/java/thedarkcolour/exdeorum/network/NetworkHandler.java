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
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public final class NetworkHandler {
    public static void register(IPayloadRegistrar registrar) {
        registrar.play(MenuPropertyMessage.ID, MenuPropertyMessage::decode, sidedHandler -> {
            sidedHandler.client((msg, ctx) -> ClientMessageHandler.handleMenuProperty(msg));
        });
        registrar.play(VisualUpdateMessage.ID, VisualUpdateMessage::decode, sidedHandler -> {
            sidedHandler.client((msg, ctx) -> ClientMessageHandler.handleVisualUpdate(msg));
        });
        // not sure if these stop working if they're in the wrong phase, so I'll put them in both
        registrar.common(VoidWorldMessage.ID, buffer -> VoidWorldMessage.INSTANCE, sidedHandler -> {
            sidedHandler.client((msg, ctx) -> ClientMessageHandler.disableVoidFogRendering());
        });
        registrar.common(RecipeCacheResetMessage.ID, buffer -> RecipeCacheResetMessage.INSTANCE, sidedHandler -> {
            sidedHandler.client((msg, ctx) -> ClientMessageHandler.reloadClientRecipeCache());
        });
    }

    public static void sendVoidWorld(ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(VoidWorldMessage.INSTANCE);
    }

    public static void sendRecipeCacheReset(ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(RecipeCacheResetMessage.INSTANCE);
    }

    public static void sendMenuProperty(ServerPlayer player, int containerId, int index, int prevSieveEnergy) {
        PacketDistributor.PLAYER.with(player).send(new MenuPropertyMessage(containerId, index, prevSieveEnergy));
    }
}
