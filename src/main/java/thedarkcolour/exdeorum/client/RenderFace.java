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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface RenderFace {
    void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float yStart, float yEnd);

    boolean isMissingTexture();

    void renderCuboid(MultiBufferSource buffers, PoseStack stack, float minY, float maxY, int r, int g, int b, int light, float edge);

    record Single(RenderType renderType, TextureAtlasSprite sprite, boolean isMissingTexture) implements RenderFace {
        public Single(RenderType renderType, TextureAtlasSprite sprite) {
            this(renderType, sprite, RenderUtil.isMissingTexture(sprite));
        }

        @Override
        public void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float yStart, float yEnd) {
            RenderUtil.renderFlatSpriteLerp(buffers.getBuffer(this.renderType), stack, percentage, r, g, b, this.sprite, light, edge, yStart, yEnd);
        }

        @Override
        public void renderCuboid(MultiBufferSource buffers, PoseStack stack, float minY, float maxY, int r, int g, int b, int light, float edge) {
            RenderUtil.renderCuboid(buffers.getBuffer(this.renderType), stack, minY, maxY, r, g, b, this.sprite, light, edge);
        }
    }

    record Composite(CompositeLayer[] layers, boolean isMissingTexture) implements RenderFace {
        public Composite(CompositeLayer[] layers) {
            this(layers, areAnyMissing(layers));
        }

        @Override
        public void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float yStart, float yEnd) {
            for (var layer : this.layers) {
                RenderUtil.renderFlatSpriteLerp(buffers.getBuffer(layer.renderType), stack, percentage, r, g, b, layer.sprite, light, edge, yStart, yEnd);
            }
        }

        @Override
        public void renderCuboid(MultiBufferSource buffers, PoseStack stack, float minY, float maxY, int r, int g, int b, int light, float edge) {
            for (var layer : this.layers) {
                RenderUtil.renderCuboid(buffers.getBuffer(layer.renderType), stack, minY, maxY, r, g, b, layer.sprite, light, edge);
            }
        }

        private static boolean areAnyMissing(CompositeLayer[] layers) {
            for (var layer : layers) {
                if (RenderUtil.isMissingTexture(layer.sprite)) {
                    return true;
                }
            }

            return false;
        }
    }

    record CompositeLayer(RenderType renderType, TextureAtlasSprite sprite) {}
}
