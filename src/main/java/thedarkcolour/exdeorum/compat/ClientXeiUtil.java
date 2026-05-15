package thedarkcolour.exdeorum.compat;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// client-only logic shared between JEI and EMI
public class ClientXeiUtil {
    private static final ItemStack OAK_BARREL_COMPOSTING = new ItemStack(DefaultMaterials.OAK_BARREL.getItemHolder(), 1, DataComponentPatch.builder().set(DataComponents.ITEM_MODEL, ExDeorum.loc("oak_barrel_composting")).build());

    public static void renderBlock(GuiGraphicsExtractor guiGraphics, BlockState state, float x, float y, float z, float scale) {
        submitBlockPreview(guiGraphics, state, x, y, z, scale);
    }

    public static void renderItemWithAsterisk(GuiGraphicsExtractor graphics, ItemStack stack) {
        graphics.fakeItem(stack, 0, 0);
        renderAsterisk(graphics, 0, 0);
    }

    public static void renderAsterisk(GuiGraphicsExtractor graphics, int xOffset, int yOffset) {
        var font = Minecraft.getInstance().font;
        // 0xff5555 is Minecraft's red text color.
        graphics.text(font, "*", xOffset + 19 - 2 - font.width("*"), yOffset + 12, 0xffff5555);
    }

    public static void renderFilledCompostBarrel(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        guiGraphics.fakeItem(OAK_BARREL_COMPOSTING, xOffset, yOffset);
    }

