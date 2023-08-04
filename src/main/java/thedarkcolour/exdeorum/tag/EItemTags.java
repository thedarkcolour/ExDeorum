package thedarkcolour.exdeorum.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.ExDeorum;

public class EItemTags {
    public static final TagKey<Item> CROOKS = tag("crooks");
    public static final TagKey<Item> HAMMERS = tag("hammers");
    public static final TagKey<Item> SIEVE_MESHES = tag("sieve_meshes");
    public static final TagKey<Item> PEBBLES = tag("pebbles");

    public static final TagKey<Item> WOODEN_BARRELS = tag("wooden_barrels");
    public static final TagKey<Item> STONE_BARRELS = tag("stone_barrels");
    public static final TagKey<Item> BARRELS = tag("barrels");

    public static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation(ExDeorum.ID, name));
    }
}
