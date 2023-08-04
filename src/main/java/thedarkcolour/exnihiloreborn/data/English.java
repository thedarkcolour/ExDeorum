package thedarkcolour.exnihiloreborn.data;

import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.modkit.data.MKEnglishProvider;

class English {
    static void addTranslations(MKEnglishProvider english) {
        english.add("fluid_type." + ExNihiloReborn.ID + ".witch_water", "Witch Water");

        english.add(TranslationKeys.MAIN_CREATIVE_TAB, "Ex Nihilo Reborn");
        english.add(TranslationKeys.VOID_WORLD_TYPE, "Void World");

        english.add(TranslationKeys.ROOT_ADVANCEMENT_TITLE, "Don't Look Down...");
        english.add(TranslationKeys.ROOT_ADVANCEMENT_DESCRIPTION, "Spawn into a SkyBlock void world");
        english.add(TranslationKeys.CROOK_ADVANCEMENT_TITLE, "Give Him The Hook");
        english.add(TranslationKeys.CROOK_ADVANCEMENT_DESCRIPTION, "Craft a Crook to double sapling drops from leaves");
        english.add(TranslationKeys.BARREL_ADVANCEMENT_TITLE, "That Goes in the GreenWaste");
        english.add(TranslationKeys.BARREL_ADVANCEMENT_DESCRIPTION, "Use a barrel to compost organic material into dirt");
        english.add(TranslationKeys.SILK_WORM_ADVANCEMENT_TITLE, "Straight Outta China");
        english.add(TranslationKeys.SILK_WORM_ADVANCEMENT_DESCRIPTION, "Obtain a silk worm, then infest a tree with it to get string");
        english.add(TranslationKeys.STRING_MESH_ADVANCEMENT_TITLE, "All the Little Cogs");
        english.add(TranslationKeys.STRING_MESH_ADVANCEMENT_DESCRIPTION, "Craft a string mesh to use in a sieve");

        english.add(TranslationKeys.SILK_WORM_JEI_INFO, "Silk worms have a 1 in 100 chance to drop from leaves harvested with a Crook. Using a silk worm on a tree's leaves will infest them, gradually spreading through the entire tree. 100% infested leaves can be harvested for string, but do not drop saplings.");
        english.add(TranslationKeys.SIEVE_JEI_INFO, "Sieves are used to filter out certain items from soft blocks like gravel and dirt. A mesh is required to use the sieve, and it can be enchanted with Fortune and Efficiency.");
        english.add(TranslationKeys.BARREL_COMPOST_CATEGORY_TITLE, "Barrel Compost");
        english.add(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, "Compost: %s");
        english.add(TranslationKeys.WATER_CRUCIBLE_CATEGORY_TITLE, "Water Crucible");
        english.add(TranslationKeys.LAVA_CRUCIBLE_CATEGORY_TITLE, "Lava Crucible");
        english.add(TranslationKeys.HAMMER_CATEGORY_TITLE, "Hammer");
        english.add(TranslationKeys.SIEVE_CATEGORY_TITLE, "Sieve");
        english.add(TranslationKeys.SIEVE_RECIPE_CHANCE, "Chance: %s%%");
        english.add(TranslationKeys.SIEVE_RECIPE_AVERAGE_OUTPUT, "Avg. Output: %s");
        english.add(TranslationKeys.SIEVE_RECIPE_MIN_OUTPUT, "Min: %s");
        english.add(TranslationKeys.SIEVE_RECIPE_MAX_OUTPUT, "Max: %s");
    }
}
