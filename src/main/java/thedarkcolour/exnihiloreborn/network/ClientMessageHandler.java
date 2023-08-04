package thedarkcolour.exnihiloreborn.network;

import net.minecraft.client.Minecraft;

public class ClientMessageHandler {
    public static boolean isInVoidWorld;

    // Removes the black sky/fog that appears when the player is below y=62
    public static void disableVoidFogRendering() {
        isInVoidWorld = true;

        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.clientLevelData.isFlat = true;
        }
    }
}
