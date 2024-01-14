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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;

public class BarrelRenderer implements BlockEntityRenderer<BarrelBlockEntity> {
    public static final ResourceLocation COMPOST_DIRT_TEXTURE = new ResourceLocation(ExDeorum.ID, "block/compost_dirt");
    private final BlockRenderDispatcher blockRenderer;

    public BarrelRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(BarrelBlockEntity barrel, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        var item = barrel.getItem().getItem();

        // render an output
        if (item instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var state = block.defaultBlockState();

            stack.pushPose();
            stack.translate(2 / 16f, 2 / 16f, 2 / 16f);
            stack.scale(12 / 16f, 12 / 16f, 12 / 16f);

            this.blockRenderer.renderSingleBlock(state, stack, buffers, light, overlay, ModelData.EMPTY, null);

            stack.popPose();
        } else {
            // todo render a flat item
        }

        barrel.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var fluidStack = tank.getFluidInTank(0);

            if (!fluidStack.isEmpty()) { // Get texture
                var fluid = fluidStack.getFluid();
                var level = barrel.getLevel();
                var pos = barrel.getBlockPos();
                var percentage = fluidStack.getAmount() / 1000.0f;
                var y = Mth.lerp(percentage, 1.0f, 14.0f) / 16f;
                var col = RenderUtil.getFluidColor(fluid, level, pos);
                // Split into RGB components
                var r = (col >> 16) & 0xff;
                var g = (col >> 8) & 0xff;
                var b = col & 0xff;

                if (barrel.isBrewing()) {
                    float progress = barrel.progress;

                    // Transition between water color and witch water color (200B41)
                    r = (int) Mth.lerp(progress, r, 32);
                    g = (int) Mth.lerp(progress, g, 11);
                    b = (int) Mth.lerp(progress, b, 65);
                }

                RenderUtil.renderFlatFluidSprite(buffers, stack, level, pos, y, 2.0f, light, r, g, b, fluid);
            }
        });

        // render compost
        if (barrel.compost > 0) {
            var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(COMPOST_DIRT_TEXTURE);
            var builder = buffers.getBuffer(RenderType.solid());

            float compostProgress = barrel.progress;
            int r, g, b;

            if (ExDeorum.IS_JUNE && barrel.getLevel() != null) {
                var rainbow = RenderUtil.getRainbowColor(barrel.getLevel().getGameTime(), partialTicks);
                r = rainbow.getRed();
                g = rainbow.getGreen();
                b = rainbow.getBlue();
            } else {
                r = barrel.r;
                g = barrel.g;
                b = barrel.b;
            }

            // Transition between default green and dirt brown
            r = (int) Mth.lerp(compostProgress, r,  238);  // default green is
            g = (int) Mth.lerp(compostProgress, g, 169);  // default green is
            b = (int) Mth.lerp(compostProgress, b,  109);  // default green is

            RenderUtil.renderFlatSpriteLerp(builder, stack, barrel.compost / 1000.0f, r, g, b, sprite, light, 2.0f, 1.0f, 14.0f);
        }
    }
}
