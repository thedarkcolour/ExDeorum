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

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkHandler.handle(ctxSupplier, ctx -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientMessageHandler::disableVoidFogRendering);
        });
    }
}
