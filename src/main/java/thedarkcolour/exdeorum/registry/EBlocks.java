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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.*;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

// READER'S NOTE: More blocks are found in DefaultMaterials.java
public class EBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExDeorum.ID);

    // Materials
    public static final RegistryObject<Block> DUST = BLOCKS.register("dust", () -> new FallingBlock(of().sound(SoundType.SAND).strength(0.4f)));
    public static final RegistryObject<Block> CRUSHED_NETHERRACK = BLOCKS.register("crushed_netherrack", () -> new FallingBlock(of().mapColor(MapColor.NETHER).sound(SoundType.SAND).strength(0.6f)));
    public static final RegistryObject<Block> CRUSHED_END_STONE = BLOCKS.register("crushed_end_stone", () -> new FallingBlock(of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.6f)));
    public static final RegistryObject<Block> CRUSHED_DEEPSLATE = BLOCKS.register("crushed_deepslate", () -> new FallingBlock(of().mapColor(DyeColor.GRAY).sound(SoundType.SAND).strength(0.8f)));
    public static final RegistryObject<Block> CRUSHED_BLACKSTONE = BLOCKS.register("crushed_blackstone", () -> new FallingBlock(of().mapColor(DyeColor.BLACK).sound(SoundType.SAND).strength(0.6f)));

    // Mechanical Sieve
    public static final RegistryObject<MechanicalSieveBlock> MECHANICAL_SIEVE = BLOCKS.register("mechanical_sieve", () -> new MechanicalSieveBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));
    // Mechanical Hammer
    public static final RegistryObject<MechanicalHammerBlock> MECHANICAL_HAMMER = BLOCKS.register("mechanical_hammer", () -> new MechanicalHammerBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));

    // Misc
    public static final RegistryObject<UnfiredCrucibleBlock> UNFIRED_PORCELAIN_CRUCIBLE = BLOCKS.register("unfired_porcelain_crucible", () -> new UnfiredCrucibleBlock(of().strength(2.0f)));
    public static final RegistryObject<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", () -> new InfestedLeavesBlock(copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<LiquidBlock> WITCH_WATER = BLOCKS.register("witch_water", () -> new WitchWaterBlock(EFluids.WITCH_WATER, copy(Blocks.WATER).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistryObject<EndCakeBlock> END_CAKE = BLOCKS.register("end_cake", () -> new EndCakeBlock(of().noLootTable().mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.BLOCK)));

    // Compressed blocks
    public static final RegistryObject<Block> COMPRESSED_DIRT = compressed(Blocks.DIRT);
    public static final RegistryObject<Block> COMPRESSED_GRAVEL = compressed(Blocks.GRAVEL);
    public static final RegistryObject<Block> COMPRESSED_SAND = compressed(Blocks.SAND);
    public static final RegistryObject<Block> COMPRESSED_DUST = compressed(DUST);
    public static final RegistryObject<Block> COMPRESSED_RED_SAND = compressed(Blocks.RED_SAND);
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_DEEPSLATE = compressed(CRUSHED_DEEPSLATE);
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_BLACKSTONE = compressed(CRUSHED_BLACKSTONE);
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_NETHERRACK = compressed(CRUSHED_NETHERRACK);
    public static final RegistryObject<Block> COMPRESSED_SOUL_SAND = compressed(Blocks.SOUL_SAND);
    public static final RegistryObject<Block> COMPRESSED_CRUSHED_END_STONE = compressed(CRUSHED_END_STONE);
    public static final RegistryObject<Block> COMPRESSED_MOSS_BLOCK = compressed(Blocks.MOSS_BLOCK);

    @SuppressWarnings("deprecation")
    private static RegistryObject<Block> compressed(Block block) {
        return BLOCKS.register("compressed_" + BuiltInRegistries.BLOCK.getKey(block).getPath(), () -> new Block(copy(block)));
    }

    private static RegistryObject<Block> compressed(RegistryObject<Block> block) {
        return BLOCKS.register("compressed_" + block.getId().getPath(), () -> new Block(copy(block.get())));
    }
}
