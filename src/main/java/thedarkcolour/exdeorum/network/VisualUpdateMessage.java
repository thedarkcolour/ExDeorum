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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;

import java.util.function.Supplier;

class VisualUpdateMessage {
    final BlockEntityType<?> blockEntityType;
    final BlockPos pos;
    // Null on the client side
    @Nullable
    private final EBlockEntity blockEntity;
    // Null on the server side
    @Nullable
    final FriendlyByteBuf payload;

    public VisualUpdateMessage(BlockPos pos, @Nullable EBlockEntity blockEntity, @Nullable FriendlyByteBuf payload) {
        this.pos = pos;
        this.blockEntity = blockEntity;
        // payload is saved on the client until it can be handled properly
        this.payload = payload;

        // If the payload is null, we're on the server. Hence, the block entity is nonnull.
        if (payload == null) {
            this.blockEntityType = blockEntity.getType();
        } else {
            this.blockEntityType = payload.readById(BuiltInRegistries.BLOCK_ENTITY_TYPE);
        }
    }

    public static void encode(VisualUpdateMessage msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeId(BuiltInRegistries.BLOCK_ENTITY_TYPE, msg.blockEntityType);
        // never null on the server side, where this packet is meant to be encoded
        msg.blockEntity.writeVisualData(buffer);
    }

    public static VisualUpdateMessage decode(FriendlyByteBuf buffer) {
        return new VisualUpdateMessage(buffer.readBlockPos(), null, buffer);
    }

    public static void handle(VisualUpdateMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkHandler.handle(ctxSupplier, ctx -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientMessageHandler.handleVisualUpdate(msg));
        });
    }
}
