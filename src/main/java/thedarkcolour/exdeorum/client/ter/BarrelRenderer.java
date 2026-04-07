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
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.material.Fluid;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.BarrelBlock;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.config.EConfig;

public class BarrelRenderer implements BlockEntityRenderer<BarrelBlockEntity, BarrelRenderer.BarrelRenderState> {
    public static final Identifier COMPOST_DIRT_TEXTURE = ExDeorum.loc("block/compost_dirt");
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final net.minecraft.client.renderer.block.BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;

    public BarrelRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockModelResolver = ctx.blockModelResolver();
        this.itemModelResolver = ctx.itemModelResolver();
    }

    @Override
    public BarrelRenderState createRenderState() {
        return new BarrelRenderState();
    }

    @Override
    public void extractRenderState(BarrelBlockEntity barrel, BarrelRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(barrel, state, partialTicks, cameraPosition, breakProgress);

        state.blockItemModel.clear();
        state.outputItem.clear();
        state.renderBlockItem = false;
        state.fluid = null;
        state.hasFluid = false;
        state.hasCompost = false;
        state.transparent = barrel.transparent;

        var item = barrel.getItem();
        if (!item.isEmpty()) {
            if (item.getItem() instanceof BlockItem blockItem) {
                this.blockModelResolver.update(state.blockItemModel, blockItem.getBlock().defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
                state.renderBlockItem = true;
            } else {
                this.itemModelResolver.updateForTopItem(state.outputItem, item, ItemDisplayContext.FIXED, barrel.getLevel(), null, 0);
            }
        }

        var fluidStack = barrel.getTank().getFluidInTank(0);
        if (!fluidStack.isEmpty() && barrel.getLevel() != null) {
            var fluid = fluidStack.getFluid();
            var percentage = fluidStack.getAmount() / 1000.0f;
            var y = Mth.lerp(percentage, BarrelBlock.BARREL_FLUID_BOTTOM, BarrelBlock.BARREL_FLUID_TOP);
            var inputFluidColor = RenderUtil.getFluidColor(fluid, barrel.getLevel(), barrel.getBlockPos());
            int r = (inputFluidColor >> 16) & 0xff;
            int g = (inputFluidColor >> 8) & 0xff;
            int b = inputFluidColor & 0xff;

            if (barrel.isBrewing()) {
                float progress = barrel.progress;
                r = (int) Mth.lerp(progress, r, barrel.r);
                g = (int) Mth.lerp(progress, g, barrel.g);
                b = (int) Mth.lerp(progress, b, barrel.b);
            }

            state.fluid = fluid;
            state.hasFluid = true;
            state.fluidY = y;
            state.fluidColor = packRgb(r, g, b);
        }

        if (barrel.compost > 0) {
            float compostProgress = barrel.progress;
            int r;
            int g;
            int b;

            if (ExDeorum.IS_JUNE && EConfig.CLIENT.rainbowCompostDuringJune.get() && barrel.getLevel() != null) {
                var rainbow = RenderUtil.getRainbowColor(barrel.getLevel().getGameTime(), partialTicks);
                r = rainbow.getRed();
                g = rainbow.getGreen();
                b = rainbow.getBlue();
            } else {
                r = barrel.r;
                g = barrel.g;
                b = barrel.b;
            }

            r = (int) Mth.lerp(compostProgress, r, 238);
            g = (int) Mth.lerp(compostProgress, g, 169);
            b = (int) Mth.lerp(compostProgress, b, 109);

            state.hasCompost = true;
            state.compostPercentage = barrel.compost / 1000.0f;
            state.compostColor = packRgb(r, g, b);
        }
    }

    @Override
    public void submit(BarrelRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (state.renderBlockItem && !state.blockItemModel.isEmpty()) {
            stack.pushPose();
            stack.translate(2 / 16f, 2 / 16f, 2 / 16f);
            stack.scale(12 / 16f, 12 / 16f, 12 / 16f);
            state.blockItemModel.submitMultiLayer(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        } else if (!state.outputItem.isEmpty()) {
            stack.pushPose();
            stack.translate(0.5, 1.5 / 16f + (state.hasFluid ? state.fluidY : 0.0f), 0.5);
            stack.mulPose(Axis.XP.rotation(Mth.HALF_PI));
            state.outputItem.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }

        if (state.hasFluid && state.fluid != null) {
            int r = (state.fluidColor >> 16) & 0xff;
            int g = (state.fluidColor >> 8) & 0xff;
            int b = state.fluidColor & 0xff;
            if (state.transparent) {
                collector.submitCustomGeometry(stack, RenderUtil.getFluidRenderType(state.fluid), (pose, buffer) ->
                    RenderUtil.renderFluidCube(buffer, pose, BarrelBlock.BARREL_FLUID_BOTTOM, state.fluidY, 2.0f, state.lightCoords, r, g, b, state.fluid)
                );
            } else {
                collector.submitCustomGeometry(stack, RenderUtil.getFluidRenderType(state.fluid), (pose, buffer) ->
                    RenderUtil.renderFlatFluidSprite(buffer, pose, state.fluidY, 2.0f, state.lightCoords, r, g, b, state.fluid)
                );
            }
        }

        if (state.hasCompost) {
            var sprite = RenderUtil.getBlockSprite(COMPOST_DIRT_TEXTURE);
            int r = (state.compostColor >> 16) & 0xff;
            int g = (state.compostColor >> 8) & 0xff;
            int b = state.compostColor & 0xff;
            collector.submitCustomGeometry(stack, RenderUtil.TINTED_CUTOUT_MIPPED, (pose, buffer) ->
                RenderUtil.renderFlatSpriteLerp(buffer, pose, state.compostPercentage, r, g, b, sprite, state.lightCoords, 2.0f, BarrelBlock.BARREL_FLUID_BOTTOM * 16f, BarrelBlock.BARREL_FLUID_TOP * 16f)
            );
        }
    }

    private static int packRgb(int r, int g, int b) {
        return (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public static class BarrelRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState blockItemModel = new BlockModelRenderState();
        public final ItemStackRenderState outputItem = new ItemStackRenderState();
        public boolean renderBlockItem;
        public boolean hasFluid;
        public boolean transparent;
        public float fluidY;
        public Fluid fluid;
        public int fluidColor;
        public boolean hasCompost;
        public float compostPercentage;
        public int compostColor;
    }
}
