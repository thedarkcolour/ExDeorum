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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.client.RenderUtil;

import java.util.HashMap;
import java.util.Map;

public class SieveRenderer<T extends EBlockEntity & SieveLogic.Owner> implements BlockEntityRenderer<T> {
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
    public void render(T sieve, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        var logic = sieve.getLogic();
        var contents = logic.getContents();

        if (!contents.isEmpty() && contents.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var percentage = logic.getProgress();
            var face = RenderUtil.getTopFace(block);

            if (shouldContentsRender3d(sieve)) {
                face.renderCuboid(buffers, stack, this.contentsMinY / 16f, Mth.lerp(percentage, this.contentsMaxY, this.contentsMinY) / 16f, 0xff, 0xff, 0xff, light, 1.0f);
            } else {
                face.renderFlatSpriteLerp(buffers, stack, percentage, 0xff, 0xff, 0xff, light, 1.0f, this.contentsMaxY, this.contentsMinY);
            }
        }

        var mesh = logic.getMesh();

        if (!mesh.isEmpty()) {
            var builder = buffers.getBuffer(RenderType.cutoutMipped());
            var meshItem = mesh.getItem();

            TextureAtlasSprite meshSprite;
            if (MESH_TEXTURES.containsKey(meshItem)) {
                meshSprite = MESH_TEXTURES.get(meshItem);
            } else {
                @SuppressWarnings("deprecation")
                ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(meshItem);
                ResourceLocation textureLoc = registryName.withPrefix("item/mesh/");
                meshSprite = RenderUtil.blockAtlas.getSprite(textureLoc);
                MESH_TEXTURES.put(meshItem, meshSprite);
            }

            RenderUtil.renderFlatSprite(builder, stack, this.meshHeight, 0xff, 0xff, 0xff, meshSprite, light, 1f);

            if (mesh.hasFoil()) {
                RenderUtil.renderFlatSprite(buffers.getBuffer(RenderType.glint()), stack, this.meshHeight, 0xff, 0xff, 0xff, meshSprite, light, 1f);
            }
        }
    }

    // todo return true for transparent sieves
    protected boolean shouldContentsRender3d(T sieve) {
        return false;
    }
}
