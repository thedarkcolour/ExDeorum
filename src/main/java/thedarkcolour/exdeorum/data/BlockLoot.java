/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.loot.InfestedStringFunction;
import thedarkcolour.exdeorum.loot.MachineLootFunction;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EItems;
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
        MKUtils.forModRegistry(Registries.BLOCK, ExDeorum.ID, (id, block) -> {
            if (block.getLootTable() != BuiltInLootTables.EMPTY) {
                dropSelf(block);
            }
        });

        add(EBlocks.INFESTED_LEAVES.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.STRING)
                                .apply(InfestedStringFunction.infestedString()))));
        // see createSingleItemTable() for reference
        add(EBlocks.MECHANICAL_SIEVE.get(), LootTable.lootTable()
                .withPool(applyExplosionCondition(EItems.MECHANICAL_SIEVE.get(), LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(EItems.MECHANICAL_SIEVE.get())
                                .apply(MachineLootFunction.machineLoot())))));
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
