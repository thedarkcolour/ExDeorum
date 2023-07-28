package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.tag.EItemTags;
import thedarkcolour.exnihiloreborn.registry.EItems;

import java.util.concurrent.CompletableFuture;

class ItemTags extends ItemTagsProvider {
    public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ExNihiloReborn.ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(EItemTags.HAMMERS).add(EItems.WOODEN_HAMMER.get(), EItems.STONE_HAMMER.get(), EItems.GOLDEN_HAMMER.get(), EItems.IRON_HAMMER.get(), EItems.DIAMOND_HAMMER.get(), EItems.NETHERITE_HAMMER.get());
        tag(EItemTags.CROOKS).add(EItems.CROOK.get(), EItems.BONE_CROOK.get());
        tag(EItemTags.WOODEN_BARRELS).add(EItems.OAK_BARREL.get(), EItems.SPRUCE_BARREL.get(), EItems.BIRCH_BARREL.get(), EItems.JUNGLE_BARREL.get(), EItems.ACACIA_BARREL.get(), EItems.DARK_OAK_BARREL.get(), EItems.MANGROVE_BARREL.get(), EItems.CHERRY_BARREL.get(), EItems.BAMBOO_BARREL.get());
        tag(EItemTags.STONE_BARRELS).add(EItems.STONE_BARREL.get());
        tag(EItemTags.BARRELS).addTags(EItemTags.WOODEN_BARRELS, EItemTags.STONE_BARRELS);
    }
}
