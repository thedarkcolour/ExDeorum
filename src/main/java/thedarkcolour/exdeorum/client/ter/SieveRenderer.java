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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;

import java.util.HashMap;
import java.util.Map;

// TODO: port SieveRenderer to MC 26.x rendering API (BlockEntityRenderer changed to extract/submit pattern)
public class SieveRenderer<T extends EBlockEntity & SieveLogic.Owner> implements BlockEntityRenderer<T, BlockEntityRenderState> {
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
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // TODO: implement sieve mesh/contents rendering using new 26.x rendering API
    }

    protected boolean shouldContentsRender3d(T sieve) {
        return false;
    }
}
