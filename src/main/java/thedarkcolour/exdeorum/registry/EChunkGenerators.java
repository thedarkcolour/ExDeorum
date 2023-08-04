package thedarkcolour.exdeorum.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.voidworld.VoidChunkGenerator;

public class EChunkGenerators {
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, ExDeorum.ID);

    public static final RegistryObject<Codec<? extends ChunkGenerator>> VOID = CHUNK_GENERATORS.register("void", () -> VoidChunkGenerator.CODEC);
}
