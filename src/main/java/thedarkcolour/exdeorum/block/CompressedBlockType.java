package thedarkcolour.exdeorum.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.function.Supplier;

public class CompressedBlockType implements ItemLike {
    private final DeferredBlock<Block> block;
    private final DeferredItem<BlockItem> item;
    private final TagKey<Item> itemTag;
    private final Supplier<Block> base;

    private boolean hasCompressium;

    public CompressedBlockType(String name, Supplier<Block> base) {
        this.block = EBlocks.BLOCKS.register("compressed_" + name, this::createBlock);
        this.item = EItems.registerItemBlock(this.block);
        this.itemTag = EItemTags.tag("compressed/" + name);
        this.base = base;
    }

    private Block createBlock(Identifier id) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(this.base.get()).setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public Block getBlock() {
        return this.block.get();
    }

    public BlockItem getItem() {
        return this.item.get();
    }

    @Override
    public Item asItem() {
        return this.item.get();
    }

    public TagKey<Item> getTag() {
        return this.itemTag;
    }

    public CompressedBlockType withCompressium() {
        this.hasCompressium = true;
        return this;
    }

    // AllTheCompressed has every vanilla block that Ex Deorum uses so far
    public boolean hasAtc() {
        return true;
    }

    public boolean hasCompressium() {
        return this.hasCompressium;
    }

    public Identifier getAtc() {
        return Identifier.fromNamespaceAndPath(ModIds.ALL_THE_COMPRESSED, BuiltInRegistries.BLOCK.getKey(this.base.get()).getPath() + "_1x");
    }

    public Identifier getCompressium() {
        return Identifier.fromNamespaceAndPath(ModIds.COMPRESSIUM, BuiltInRegistries.BLOCK.getKey(this.base.get()).getPath() + "_1");
    }

    public Block getBase() {
        return this.base.get();
    }

    public Identifier getId() {
        return this.block.getId();
    }
}
