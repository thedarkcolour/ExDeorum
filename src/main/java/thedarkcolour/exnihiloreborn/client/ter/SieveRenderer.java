package thedarkcolour.exnihiloreborn.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import thedarkcolour.exnihiloreborn.blockentity.SieveBlockEntity;

// todo
public class SieveRenderer implements BlockEntityRenderer<SieveBlockEntity> {
    public SieveRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(SieveBlockEntity sieve, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        var contents = sieve.getItem();

        if (!contents.isEmpty() && contents.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var percentage = (float) sieve.getProgress() / 100.0f;
            var builder = buffers.getBuffer(CrucibleRenderer.TOP_RENDER_TYPES.getUnchecked(block));
            var sprite = CrucibleRenderer.TOP_TEXTURES.getUnchecked(block);

            BarrelRenderer.renderContents(builder, stack, percentage, 0xffffff, sprite, light, 1.0f, 11.0f, 15.0f);
        }
    }
}
