/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.blockentity.SieveBlockEntity;
import thedarkcolour.exdeorum.client.RenderUtil;

public class SieveRenderer implements BlockEntityRenderer<SieveBlockEntity> {
    public static final LoadingCache<Item, TextureAtlasSprite> MESH_TEXTURES = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public TextureAtlasSprite load(Item key) {
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(key);
            var textureLoc = registryName.withPrefix("item/mesh/");
            return RenderUtil.blockAtlas.getSprite(textureLoc);
        }
    });

    public SieveRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(SieveBlockEntity sieve, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        var contents = sieve.getContents();

        if (!contents.isEmpty() && contents.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var percentage = (float) sieve.getProgress() / 100.0f;
            var builder = buffers.getBuffer(RenderUtil.TOP_RENDER_TYPES.getUnchecked(block));
            var sprite = RenderUtil.TOP_TEXTURES.getUnchecked(block);

            RenderUtil.renderFlatSpriteLerp(builder, stack, percentage, 0xff, 0xff, 0xff, sprite, light, 1.0f, 13f, 15f);
        }

        var mesh = sieve.getMesh();

        if (!mesh.isEmpty()) {
            var builder = buffers.getBuffer(RenderType.cutoutMipped());
            var sprite = MESH_TEXTURES.getUnchecked(mesh.getItem());
            RenderUtil.renderFlatSprite(builder, stack, 0.75f, 0xff, 0xff, 0xff, sprite, light, 1f);

            if (mesh.hasFoil()) {
                RenderUtil.renderFlatSprite(buffers.getBuffer(RenderType.glint()), stack, 0.75f, 0xff, 0xff, 0xff, sprite, light, 1f);
            }
        }
    }
}
