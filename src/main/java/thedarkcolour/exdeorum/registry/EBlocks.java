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

package thedarkcolour.exdeorum.registry;

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
import thedarkcolour.exdeorum.block.BarrelBlock;
import thedarkcolour.exdeorum.block.EndCakeBlock;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.block.LavaCrucibleBlock;
import thedarkcolour.exdeorum.block.SieveBlock;
import thedarkcolour.exdeorum.block.UnfiredCrucibleBlock;
import thedarkcolour.exdeorum.block.WaterCrucibleBlock;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public class EBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExDeorum.ID);

    // Materials
    public static final RegistryObject<Block> DUST = BLOCKS.register("dust", () -> new FallingBlock(of().sound(SoundType.SAND).strength(0.4f)));
    public static final RegistryObject<Block> CRUSHED_NETHERRACK = BLOCKS.register("crushed_netherrack", () -> new FallingBlock(of().mapColor(MapColor.NETHER).sound(SoundType.SAND).strength(0.6f)));
    public static final RegistryObject<Block> CRUSHED_END_STONE = BLOCKS.register("crushed_end_stone", () -> new FallingBlock(of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.6f)));
    public static final RegistryObject<Block> CRUSHED_DEEPSLATE = BLOCKS.register("crushed_deepslate", () -> new FallingBlock(of().mapColor(DyeColor.GRAY).sound(SoundType.SAND).strength(0.8f)));
    public static final RegistryObject<Block> CRUSHED_BLACKSTONE = BLOCKS.register("crushed_blackstone", () -> new FallingBlock(of().mapColor(DyeColor.BLACK).sound(SoundType.SAND).strength(0.6f)));

    // Barrels
    public static final RegistryObject<BarrelBlock> OAK_BARREL = registerBarrel("oak_barrel", false, false, MapColor.WOOD);
    public static final RegistryObject<BarrelBlock> SPRUCE_BARREL = registerBarrel("spruce_barrel", false, false, MapColor.PODZOL);
    public static final RegistryObject<BarrelBlock> BIRCH_BARREL = registerBarrel("birch_barrel", false, false, MapColor.SAND);
    public static final RegistryObject<BarrelBlock> JUNGLE_BARREL = registerBarrel("jungle_barrel", false, false, MapColor.DIRT);
    public static final RegistryObject<BarrelBlock> ACACIA_BARREL = registerBarrel("acacia_barrel", false, false, MapColor.COLOR_ORANGE);
    public static final RegistryObject<BarrelBlock> DARK_OAK_BARREL = registerBarrel("dark_oak_barrel", false, false, MapColor.COLOR_BROWN);
    public static final RegistryObject<BarrelBlock> MANGROVE_BARREL = registerBarrel("mangrove_barrel", false, false, MapColor.COLOR_RED);
    public static final RegistryObject<BarrelBlock> CHERRY_BARREL = registerBarrel("cherry_barrel", false, false, MapColor.TERRACOTTA_WHITE);
    public static final RegistryObject<BarrelBlock> BAMBOO_BARREL = registerBarrel("bamboo_barrel", false, false, MapColor.COLOR_YELLOW);
    public static final RegistryObject<BarrelBlock> CRIMSON_BARREL = registerBarrel("crimson_barrel", false, true, MapColor.CRIMSON_STEM);
    public static final RegistryObject<BarrelBlock> WARPED_BARREL = registerBarrel("warped_barrel", false, true, MapColor.WARPED_STEM);
    public static final RegistryObject<BarrelBlock> STONE_BARREL = registerBarrel("stone_barrel", true, true, MapColor.STONE);

    // Sieves
    public static final RegistryObject<SieveBlock> OAK_SIEVE = registerSieve("oak_sieve");
    public static final RegistryObject<SieveBlock> SPRUCE_SIEVE = registerSieve("spruce_sieve");
    public static final RegistryObject<SieveBlock> BIRCH_SIEVE = registerSieve("birch_sieve");
    public static final RegistryObject<SieveBlock> JUNGLE_SIEVE = registerSieve("jungle_sieve");
    public static final RegistryObject<SieveBlock> ACACIA_SIEVE = registerSieve("acacia_sieve");
    public static final RegistryObject<SieveBlock> DARK_OAK_SIEVE = registerSieve("dark_oak_sieve");
    public static final RegistryObject<SieveBlock> MANGROVE_SIEVE = registerSieve("mangrove_sieve");
    public static final RegistryObject<SieveBlock> CHERRY_SIEVE = registerSieve("cherry_sieve");
    public static final RegistryObject<SieveBlock> BAMBOO_SIEVE = registerSieve("bamboo_sieve");
    public static final RegistryObject<SieveBlock> CRIMSON_SIEVE = registerSieve("crimson_sieve");
    public static final RegistryObject<SieveBlock> WARPED_SIEVE = registerSieve("warped_sieve");

    // Lava Crucibles
    public static final RegistryObject<LavaCrucibleBlock> PORCELAIN_CRUCIBLE = registerLavaCrucible("porcelain_crucible", false);
    public static final RegistryObject<LavaCrucibleBlock> WARPED_CRUCIBLE = registerLavaCrucible("warped_crucible", true);
    public static final RegistryObject<LavaCrucibleBlock> CRIMSON_CRUCIBLE = registerLavaCrucible("crimson_crucible", true);
    public static final RegistryObject<UnfiredCrucibleBlock> UNFIRED_PORCELAIN_CRUCIBLE = BLOCKS.register("unfired_porcelain_crucible", () -> new UnfiredCrucibleBlock(of().strength(2.0f)));

    // Water Crucibles
    public static final RegistryObject<WaterCrucibleBlock> OAK_CRUCIBLE = registerWaterCrucible("oak_crucible");
    public static final RegistryObject<WaterCrucibleBlock> SPRUCE_CRUCIBLE = registerWaterCrucible("spruce_crucible");
    public static final RegistryObject<WaterCrucibleBlock> BIRCH_CRUCIBLE = registerWaterCrucible("birch_crucible");
    public static final RegistryObject<WaterCrucibleBlock> JUNGLE_CRUCIBLE = registerWaterCrucible("jungle_crucible");
    public static final RegistryObject<WaterCrucibleBlock> ACACIA_CRUCIBLE = registerWaterCrucible("acacia_crucible");
    public static final RegistryObject<WaterCrucibleBlock> DARK_OAK_CRUCIBLE = registerWaterCrucible("dark_oak_crucible");
    public static final RegistryObject<WaterCrucibleBlock> MANGROVE_CRUCIBLE = registerWaterCrucible("mangrove_crucible");
    public static final RegistryObject<WaterCrucibleBlock> CHERRY_CRUCIBLE = registerWaterCrucible("cherry_crucible");
    public static final RegistryObject<WaterCrucibleBlock> BAMBOO_CRUCIBLE = registerWaterCrucible("bamboo_crucible");

    // Misc
    public static final RegistryObject<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", () -> new InfestedLeavesBlock(copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<LiquidBlock> WITCH_WATER = BLOCKS.register("witch_water", () -> new LiquidBlock(EFluids.WITCH_WATER, copy(Blocks.WATER).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistryObject<EndCakeBlock> END_CAKE = BLOCKS.register("end_cake", () -> new EndCakeBlock(of().noLootTable().mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.BLOCK)));

    public static RegistryObject<SieveBlock> registerSieve(String name) {
        var bamboo = name.equals("bamboo_sieve");
        return BLOCKS.register(name, () -> new SieveBlock(of().strength(2.0f).noOcclusion().sound(bamboo ? SoundType.BAMBOO_WOOD : SoundType.WOOD)));
    }

    public static RegistryObject<BarrelBlock> registerBarrel(String name, boolean stone, boolean fireproof, MapColor color) {
        var bamboo = name.equals("bamboo_barrel");
        return BLOCKS.register(name, () -> {
            var props = of().noOcclusion().strength(stone ? 4.0f : 2.0f).sound(stone ? SoundType.STONE : (bamboo ? SoundType.BAMBOO_WOOD : SoundType.WOOD));
            if (!stone) {
                if (!fireproof) {
                    props.ignitedByLava();
                }
            } else {
                props.requiresCorrectToolForDrops();
            }
            props.mapColor(color);
            return new BarrelBlock(props);
        });
    }

    public static RegistryObject<LavaCrucibleBlock> registerLavaCrucible(String name, boolean stem) {
        return BLOCKS.register(name, () -> {
            var props = of().noOcclusion().strength(stem ? 1.5f : 2.0f).sound(stem ? SoundType.STEM : SoundType.STONE);
            if (!stem) {
                props.requiresCorrectToolForDrops();
            }
            return new LavaCrucibleBlock(props);
        });
    }

    public static RegistryObject<WaterCrucibleBlock> registerWaterCrucible(String name) {
        var bamboo = name.equals("bamboo_crucible");
        return BLOCKS.register(name, () -> new WaterCrucibleBlock(of().strength(1.5f).sound(bamboo ? SoundType.BAMBOO_WOOD : SoundType.WOOD)));
    }
}
