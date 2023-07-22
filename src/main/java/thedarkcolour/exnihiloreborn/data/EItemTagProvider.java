package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.item.EItemTags;
import thedarkcolour.exnihiloreborn.registry.EItems;

import javax.annotation.Nullable;

public class EItemTagProvider extends ItemTagsProvider {
    public EItemTagProvider(DataGenerator gen, BlockTagsProvider blocKTags, @Nullable ExistingFileHelper helper) {
        super(gen, blocKTags, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void addTags() {
        tag(EItemTags.HAMMERS).add(EItems.WOODEN_HAMMER.get(), EItems.STONE_HAMMER.get(), EItems.GOLDEN_HAMMER.get(), EItems.IRON_HAMMER.get(), EItems.DIAMOND_HAMMER.get(), EItems.NETHERITE_HAMMER.get());
        tag(EItemTags.CROOKS).add(EItems.CROOK.get(), EItems.COMPRESSED_CROOK.get(), EItems.BONE_CROOK.get());
        tag(EItemTags.COMPRESSED_HAMMERS).add(EItems.COMPRESSED_WOODEN_HAMMER.get(), EItems.COMPRESSED_STONE_HAMMER.get(), EItems.COMPRESSED_GOLDEN_HAMMER.get(), EItems.COMPRESSED_IRON_HAMMER.get(), EItems.COMPRESSED_DIAMOND_HAMMER.get(), EItems.COMPRESSED_NETHERITE_HAMMER.get());
    }
}
