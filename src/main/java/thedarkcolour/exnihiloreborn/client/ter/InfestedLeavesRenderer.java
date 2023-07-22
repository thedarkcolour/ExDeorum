package thedarkcolour.exnihiloreborn.client.ter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.EmptyModelData;
import thedarkcolour.exnihiloreborn.blockentity.InfestedLeavesBlockEntity;

public class InfestedLeavesRenderer extends TileEntityRenderer<InfestedLeavesBlockEntity> {
    public InfestedLeavesRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(InfestedLeavesBlockEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = te.getMimic();

        // Default to oak leaves
        if (state == null) state = Blocks.OAK_LEAVES.defaultBlockState();

        // If something is wrong render default leaves
        if (!te.hasLevel()) {
            Minecraft.getInstance().getBlockRenderer().renderBlock(state, stack, buffer, light, overlay, EmptyModelData.INSTANCE);
            return;
        }

        // Get infested percentage
        float progress = te.getProgress();

        // Get colors
        int col = mc.getBlockColors().getColor(state, te.getLevel(), te.getBlockPos(), 0);

        // Average the white color with the biome color
        float r = MathHelper.lerp(progress, (col >> 16) & 0xff, 255.0f);
        float g = MathHelper.lerp(progress, (col >> 8 ) & 0xff, 255.0f);
        float b = MathHelper.lerp(progress, (col >> 0 ) & 0xff, 255.0f);

        // Cap to 255
        float red = (Math.min(255, r)) / 255.0f;
        float green = (Math.min(255, g)) / 255.0f;
        float blue = (Math.min(255, b)) / 255.0f;

        // Render
        IBakedModel model = mc.getBlockRenderer().getBlockModel(state);
        mc.getBlockRenderer().getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderTypeLookup.getRenderType(state, false)), state, model, red, green, blue, light, overlay, EmptyModelData.INSTANCE);
    }
}
