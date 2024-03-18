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
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.fluid.WitchWaterFluid;

public class EFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, ExDeorum.ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, ExDeorum.ID);

    public static final DeferredHolder<FluidType, WitchWaterFluid> WITCH_WATER_TYPE = FLUID_TYPES.register("witch_water", WitchWaterFluid::new);

    public static final DeferredHolder<Fluid, BaseFlowingFluid> WITCH_WATER = FLUIDS.register("witch_water", () -> new BaseFlowingFluid.Source(WitchWaterFluid.properties()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid> WITCH_WATER_FLOWING = FLUIDS.register("flowing_witch_water", () -> new BaseFlowingFluid.Flowing(WitchWaterFluid.properties()));
}
