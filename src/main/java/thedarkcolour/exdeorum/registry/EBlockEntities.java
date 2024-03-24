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
    public static final RegistryObject<BlockEntityType<LavaCrucibleBlockEntity>> LAVA_CRUCIBLE = BLOCK_ENTITIES.register("lava_crucible", () -> DefaultMaterials.LAVA_CRUCIBLES.createBlockEntityType(LavaCrucibleBlockEntity::new));
    public static final RegistryObject<BlockEntityType<WaterCrucibleBlockEntity>> WATER_CRUCIBLE = BLOCK_ENTITIES.register("water_crucible", () -> DefaultMaterials.WATER_CRUCIBLES.createBlockEntityType(WaterCrucibleBlockEntity::new));
    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register("barrel", () -> DefaultMaterials.BARRELS.createBlockEntityType(BarrelBlockEntity::new));
    public static final RegistryObject<BlockEntityType<SieveBlockEntity>> SIEVE = BLOCK_ENTITIES.register("sieve", () -> DefaultMaterials.SIEVES.createBlockEntityType(SieveBlockEntity::new));
    public static final RegistryObject<BlockEntityType<CompressedSieveBlockEntity>> COMPRESSED_SIEVE = BLOCK_ENTITIES.register("compressed_sieve", () -> DefaultMaterials.COMPRESSED_SIEVES.createBlockEntityType(CompressedSieveBlockEntity::new));
    public static final RegistryObject<BlockEntityType<MechanicalSieveBlockEntity>> MECHANICAL_SIEVE = BLOCK_ENTITIES.register("mechanical_sieve", () -> BlockEntityType.Builder.of(MechanicalSieveBlockEntity::new, EBlocks.MECHANICAL_SIEVE.get()).build(null));
    public static final RegistryObject<BlockEntityType<MechanicalHammerBlockEntity>> MECHANICAL_HAMMER = BLOCK_ENTITIES.register("mechanical_hammer", () -> BlockEntityType.Builder.of(MechanicalHammerBlockEntity::new, EBlocks.MECHANICAL_HAMMER.get()).build(null));
}
