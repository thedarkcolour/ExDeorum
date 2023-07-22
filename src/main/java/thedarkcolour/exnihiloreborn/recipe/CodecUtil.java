package thedarkcolour.exnihiloreborn.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;

public class CodecUtil {
    public static <T> JsonElement encode(Codec<T> codec, T object) {
        return codec.encodeStart(JsonOps.INSTANCE, object).result().get();
    }

    public static <T> T decode(Codec<T> codec, JsonElement json) {
        return codec.parse(JsonOps.INSTANCE, json).result().get();
    }

    public static <T> INBT encodeNbt(Codec<T> codec, T object) {
        return codec.encodeStart(NBTDynamicOps.INSTANCE, object).result().get();
    }

    public static <T> T decodeNbt(Codec<T> codec, INBT json) {
        return codec.parse(NBTDynamicOps.INSTANCE, json).result().get();
    }
}
