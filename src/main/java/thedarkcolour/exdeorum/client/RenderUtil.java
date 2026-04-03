/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.joml.Vector3f;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.ter.SieveRenderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class RenderUtil {
    private static final Map<Block, RenderFace> TOP_FACES = new HashMap<>();
    // TODO: port TINTED_CUTOUT_MIPPED to MC 26.x RenderSetup API (RenderStateShard/CompositeState removed)
    public static final Object TINTED_CUTOUT_MIPPED = null;
    public static TextureAtlas blockAtlas;
    public static final IrisAccess IRIS_ACCESS;

    static {
        IRIS_ACCESS = () -> false;
    }

    public static void reload() {
        invalidateCaches();
        blockAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
    }

    public static void invalidateCaches() {
        SieveRenderer.MESH_TEXTURES.clear();
        TOP_FACES.clear();
        blockAtlas = null;
    }

    // TODO: port getTopFace to MC 26.x (BakedModel/BlockRenderDispatcher removed; use FluidStateModelSet/BlockStateModelSet)
    public static RenderFace getTopFaceOrDefault(Block block, Block defaultBlock) {
        return getTopFace(block);
    }

    public static RenderFace getTopFace(Block block) {
        return TOP_FACES.computeIfAbsent(block, b -> {
            // TODO: implement using 26.x block model API
            // Placeholder: use missing texture sprite
            var sprite = blockAtlas != null ? blockAtlas.getSprite(MissingTextureAtlasSprite.getLocation()) : null;
            return new RenderFace.Single(null, sprite);
        });
    }

    public static boolean isMissingTexture(TextureAtlasSprite sprite) {
        return sprite.contents().name() == MissingTextureAtlasSprite.getLocation();
    }

    // TODO: port renderFlatFluidSprite to 26.x (IClientFluidTypeExtensions no longer has getStillTexture/getTintColor)
    public static void renderFlatFluidSprite(MultiBufferSource buffers, PoseStack stack, Level level, BlockPos pos, float y, float edge, int light, int r, int g, int b, Fluid fluid) {
        if (blockAtlas == null) return;
        var builder = buffers.getBuffer(Sheets.translucentBlockSheet());
        // Use a placeholder sprite until fluid model system is ported
        var sprite = blockAtlas.getSprite(MissingTextureAtlasSprite.getLocation());
        RenderUtil.renderFlatSprite(builder, stack, y, r, g, b, sprite, light, edge);
    }

    // TODO: port renderFluidCube to 26.x (IClientFluidTypeExtensions no longer has getStillTexture/getTintColor)
    @SuppressWarnings("DuplicatedCode")
    public static void renderFluidCube(MultiBufferSource buffers, PoseStack stack, Level level, BlockPos pos, float minY, float maxY, float edge, int light, int r, int g, int b, Fluid fluid) {
        if (blockAtlas == null) return;
        var builder = buffers.getBuffer(Sheets.translucentBlockSheet());
        // Use a placeholder sprite until fluid model system is ported
        var sprite = blockAtlas.getSprite(MissingTextureAtlasSprite.getLocation());

        var pose = stack.last().pose();
        var poseNormal = stack.last().normal();

        Vector3f normal;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        float edgeMin = edge / 16f;
        float edgeMax = 1f - edge / 16f;

        // Top face
        normal = poseNormal.transform(new Vector3f(0, 1, 0));
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        // Bottom face
        normal = poseNormal.transform(new Vector3f(0, -1, 0));
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);

        // South face
        normal = poseNormal.transform(new Vector3f(0, 0, 1));
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        // North face
        normal = poseNormal.transform(new Vector3f(0, 0, -1));
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        // East face
        normal = poseNormal.transform(new Vector3f(1, 0, 0));
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        // West face
        normal = poseNormal.transform(new Vector3f(-1, 0, 0));
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
    }

    // Renders a sprite inside the barrel with the height determined by how full the barrel is.
    public static void renderFlatSpriteLerp(VertexConsumer builder, PoseStack stack, float percentage, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        float y = Mth.lerp(percentage, yMin, yMax) / 16f;
        renderFlatSprite(builder, stack, y, r, g, b, sprite, light, edge);
    }

    // Renders a sprite (y should be between 0 and 1)
    @SuppressWarnings("DuplicatedCode")
    public static void renderFlatSprite(VertexConsumer builder, PoseStack stack, float y, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge) {
        if (sprite == null) return;
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
        builder.addVertex(pose, edgeMin, y, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, y, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, y, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, y, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
    }

    public static Color getRainbowColor(long time, float partialTicks) {
        return Color.getHSBColor((180 * Mth.sin((time + partialTicks) / 30.0f) - 180) / 360.0f, 0.5f, 0.8f);
    }

    // TODO: port getFluidColor to 26.x (IClientFluidTypeExtensions no longer has getTintColor)
    public static int getFluidColor(Fluid fluid, Level level, BlockPos pos) {
        return -1; // white/no tint; use FluidModel.fluidTintSource() in 26.x
    }

    // todo use ambient occlusion
    // Renders a cuboid using the same side sprite on all six sides
    public static void renderCuboid(VertexConsumer builder, PoseStack stack, float minY, float maxY, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge) {
        if (sprite == null) return;
        var pose = stack.last().pose();
        var poseNormal = stack.last().normal();

        Vector3f normal;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        float edgeMin = edge / 16f;
        float edgeMax = 1f - edge / 16f;

        int lightU = light & '\uffff';
        int lightV = light >> 16 & '\uffff';

        // Top face
        normal = poseNormal.transform(new Vector3f(0, 1, 0));
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        // Bottom face
        normal = poseNormal.transform(new Vector3f(0, -1, 0));
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        // Adjust UV based on height of cuboid, rendering from the top down to the bottom of the texture
        float f = sprite.getV1() - sprite.getV0();
        vMax = sprite.getV0() + f * (maxY - minY);

        // South face
        normal = poseNormal.transform(new Vector3f(0, 0, -1));
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        // North face
        normal = poseNormal.transform(new Vector3f(0, 0, -1));
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        // East face
        normal = poseNormal.transform(new Vector3f(1, 0, 0));
        builder.addVertex(pose, edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        // West face
        normal = poseNormal.transform(new Vector3f(-1, 0, 0));
        builder.addVertex(pose, edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose, edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
    }

    public interface IrisAccess {
        boolean areShadersEnabled();
    }
}
