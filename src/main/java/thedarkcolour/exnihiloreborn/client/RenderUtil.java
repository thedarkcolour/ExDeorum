package thedarkcolour.exnihiloreborn.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import thedarkcolour.exnihiloreborn.client.ter.SieveRenderer;

public class RenderUtil {
    public static final LoadingCache<Block, RenderType> TOP_RENDER_TYPES;
    public static final LoadingCache<Block, TextureAtlasSprite> TOP_TEXTURES;
    public static TextureAtlas blockAtlas;

    static {
        TOP_RENDER_TYPES = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<>() {
            @Override
            public RenderType load(Block key) {
                var rand = new LegacyRandomSource(key.hashCode());
                var blockTypes = Minecraft.getInstance().getBlockRenderer().getBlockModel(key.defaultBlockState()).getRenderTypes(key.defaultBlockState(), rand, ModelData.EMPTY);
                return RenderType.chunkBufferLayers().stream().filter(blockTypes::contains).findFirst().get();
            }
        });
        TOP_TEXTURES = CacheBuilder.newBuilder().weakValues().build(new CacheLoader<>() {
            @Override
            public TextureAtlasSprite load(Block key) {
                ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(key);
                var textureLoc = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath());
                var sprite = blockAtlas.getSprite(textureLoc);
                // for stuff like azalea bush, retry to get the top texture
                if (sprite.contents().name() == MissingTextureAtlasSprite.getLocation()) {
                    textureLoc = new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_top");
                    sprite = blockAtlas.getSprite(textureLoc);
                }
                return sprite;
            }
        });
    }

    public static void reload() {
        invalidateCaches();
        blockAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
    }

    public static void invalidateCaches() {
        SieveRenderer.MESH_TEXTURES.invalidateAll();
        blockAtlas = null;
    }

    // uses a RGB color instead of three color components
    public static void renderFlatSpriteLerp(VertexConsumer builder, PoseStack stack, float percentage, int color, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        renderFlatSpriteLerp(builder, stack, percentage, (color >> 16) & 0xff, (color >> 8) & 0xff, (color >> 0) & 0xff, sprite, light, edge, yMin, yMax);
    }

    // Renders a sprite inside the barrel with the height determined by how full the barrel is.
    public static void renderFlatSpriteLerp(VertexConsumer builder, PoseStack stack, float percentage, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        float y = Mth.lerp(percentage, yMin, yMax) / 16f;

        renderFlatSprite(builder, stack, y, r, g, b, sprite, light, edge);
    }

    // Renders a sprite (y should be between 0 and 1)
    @SuppressWarnings("DuplicatedCode")
    public static void renderFlatSprite(VertexConsumer builder, PoseStack stack, float y, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge) {
        var pose = stack.last().pose();
        var normal = stack.last().normal().transform(new Vector3f(0, 1, 0));

        // Position coordinates
        float edgeMin = edge / 16.0f;
        float edgeMax = (16.0f - edge) / 16.0f;

        // Texture coordinates
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        // overlayCoords(0, 10) is NO_OVERLAY (0xA0000)
        builder.vertex(pose, edgeMin, y, edgeMin).color(r, g, b, 255).overlayCoords(0, 10).uv(uMin, vMin).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMin, y, edgeMax).color(r, g, b, 255).overlayCoords(0, 10).uv(uMin, vMax).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMax, y, edgeMax).color(r, g, b, 255).overlayCoords(0, 10).uv(uMax, vMax).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMax, y, edgeMin).color(r, g, b, 255).overlayCoords(0, 10).uv(uMax, vMin).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
    }
}
