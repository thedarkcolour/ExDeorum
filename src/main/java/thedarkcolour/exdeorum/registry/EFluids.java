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

    public static final RegistryObject<ForgeFlowingFluid> WITCH_WATER_STILL = FLUIDS.register("witch_water", () -> new ForgeFlowingFluid.Source(WitchWaterFluid.properties()));
    public static final RegistryObject<ForgeFlowingFluid> WITCH_WATER_FLOWING = FLUIDS.register("flowing_witch_water", () -> new ForgeFlowingFluid.Flowing(WitchWaterFluid.properties()));
}
