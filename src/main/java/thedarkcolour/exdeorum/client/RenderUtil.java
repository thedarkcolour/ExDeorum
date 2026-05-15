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
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;
import thedarkcolour.exdeorum.client.ter.SieveRenderer;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RenderUtil {
    private static final Map<Block, RenderFace> TOP_FACES = new HashMap<>();
    public static final RenderType TINTED_CUTOUT_MIPPED = Sheets.cutoutBlockItemSheet();
    public static final IrisAccess IRIS_ACCESS;

    static {
        IRIS_ACCESS = () -> false;
    }

    public static void reload() {
        invalidateCaches();
    }

    public static void invalidateCaches() {
        SieveRenderer.MESH_TEXTURES.clear();
        TOP_FACES.clear();
    }

    public static RenderFace getTopFaceOrDefault(Block block, Block defaultBlock) {
        var face = getTopFace(block);
        return face.isMissingTexture() ? getTopFace(defaultBlock) : face;
    }

    public static RenderFace getTopFace(Block block) {
        return TOP_FACES.computeIfAbsent(block, RenderUtil::loadTopFace);
    }

    public static boolean isMissingTexture(TextureAtlasSprite sprite) {
        return sprite.contents().name() == MissingTextureAtlasSprite.getLocation();
    }

    public static void renderFlatFluidSprite(MultiBufferSource buffers, PoseStack stack, Level level, BlockPos pos, float y, float edge, int light, int r, int g, int b, Fluid fluid) {
        var builder = buffers.getBuffer(getFluidRenderType(fluid));
        var sprite = getFluidSprite(fluid);
        RenderUtil.renderFlatSprite(builder, stack, y, r, g, b, sprite, light, edge);
    }

    public static void renderFlatFluidSprite(VertexConsumer builder, PoseStack.Pose pose, float y, float edge, int light, int r, int g, int b, Fluid fluid) {
        RenderUtil.renderFlatSprite(builder, pose, y, r, g, b, getFluidSprite(fluid), light, edge);
    }

    @SuppressWarnings("DuplicatedCode")
    public static void renderFluidCube(MultiBufferSource buffers, PoseStack stack, Level level, BlockPos pos, float minY, float maxY, float edge, int light, int r, int g, int b, Fluid fluid) {
        var builder = buffers.getBuffer(getFluidRenderType(fluid));
        var sprite = getFluidSprite(fluid);

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

    public static void renderFluidCube(VertexConsumer builder, PoseStack.Pose pose, float minY, float maxY, float edge, int light, int r, int g, int b, Fluid fluid) {
        RenderUtil.renderCuboid(builder, pose, minY, maxY, r, g, b, getFluidSprite(fluid), light, edge);
    }

    // Renders a sprite inside the barrel with the height determined by how full the barrel is.
    public static void renderFlatSpriteLerp(VertexConsumer builder, PoseStack stack, float percentage, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        float y = Mth.lerp(percentage, yMin, yMax) / 16f;
        renderFlatSprite(builder, stack, y, r, g, b, sprite, light, edge);
    }

    public static void renderFlatSpriteLerp(VertexConsumer builder, PoseStack.Pose pose, float percentage, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge, float yMin, float yMax) {
        float y = Mth.lerp(percentage, yMin, yMax) / 16f;
        renderFlatSprite(builder, pose, y, r, g, b, sprite, light, edge);
    }

    // Renders a sprite (y should be between 0 and 1)
    @SuppressWarnings("DuplicatedCode")
    public static void renderFlatSprite(VertexConsumer builder, PoseStack stack, float y, int r, int g, int b, @Nullable TextureAtlasSprite sprite, int light, float edge) {
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

    public static void renderFlatSprite(VertexConsumer builder, PoseStack.Pose pose, float y, int r, int g, int b, @Nullable TextureAtlasSprite sprite, int light, float edge) {
        if (sprite == null) return;
        var normal = pose.normal().transform(new Vector3f(0, 1, 0));
        float edgeMin = edge / 16.0f;
        float edgeMax = (16.0f - edge) / 16.0f;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        builder.addVertex(pose.pose(), edgeMin, y, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, y, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, y, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, y, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setLight(light).setNormal(normal.x, normal.y, normal.z);
    }

    public static Color getRainbowColor(long time, float partialTicks) {
        return Color.getHSBColor((180 * Mth.sin((time + partialTicks) / 30.0f) - 180) / 360.0f, 0.5f, 0.8f);
    }

    public static TextureAtlasSprite getBlockSprite(Identifier location) {
        return ((TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS)).getSprite(location);
    }

    public static int getFluidColor(Fluid fluid, Level level, BlockPos pos) {
        var tintSource = getFluidModel(fluid).fluidTintSource();
        if (tintSource == null) {
            return -1;
        }
        if (level instanceof BlockAndTintGetter getter) {
            return tintSource.colorInWorld(fluid.defaultFluidState(), level.getBlockState(pos), getter, pos);
        }
        return tintSource.color(fluid.defaultFluidState());
    }

    private static RenderFace loadTopFace(Block block) {
        var state = block.defaultBlockState();
        var model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        var random = RandomSource.create(block.hashCode());
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(BlockAndTintGetter.EMPTY, BlockPos.ZERO, state, random, parts);

        var layers = new LinkedHashMap<String, RenderFace.CompositeLayer>();
        for (var part : parts) {
            for (var quad : part.getQuads(Direction.UP)) {
                var materialInfo = quad.materialInfo();
                var key = materialInfo.itemRenderType() + "::" + materialInfo.sprite().contents().name();
                layers.putIfAbsent(key, new RenderFace.CompositeLayer(materialInfo.itemRenderType(), materialInfo.sprite()));
            }
        }

        if (layers.isEmpty()) {
            var particle = getTopTexture(block, state);
            return new RenderFace.Single(inferMaterialRenderType(particle), particle);
        }

        if (layers.size() == 1) {
            return new RenderFace.Single(layers.values().iterator().next().renderType(), layers.values().iterator().next().sprite());
        }

        return new RenderFace.Composite(layers.values().toArray(RenderFace.CompositeLayer[]::new));
    }

    private static TextureAtlasSprite getTopTexture(Block block, net.minecraft.world.level.block.state.BlockState state) {
        var registryName = BuiltInRegistries.BLOCK.getKey(block);
        var sprite = getBlockSprite(registryName.withPrefix("block/"));
        if (isMissingTexture(sprite)) {
            sprite = getBlockSprite(Identifier.fromNamespaceAndPath(registryName.getNamespace(), "block/" + registryName.getPath() + "_top"));
        }
        if (isMissingTexture(sprite)) {
            sprite = Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite();
        }
        return sprite;
    }

    private static RenderType inferMaterialRenderType(TextureAtlasSprite sprite) {
        return sprite.transparency().hasTranslucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
    }

    private static FluidModel getFluidModel(Fluid fluid) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
    }

    public static TextureAtlasSprite getFluidSprite(Fluid fluid) {
        return getFluidModel(fluid).stillMaterial().sprite();
    }

    public static RenderType getFluidRenderType(Fluid fluid) {
        return getFluidModel(fluid).layer().translucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet();
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

    public static void renderCuboid(VertexConsumer builder, PoseStack.Pose pose, float minY, float maxY, int r, int g, int b, TextureAtlasSprite sprite, int light, float edge) {
        if (sprite == null) return;
        var poseNormal = pose.normal();

        Vector3f normal;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        float edgeMin = edge / 16f;
        float edgeMax = 1f - edge / 16f;

        int lightU = light & '\uffff';
        int lightV = light >> 16 & '\uffff';

        normal = poseNormal.transform(new Vector3f(0, 1, 0));
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        normal = poseNormal.transform(new Vector3f(0, -1, 0));
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        float f = sprite.getV1() - sprite.getV0();
        vMax = sprite.getV0() + f * (maxY - minY);

        normal = poseNormal.transform(new Vector3f(0, 0, -1));
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        normal = poseNormal.transform(new Vector3f(0, 0, -1));
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        normal = poseNormal.transform(new Vector3f(1, 0, 0));
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMax, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);

        normal = poseNormal.transform(new Vector3f(-1, 0, 0));
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, maxY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMin).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMin).setColor(r, g, b, 255).setUv(uMin, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
        builder.addVertex(pose.pose(), edgeMin, minY, edgeMax).setColor(r, g, b, 255).setUv(uMax, vMax).setUv1(0, 10).setUv2(lightU, lightV).setNormal(normal.x, normal.y, normal.z);
    }

    public interface IrisAccess {
        boolean areShadersEnabled();
    }
}
