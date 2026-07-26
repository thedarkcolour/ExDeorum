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

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

// Describes the top face of a block, which renderers destructure to submit one draw per layer.
public sealed interface RenderFace {
    boolean isMissingTexture();

    record Single(RenderType renderType, TextureAtlasSprite sprite, boolean isMissingTexture) implements RenderFace {
        public Single(RenderType renderType, TextureAtlasSprite sprite) {
            this(renderType, sprite, RenderUtil.isMissingTexture(sprite));
        }
    }

    record Composite(CompositeLayer[] layers, boolean isMissingTexture) implements RenderFace {
        public Composite(CompositeLayer[] layers) {
            this(layers, areAnyMissing(layers));
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
