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
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import thedarkcolour.exdeorum.ExDeorum;

// Server -> Client
// used to tell the client to disable the cave darkness rendering in a void world
public enum VoidWorldMessage implements CustomPacketPayload {
    INSTANCE;

    public static final ResourceLocation ID = new ResourceLocation(ExDeorum.ID, "void_world_msg");

    @Override
    public void write(FriendlyByteBuf pBuffer) {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
