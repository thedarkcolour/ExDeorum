package thedarkcolour.exdeorum.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredBlock;
import thedarkcolour.exdeorum.block.CompressedBlockType;

import java.util.ArrayList;
import java.util.List;

public class ECompressedBlocks {
    public static final List<CompressedBlockType> ALL_VARIANTS = new ArrayList<>();

    public static final CompressedBlockType COMPRESSED_DIRT = register(Blocks.DIRT).withCompressium();
    public static final CompressedBlockType COMPRESSED_COBBLESTONE = register(Blocks.COBBLESTONE).withCompressium();
    public static final CompressedBlockType COMPRESSED_DIORITE = register(Blocks.DIORITE).withCompressium();
    public static final CompressedBlockType COMPRESSED_GRANITE = register(Blocks.GRANITE).withCompressium();
    public static final CompressedBlockType COMPRESSED_ANDESITE = register(Blocks.ANDESITE).withCompressium();
    public static final CompressedBlockType COMPRESSED_GRAVEL = register(Blocks.GRAVEL).withCompressium();
    public static final CompressedBlockType COMPRESSED_SAND = register(Blocks.SAND).withCompressium();
    public static final CompressedBlockType COMPRESSED_DUST = register(EBlocks.DUST);
    public static final CompressedBlockType COMPRESSED_RED_SAND = register(Blocks.RED_SAND).withCompressium();
    public static final CompressedBlockType COMPRESSED_DEEPSLATE = register(Blocks.DEEPSLATE);
    public static final CompressedBlockType COMPRESSED_COBBLED_DEEPSLATE = register(Blocks.COBBLED_DEEPSLATE);
    public static final CompressedBlockType COMPRESSED_NETHERRACK = register(Blocks.NETHERRACK).withCompressium();
    public static final CompressedBlockType COMPRESSED_BLACKSTONE = register(Blocks.BLACKSTONE);
    public static final CompressedBlockType COMPRESSED_END_STONE = register(Blocks.END_STONE);
    public static final CompressedBlockType COMPRESSED_CRUSHED_DEEPSLATE = register(EBlocks.CRUSHED_DEEPSLATE);
    public static final CompressedBlockType COMPRESSED_CRUSHED_BLACKSTONE = register(EBlocks.CRUSHED_BLACKSTONE);
    public static final CompressedBlockType COMPRESSED_CRUSHED_NETHERRACK = register(EBlocks.CRUSHED_NETHERRACK);
    public static final CompressedBlockType COMPRESSED_SOUL_SAND = register(Blocks.SOUL_SAND).withCompressium();
    public static final CompressedBlockType COMPRESSED_CRUSHED_END_STONE = register(EBlocks.CRUSHED_END_STONE);
    public static final CompressedBlockType COMPRESSED_MOSS_BLOCK = register(Blocks.MOSS_BLOCK);

    private static CompressedBlockType register(Block vanillaBase) {
        CompressedBlockType type = new CompressedBlockType(BuiltInRegistries.BLOCK.getKey(vanillaBase).getPath(), () -> vanillaBase);
        // AllTheCompressed has every vanilla block that Ex Deorum uses so far
        type.withAtc();
        ALL_VARIANTS.add(type);
        return type;
    }

    private static CompressedBlockType register(DeferredBlock<Block> base) {
        CompressedBlockType type = new CompressedBlockType(base.getId().getPath(), base);
        ALL_VARIANTS.add(type);
        return type;
    }

    public static void register() {
    }
}
