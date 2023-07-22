package thedarkcolour.exnihiloreborn.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thedarkcolour.exnihiloreborn.loot.InfestedString;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ELootProvider extends LootTableProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;
    private final Map<Block, LootTable.Builder> blockTables = Maps.newHashMap();

    public ELootProvider(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    private void addBlockLootTables() {
        addLoot(EBlocks.INFESTED_LEAVES, new LootTable.Builder().withPool(new LootPool.Builder().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(Items.STRING).apply(InfestedString.infestedString()))));

        addSelfDrops();
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
    public void run(DirectoryCache directoryCache) {
        addBlockLootTables();

        HashMap<ResourceLocation, LootTable> tables = new HashMap<>(blockTables.size());

        for (Map.Entry<Block, LootTable.Builder> entry : blockTables.entrySet()) {
            // Add tables to the block loot parameter set automatically
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootParameterSets.BLOCK).build());
        }

        writeLootTables(tables, directoryCache);
    }

    // Loot table to drop the block itself. Think Diamond Block, Sand, etc.
    private void dropSelf(RegistryObject<? extends Block> block) {
        // refer to the diamond block loot table for this one
        addLoot(block, new LootTable.Builder().withPool(new LootPool.Builder().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(block.get())).when(SurvivesExplosion.survivesExplosion())));
    }

    private void addLoot(RegistryObject<? extends Block> block, LootTable.Builder builder) {
        blockTables.put(block.get(), builder);
    }

    private void writeLootTables(Map<ResourceLocation, LootTable> tables, DirectoryCache cache) {
        Path outputFolder = generator.getOutputFolder();

        for (Map.Entry<ResourceLocation, LootTable> entry : tables.entrySet()) {
            Path path = outputFolder.resolve("data/" + entry.getKey().getNamespace() + "/loot_tables/" + entry.getKey().getPath() + ".json");

            try {
                IDataProvider.save(GSON, cache, LootTableManager.serialize(entry.getValue()), path);
            } catch (Exception e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        }
    }
}
