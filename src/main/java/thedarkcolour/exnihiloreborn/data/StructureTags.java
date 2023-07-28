package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.tag.EStructureSetTags;

import java.util.concurrent.CompletableFuture;

class StructureTags extends TagsProvider<StructureSet> {
    public StructureTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, Registries.STRUCTURE_SET, lookup, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(EStructureSetTags.OVERWORLD_VOID_STRUCTURES);
        tag(EStructureSetTags.THE_NETHER_VOID_STRUCTURES).add(BuiltinStructureSets.NETHER_COMPLEXES);
        tag(EStructureSetTags.THE_END_VOID_STRUCTURES);
    }
}
