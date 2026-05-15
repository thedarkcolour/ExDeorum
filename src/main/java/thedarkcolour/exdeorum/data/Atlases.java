package thedarkcolour.exdeorum.data;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import thedarkcolour.exdeorum.ExDeorum;

import java.util.concurrent.CompletableFuture;

class Atlases extends SpriteSourceProvider {
    public Atlases(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ExDeorum.ID);
    }

    @Override
    protected void gather() {
        SourceList gui = atlas(AtlasIds.GUI);
        gui.addSource(new DirectoryLister("gui/sprites", ""));

        SourceList blocks = atlas(AtlasIds.BLOCKS);
        blocks.addSource(new DirectoryLister("item/mesh", ""));
    }
}
