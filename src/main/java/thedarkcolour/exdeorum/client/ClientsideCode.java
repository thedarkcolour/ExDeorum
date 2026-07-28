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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3i;
import thedarkcolour.exdeorum.ExDeorum;

public class ClientsideCode {
    // Development-only: recomputes COLORS by averaging each item's icon instead of reading the
    // text files, so that vanilla_compost_colors.txt can be regenerated after a texture change.
    // Only reachable from the ".compost_colors" chat command, which is client + ExDeorum.DEBUG only.
    public static void debugCompute() {
        var minecraft = Minecraft.getInstance();
        var resolver = minecraft.getItemModelResolver();
        var renderState = new ItemStackRenderState();
        var tintCollector = new ItemTintCollector();
        var poseStack = new PoseStack();
        // Fixed seed: pickParticleMaterial chooses a random layer for multi-layer models, and a
        // regenerated file should not churn between runs.
        var random = RandomSource.create(0L);

        for (var item : BuiltInRegistries.ITEM) {
            resolver.updateForTopItem(renderState, new ItemStack(item), ItemDisplayContext.GUI, null, null, 0);

            var material = renderState.pickParticleMaterial(random);
            if (material == null) {
                continue;
            }

            var sprite = material.sprite();
            if (RenderUtil.isMissingTexture(sprite)) {
                continue;
            }

            // Ask the item to submit itself so we can see the tints it would actually render with.
            // Items drawn by a special renderer (beds, chests, heads) submit no quads and stay
            // untinted, which is the same as what they got before.
            tintCollector.clear();
            try {
                renderState.submit(poseStack, tintCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
            } catch (Exception exception) {
                ExDeorum.LOGGER.debug("Could not collect tints for {}", BuiltInRegistries.ITEM.getKey(item), exception);
            }

            // Sprite-local coordinates over the first frame; SpriteContents reports the frame size
            // and getPixelRGBA applies the frame offset for animated textures.
            int width = sprite.contents().width();
            int height = sprite.contents().height();
            int pixels = 0;
            int totalR = 0;
            int totalG = 0;
            int totalB = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = sprite.getPixelRGBA(0, x, y);

                    if (ARGB.alpha(pixel) != 0) {
                        totalR += ARGB.red(pixel);
                        totalG += ARGB.green(pixel);
                        totalB += ARGB.blue(pixel);
                        pixels++;
                    }
                }
            }

            putColor(pixels, totalR, totalG, totalB, tintCollector.tintFor(sprite), item);
        }
    }

    // Greyscale textures like leaves and grass only look right once their tint is applied, so the
    // average is multiplied by whatever colour the item renders that sprite with.
    private static void putColor(int pixels, int totalR, int totalG, int totalB, int tint, Item item) {
        if (pixels > 0 && (totalR | totalG | totalB) != 0) {
            CompostColors.COLORS.put(item, new Vector3i(
                    tintedAverage(totalR, pixels, ARGB.red(tint)),
                    tintedAverage(totalG, pixels, ARGB.green(tint)),
                    tintedAverage(totalB, pixels, ARGB.blue(tint))
            ));
        }
    }

    private static int tintedAverage(int total, int pixels, int tintChannel) {
        return Math.round((float) total / pixels * (tintChannel / 255f));
    }
}
