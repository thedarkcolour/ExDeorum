package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

import java.util.concurrent.CompletableFuture;

public class WorldPresetTags extends WorldPresetTagsProvider {
    public WorldPresetTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, lookup, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(net.minecraft.tags.WorldPresetTags.NORMAL).add(ResourceKey.create(Registries.WORLD_PRESET, new ResourceLocation(ExNihiloReborn.ID, "void_world")));
    }
}
