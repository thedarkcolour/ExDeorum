package thedarkcolour.exdeorum.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.client.ClientHandler;
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

            blockRenderer.renderSingleBlock(state, stack, buffers, light, overlay, ModelData.EMPTY, null);

            stack.popPose();
        } else {
            // todo render a flat item
        }

        barrel.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
            var fluidStack = tank.getFluidInTank(0);

            if (!fluidStack.isEmpty()) { // Get texture
                var fluid = fluidStack.getFluid();
                var level = barrel.getLevel();
                var blockPos = barrel.getBlockPos();
                var sprite = ForgeHooksClient.getFluidSprites(level, blockPos, fluid.defaultFluidState())[0];

                // Get color
                var col = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);
                // Split into RGB components
                var r = (col >> 16) & 0xff;
                var g = (col >> 8) & 0xff;
                var b = (col >> 0) & 0xff;

                if (barrel.isBrewing()) {
                    float progress = barrel.progress;

                    // Transition between water color and witch water color (551ec6)
                    r = (int) Mth.lerp(progress, r, 85);
                    g = (int) Mth.lerp(progress, g, 30);
                    b = (int) Mth.lerp(progress, b, 198);
                }


                // Setup rendering
                var builder = buffers.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState()));

                RenderUtil.renderFlatSpriteLerp(builder, stack, fluidStack.getAmount() / 1000.0f, r, g, b, sprite, light, 2.0f, 1.0f, 14.0f);
            }
        });

        // render compost
        if (barrel.compost > 0) {
            var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(COMPOST_DIRT_TEXTURE);
            var builder = buffers.getBuffer(RenderType.solid());

            float compostProgress = barrel.progress;
            int r, g, b;

            if (ExDeorum.IS_JUNE && barrel.getLevel() != null) {
                var rainbow = ClientHandler.getRainbowColor(barrel.getLevel().getGameTime(), partialTicks);
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
