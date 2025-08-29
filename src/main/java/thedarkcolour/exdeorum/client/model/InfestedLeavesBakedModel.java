package thedarkcolour.exdeorum.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;

import java.util.List;

public class InfestedLeavesBakedModel implements IDynamicBakedModel {
    private final BlockModelShaper modelShaper;
    private final BlockState fallbackState;

    public InfestedLeavesBakedModel() {
        this.modelShaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
        this.fallbackState = Blocks.OAK_LEAVES.defaultBlockState();
    }

    private BlockState getMimicState(ModelData modelData) {
        var mimicState = modelData.get(InfestedLeavesBlockEntity.MIMIC_PROPERTY);
        if (mimicState == null) {
            return this.fallbackState;
        }
        return mimicState;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource, ModelData modelData, @Nullable RenderType renderType) {
        var mimicState = getMimicState(modelData);
        var model = modelShaper.getBlockModel(mimicState);

        return model.getQuads(mimicState, direction, randomSource, modelData, renderType);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        var mimicState = getMimicState(data);
        var model = modelShaper.getBlockModel(mimicState);

        return model.getRenderTypes(mimicState, rand, data);
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        var mimicState = getMimicState(data);
        var model = modelShaper.getBlockModel(mimicState);
        return model.useAmbientOcclusion(mimicState, data, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        return modelShaper.getBlockModel(getMimicState(data)).getParticleIcon(data);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return modelShaper.getParticleIcon(this.fallbackState);
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
