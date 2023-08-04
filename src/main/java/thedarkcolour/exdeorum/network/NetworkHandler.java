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
