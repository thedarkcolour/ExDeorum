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

package thedarkcolour.exdeorum.fluid;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Vector4f;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;

public class WitchWaterFluid extends FluidType {
    public static final Identifier STILL_TEXTURE = ExDeorum.loc("block/witch_water_still");
    public static final Identifier FLOWING_TEXTURE = ExDeorum.loc("block/witch_water_flowing");
    public static final Identifier OVERLAY_TEXTURE = Identifier.withDefaultNamespace("block/water_overlay");

    public static BaseFlowingFluid.Properties properties() {
        return new BaseFlowingFluid.Properties(EFluids.WITCH_WATER_TYPE, EFluids.WITCH_WATER, EFluids.WITCH_WATER_FLOWING).block(EBlocks.WITCH_WATER).bucket(EItems.WITCH_WATER_BUCKET);
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

    public static IClientFluidTypeExtensions createClientExtensions() {
        return new IClientFluidTypeExtensions() {
            @Override
            public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                fluidFogColor.set(32f / 255f, 12f / 255f, 64f / 255f, fluidFogColor.w);
            }
        };
    }
}
