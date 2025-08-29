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
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;

public class InfestedLeavesRenderer implements BlockEntityRenderer<InfestedLeavesBlockEntity> {

    @Override
    public void render(InfestedLeavesBlockEntity te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int unused) {
        // We render a static model when the animation is finished
        if (te.getBlockState().getValue(InfestedLeavesBlock.FULLY_INFESTED)) {
            return;
        }

        var mc = Minecraft.getInstance();
        var state = te.getMimic();

        // Default to oak leaves
        if (state == null) state = Blocks.OAK_LEAVES.defaultBlockState();

        // If something is wrong skip rendering
        var level = te.getLevel();
        if (level == null) {
            return;
        }

        // Get infested percentage
        float progress = Math.min(te.getProgress(), 16000) / 16000f;
        // Render
        var model = mc.getBlockRenderer().getBlockModel(state);
        var pos = te.getBlockPos();

        for (var renderType : model.getRenderTypes(state, level.random, ModelData.EMPTY)) {
            // Dynamically blend the provided vertex colors towards grayscale
            var vertexConsumer = new VertexConsumerWrapper(buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType))) {
                @Override
                public VertexConsumer setColor(int r, int g, int b, int a) {
                    float rF = (r / 255f), gF = (g / 255f), bF = (b / 255f);

                    float avg = rF * 0.3f + gF * 0.59f + bF * 0.11f;

                    return super.setColor(
                            Math.round(RenderUtil.mix(rF, avg, progress) * 255),
                            Math.round(RenderUtil.mix(gF, avg, progress) * 255),
                            Math.round(RenderUtil.mix(bF, avg, progress) * 255),
                            a
                    );
                }
            };

            mc.getBlockRenderer().getModelRenderer().tesselateBlock(level, model, state, pos, stack, vertexConsumer,
                    true, level.random, state.getSeed(pos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY,
                    renderType);
        }
    }
}
