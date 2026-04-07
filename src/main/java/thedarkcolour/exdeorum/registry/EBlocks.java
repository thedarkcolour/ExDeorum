/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
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

package thedarkcolour.exdeorum.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.*;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.*;

// READER'S NOTE: More blocks are found in DefaultMaterials.java and ECompressedBlocks.java
public class EBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExDeorum.ID);

    // Materials
    public static final DeferredBlock<Block> DUST = BLOCKS.register("dust", id -> new ColoredFallingBlock(new ColorRGBA(0xC8C6AB), props(id, of().sound(SoundType.SAND).strength(0.4f))));
    public static final DeferredBlock<Block> CRUSHED_NETHERRACK = BLOCKS.register("crushed_netherrack", id -> new ColoredFallingBlock(new ColorRGBA(0x501B1B), props(id, of().mapColor(MapColor.NETHER).sound(SoundType.SAND).strength(0.6f))));
    public static final DeferredBlock<Block> CRUSHED_END_STONE = BLOCKS.register("crushed_end_stone", id -> new ColoredFallingBlock(new ColorRGBA(0xEEF6B4), props(id, of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.6f))));
    public static final DeferredBlock<Block> CRUSHED_DEEPSLATE = BLOCKS.register("crushed_deepslate", id -> new ColoredFallingBlock(new ColorRGBA(0x4A4A4F), props(id, of().mapColor(DyeColor.GRAY).sound(SoundType.SAND).strength(0.8f))));
    public static final DeferredBlock<Block> CRUSHED_BLACKSTONE = BLOCKS.register("crushed_blackstone", id -> new ColoredFallingBlock(new ColorRGBA(0x20131C), props(id, of().mapColor(DyeColor.BLACK).sound(SoundType.SAND).strength(0.6f))));

    // Mechanical Sieve
    public static final DeferredBlock<MechanicalSieveBlock> MECHANICAL_SIEVE = BLOCKS.register("mechanical_sieve", id -> new MechanicalSieveBlock(props(id, of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f))));
    // Mechanical Hammer
    public static final DeferredBlock<MechanicalHammerBlock> MECHANICAL_HAMMER = BLOCKS.register("mechanical_hammer", id -> new MechanicalHammerBlock(props(id, of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f))));

    // Misc
    public static final DeferredBlock<UnfiredCrucibleBlock> UNFIRED_PORCELAIN_CRUCIBLE = BLOCKS.register("unfired_porcelain_crucible", id -> new UnfiredCrucibleBlock(props(id, of().strength(2.0f))));
    public static final DeferredBlock<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", id -> new InfestedLeavesBlock(props(id, ofFullCopy(Blocks.OAK_LEAVES))));
    public static final DeferredBlock<LiquidBlock> WITCH_WATER = BLOCKS.register("witch_water", id -> new WitchWaterBlock(EFluids.WITCH_WATER, props(id, ofFullCopy(Blocks.WATER).mapColor(MapColor.COLOR_PURPLE))));
    public static final DeferredBlock<EndCakeBlock> END_CAKE = BLOCKS.register("end_cake", id -> new EndCakeBlock(props(id, of().noLootTable().mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.BLOCK))));

    private static BlockBehaviour.Properties props(Identifier id, BlockBehaviour.Properties properties) {
        return properties.setId(ResourceKey.create(Registries.BLOCK, id));
    }
}
