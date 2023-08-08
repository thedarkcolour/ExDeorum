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

package thedarkcolour.exdeorum.fluid;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.function.Consumer;

public class WitchWaterFluid extends FluidType {
    private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(ExDeorum.ID, "block/witch_water_still");
    private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(ExDeorum.ID, "block/witch_water_flowing");
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("block/water_overlay");

    public static ForgeFlowingFluid.Properties properties() {
        return new ForgeFlowingFluid.Properties(EFluids.WITCH_WATER_TYPE, EFluids.WITCH_WATER, EFluids.WITCH_WATER_FLOWING).block(EBlocks.WITCH_WATER).bucket(EItems.WITCH_WATER_BUCKET);
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
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return new Vector3f(32f / 255f, 12f / 255f, 64f / 255f);
            }

            @Override
            public int getTintColor() {
                return 0xFFFFFFFF;
            }
        });
    }
}
