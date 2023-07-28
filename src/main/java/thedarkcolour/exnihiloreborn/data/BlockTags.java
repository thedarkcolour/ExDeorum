package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

class BlockTags extends BlockTagsProvider {
    public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, lookup, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
    }
}
