package thedarkcolour.exnihiloreborn.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EItems;

import java.util.function.Consumer;

public class WitchWaterFluid extends FluidType {
    private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "block/witch_water_still");
    private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "block/witch_water_flowing");
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("block/water_overlay");

    public static ForgeFlowingFluid.Properties properties() {
        return new ForgeFlowingFluid.Properties(EFluids.WITCH_WATER_TYPE, EFluids.WITCH_WATER_STILL, EFluids.WITCH_WATER_FLOWING).block(EBlocks.WITCH_WATER).bucket(EItems.WITCH_WATER_BUCKET);
    }

    public WitchWaterFluid() {
        super(FluidType.Properties.create()
                .fallDistanceModifier(0F)
                .canExtinguish(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return OVERLAY_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return 0xFFFFFFFF;
            }
        });
    }
}
