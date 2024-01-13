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
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.ModelData;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.config.EConfig;

public class InfestedLeavesRenderer implements BlockEntityRenderer<InfestedLeavesBlockEntity> {
    @Override
    public void render(InfestedLeavesBlockEntity te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int unused) {
        if (EConfig.CLIENT.useFastInfestedLeaves.get() || RenderUtil.IRIS_ACCESS.areShadersEnabled()) return;

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
        int progress = Math.min((int) (te.getProgress() * 16000), 16000);
        // Render
        var model = mc.getBlockRenderer().getBlockModel(state);
        var pos = te.getBlockPos();
        mc.getBlockRenderer().getModelRenderer().tesselateBlock(level, model, state, pos, stack, buffer.getBuffer(RenderUtil.TINTED_CUTOUT_MIPPED), false, level.random, state.getSeed(pos), progress, ModelData.EMPTY, null);
    }
}
