package thedarkcolour.exnihiloreborn.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.blockentity.BarrelBlockEntity;

public class BarrelRenderer implements BlockEntityRenderer<BarrelBlockEntity> {
    public static final ResourceLocation COMPOST_DIRT_TEXTURE = new ResourceLocation(ExNihiloReborn.ID, "block/compost_dirt");
    private final BlockRenderDispatcher blockRenderer;

    public BarrelRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BarrelBlockEntity barrel, float pPartialTick, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        var item = barrel.getItem().getItem();

        // render an output
        if (item instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var state = block.defaultBlockState();

            stack.pushPose();
            stack.translate(2 / 16f, 2 / 16f, 2 / 16f);
            stack.scale(12 / 16f, 12 / 16f, 12 / 16f);

            blockRenderer.renderSingleBlock(state, stack, buffers, light, overlay, ModelData.EMPTY, null);
        } else {
            // todo render a flat item
        }

        barrel.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var stack = tank.getFluidInTank(0);
        });
    }

    @Override
    public void render(BarrelBlockEntity barrel, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffers, int light, int overlay) {
        Item item = barrel.getItem().getItem();

        // render an output
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            BlockState state = block.defaultBlockState();

            stack.pushPose();
            stack.translate(2.0f / 16.0f, 2.0f / 16.0f, 2.0f / 16.0f);
            stack.scale(12.0f / 16.0f, 12.0f / 16.0f, 12.0f / 16.0f);

            Minecraft.getInstance().getBlockRenderer().renderBlock(state, stack, buffers, light, overlay, EmptyModelData.INSTANCE);

            stack.popPose();
        }
        // render a fluid
        barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);

            if (!fluidStack.isEmpty()) { // Get texture
                Fluid fluidType = fluidStack.getFluid();
                World level = barrel.getLevel();
                BlockPos blockPos = barrel.getBlockPos();
                TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(level, blockPos, fluidType.defaultFluidState())[0];

                // Get color
                int col = fluidType.getAttributes().getColor(level, blockPos);
                // Split into RGB components
                int r = (col >> 16) & 0xff;
                int g = (col >> 8) & 0xff;
                int b = (col >> 0) & 0xff;

                if (barrel.isBrewing()) {
                    float progress = barrel.progress;

                    // Transition between water color and witch water color (551ec6)
                    r = (int) MathHelper.lerp(progress, r, 85);
                    g = (int) MathHelper.lerp(progress, g, 30);
                    b = (int) MathHelper.lerp(progress, b, 198);
                }

                // Setup rendering
                IVertexBuilder builder = buffers.getBuffer(RenderTypeLookup.canRenderInLayer(fluidType.defaultFluidState(), RenderType.translucent()) ? RenderType.translucent() : RenderType.solid());

                renderContents(builder, stack, fluidStack.getAmount() / 1000.0f, r, g, b, sprite, light, 2.0f, 1.0f, 14.0f);
            }
        });

        // render compost
        if (barrel.compost > 0) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(COMPOST_DIRT_TEXTURE);
            IVertexBuilder builder = buffers.getBuffer(RenderType.solid());

            float compostProgress = barrel.progress;

            // todo custom compost colors
            // Transition between default green and dirt brown
            int r = (int) MathHelper.lerp(compostProgress, 53,  238);
            int g = (int) MathHelper.lerp(compostProgress, 168, 169);
            int b = (int) MathHelper.lerp(compostProgress, 42,  109);

            renderContents(builder, stack, barrel.compost / 1000.0f, r, g, b, sprite, light, 2.0f, 1.0f, 14.0f);
        }
    }

    public static void renderContents(IVertexBuilder builder, MatrixStack stack, float percentage, int color, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        renderContents(builder, stack, percentage, (color >> 16) & 0xff, (color >> 8) & 0xff, (color >> 0) & 0xff, sprite, light, edge, yMin, yMax);
    }

    // Renders a sprite inside the barrel with the height determined by how full the barrel is.
    public static void renderContents(IVertexBuilder builder, MatrixStack stack, float percentage, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        // Height
        float height = ((yMax - yMin) / 16.0f) * percentage;

        // Offset by specified number of pixels
        stack.pushPose();
        stack.translate(0.0, yMin / 16.0, 0.0);

        // Render quad
        renderQuad(builder, stack, height, r, g, b, sprite, light, edge);

        stack.popPose();
    }

    // Renders a sprite
    private static void renderQuad(IVertexBuilder builder, MatrixStack stack, float quadHeight, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge) {
        Matrix4f pose = stack.last().pose();

        // Texture coordinates
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        float edgeMin = edge / 16.0f;
        float edgeMax = (16.0f - edge) / 16.0f;

        builder.vertex(pose, edgeMin, quadHeight, edgeMin).color(r, g, b, 255).uv(uMin, vMin).uv2(light).normal(0.0f, 1.0f, 0.0f).endVertex();
        builder.vertex(pose, edgeMin, quadHeight, edgeMax).color(r, g, b, 255).uv(uMin, vMax).uv2(light).normal(0.0f, 1.0f, 0.0f).endVertex();
        builder.vertex(pose, edgeMax, quadHeight, edgeMax).color(r, g, b, 255).uv(uMax, vMax).uv2(light).normal(0.0f, 1.0f, 0.0f).endVertex();
        builder.vertex(pose, edgeMax, quadHeight, edgeMin).color(r, g, b, 255).uv(uMax, vMin).uv2(light).normal(0.0f, 1.0f, 0.0f).endVertex();
    }
}
