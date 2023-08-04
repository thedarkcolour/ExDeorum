package thedarkcolour.exdeorum.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.loot.InfestedStringCount;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.modkit.MKUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class BlockLoot extends BlockLootSubProvider {
    private final List<Block> added = new ArrayList<>();

    protected BlockLoot() {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    protected void generate() {
        MKUtils.forModRegistry(Registries.BLOCK, ExDeorum.ID, (id, block) -> dropSelf(block));

        add(EBlocks.INFESTED_LEAVES.get(), new LootTable.Builder()
                .withPool(new LootPool.Builder()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.STRING).apply(InfestedStringCount.infestedString()))));
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
