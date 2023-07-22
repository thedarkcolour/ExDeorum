package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EItems;

public class ELangProvider extends LanguageProvider {
    public ELangProvider(DataGenerator gen) {
        super(gen, ExNihiloReborn.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(EItems.SILK_WORM, "Silk Worm");
        addItem(EItems.COOKED_SILK_WORM, "Cooked Silk Worm");

        addItem(EItems.CROOK, "Crook");
        addItem(EItems.COMPRESSED_CROOK, "Compressed Crook");
        addItem(EItems.BONE_CROOK, "Bone Crook");

        addItem(EItems.WOODEN_HAMMER, "Wooden Hammer");
        addItem(EItems.STONE_HAMMER, "Stone Hammer");
        addItem(EItems.GOLDEN_HAMMER, "Golden Hammer");
        addItem(EItems.IRON_HAMMER, "Iron Hammer");
        addItem(EItems.DIAMOND_HAMMER, "Diamond Hammer");
        addItem(EItems.NETHERITE_HAMMER, "Netherite Hammer");

        addItem(EItems.COMPRESSED_WOODEN_HAMMER, "Compressed Wooden Hammer");
        addItem(EItems.COMPRESSED_STONE_HAMMER, "Compressed Stone Hammer");
        addItem(EItems.COMPRESSED_GOLDEN_HAMMER, "Compressed Golden Hammer");
        addItem(EItems.COMPRESSED_IRON_HAMMER, "Compressed Iron Hammer");
        addItem(EItems.COMPRESSED_DIAMOND_HAMMER, "Compressed Diamond Hammer");
        addItem(EItems.COMPRESSED_NETHERITE_HAMMER, "Compressed Netherite Hammer");

        addItem(EItems.IRON_ORE_PIECES, "Iron Ore Pieces");
        addItem(EItems.COPPER_ORE_PIECES, "Copper Ore Pieces");
        addItem(EItems.GOLD_ORE_PIECES, "Gold Ore Pieces");

        addItem(EItems.STONE_PEBBLE, "Stone Pebble");
        addItem(EItems.DEEPSLATE_PEBBLE, "Deepslate Pebble");

        addBlock(EBlocks.DUST, "Dust");
        addBlock(EBlocks.CRUSHED_NETHERRACK, "Crushed Netherrack");
        addBlock(EBlocks.CRUSHED_END_STONE, "Crushed End Stone");

        addBlock(EBlocks.COMPRESSED_COBBLESTONE, "Compressed Cobblestone");
        addBlock(EBlocks.COMPRESSED_DIRT, "Compressed Dirt");
        addBlock(EBlocks.COMPRESSED_SAND, "Compressed Sand");
        addBlock(EBlocks.COMPRESSED_DUST, "Compressed Dust");
        addBlock(EBlocks.COMPRESSED_CRUSHED_NETHERRACK, "Compressed Crushed Netherrack");
        addBlock(EBlocks.COMPRESSED_CRUSHED_END_STONE, "Compressed Crushed End Stone");

        addBlock(EBlocks.OAK_BARREL, "Oak Barrel");
        addBlock(EBlocks.SPRUCE_BARREL, "Spruce Barrel");
        addBlock(EBlocks.BIRCH_BARREL, "Birch Barrel");
        addBlock(EBlocks.JUNGLE_BARREL, "Jungle Barrel");
        addBlock(EBlocks.ACACIA_BARREL, "Acacia Barrel");
        addBlock(EBlocks.DARK_OAK_BARREL, "Dark_oak Barrel");
        addBlock(EBlocks.CRIMSON_BARREL, "Crimson Barrel");
        addBlock(EBlocks.WARPED_BARREL, "Warped Barrel");
        addBlock(EBlocks.STONE_BARREL, "Stone Barrel");

        addBlock(EBlocks.PORCELAIN_CRUCIBLE, "Porcelain Crucible");
        addBlock(EBlocks.WARPED_CRUCIBLE, "Warped Crucible");
        addBlock(EBlocks.CRIMSON_CRUCIBLE, "Crimson Crucible");
        addBlock(EBlocks.UNFIRED_CRUCIBLE, "Unfired Crucible");

        addBlock(EBlocks.OAK_CRUCIBLE, "Oak Crucible");
        addBlock(EBlocks.SPRUCE_CRUCIBLE, "Spruce Crucible");
        addBlock(EBlocks.BIRCH_CRUCIBLE, "Birch Crucible");
        addBlock(EBlocks.JUNGLE_CRUCIBLE, "Jungle Crucible");
        addBlock(EBlocks.ACACIA_CRUCIBLE, "Acacia Crucible");
        addBlock(EBlocks.DARK_OAK_CRUCIBLE, "Dark Oak Crucible");
    }
}
