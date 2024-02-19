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

    // Sieves
    public static final RegistryObject<SieveBlock> OAK_SIEVE = registerSieve("oak_sieve");
    public static final RegistryObject<SieveBlock> SPRUCE_SIEVE = registerSieve("spruce_sieve");
    public static final RegistryObject<SieveBlock> BIRCH_SIEVE = registerSieve("birch_sieve");
    public static final RegistryObject<SieveBlock> JUNGLE_SIEVE = registerSieve("jungle_sieve");
    public static final RegistryObject<SieveBlock> ACACIA_SIEVE = registerSieve("acacia_sieve");
    public static final RegistryObject<SieveBlock> DARK_OAK_SIEVE = registerSieve("dark_oak_sieve");
    public static final RegistryObject<SieveBlock> MANGROVE_SIEVE = registerSieve("mangrove_sieve");
    public static final RegistryObject<SieveBlock> CHERRY_SIEVE = registerSieve("cherry_sieve");
    public static final RegistryObject<SieveBlock> BAMBOO_SIEVE = registerSieve("bamboo_sieve", SoundType.BAMBOO_WOOD);
    public static final RegistryObject<SieveBlock> CRIMSON_SIEVE = registerSieve("crimson_sieve");
    public static final RegistryObject<SieveBlock> WARPED_SIEVE = registerSieve("warped_sieve");
    // BOP Sieves
    public static final RegistryObject<SieveBlock> FIR_SIEVE = registerSieve("fir_sieve");
    public static final RegistryObject<SieveBlock> REDWOOD_SIEVE = registerSieve("redwood_sieve");
    public static final RegistryObject<SieveBlock> MAHOGANY_SIEVE = registerSieve("mahogany_sieve");
    public static final RegistryObject<SieveBlock> JACARANDA_SIEVE = registerSieve("jacaranda_sieve");
    public static final RegistryObject<SieveBlock> PALM_SIEVE = registerSieve("palm_sieve");
    public static final RegistryObject<SieveBlock> WILLOW_SIEVE = registerSieve("willow_sieve");
    public static final RegistryObject<SieveBlock> DEAD_SIEVE = registerSieve("dead_sieve");
    public static final RegistryObject<SieveBlock> MAGIC_SIEVE = registerSieve("magic_sieve");
    public static final RegistryObject<SieveBlock> UMBRAN_SIEVE = registerSieve("umbran_sieve");
    public static final RegistryObject<SieveBlock> HELLBARK_SIEVE = registerSieve("hellbark_sieve");
    // Ars Nouveau Sieves
    public static final RegistryObject<SieveBlock> ARCHWOOD_SIEVE = registerSieve("archwood_sieve");
    // Aether Sieves
    public static final RegistryObject<SieveBlock> SKYROOT_SIEVE = registerSieve("skyroot_sieve");
    // Blue Skies Sieves
    public static final RegistryObject<SieveBlock> BLUEBRIGHT_SIEVE = registerSieve("bluebright_sieve");
    public static final RegistryObject<SieveBlock> STARLIT_SIEVE = registerSieve("starlit_sieve");
    public static final RegistryObject<SieveBlock> FROSTBRIGHT_SIEVE = registerSieve("frostbright_sieve");
    public static final RegistryObject<SieveBlock> COMET_SIEVE = registerSieve("comet_sieve");
    public static final RegistryObject<SieveBlock> LUNAR_SIEVE = registerSieve("lunar_sieve");
    public static final RegistryObject<SieveBlock> DUSK_SIEVE = registerSieve("dusk_sieve");
    public static final RegistryObject<SieveBlock> MAPLE_SIEVE = registerSieve("maple_sieve");
    public static final RegistryObject<SieveBlock> CRYSTALLIZED_SIEVE = registerSieve("crystallized_sieve", SoundType.GLASS);
    // Mechanical Sieve
    public static final RegistryObject<MechanicalSieveBlock> MECHANICAL_SIEVE = BLOCKS.register("mechanical_sieve", () -> new MechanicalSieveBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));
    // Mechanical Hammer
    public static final RegistryObject<MechanicalHammerBlock> MECHANICAL_HAMMER = BLOCKS.register("mechanical_hammer", () -> new MechanicalHammerBlock(of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5f, 1200f)));

    // Lava Crucibles
    public static final RegistryObject<LavaCrucibleBlock> PORCELAIN_CRUCIBLE = registerLavaCrucible("porcelain_crucible", true, SoundType.STONE);
    public static final RegistryObject<LavaCrucibleBlock> WARPED_CRUCIBLE = registerLavaCrucible("warped_crucible", false, SoundType.STEM);
    public static final RegistryObject<LavaCrucibleBlock> CRIMSON_CRUCIBLE = registerLavaCrucible("crimson_crucible", false, SoundType.STEM);
    public static final RegistryObject<UnfiredCrucibleBlock> UNFIRED_PORCELAIN_CRUCIBLE = BLOCKS.register("unfired_porcelain_crucible", () -> new UnfiredCrucibleBlock(of().strength(2.0f)));
    // BOP Lava Crucibles
    public static final RegistryObject<LavaCrucibleBlock> HELLBARK_CRUCIBLE = registerLavaCrucible("hellbark_crucible", false, SoundType.WOOD);
    // Blue Skies Lava Crucibles
    public static final RegistryObject<LavaCrucibleBlock> CRYSTALLIZED_CRUCIBLE = registerLavaCrucible("crystallized_crucible", true, SoundType.GLASS);

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
    // BOP Water Crucibles
    public static final RegistryObject<WaterCrucibleBlock> FIR_CRUCIBLE = registerWaterCrucible("fir_crucible");
    public static final RegistryObject<WaterCrucibleBlock> REDWOOD_CRUCIBLE = registerWaterCrucible("redwood_crucible");
    public static final RegistryObject<WaterCrucibleBlock> MAHOGANY_CRUCIBLE = registerWaterCrucible("mahogany_crucible");
    public static final RegistryObject<WaterCrucibleBlock> JACARANDA_CRUCIBLE = registerWaterCrucible("jacaranda_crucible");
    public static final RegistryObject<WaterCrucibleBlock> PALM_CRUCIBLE = registerWaterCrucible("palm_crucible");
    public static final RegistryObject<WaterCrucibleBlock> WILLOW_CRUCIBLE = registerWaterCrucible("willow_crucible");
    public static final RegistryObject<WaterCrucibleBlock> DEAD_CRUCIBLE = registerWaterCrucible("dead_crucible");
    public static final RegistryObject<WaterCrucibleBlock> MAGIC_CRUCIBLE = registerWaterCrucible("magic_crucible");
    public static final RegistryObject<WaterCrucibleBlock> UMBRAN_CRUCIBLE = registerWaterCrucible("umbran_crucible");
    // Ars Nouveau Water Crucibles
    public static final RegistryObject<WaterCrucibleBlock> CASCADING_ARCHWOOD_CRUCIBLE = registerWaterCrucible("blue_archwood_crucible");
    public static final RegistryObject<WaterCrucibleBlock> BLAZING_ARCHWOOD_CRUCIBLE = registerWaterCrucible("red_archwood_crucible");
    public static final RegistryObject<WaterCrucibleBlock> VEXING_ARCHWOOD_CRUCIBLE = registerWaterCrucible("purple_archwood_crucible");
    public static final RegistryObject<WaterCrucibleBlock> FLOURISHING_ARCHWOOD_CRUCIBLE = registerWaterCrucible("green_archwood_crucible");
    // Aether Crucibles
    public static final RegistryObject<WaterCrucibleBlock> SKYROOT_CRUCIBLE = registerWaterCrucible("skyroot_crucible");
    public static final RegistryObject<WaterCrucibleBlock> GOLDEN_OAK_CRUCIBLE = registerWaterCrucible("golden_oak_crucible");
    // Blue Skies Crucibles
    public static final RegistryObject<WaterCrucibleBlock> BLUEBRIGHT_CRUCIBLE = registerWaterCrucible("bluebright_crucible");
    public static final RegistryObject<WaterCrucibleBlock> STARLIT_CRUCIBLE = registerWaterCrucible("starlit_crucible");
    public static final RegistryObject<WaterCrucibleBlock> FROSTBRIGHT_CRUCIBLE = registerWaterCrucible("frostbright_crucible");
    public static final RegistryObject<WaterCrucibleBlock> COMET_CRUCIBLE = registerWaterCrucible("comet_crucible");
    public static final RegistryObject<WaterCrucibleBlock> LUNAR_CRUCIBLE = registerWaterCrucible("lunar_crucible");
    public static final RegistryObject<WaterCrucibleBlock> DUSK_CRUCIBLE = registerWaterCrucible("dusk_crucible");
    public static final RegistryObject<WaterCrucibleBlock> MAPLE_CRUCIBLE = registerWaterCrucible("maple_crucible");

    // Misc
    public static final RegistryObject<InfestedLeavesBlock> INFESTED_LEAVES = BLOCKS.register("infested_leaves", () -> new InfestedLeavesBlock(copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<LiquidBlock> WITCH_WATER = BLOCKS.register("witch_water", () -> new WitchWaterBlock(EFluids.WITCH_WATER, copy(Blocks.WATER).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistryObject<EndCakeBlock> END_CAKE = BLOCKS.register("end_cake", () -> new EndCakeBlock(of().noLootTable().mapColor(MapColor.COLOR_BLACK).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.BLOCK)));

    public static RegistryObject<SieveBlock> registerSieve(String name) {
        return registerSieve(name, SoundType.WOOD);
    }

    public static RegistryObject<SieveBlock> registerSieve(String name, SoundType sound) {
        return BLOCKS.register(name, () -> new SieveBlock(of().strength(2.0f).noOcclusion().sound(sound)));
    }

    public static RegistryObject<LavaCrucibleBlock> registerLavaCrucible(String name, boolean stone, SoundType sound) {
        return BLOCKS.register(name, () -> {
            var props = of().noOcclusion().strength(stone ? 2.0f : 1.5f).sound(sound);
            if (stone) {
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
