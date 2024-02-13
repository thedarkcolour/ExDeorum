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
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;

public class CrucibleRenderer implements BlockEntityRenderer<AbstractCrucibleBlockEntity> {
    @Override
    public void render(AbstractCrucibleBlockEntity crucible, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        crucible.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var level = crucible.getLevel();
            if (level == null) return;

            var fluidStack = tank.getFluidInTank(0);

            // These are percentages
            var solids = (float) crucible.getSolids() / (float) AbstractCrucibleBlockEntity.MAX_SOLIDS;
            var liquid = (float) fluidStack.getAmount() / (float) tank.getTankCapacity(0);

            if (solids != 0 || liquid != 0) {
                var pos = crucible.getBlockPos();

                if (liquid != 0) {
                    var fluid = fluidStack.getFluid();
                    var color = RenderUtil.getFluidColor(fluid, level, pos);
                    float y = Mth.lerp(liquid, 4.0f, 14.0f) / 16f;

                    RenderUtil.renderFlatFluidSprite(buffers, stack, level, pos, y, 2.0f, light, (color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, fluid);
                }
                if (solids != 0) {
                    // eating my words rn :(
                    var lastMelted = crucible.getLastMelted();
                    if (lastMelted == null) {
                        lastMelted = crucible.getDefaultMeltBlock();
                    }

                    var face = RenderUtil.getTopFaceOrDefault(lastMelted, crucible.getDefaultMeltBlock());

                    var color = Minecraft.getInstance().getBlockColors().getColor(lastMelted.defaultBlockState(), level, pos, 0);

                    if (color == -1) color = 0xffffff;

                    face.renderFlatSpriteLerp(buffers, stack, solids, (color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, light, 2.0f, 4.0f, 14.0f);

                }
            }
        });
    }
}
