package thedarkcolour.exnihiloreborn.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EFluids;

import java.util.function.Consumer;

public class WitchWaterFluidType extends FluidType {
    private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "block/witch_water_still");
    private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "block/witch_water_flowing");
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("block/water_overlay");

    public WitchWaterFluidType() {
        super(FluidType.Properties.create());
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return IClientFluidTypeExtensions.super.getStillTexture();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return IClientFluidTypeExtensions.super.getFlowingTexture();
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return IClientFluidTypeExtensions.super.getOverlayTexture();
            }
        });
    }

    /*, FluidAttributes.builder(STILL_TEXTURE, FLOWING_TEXTURE)
            .color(0xff551ec6)
            .translationKey("block." + ExNihiloReborn.ID + ".witch_water")
            .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
            .overlay(new ResourceLocation("block/water_overlay"))
    ).block(EBlocks.WITCH_WATER).bucket(EItems.WITCH_WATER_BUCKET);*/

    public static class Flowing extends ForgeFlowingFluid.Flowing {
        public Flowing() {
            super(new ForgeFlowingFluid.Properties(EFluids.WITCH_WATER, EFluids.WITCH_WATER_STILL, EFluids.WITCH_WATER_FLOWING));
        }
    }

    public static class Source extends ForgeFlowingFluid.Source {
        public Source() {
            super(new ForgeFlowingFluid.Properties(EFluids.WITCH_WATER, EFluids.WITCH_WATER_STILL, EFluids.WITCH_WATER_FLOWING));
        }
    }
}