    // Takes a decimal probability and returns a user-friendly percentage value
    public static Component formatChance(double probability) {
        var chance = XeiUtil.FORMATTER.format(probability * 100);
        return Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY);
    }

    private static void submitBlockPreview(GuiGraphicsExtractor graphics, BlockState state, float x, float y, float z, float scale) {
        graphics.submitGuiElementRenderState(new BlockPreviewRenderState(new Matrix3x2f(graphics.pose()), state, x, y, z, scale));
    }

    private record BlockPreviewRenderState(Matrix3x2f pose, BlockState state, float x, float y, float z,
                                           float scale) implements GuiElementRenderState {
        private static final ScreenRectangle NO_SCISSOR = null;

        @Override
        public void buildVertices(VertexConsumer consumer) {
            var matrix = createBlockPreviewMatrix(x, y, z, scale);
            var quads = new ArrayList<PreviewQuad>();
            var fluidState = state.getFluidState();

            if (fluidState.isEmpty()) {
                var model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
                var parts = new ArrayList<BlockStateModelPart>();
                model.collectParts(BlockAndTintGetter.EMPTY, BlockPos.ZERO, state, RandomSource.create(42), parts);

                for (var part : parts) {
                    for (var direction : net.minecraft.core.Direction.values()) {
                        for (var quad : part.getQuads(direction)) {
                            quads.add(PreviewQuad.fromBakedQuad(quad, matrix, getBlockTint(state, quad)));
                        }
                    }
                    for (var quad : part.getQuads(null)) {
                        quads.add(PreviewQuad.fromBakedQuad(quad, matrix, getBlockTint(state, quad)));
                    }
                }
            } else {
                addFluidCube(quads, matrix, fluidState.getType());
            }

            quads.sort(Comparator.comparingDouble(PreviewQuad::depth));
            for (var quad : quads) {
                quad.emit(consumer, pose);
            }
        }

        @Override
        public RenderPipeline pipeline() {
            return RenderPipelines.GUI_TEXTURED;
        }

        @Override
        public TextureSetup textureSetup() {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS);
            return TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler());
        }

        @Override
        public ScreenRectangle scissorArea() {
            return NO_SCISSOR;
        }

        @Override
        public ScreenRectangle bounds() {
            int minX = (int) Math.floor(x - scale);
            int minY = (int) Math.floor(y - scale);
            int size = (int) Math.ceil(scale * 2.0f);
            return new ScreenRectangle(minX, minY, size, size).transformAxisAligned(pose);
        }
    }

    private record PreviewQuad(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2, ProjectedVertex v3,
                               float depth) {
        private static PreviewQuad fromBakedQuad(net.minecraft.client.resources.model.geometry.BakedQuad quad, Matrix4f matrix, int color) {
            var v0 = ProjectedVertex.fromBakedQuad(quad.position(0), quad.packedUV(0), matrix, color);
            var v1 = ProjectedVertex.fromBakedQuad(quad.position(1), quad.packedUV(1), matrix, color);
            var v2 = ProjectedVertex.fromBakedQuad(quad.position(2), quad.packedUV(2), matrix, color);
            var v3 = ProjectedVertex.fromBakedQuad(quad.position(3), quad.packedUV(3), matrix, color);
            float centerX = matrix.transformPosition(0.5f, 0.5f, 0.5f, new Vector3f()).x;
            float quadX = (v0.x + v1.x + v2.x + v3.x) * 0.25f;
            float shade = quad.materialInfo().shade() ? getGuiFaceShade(quad.direction(), quadX, centerX) : 1.0f;
            return new PreviewQuad(
                    v0.withColor(shadeColor(v0.color, shade)),
                    v1.withColor(shadeColor(v1.color, shade)),
                    v2.withColor(shadeColor(v2.color, shade)),
                    v3.withColor(shadeColor(v3.color, shade)),
                    (v0.z + v1.z + v2.z + v3.z) * 0.25f
            );
        }

        private static PreviewQuad fromSprite(Vector3fc p0, Vector3fc p1, Vector3fc p2, Vector3fc p3, Matrix4f matrix, TextureAtlasSprite sprite, int color) {
            var v0 = ProjectedVertex.fromSprite(p0, matrix, sprite.getU0(), sprite.getV0(), color);
            var v1 = ProjectedVertex.fromSprite(p1, matrix, sprite.getU0(), sprite.getV1(), color);
            var v2 = ProjectedVertex.fromSprite(p2, matrix, sprite.getU1(), sprite.getV1(), color);
            var v3 = ProjectedVertex.fromSprite(p3, matrix, sprite.getU1(), sprite.getV0(), color);
            return new PreviewQuad(v0, v1, v2, v3, (v0.z + v1.z + v2.z + v3.z) * 0.25f);
        }

        private void emit(VertexConsumer consumer, Matrix3x2f pose) {
            v0.emit(consumer, pose);
            v1.emit(consumer, pose);
            v2.emit(consumer, pose);
            v3.emit(consumer, pose);
        }
    }

    private record ProjectedVertex(float x, float y, float z, float u, float v, int color) {
        private static ProjectedVertex fromBakedQuad(Vector3fc position, long packedUv, Matrix4f matrix, int color) {
            var transformed = matrix.transformPosition(position, new Vector3f());
            float u = Float.intBitsToFloat((int) (packedUv >> 32));
            float v = Float.intBitsToFloat((int) packedUv);
            return new ProjectedVertex(transformed.x, transformed.y, transformed.z, u, v, color);
        }

        private static ProjectedVertex fromSprite(Vector3fc position, Matrix4f matrix, float u, float v, int color) {
            var transformed = matrix.transformPosition(position, new Vector3f());
            return new ProjectedVertex(transformed.x, transformed.y, transformed.z, u, v, color);
        }

        private void emit(VertexConsumer consumer, Matrix3x2f pose) {
            consumer.addVertexWith2DPose(pose, x, y).setUv(u, v).setColor(color);
        }

        private ProjectedVertex withColor(int color) {
            return new ProjectedVertex(x, y, z, u, v, color);
        }
    }

    private static Matrix4f createBlockPreviewMatrix(float x, float y, float z, float scale) {
        return new Matrix4f()
                .translate(x, y, z)
                .scale(-scale, -scale, -scale)
                .translate(-0.5f, -0.5f, 0.0f)
                .rotateX((float) Math.toRadians(-30.0f))
                .translate(0.5f, 0.0f, -0.5f)
                .rotateY((float) Math.toRadians(45.0f))
                .translate(-0.5f, 0.0f, 0.5f)
                .translate(0.0f, 0.0f, -1.0f);
    }

    private static void addFluidCube(List<PreviewQuad> quads, Matrix4f matrix, net.minecraft.world.level.material.Fluid fluid) {
        var still = RenderUtil.getFluidSprite(fluid);
        int color = 0xffffffff;
        var level = Minecraft.getInstance().level;
        if (level != null) {
            color = 0xff000000 | (RenderUtil.getFluidColor(fluid, level, net.minecraft.core.BlockPos.ZERO) & 0xffffff);
        }

        var p000 = new Vector3f(0.0f, 0.0f, 0.0f);
        var p001 = new Vector3f(0.0f, 0.0f, 1.0f);
        var p010 = new Vector3f(0.0f, 1.0f, 0.0f);
        var p011 = new Vector3f(0.0f, 1.0f, 1.0f);
        var p100 = new Vector3f(1.0f, 0.0f, 0.0f);
        var p101 = new Vector3f(1.0f, 0.0f, 1.0f);
        var p110 = new Vector3f(1.0f, 1.0f, 0.0f);
        var p111 = new Vector3f(1.0f, 1.0f, 1.0f);

        quads.add(PreviewQuad.fromSprite(p010, p011, p111, p110, matrix, still, color));
        quads.add(PreviewQuad.fromSprite(p000, p100, p101, p001, matrix, still, color));
        quads.add(PreviewQuad.fromSprite(p001, p101, p111, p011, matrix, still, color));
        quads.add(PreviewQuad.fromSprite(p100, p000, p010, p110, matrix, still, color));
        quads.add(PreviewQuad.fromSprite(p000, p001, p011, p010, matrix, still, color));
        quads.add(PreviewQuad.fromSprite(p101, p100, p110, p111, matrix, still, color));
    }

    private static int getBlockTint(BlockState state, net.minecraft.client.resources.model.geometry.BakedQuad quad) {
        var material = quad.materialInfo();
        if (!material.isTinted()) {
            return -1;
        }
        var tintSource = Minecraft.getInstance().getBlockColors().getTintSource(state, material.tintIndex());
        if (tintSource == null) {
            return -1;
        }
        return 0xff000000 | (tintSource.color(state) & 0xffffff);
    }

    private static float getGuiFaceShade(net.minecraft.core.Direction direction, float quadX, float centerX) {
        return switch (direction) {
            case UP -> 1.0f;
            case DOWN -> 0.5f;
            default -> quadX < centerX ? 0.8f : 0.6f;
        };
    }

    private static int shadeColor(int color, float shade) {
        int alpha = color & 0xff000000;
        int red = Math.min(255, Math.round(((color >> 16) & 0xff) * shade));
        int green = Math.min(255, Math.round(((color >> 8) & 0xff) * shade));
        int blue = Math.min(255, Math.round((color & 0xff) * shade));
        return alpha | red << 16 | green << 8 | blue;
    }
}
