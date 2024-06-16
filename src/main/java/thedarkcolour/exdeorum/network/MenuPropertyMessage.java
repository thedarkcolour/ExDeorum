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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import thedarkcolour.exdeorum.ExDeorum;

// Like ClientboundContainerSetDataPacket except that the value is 32 bits instead of 16 bits
public record MenuPropertyMessage(int containerId, int index, int value) implements CustomPacketPayload {
    public static final Type<MenuPropertyMessage> TYPE = new Type<>(ExDeorum.loc("menu_property"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MenuPropertyMessage> STREAM_CODEC = StreamCodec.of(MenuPropertyMessage::write, MenuPropertyMessage::decode);

    public static void write(RegistryFriendlyByteBuf buffer, MenuPropertyMessage msg) {
        buffer.writeByte(msg.containerId);
        buffer.writeShort(msg.index);
        buffer.writeVarInt(msg.value);
    }

    public static MenuPropertyMessage decode(RegistryFriendlyByteBuf buffer) {
        return new MenuPropertyMessage(buffer.readByte(), buffer.readShort(), buffer.readVarInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
