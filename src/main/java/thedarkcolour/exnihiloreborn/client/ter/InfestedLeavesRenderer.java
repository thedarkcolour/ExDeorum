package thedarkcolour.exnihiloreborn.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import thedarkcolour.exnihiloreborn.blockentity.InfestedLeavesBlockEntity;

public class InfestedLeavesRenderer implements BlockEntityRenderer<InfestedLeavesBlockEntity> {
    @Override
    public void render(InfestedLeavesBlockEntity te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        var mc = Minecraft.getInstance();
        var state = te.getMimic();

        // Default to oak leaves
        if (state == null) state = Blocks.OAK_LEAVES.defaultBlockState();

        // If something is wrong render default leaves
        var level = te.getLevel();
        if (level == null) {
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, stack, buffer, light, overlay, ModelData.EMPTY, null);
            return;
        }

        // Get infested percentage
        float progress = te.getProgress();

        // Get colors
        int col = mc.getBlockColors().getColor(state, level, te.getBlockPos(), 0);

        // Average the white color with the biome color
        float r = Mth.lerp(progress, (col >> 16) & 0xff, 255.0f);
        float g = Mth.lerp(progress, (col >> 8 ) & 0xff, 255.0f);
        float b = Mth.lerp(progress, (col) & 0xff, 255.0f);

        // Cap to 255
        float red = (Math.min(255, r)) / 255.0f;
        float green = (Math.min(255, g)) / 255.0f;
        float blue = (Math.min(255, b)) / 255.0f;

        // Render
        var model = mc.getBlockRenderer().getBlockModel(state);
        for (var renderType : model.getRenderTypes(state, level.random, ModelData.EMPTY)) {
            mc.getBlockRenderer().getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false)), state, model, red, green, blue, light, overlay, ModelData.EMPTY, renderType);
        }
    }
}
