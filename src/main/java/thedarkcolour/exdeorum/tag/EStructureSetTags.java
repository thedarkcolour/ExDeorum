package thedarkcolour.exdeorum.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import thedarkcolour.exdeorum.ExDeorum;

public class EStructureSetTags {
    public static final TagKey<StructureSet> OVERWORLD_VOID_STRUCTURES = tag("overworld_void_structure_sets");
    public static final TagKey<StructureSet> THE_NETHER_VOID_STRUCTURES = tag("the_nether_void_structure_sets");
    public static final TagKey<StructureSet> THE_END_VOID_STRUCTURES = tag("the_end_void_structure_sets");

    public static TagKey<StructureSet> tag(String name) {
        return TagKey.create(Registries.STRUCTURE_SET, new ResourceLocation(ExDeorum.ID, name));
    }
}
