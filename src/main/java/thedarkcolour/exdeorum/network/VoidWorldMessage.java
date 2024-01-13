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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Server -> Client
// used to tell the client to disable the cave darkness rendering in a void world
public class VoidWorldMessage {
    public static void encode(VoidWorldMessage msg, FriendlyByteBuf packet) {
    }

    public static VoidWorldMessage decode(FriendlyByteBuf packet) {
        return new VoidWorldMessage();
    }

    public static void handle(VoidWorldMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkHandler.handle(ctxSupplier, ctx -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientMessageHandler::disableVoidFogRendering);
        });
    }
}
