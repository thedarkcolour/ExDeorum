package thedarkcolour.exnihiloreborn.client.ter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import thedarkcolour.exnihiloreborn.blockentity.SieveBlockEntity;

// todo
public class SieveRenderer extends TileEntityRenderer<SieveBlockEntity> {
    public SieveRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SieveBlockEntity sieve, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffers, int light, int overlay) {
        ItemStack contents = sieve.getItem();

        if (!contents.isEmpty() && contents.getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem) contents.getItem();
            float percentage = (float) sieve.getProgress() / 100.0f;
            IVertexBuilder builder = buffers.getBuffer(CrucibleRenderer.TOP_LAYERS.getUnchecked(item.getBlock()));
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(CrucibleRenderer.TOP_TEXTURES.getUnchecked(item.getBlock()));

            BarrelRenderer.renderContents(builder, stack, percentage, 0xffffff, sprite, light, 1.0f, 11.0f, 15.0f);
        }
    }
}
