package thedarkcolour.exdeorum.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;

public class CrucibleRenderer implements BlockEntityRenderer<AbstractCrucibleBlockEntity> {
    @Override
    public void render(AbstractCrucibleBlockEntity crucible, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        crucible.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var fluidStack = tank.getFluidInTank(0);

            var solids = (float) crucible.getSolids() / (float) AbstractCrucibleBlockEntity.MAX_SOLIDS;
            var liquid = (float) fluidStack.getAmount() / (float) tank.getTankCapacity(0);

            if (solids != 0 || liquid != 0) {
                var level = crucible.getLevel();
                var blockPos = crucible.getBlockPos();

                VertexConsumer builder;
                TextureAtlasSprite sprite;
                float percentage;
                int col;

                // Solids will render on top
                if (liquid != 0) {
                    var fluid = fluidStack.getFluid().defaultFluidState();
                    var extensions = IClientFluidTypeExtensions.of(fluid);

                    percentage = liquid;
                    builder = buffers.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid));
                    sprite = RenderUtil.blockAtlas.getSprite(extensions.getStillTexture(fluid, level, blockPos));

                    // Set biome colors
                    col = extensions.getTintColor(fluidStack);

                    RenderUtil.renderFlatSpriteLerp(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
                if (solids != 0){
                    percentage = solids;
                    builder = buffers.getBuffer(RenderUtil.TOP_RENDER_TYPES.getUnchecked(crucible.getLastMelted()));
                    sprite = RenderUtil.TOP_TEXTURES.getUnchecked(crucible.getLastMelted());

                    col = Minecraft.getInstance().getBlockColors().getColor(crucible.getLastMelted().defaultBlockState(), level, blockPos, 0);

                    if (col == -1) col = 0xffffff;

                    RenderUtil.renderFlatSpriteLerp(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
            }
        });
    }
}
