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

package thedarkcolour.exdeorum.compat.jei;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

class ClientJeiUtil {
    private static final FluidState EMPTY = Fluids.EMPTY.defaultFluidState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    // https://github.com/way2muchnoise/JustEnoughResources/blob/89ee40ff068c8d6eb6ab103f76381445691cffc9/Common/src/main/java/jeresources/util/RenderHelper.java#L100
    static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float scale) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();

        poseStack.translate(x, y, z);
        poseStack.scale(-scale, -scale, -scale);
        poseStack.translate(-0.5F, -0.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30F));
        poseStack.translate(0.5F, 0, -0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(45f));
        poseStack.translate(-0.5F, 0, 0.5F);

        poseStack.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        poseStack.translate(0, 0, -1);

        FluidState fluidState = block.getFluidState();

        if (fluidState.isEmpty()) {
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

            mc.getBlockRenderer().renderSingleBlock(block, poseStack, buffers, 15728880, OverlayTexture.NO_OVERLAY);

            buffers.endBatch();
        } else {
            RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluidState);
            PoseStack modelView = RenderSystem.getModelViewStack();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            renderType.setupRenderState();
            modelView.pushPose();
            modelView.mulPoseMatrix(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();

            builder.begin(renderType.mode(), renderType.format());

            Dummy.tempState = block;
            Dummy.tempFluid = fluidState;
            mc.getBlockRenderer().renderLiquid(BlockPos.ZERO, Dummy.INSTANCE, builder, block, block.getFluidState());
            Dummy.tempFluid = EMPTY;
            Dummy.tempState = AIR;

            if (builder.building()) {
                tesselator.end();
            }

            renderType.clearRenderState();
            modelView.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        poseStack.popPose();
    }

    private enum Dummy implements BlockAndTintGetter {
        INSTANCE;

        private static BlockState tempState = AIR;
        private static FluidState tempFluid = EMPTY;

        @Override
        public float getShade(Direction pDirection, boolean pShade) {
            return 1;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return Minecraft.getInstance().level.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver) {
            return 0;
        }

        @Override
        public int getBrightness(LightLayer pLightType, BlockPos pBlockPos) {
            return 15;
        }

        @Override
        public int getRawBrightness(BlockPos pBlockPos, int pAmount) {
            return 15;
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pPos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return pos.equals(BlockPos.ZERO) ? tempState : AIR;
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return pos.equals(BlockPos.ZERO) ? tempFluid : EMPTY;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getMinBuildHeight() {
            return 0;
        }
    }
}
