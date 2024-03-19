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

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;

// todo rewrite this to be less jank
class VisualUpdateMessage implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(ExDeorum.ID, "visual_update");

    final BlockEntityType<?> blockEntityType;
    final BlockPos pos;
    // Null on the dedicated client side
    @Nullable
    final EBlockEntity blockEntity;
    // Null on the server side
    @Nullable
    final FriendlyByteBuf payload;

    public VisualUpdateMessage(BlockPos pos, @Nullable EBlockEntity blockEntity, @Nullable BlockEntityType<?> blockEntityType, @Nullable FriendlyByteBuf payload) {
        this.pos = pos;
        this.blockEntity = blockEntity;
        // payload is saved on the client until it can be handled properly
        this.payload = payload;

        this.blockEntityType = blockEntityType;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeId(BuiltInRegistries.BLOCK_ENTITY_TYPE, this.blockEntityType);
        // write a placeholder value for the number of data bytes
        var dataBytesIndex = buffer.writerIndex();
        buffer.writeInt(0);
        // write data bytes
        this.blockEntity.writeVisualData(buffer);
        // set the correct number of data bytes
        var numDataBytes = buffer.writerIndex() - dataBytesIndex - 4;
        buffer.setInt(dataBytesIndex, numDataBytes);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static VisualUpdateMessage decode(FriendlyByteBuf buffer) {
        return new VisualUpdateMessage(buffer.readBlockPos(), null, buffer.readById(BuiltInRegistries.BLOCK_ENTITY_TYPE), new FriendlyByteBuf(buffer.readBytes(buffer.readInt())));
    }
}
