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

package thedarkcolour.exdeorum.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.block.AbstractCrucibleBlock;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.client.RenderFace;
import thedarkcolour.exdeorum.client.RenderUtil;

public class CrucibleRenderer implements BlockEntityRenderer<AbstractCrucibleBlockEntity, CrucibleRenderer.CrucibleRenderState> {
    @Override
    public CrucibleRenderState createRenderState() {
        return new CrucibleRenderState();
    }

    @Override
    public void extractRenderState(AbstractCrucibleBlockEntity crucible, CrucibleRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(crucible, state, partialTicks, cameraPosition, breakProgress);

        state.hasFluid = false;
        state.fluid = null;
        state.solidsFace = null;

        var tank = crucible.getTank();
        var level = crucible.getLevel();
        if (level == null) {
            return;
        }

        var fluidStack = tank.getFluidInTank(0);
        var solids = (float) crucible.getSolids() / (float) AbstractCrucibleBlockEntity.MAX_SOLIDS;
        var liquid = (float) fluidStack.getAmount() / (float) tank.getTankCapacity(0);

        if (!fluidStack.isEmpty() && liquid != 0) {
            var fluid = fluidStack.getFluid();
            var color = RenderUtil.getFluidColor(fluid, level, crucible.getBlockPos());
            state.hasFluid = true;
            state.fluid = fluid;
            state.fluidY = Mth.lerp(liquid, AbstractCrucibleBlock.CRUCIBLE_FLUID_BOTTOM, AbstractCrucibleBlock.CRUCIBLE_FLUID_TOP);
            state.fluidColor = color == -1 ? 0xffffff : color;
        }

        if (solids != 0) {
            var lastMelted = crucible.getLastMelted();
            if (lastMelted == null) {
                lastMelted = crucible.getDefaultMeltBlock();
            }

            state.solidsFace = RenderUtil.getTopFaceOrDefault(lastMelted, crucible.getDefaultMeltBlock());
            state.solidsPercentage = solids;

            var tintSource = Minecraft.getInstance().getBlockColors().getTintSource(lastMelted.defaultBlockState(), 0);
            var color = tintSource != null && level instanceof net.minecraft.client.renderer.block.BlockAndTintGetter getter
                    ? tintSource.colorInWorld(lastMelted.defaultBlockState(), getter, crucible.getBlockPos())
                    : -1;
            state.solidsColor = color == -1 ? 0xffffff : color;
        }
    }

    @Override
    public void submit(CrucibleRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (state.hasFluid && state.fluid != null) {
            int r = (state.fluidColor >> 16) & 0xff;
            int g = (state.fluidColor >> 8) & 0xff;
            int b = state.fluidColor & 0xff;
            collector.submitCustomGeometry(stack, RenderUtil.getFluidRenderType(state.fluid), (pose, buffer) ->
                RenderUtil.renderFlatFluidSprite(buffer, pose, state.fluidY, 2.0f, state.lightCoords, r, g, b, state.fluid)
            );
        }

        if (state.solidsFace instanceof RenderFace.Single single) {
            submitSolidLayer(collector, stack, state, single.renderType(), single.sprite());
        } else if (state.solidsFace instanceof RenderFace.Composite composite) {
            for (var layer : composite.layers()) {
                submitSolidLayer(collector, stack, state, layer.renderType(), layer.sprite());
            }
        }
    }

    private static void submitSolidLayer(SubmitNodeCollector collector, PoseStack stack, CrucibleRenderState state, net.minecraft.client.renderer.rendertype.RenderType renderType, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) {
        int r = (state.solidsColor >> 16) & 0xff;
        int g = (state.solidsColor >> 8) & 0xff;
        int b = state.solidsColor & 0xff;
        collector.submitCustomGeometry(stack, renderType, (pose, buffer) ->
            RenderUtil.renderFlatSpriteLerp(buffer, pose, state.solidsPercentage, r, g, b, sprite, state.lightCoords, 2.0f, AbstractCrucibleBlock.CRUCIBLE_FLUID_BOTTOM * 16f, AbstractCrucibleBlock.CRUCIBLE_FLUID_TOP * 16f)
        );
    }

    public static class CrucibleRenderState extends BlockEntityRenderState {
        public boolean hasFluid;
        public Fluid fluid;
        public float fluidY;
        public int fluidColor;
        public RenderFace solidsFace;
        public float solidsPercentage;
        public int solidsColor;
    }
}
