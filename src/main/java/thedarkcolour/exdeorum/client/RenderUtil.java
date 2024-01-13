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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.Pair;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.ter.SieveRenderer;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.HashMap;
import java.util.Map;

public class RenderUtil {
    private static final VarHandle COMPOSITE_MODEL_CHILDREN;
    private static final Map<Block, RenderFace> TOP_FACES = new HashMap<>();
    public static final RenderStateShard.ShaderStateShard RENDER_TYPE_TINTED_CUTOUT_MIPPED_SHADER = new RenderStateShard.ShaderStateShard(RenderUtil::getRenderTypeTintedCutoutMippedShader);
    public static final RenderType TINTED_CUTOUT_MIPPED = RenderType.create(ExDeorum.ID + ":tinted_cutout_mipped", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, RenderType.SMALL_BUFFER_SIZE, false, false, RenderType.CompositeState.builder().setLightmapState(RenderStateShard.LIGHTMAP).setShaderState(RENDER_TYPE_TINTED_CUTOUT_MIPPED_SHADER).setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED).createCompositeState(true));
    public static TextureAtlas blockAtlas;
    public static ShaderInstance renderTypeTintedCutoutMippedShader;
    public static final IrisAccess IRIS_ACCESS;

    static {
        IrisAccess irisAccess;
        try {
            Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            irisAccess = IrisApi.getInstance()::isShaderPackInUse;
        } catch (ClassNotFoundException e) {
            irisAccess = () -> false;
        }
        IRIS_ACCESS = irisAccess;

        var lookup = MethodHandles.lookup();
        try {
            COMPOSITE_MODEL_CHILDREN = MethodHandles.privateLookupIn(CompositeModel.Baked.class, lookup).findVarHandle(CompositeModel.Baked.class, "children", ImmutableMap.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    public static RenderFace getTopFaceOrDefault(Block block, Block defaultBlock) {
        var face = getTopFace(block);
        if (face.isMissingTexture()) {
            return getTopFace(defaultBlock);
        } else {
            return face;
        }
    }

    public static RenderFace getTopFace(Block block) {
        if (TOP_FACES.containsKey(block)) {
            return TOP_FACES.get(block);
        } else {
            var rand = new LegacyRandomSource(block.hashCode());
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(block.defaultBlockState());
            RenderFace face;

            if (model instanceof CompositeModel.Baked composite) {
                @SuppressWarnings("unchecked")
                ImmutableMap<String, BakedModel> children = (ImmutableMap<String, BakedModel>) COMPOSITE_MODEL_CHILDREN.get(composite);
                var builder = new ImmutableList.Builder<Pair<RenderType, TextureAtlasSprite>>();

                for (var childModel : children.values()) {
                    var singleFace = getFaceFromModel(block, rand, childModel);
                    builder.add(Pair.of(singleFace.renderType(), singleFace.sprite()));
                }

                face = new RenderFace.Composite(builder.build());
            } else {
                face = getFaceFromModel(block, rand, model);
            }

            TOP_FACES.put(block, face);

            return face;
        }
    }

    private static RenderFace.Single getFaceFromModel(Block block, RandomSource rand, BakedModel model) {
        var texture = getTopTexture(block, model);
        var blockTypes = model.getRenderTypes(block.defaultBlockState(), rand, ModelData.EMPTY);
        for (var bufferLayer : RenderType.chunkBufferLayers()) {
            if (blockTypes.contains(bufferLayer)) {
                return new RenderFace.Single(bufferLayer, texture);
            }
        }
        throw new IllegalStateException("No render type found for block " + block);
    }

    private static TextureAtlasSprite getTopTexture(Block block, BakedModel model) {
        var registryName = ForgeRegistries.BLOCKS.getKey(block);
        var sprite = blockAtlas.getSprite(registryName.withPrefix("block/"));
        // for stuff like azalea bush, retry to get the top texture
        if (isMissingTexture(sprite)) {
            sprite = blockAtlas.getSprite(new ResourceLocation(registryName.getNamespace(), "block/" + registryName.getPath() + "_top"));
        }
        if (isMissingTexture(sprite)) {
            sprite = model.getParticleIcon(ModelData.EMPTY);
        }
        return sprite;
    }

    public static boolean isMissingTexture(TextureAtlasSprite sprite) {
        return sprite.contents().name() == MissingTextureAtlasSprite.getLocation();
    }

    public static void renderFlatFluidSprite(MultiBufferSource buffers, PoseStack stack, Level level, BlockPos pos, float y, float edge, int light, int r, int g, int b, Fluid fluid) {
        var extensions = IClientFluidTypeExtensions.of(fluid);
        var state = fluid.defaultFluidState();
        var builder = buffers.getBuffer(Sheets.translucentCullBlockSheet());

        RenderUtil.renderFlatSprite(builder, stack, y, r, g, b, RenderUtil.blockAtlas.getSprite(extensions.getStillTexture(state, level, pos)), light, edge);
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
        builder.vertex(pose, edgeMin, y, edgeMin).color(r, g, b, 255).uv(uMin, vMin).overlayCoords(0, 10).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMin, y, edgeMax).color(r, g, b, 255).uv(uMin, vMax).overlayCoords(0, 10).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMax, y, edgeMax).color(r, g, b, 255).uv(uMax, vMax).overlayCoords(0, 10).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
        builder.vertex(pose, edgeMax, y, edgeMin).color(r, g, b, 255).uv(uMax, vMin).overlayCoords(0, 10).uv2(light).normal(normal.x, normal.y, normal.z).endVertex();
    }

    public static Color getRainbowColor(long time, float partialTicks) {
        return Color.getHSBColor((180 * Mth.sin((time + partialTicks) / 16.0f) - 180) / 360.0f, 0.7f, 0.8f);
    }

    public static ShaderInstance getRenderTypeTintedCutoutMippedShader() {
        return renderTypeTintedCutoutMippedShader;
    }

    public static int getFluidColor(Fluid fluid, Level level, BlockPos pos) {
        return IClientFluidTypeExtensions.of(fluid).getTintColor(fluid.defaultFluidState(), level, pos);
    }

    public interface IrisAccess {
        boolean areShadersEnabled();
    }
}
