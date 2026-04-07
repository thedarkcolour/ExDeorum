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

package thedarkcolour.exdeorum.client.ter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.client.RenderFace;
import thedarkcolour.exdeorum.client.RenderUtil;

import java.util.HashMap;
import java.util.Map;

public class SieveRenderer<T extends EBlockEntity & SieveLogic.Owner> implements BlockEntityRenderer<T, SieveRenderer.SieveRenderState> {
    public static final Map<Item, TextureAtlasSprite> MESH_TEXTURES = new HashMap<>();

    private final float meshHeight;
    private final float contentsMinY;
    private final float contentsMaxY;

    public SieveRenderer(float meshHeight, float contentsMaxY) {
        this.meshHeight = meshHeight;
        this.contentsMinY = meshHeight * 16f + 1f;
        this.contentsMaxY = contentsMaxY;
    }

    @Override
    public SieveRenderState createRenderState() {
        return new SieveRenderState();
    }

    @Override
    public void extractRenderState(T sieve, SieveRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(sieve, state, partialTicks, cameraPosition, breakProgress);

        var logic = sieve.getLogic();
        var contents = logic.getContents();
        state.contentsFace = null;
        state.contentsPercentage = logic.getProgress();
        state.renderContents3d = shouldContentsRender3d(sieve);

        if (!contents.isEmpty() && contents.getItem() instanceof BlockItem blockItem) {
            state.contentsFace = RenderUtil.getTopFace(blockItem.getBlock());
        }

        var mesh = logic.getMesh();
        state.meshSprite = null;
        state.meshHasFoil = false;
        if (!mesh.isEmpty()) {
            var meshItem = mesh.getItem();
            if (MESH_TEXTURES.containsKey(meshItem)) {
                state.meshSprite = MESH_TEXTURES.get(meshItem);
            } else {
                Identifier textureLoc = BuiltInRegistries.ITEM.getKey(meshItem).withPrefix("item/mesh/");
                var sprite = RenderUtil.getBlockSprite(textureLoc);
                MESH_TEXTURES.put(meshItem, sprite);
                state.meshSprite = sprite;
            }
            state.meshHasFoil = mesh.hasFoil();
        }
    }

    @Override
    public void submit(SieveRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        if (state.contentsFace != null) {
            if (state.contentsFace instanceof RenderFace.Single single) {
                submitContentsLayer(collector, stack, state, single.renderType(), single.sprite());
            } else if (state.contentsFace instanceof RenderFace.Composite composite) {
                for (var layer : composite.layers()) {
                    submitContentsLayer(collector, stack, state, layer.renderType(), layer.sprite());
                }
            }
        }

        if (state.meshSprite != null) {
            collector.submitCustomGeometry(stack, Sheets.cutoutBlockSheet(), (pose, buffer) ->
                RenderUtil.renderFlatSprite(buffer, pose, this.meshHeight, 0xff, 0xff, 0xff, state.meshSprite, state.lightCoords, 1f)
            );
            if (state.meshHasFoil) {
                collector.submitCustomGeometry(stack, RenderTypes.glint(), (pose, buffer) ->
                    RenderUtil.renderFlatSprite(buffer, pose, this.meshHeight, 0xff, 0xff, 0xff, state.meshSprite, state.lightCoords, 1f)
                );
            }
        }
    }

    private void submitContentsLayer(SubmitNodeCollector collector, PoseStack stack, SieveRenderState state, net.minecraft.client.renderer.rendertype.RenderType renderType, TextureAtlasSprite sprite) {
        collector.submitCustomGeometry(stack, renderType, (pose, buffer) -> {
            if (state.renderContents3d) {
                RenderUtil.renderCuboid(buffer, pose, this.contentsMinY / 16f, Mth.lerp(state.contentsPercentage, this.contentsMaxY, this.contentsMinY) / 16f, 0xff, 0xff, 0xff, sprite, state.lightCoords, 1.0f);
            } else {
                RenderUtil.renderFlatSpriteLerp(buffer, pose, state.contentsPercentage, 0xff, 0xff, 0xff, sprite, state.lightCoords, 1.0f, this.contentsMaxY, this.contentsMinY);
            }
        });
    }

    protected boolean shouldContentsRender3d(T sieve) {
        return false;
    }

    public static class SieveRenderState extends BlockEntityRenderState {
        public RenderFace contentsFace;
        public float contentsPercentage;
        public boolean renderContents3d;
        public TextureAtlasSprite meshSprite;
        public boolean meshHasFoil;
    }
}
