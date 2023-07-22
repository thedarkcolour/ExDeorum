package thedarkcolour.exnihiloreborn.item;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

public class EItemTags {
    public static final ITag.INamedTag<Item> CROOKS = tag("crooks");
    public static final ITag.INamedTag<Item> HAMMERS = tag("hammers");
    public static final ITag.INamedTag<Item> COMPRESSED_HAMMERS = tag("compressed_hammers");

    public static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.bind(ExNihiloReborn.ID + ":" + name);
    }
}
