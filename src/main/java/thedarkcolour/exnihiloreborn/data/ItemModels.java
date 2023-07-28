package thedarkcolour.exnihiloreborn.data;

import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.modkit.data.MKItemModelProvider;

class ItemModels {
    public static void addItemModels(MKItemModelProvider models) {
        models.generic2d(EItems.SILK_WORM);
        models.generic2d(EItems.COOKED_SILK_WORM);

        models.handheld(EItems.WOODEN_HAMMER);
        models.handheld(EItems.STONE_HAMMER);
        models.handheld(EItems.GOLDEN_HAMMER);
        models.handheld(EItems.IRON_HAMMER);
        models.handheld(EItems.DIAMOND_HAMMER);
        models.handheld(EItems.NETHERITE_HAMMER);

        models.handheld(EItems.CROOK);
        models.handheld(EItems.BONE_CROOK);
    }
}
