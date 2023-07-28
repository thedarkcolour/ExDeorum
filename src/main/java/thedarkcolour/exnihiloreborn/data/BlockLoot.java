package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import thedarkcolour.exnihiloreborn.loot.InfestedStringCount;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

class BlockLoot extends BlockLootSubProvider {
    private final List<Block> added = new ArrayList<>();

    protected BlockLoot() {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    protected void generate() {
        add(EBlocks.INFESTED_LEAVES.get(), new LootTable.Builder()
                .withPool(new LootPool.Builder()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.STRING).apply(InfestedStringCount.infestedString()))));

        addSelfDrops();
    }

    private void dropSelf(Supplier<? extends Block> item) {
        this.dropSelf(item.get());
    }

    private void addSelfDrops() {
        dropSelf(EBlocks.DUST);

        dropSelf(EBlocks.COMPRESSED_COBBLESTONE);
        dropSelf(EBlocks.COMPRESSED_DIRT);
        dropSelf(EBlocks.COMPRESSED_SAND);
        dropSelf(EBlocks.COMPRESSED_DUST);

        dropSelf(EBlocks.OAK_BARREL);
        dropSelf(EBlocks.SPRUCE_BARREL);
        dropSelf(EBlocks.BIRCH_BARREL);
        dropSelf(EBlocks.JUNGLE_BARREL);
        dropSelf(EBlocks.ACACIA_BARREL);
        dropSelf(EBlocks.DARK_OAK_BARREL);
        dropSelf(EBlocks.CRIMSON_BARREL);
        dropSelf(EBlocks.WARPED_BARREL);
        dropSelf(EBlocks.STONE_BARREL);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return added;
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        added.add(block);
    }
}
