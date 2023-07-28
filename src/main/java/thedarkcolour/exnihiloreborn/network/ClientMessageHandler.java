package thedarkcolour.exnihiloreborn.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import sun.misc.Unsafe;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

import java.lang.reflect.Field;

public class ClientMessageHandler {
    private static final Field CLIENT_LEVEL_DATA_FIELD;
    private static final Field IS_FLAT_FIELD;
    private static final boolean PATCH_VOID;
    private static final Unsafe UNSAFE;

    public static boolean isInVoidWorld;

    static {
        Field clientLevelDataField = null;
        Field isFlatField = null;
        Unsafe unsafe = null;

        try {
            clientLevelDataField = ObfuscationReflectionHelper.findField(ClientLevel.class, "f_104563_");
            isFlatField = ObfuscationReflectionHelper.findField(ClientLevel.ClientLevelData.class, "f_104832_");

            // copied from UnsafeHacks
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe)theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            ExNihiloReborn.LOGGER.error("Error: Could not patch void renderer. Please open an issue on ExNihiloReborn GitHub!");
        }
        CLIENT_LEVEL_DATA_FIELD = clientLevelDataField;
        IS_FLAT_FIELD = isFlatField;
        UNSAFE = unsafe;
        PATCH_VOID = CLIENT_LEVEL_DATA_FIELD != null && IS_FLAT_FIELD != null && UNSAFE != null;
    }

    // Removes the black sky/fog that appears when the player is below y=62
    public static void disableVoidFogRendering() {
        isInVoidWorld = true;

        if (PATCH_VOID) {
            var obj = UnsafeHacks.getField(CLIENT_LEVEL_DATA_FIELD, Minecraft.getInstance().level);
            long offset = UNSAFE.objectFieldOffset(IS_FLAT_FIELD);
            UNSAFE.putBoolean(obj, offset, true);
        }
    }
}
