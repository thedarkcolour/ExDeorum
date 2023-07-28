package thedarkcolour.exnihiloreborn.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CodecUtil {
    public static <T> JsonElement encode(Codec<T> codec, T object) {
        return codec.encodeStart(JsonOps.INSTANCE, object).result().get();
    }

    public static <T> T decode(Codec<T> codec, JsonElement json) {
        return codec.parse(JsonOps.INSTANCE, json).result().get();
    }

    public static <T> Tag encodeNbt(Codec<T> codec, T object) {
        return codec.encodeStart(NbtOps.INSTANCE, object).result().get();
    }

    public static <T> T decodeNbt(Codec<T> codec, Tag json) {
        return codec.parse(NbtOps.INSTANCE, json).result().get();
    }
}
