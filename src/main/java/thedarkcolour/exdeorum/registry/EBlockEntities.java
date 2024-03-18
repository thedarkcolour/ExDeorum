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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.*;
import thedarkcolour.exdeorum.material.DefaultMaterials;

public class EBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExDeorum.ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfestedLeavesBlockEntity>> INFESTED_LEAVES = BLOCK_ENTITIES.register("infested_leaves", () -> BlockEntityType.Builder.of(InfestedLeavesBlockEntity::new, EBlocks.INFESTED_LEAVES.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LavaCrucibleBlockEntity>> LAVA_CRUCIBLE = BLOCK_ENTITIES.register("lava_crucible", () -> DefaultMaterials.LAVA_CRUCIBLES.createBlockEntityType(LavaCrucibleBlockEntity::new));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterCrucibleBlockEntity>> WATER_CRUCIBLE = BLOCK_ENTITIES.register("water_crucible", () -> DefaultMaterials.WATER_CRUCIBLES.createBlockEntityType(WaterCrucibleBlockEntity::new));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register("barrel", () -> DefaultMaterials.BARRELS.createBlockEntityType(BarrelBlockEntity::new));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SieveBlockEntity>> SIEVE = BLOCK_ENTITIES.register("sieve", () -> DefaultMaterials.SIEVES.createBlockEntityType(SieveBlockEntity::new));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MechanicalSieveBlockEntity>> MECHANICAL_SIEVE = BLOCK_ENTITIES.register("mechanical_sieve", () -> BlockEntityType.Builder.of(MechanicalSieveBlockEntity::new, EBlocks.MECHANICAL_SIEVE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MechanicalHammerBlockEntity>> MECHANICAL_HAMMER = BLOCK_ENTITIES.register("mechanical_hammer", () -> BlockEntityType.Builder.of(MechanicalHammerBlockEntity::new, EBlocks.MECHANICAL_HAMMER.get()).build(null));
}
