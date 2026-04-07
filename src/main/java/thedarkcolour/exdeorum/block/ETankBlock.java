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

package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import thedarkcolour.exdeorum.blockentity.ETankBlockEntity;
import thedarkcolour.exdeorum.registry.EFluids;

import java.util.function.Supplier;

// Base class for blocks that hold visible fluids (crucible, barrel)
public abstract class ETankBlock extends EBlock {
    public ETankBlock(BlockBehaviour.Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean shouldApplyEffects) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof ETankBlockEntity blockEntity) {
            var tank = blockEntity.getTank();
            var fluid = tank.getFluid();

            if (!fluid.isEmpty() && isEntityInFluid(level, pos, entity, (float) fluid.getAmount() / tank.getCapacity())) {
                entityInFluidLogic(level, entity, fluid);
            }
        }
    }

    protected abstract boolean isEntityInFluid(Level level, BlockPos pos, Entity entity, float fillRatio);

    // Only call this on the server
    public static void entityInFluidLogic(Level level, Entity entity, FluidStack fluid) {
        var fluidType = fluid.getFluid();

        if (fluidType == Fluids.LAVA) {
            entity.lavaHurt();
        } else if (fluidType == EFluids.WITCH_WATER.get()) {
            WitchWaterBlock.witchWaterEntityEffects(level, entity);
        } else if (fluidType.getFluidType().canExtinguish(entity)) {
            entity.extinguishFire();
        } else if (entity instanceof LivingEntity living && NeoForgeMod.MILK.isBound() && fluidType == NeoForgeMod.MILK.get()) {
            living.removeAllEffects();
        }
    }
}
