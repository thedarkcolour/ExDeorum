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

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.*;
import thedarkcolour.exdeorum.material.DefaultMaterials;

public class EBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExDeorum.ID);

    public static final RegistryObject<BlockEntityType<InfestedLeavesBlockEntity>> INFESTED_LEAVES = BLOCK_ENTITIES.register("infested_leaves", () -> BlockEntityType.Builder.of(InfestedLeavesBlockEntity::new, EBlocks.INFESTED_LEAVES.get()).build(null));
    public static final RegistryObject<BlockEntityType<LavaCrucibleBlockEntity>> LAVA_CRUCIBLE = BLOCK_ENTITIES.register("lava_crucible", () -> BlockEntityType.Builder.of(LavaCrucibleBlockEntity::new,
            EBlocks.WARPED_CRUCIBLE.get(),
            EBlocks.CRIMSON_CRUCIBLE.get(),
            EBlocks.PORCELAIN_CRUCIBLE.get(),
            // BOP
            EBlocks.HELLBARK_CRUCIBLE.get(),
            // Blue Skies
            EBlocks.CRYSTALLIZED_CRUCIBLE.get()
    ).build(null));
    public static final RegistryObject<BlockEntityType<WaterCrucibleBlockEntity>> WATER_CRUCIBLE = BLOCK_ENTITIES.register("water_crucible", () -> BlockEntityType.Builder.of(WaterCrucibleBlockEntity::new,
            EBlocks.OAK_CRUCIBLE.get(),
            EBlocks.SPRUCE_CRUCIBLE.get(),
            EBlocks.BIRCH_CRUCIBLE.get(),
            EBlocks.JUNGLE_CRUCIBLE.get(),
            EBlocks.ACACIA_CRUCIBLE.get(),
            EBlocks.DARK_OAK_CRUCIBLE.get(),
            EBlocks.MANGROVE_CRUCIBLE.get(),
            EBlocks.CHERRY_CRUCIBLE.get(),
            EBlocks.BAMBOO_CRUCIBLE.get(),
            // BOP
            EBlocks.FIR_CRUCIBLE.get(),
            EBlocks.REDWOOD_CRUCIBLE.get(),
            EBlocks.MAHOGANY_CRUCIBLE.get(),
            EBlocks.JACARANDA_CRUCIBLE.get(),
            EBlocks.PALM_CRUCIBLE.get(),
            EBlocks.WILLOW_CRUCIBLE.get(),
            EBlocks.DEAD_CRUCIBLE.get(),
            EBlocks.MAGIC_CRUCIBLE.get(),
            EBlocks.UMBRAN_CRUCIBLE.get(),
            // Ars Nouveau
            EBlocks.CASCADING_ARCHWOOD_CRUCIBLE.get(),
            EBlocks.BLAZING_ARCHWOOD_CRUCIBLE.get(),
            EBlocks.VEXING_ARCHWOOD_CRUCIBLE.get(),
            EBlocks.FLOURISHING_ARCHWOOD_CRUCIBLE.get(),
            // Aether
            EBlocks.SKYROOT_CRUCIBLE.get(),
            EBlocks.GOLDEN_OAK_CRUCIBLE.get(),
            // Blue Skies
            EBlocks.BLUEBRIGHT_CRUCIBLE.get(),
            EBlocks.STARLIT_CRUCIBLE.get(),
            EBlocks.FROSTBRIGHT_CRUCIBLE.get(),
            EBlocks.COMET_CRUCIBLE.get(),
            EBlocks.LUNAR_CRUCIBLE.get(),
            EBlocks.DUSK_CRUCIBLE.get(),
            EBlocks.MAPLE_CRUCIBLE.get()
    ).build(null));
    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register("barrel", () -> DefaultMaterials.BARRELS.createBlockEntityType(BarrelBlockEntity::new));
    public static final RegistryObject<BlockEntityType<SieveBlockEntity>> SIEVE = BLOCK_ENTITIES.register("sieve", () -> DefaultMaterials.SIEVES.createBlockEntityType(SieveBlockEntity::new));
    public static final RegistryObject<BlockEntityType<MechanicalSieveBlockEntity>> MECHANICAL_SIEVE = BLOCK_ENTITIES.register("mechanical_sieve", () -> BlockEntityType.Builder.of(MechanicalSieveBlockEntity::new, EBlocks.MECHANICAL_SIEVE.get()).build(null));
    public static final RegistryObject<BlockEntityType<MechanicalHammerBlockEntity>> MECHANICAL_HAMMER = BLOCK_ENTITIES.register("mechanical_hammer", () -> BlockEntityType.Builder.of(MechanicalHammerBlockEntity::new, EBlocks.MECHANICAL_HAMMER.get()).build(null));
}
