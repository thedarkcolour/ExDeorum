package thedarkcolour.exnihiloreborn.client.ter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import thedarkcolour.exnihiloreborn.blockentity.AbstractCrucibleBlockEntity;

public class CrucibleRenderer extends TileEntityRenderer<AbstractCrucibleBlockEntity> {
    public static final LoadingCache<Block, ResourceLocation> TOP_TEXTURES = CacheBuilder.newBuilder().maximumSize(30).build(new CacheLoader<Block, ResourceLocation>() {
        @Override
        public ResourceLocation load(Block key) {
            ResourceLocation registryName = key.getRegistryName();
            return new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath());
        }
    });
    public static final LoadingCache<Block, RenderType> TOP_LAYERS = CacheBuilder.newBuilder().maximumSize(30).build(new CacheLoader<Block, RenderType>() {
        @Override
        public RenderType load(Block key) {
            return RenderType.chunkBufferLayers().stream().filter(layer -> RenderTypeLookup.canRenderInLayer(key.defaultBlockState(), layer)).findFirst().get();
        }
    });

    public CrucibleRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(AbstractCrucibleBlockEntity crucible, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffers, int light, int overlay) {
        crucible.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);

            float solids = (float) crucible.getSolids() / (float) AbstractCrucibleBlockEntity.MAX_SOLIDS;
            float liquid = (float) fluidStack.getAmount() / (float) tank.getTankCapacity(0);

            if (solids != 0 || liquid != 0) {
                World level = crucible.getLevel();
                BlockPos blockPos = crucible.getBlockPos();

                IVertexBuilder builder;
                TextureAtlasSprite sprite;
                float percentage;

                if (solids != 0) {
                    percentage = solids;
                    builder = buffers.getBuffer(TOP_LAYERS.getUnchecked(crucible.getLastMelted()));
                    sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(TOP_TEXTURES.getUnchecked(crucible.getLastMelted()));

                    int col = Minecraft.getInstance().getBlockColors().getColor(crucible.getLastMelted().defaultBlockState(), level, blockPos, 0);

                    if (col == -1) col = 0xffffff;

                    BarrelRenderer.renderContents(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
                if (liquid != 0) {
                    Fluid fluidType = fluidStack.getFluid();

                    percentage = liquid;
                    builder = buffers.getBuffer(RenderTypeLookup.canRenderInLayer(fluidType.defaultFluidState(), RenderType.translucent()) ? RenderType.translucent() : RenderType.solid());
                    sprite = ForgeHooksClient.getFluidSprites(level, blockPos, fluidType.defaultFluidState())[0];

                    // Set biome colors
                    int col = fluidType.getAttributes().getColor(level, blockPos);

                    BarrelRenderer.renderContents(builder, stack, percentage, col, sprite, light, 2.0f, 4.0f, 14.0f);
                }
            }
        });
    }
}
