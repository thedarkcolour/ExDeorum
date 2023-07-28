package thedarkcolour.exnihiloreborn.client.ter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.blockentity.AbstractCrucibleBlockEntity;

public class CrucibleRenderer implements BlockEntityRenderer<AbstractCrucibleBlockEntity> {
    public static final int TEXTURE_CACHE = 10;
    public static final LoadingCache<Block, TextureAtlasSprite> TOP_TEXTURES = CacheBuilder.newBuilder().weakValues().maximumSize(30).build(new CacheLoader<>() {
        @Override
        public TextureAtlasSprite load(Block key) {
            ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(key);
            var textureLoc = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath());
            var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureLoc);
            // for stuff like azalea bush
            if (sprite.contents().name() == MissingTextureAtlasSprite.getLocation()) {
                textureLoc = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_top");
            }
            return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureLoc);
        }
    });
    public static final LoadingCache<Block, RenderType> TOP_RENDER_TYPES = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<>() {
        @Override
        public RenderType load(Block key) {
            var rand = new LegacyRandomSource(key.hashCode());
            var blockTypes = Minecraft.getInstance().getBlockRenderer().getBlockModel(key.defaultBlockState()).getRenderTypes(key.defaultBlockState(), rand, ModelData.EMPTY);
            return RenderType.chunkBufferLayers().stream().filter(blockTypes::contains).findFirst().get();
        }
    });

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
                    var fluid = fluidStack.getFluid();

                    percentage = liquid;
                    builder = buffers.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState()));
                    sprite = ForgeHooksClient.getFluidSprites(level, blockPos, fluid.defaultFluidState())[0];

                    // Set biome colors
                    col = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);

                    BarrelRenderer.renderContents(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
                if (solids != 0){
                    percentage = solids;
                    builder = buffers.getBuffer(TOP_RENDER_TYPES.getUnchecked(crucible.getLastMelted()));
                    sprite = TOP_TEXTURES.getUnchecked(crucible.getLastMelted());

                    col = Minecraft.getInstance().getBlockColors().getColor(crucible.getLastMelted().defaultBlockState(), level, blockPos, 0);

                    if (col == -1) col = 0xffffff;

                    BarrelRenderer.renderContents(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
            }
        });
    }
}
