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

import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.*;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.*;

// READER'S NOTE: More blocks are found in DefaultMaterials.java
public class EBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExDeorum.ID);

    // Materials
    public static final DeferredBlock<Block> DUST = BLOCKS.register("dust", () -> new ColoredFallingBlock(new ColorRGBA(0xC8C6AB), of().sound(SoundType.SAND).strength(0.4f)));
    public static final DeferredBlock<Block> CRUSHED_NETHERRACK = BLOCKS.register("crushed_netherrack", () -> new ColoredFallingBlock(new ColorRGBA(0x501B1B), of().mapColor(MapColor.NETHER).sound(SoundType.SAND).strength(0.6f)));
    public static final DeferredBlock<Block> CRUSHED_END_STONE = BLOCKS.register("crushed_end_stone", () -> new ColoredFallingBlock(new ColorRGBA(0xEEF6B4), of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.6f)));
    public static final DeferredBlock<Block> CRUSHED_DEEPSLATE = BLOCKS.register("crushed_deepslate", () -> new ColoredFallingBlock(new ColorRGBA(0x4A4A4F), of().mapColor(DyeColor.GRAY).sound(SoundType.SAND).strength(0.8f)));
    public static final DeferredBlock<Block> CRUSHED_BLACKSTONE = BLOCKS.register("crushed_blackstone", () -> new ColoredFallingBlock(new ColorRGBA(0x20131C), of().mapColor(DyeColor.BLACK).sound(SoundType.SAND).strength(0.6f)));

    // Mechanical Sieve
    public static final DeferredBlock<MechanicalSieveBlock> MECHANICAL_SIEVE = BLOCKS.register("mechanical_sieve", () -> new MechanicalSieveBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));
    // Mechanical Hammer
    public static final DeferredBlock<MechanicalHammerBlock> MECHANICAL_HAMMER = BLOCKS.register("mechanical_hammer", () -> new MechanicalHammerBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));

    // Misc
    public static final DeferredBlock<UnfiredCrucibleBlock> UNFIRED_PORCELAIN_CRUCIBLE = BLOCKS.register("unfired_porcelain_crucible", () -> new UnfiredCrucibleBlock(of().strength(2.0f)));
    public static final DeferredBlock<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", () -> new InfestedLeavesBlock(ofFullCopy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<LiquidBlock> WITCH_WATER = BLOCKS.register("witch_water", () -> new WitchWaterBlock(EFluids.WITCH_WATER, ofFullCopy(Blocks.WATER).mapColor(MapColor.COLOR_PURPLE)));
    public static final DeferredBlock<EndCakeBlock> END_CAKE = BLOCKS.register("end_cake", () -> new EndCakeBlock(of().noLootTable().mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.BLOCK)));
}
