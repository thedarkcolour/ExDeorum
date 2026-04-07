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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.config.EConfig;

public class InfestedLeavesRenderer implements BlockEntityRenderer<InfestedLeavesBlockEntity, InfestedLeavesRenderer.InfestedLeavesRenderState> {
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public InfestedLeavesRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public InfestedLeavesRenderState createRenderState() {
        return new InfestedLeavesRenderState();
    }

    @Override
    public void extractRenderState(InfestedLeavesBlockEntity leaves, InfestedLeavesRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(leaves, state, partialTicks, cameraPosition, breakProgress);

        state.model.clear();
        if (EConfig.CLIENT.useFastInfestedLeaves.get() || RenderUtil.IRIS_ACCESS.areShadersEnabled()) {
            return;
        }

        var level = leaves.getLevel();
        if (!(level instanceof net.minecraft.client.renderer.block.BlockAndTintGetter getter)) {
            return;
        }

        BlockState mimic = leaves.getMimic();
        if (mimic == null || mimic.isAir() || mimic.getBlock() == leaves.getBlockState().getBlock()) {
            mimic = Blocks.OAK_LEAVES.defaultBlockState();
        }

        this.blockModelResolver.update(state.model, mimic, BLOCK_DISPLAY_CONTEXT);

        int baseColor = 0xFFFFFF;
        var tintSource = Minecraft.getInstance().getBlockColors().getTintSource(mimic, 0);
        if (tintSource != null) {
            var tint = tintSource.colorInWorld(mimic, getter, leaves.getBlockPos());
            if (tint != -1) {
                baseColor = tint;
            }
        }

        float progress = Mth.clamp(leaves.getProgress() / (float) InfestedLeavesBlockEntity.MAX_PROGRESS, 0.0f, 1.0f);
        int tintedColor = blendToGray(baseColor, progress);
        var tintLayers = state.model.tintLayers();
        for (int i = 0; i < tintLayers.size(); ++i) {
            tintLayers.set(i, tintedColor);
        }
    }

    @Override
    public void submit(InfestedLeavesRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (!state.model.isEmpty()) {
            state.model.submitMultiLayer(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
    }

    private static int blendToGray(int color, float progress) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int gray = Math.round(r * 0.3f + g * 0.59f + b * 0.11f);
        int blendedR = Math.round(Mth.lerp(progress, r, gray));
        int blendedG = Math.round(Mth.lerp(progress, g, gray));
        int blendedB = Math.round(Mth.lerp(progress, b, gray));
        return (blendedR & 0xFF) << 16 | (blendedG & 0xFF) << 8 | (blendedB & 0xFF);
    }

    public static class InfestedLeavesRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState model = new BlockModelRenderState();
    }
}
