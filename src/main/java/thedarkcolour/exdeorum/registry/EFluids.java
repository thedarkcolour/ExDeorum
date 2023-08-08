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

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.fluid.WitchWaterFluid;

public class EFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ExDeorum.ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ExDeorum.ID);

    public static final RegistryObject<FluidType> WITCH_WATER_TYPE = FLUID_TYPES.register("witch_water", WitchWaterFluid::new);

    public static final RegistryObject<ForgeFlowingFluid> WITCH_WATER = FLUIDS.register("witch_water", () -> new ForgeFlowingFluid.Source(WitchWaterFluid.properties()));
    public static final RegistryObject<ForgeFlowingFluid> WITCH_WATER_FLOWING = FLUIDS.register("flowing_witch_water", () -> new ForgeFlowingFluid.Flowing(WitchWaterFluid.properties()));
}
