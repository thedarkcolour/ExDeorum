package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.fluid.WitchWaterFluidType;

public class EFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.FLUID_TYPES, ExNihiloReborn.ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ExNihiloReborn.ID);

    public static final RegistryObject<FluidType> WITCH_WATER = FLUID_TYPES.register("witch_water", () -> new WitchWaterFluidType());

    public static final RegistryObject<Fluid> WITCH_WATER_STILL = FLUIDS.register("witch_water", WitchWaterFluidType.Source::new);
    public static final RegistryObject<Fluid> WITCH_WATER_FLOWING = FLUIDS.register("flowing_witch_water", WitchWaterFluidType.Flowing::new);
}
