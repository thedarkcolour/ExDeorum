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
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;

public interface RenderFace {
    void renderFlatSprite(MultiBufferSource buffers, PoseStack stack, float y, int r, int g, int b, int light, float edge);

    void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float minY, float maxY);

    boolean isMissingTexture();

    record Single(RenderType renderType, TextureAtlasSprite sprite, boolean isMissingTexture) implements RenderFace {
        public Single(RenderType renderType, TextureAtlasSprite sprite) {
            this(renderType, sprite, RenderUtil.isMissingTexture(sprite));
        }
        @Override
        public void renderFlatSprite(MultiBufferSource buffers, PoseStack stack, float y, int r, int g, int b, int light, float edge) {
            RenderUtil.renderFlatSprite(buffers.getBuffer(this.renderType), stack, y, r, g, b, this.sprite, light, edge);
        }

        @Override
        public void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float minY, float maxY) {
            RenderUtil.renderFlatSpriteLerp(buffers.getBuffer(this.renderType), stack, percentage, r, g, b, this.sprite, light, edge, minY, maxY);
        }
    }

    record Composite(List<Pair<RenderType, TextureAtlasSprite>> layers, boolean isMissingTexture) implements RenderFace {
        public Composite(List<Pair<RenderType, TextureAtlasSprite>> layers) {
            this(layers, areAnyMissing(layers));
        }

        @Override
        public void renderFlatSprite(MultiBufferSource buffers, PoseStack stack, float y, int r, int g, int b, int light, float edge) {
            for (var layer : this.layers) {
                RenderUtil.renderFlatSprite(buffers.getBuffer(layer.first()), stack, y, r, g, b, layer.second(), light, edge);
            }
        }

        @Override
        public void renderFlatSpriteLerp(MultiBufferSource buffers, PoseStack stack, float percentage, int r, int g, int b, int light, float edge, float minY, float maxY) {
            for (var layer : this.layers) {
                RenderUtil.renderFlatSpriteLerp(buffers.getBuffer(layer.first()), stack, percentage, r, g, b, layer.second(), light, edge, minY, maxY);
            }
        }

        private static boolean areAnyMissing(List<Pair<RenderType, TextureAtlasSprite>> layers) {
            for (var layer : layers) {
                if (RenderUtil.isMissingTexture(layer.second())) {
                    return true;
                }
            }

            return false;
        }
    }
}
