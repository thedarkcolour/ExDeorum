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
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Collects the quads and resolved tint colours an item would render with, by standing in for the
// real collector while ItemStackRenderState submits itself. Tints are otherwise unreachable:
// ItemStackRenderState keeps its layers private, and submitItem is the only place they surface.
//
// Everything except submitItem is a no-op; nothing else is reached when submitting an item.
class ItemTintCollector implements SubmitNodeCollector {
    private final List<Layer> layers = new ArrayList<>();

    public void clear() {
        this.layers.clear();
    }

    // The colour the given sprite renders with, or -1 (white) when it is untinted.
    public int tintFor(TextureAtlasSprite sprite) {
        // Prefer the tint of a quad that actually draws this sprite. Answering from the quad is
        // what keeps untinted icons untinted: seeds and redstone dust carry a tint index from
        // item/generated, but no matching entry, which resolves to white just as it does in
        // ItemFeatureRenderer#getLayerColorSafe.
        for (var layer : this.layers) {
            for (var quad : layer.quads) {
                var material = quad.materialInfo();

                if (material.sprite() == sprite && material.isTinted()) {
                    return resolve(layer, material.tintIndex());
                }
            }
        }

        // The sampled sprite can be the model's particle texture rather than one of its faces --
        // a grass block's particle is dirt, whose face is untinted, so the block's green would be
        // lost. Fall back to the model's own tint in that case.
        for (var layer : this.layers) {
            for (var quad : layer.quads) {
                var material = quad.materialInfo();

                if (material.isTinted()) {
                    int tint = resolve(layer, material.tintIndex());

                    if (tint != -1) {
                        return tint;
                    }
                }
            }
        }

        return -1;
    }

    // Mirrors ItemFeatureRenderer#getLayerColorSafe: a tint index with no matching entry is white.
    private static int resolve(Layer layer, int tintIndex) {
        return tintIndex < layer.tints.length ? layer.tints[tintIndex] : -1;
    }

    @Override
    public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
        this.layers.add(new Layer(tintLayers, List.copyOf(quads)));
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        return this;
    }

    @Override
    public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {}

    @Override
    public void submitNameTag(PoseStack poseStack, @Nullable Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera) {}

    @Override
    public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {}

    @Override
    public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {}

    @Override
    public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {}

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {}

    @Override
    public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState, int outlineColor) {}

    @Override
    public void submitBlockModel(PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> parts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {}

    @Override
    public void submitBreakingBlockModel(PoseStack poseStack, List<BlockStateModelPart> parts, int progress) {}

    @Override
    public void submitShapeOutline(PoseStack poseStack, VoxelShape shape, RenderType renderType, int color, float width, boolean afterTerrain) {}

    @Override
    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {}

    @Override
    public void submitQuadParticleGroup(QuadParticleRenderState particles) {}

    @Override
    public void submitGizmoPrimitives(DrawableGizmoPrimitives.Group group, CameraRenderState camera, boolean onTop) {}

    private record Layer(int[] tints, List<BakedQuad> quads) {}
}
