/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.data;

import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.modkit.data.MKEnglishProvider;

class English {
    static void addTranslations(MKEnglishProvider english) {
        english.add("fluid_type." + ExDeorum.ID + ".witch_water", "Witch Water");

        english.add(TranslationKeys.MAIN_CREATIVE_TAB, "Ex Deorum");
        english.add(TranslationKeys.VOID_WORLD_TYPE, "Void World");
        english.add(TranslationKeys.WATERING_CAN_FLUID_DISPLAY, ": %s / %s");

        english.add(TranslationKeys.ROOT_ADVANCEMENT_TITLE, "Don't Look Down...");
        english.add(TranslationKeys.ROOT_ADVANCEMENT_DESCRIPTION, "Spawn into a SkyBlock void world");
        english.add(TranslationKeys.CROOK_ADVANCEMENT_TITLE, "Give Him The Hook");
        english.add(TranslationKeys.CROOK_ADVANCEMENT_DESCRIPTION, "Craft a Crook to double sapling drops from leaves");
        english.add(TranslationKeys.BARREL_ADVANCEMENT_TITLE, "That Goes in the GreenWaste");
        english.add(TranslationKeys.BARREL_ADVANCEMENT_DESCRIPTION, "Use a barrel to compost organic material into dirt");
        english.add(TranslationKeys.SILK_WORM_ADVANCEMENT_TITLE, "This Looks Edible");
        english.add(TranslationKeys.SILK_WORM_ADVANCEMENT_DESCRIPTION, "Obtain a silk worm, then infest a tree with it to get string");
        english.add(TranslationKeys.STRING_MESH_ADVANCEMENT_TITLE, "All the Little Cogs");
        english.add(TranslationKeys.STRING_MESH_ADVANCEMENT_DESCRIPTION, "Craft a string mesh to use in a sieve");

        english.add(TranslationKeys.SILK_WORM_JEI_INFO, "Silk worms have a 1 in 100 chance to drop from leaves harvested with a Crook. Using a silk worm on a tree's leaves will infest them, gradually spreading through the entire tree. 100% infested leaves can be harvested for string, but do not drop saplings.");
        english.add(TranslationKeys.SIEVE_JEI_INFO, "Sieves are used to sift for items from soft blocks like gravel and dirt. A mesh is required to use the sieve. Meshes can be enchanted with Fortune and Efficiency. Sieves in a 3x3 area can be used simultaneously.");
        english.add(TranslationKeys.WATERING_CAN_JEI_INFO, "Watering cans speed up crop growth, tree growth, and grass spreading, among other things. They can be filled with water from barrels and wooden crucibles. Golden and above watering cans do not need to be refilled once full. Diamond watering cans water in a 3x3 area, and Netherite watering cans are usable by machinery.");
        english.add(TranslationKeys.WITCH_WATER_JEI_INFO, "Witch water is obtained by putting water in a barrel on top of mycelium. More mycelium speeds up the process. A barrel with witch water will grow mushrooms on nearby mycelium. Witch water and lava can make a netherrack generator, similar to a cobblestone generator.");
        english.add(TranslationKeys.MYCELIUM_SPORES_JEI_INFO, "Use on dirt to turn it into mycelium.");
        english.add(TranslationKeys.GRASS_SEEDS_JEI_INFO, "Use on dirt to turn it into a grass block.");
        english.add(TranslationKeys.WARPED_NYLIUM_SPORES_JEI_INFO, "Use on netherrack to turn it into a warped nylium block.");
        english.add(TranslationKeys.CRIMSON_NYLIUM_SPORES_JEI_INFO, "Use on netherrack to turn it into a crimson nylium block.");
        english.add(TranslationKeys.SCULK_CORE_JEI_INFO, "Use a sculk core on a Sculk Shrieker to enable it to spawn Wardens. Normally, Sculk Shriekers placed by players cannot spawn Wardens, so this item is useful for obtaining Sculk items in a SkyBlock world.");
        english.add(TranslationKeys.BARREL_COMPOST_CATEGORY_TITLE, "Barrel Compost");
        english.add(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, "Compost: %s");
        english.add(TranslationKeys.BARREL_MIXING_CATEGORY_TITLE, "Barrel Mixing");
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
