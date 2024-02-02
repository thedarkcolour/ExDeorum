/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
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
import thedarkcolour.exdeorum.client.screen.RedstoneControlWidget;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.modkit.data.MKEnglishProvider;

class English {
    static void addTranslations(MKEnglishProvider english) {
        english.add("fluid_type." + ExDeorum.ID + ".witch_water", "Witch Water");

        english.add(TranslationKeys.MAIN_CREATIVE_TAB, "Ex Deorum");
        english.add(TranslationKeys.VOID_WORLD_TYPE, "Void World");
        english.add(TranslationKeys.FRACTION_DISPLAY, ": %s / %s");
        english.add(TranslationKeys.MECHANICAL_SIEVE_MESH_LABEL, "Mesh: ");
        english.add(TranslationKeys.MECHANICAL_HAMMER_HAMMER_LABEL, "Hammer: ");
        english.add(TranslationKeys.ENERGY, "Energy");

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
        english.add(TranslationKeys.SIEVE_MESH_JEI_INFO, "Meshes are used in sieves. Different meshes yield different drops. Meshes can be enchanted with Fortune and Efficiency to increase likelihood of drops and sifting speed, respectively.");
        english.add(TranslationKeys.WATERING_CAN_JEI_INFO, "Watering cans speed up crop growth, tree growth, and grass spreading, among other things. They can be filled with water from barrels and wooden crucibles. Golden and above watering cans do not need to be refilled once full. Diamond watering cans water in a 3x3 area, and Netherite watering cans are usable by machinery.");
        english.add(TranslationKeys.WITCH_WATER_JEI_INFO, "Witch water is obtained by putting water in a barrel on top of mycelium. More mycelium speeds up the process. A barrel with witch water will grow mushrooms on nearby mycelium. Witch water and lava can make a netherrack generator, similar to a cobblestone generator.");
        english.add(TranslationKeys.MYCELIUM_SPORES_JEI_INFO, "Use on dirt to turn it into mycelium.");
        english.add(TranslationKeys.GRASS_SEEDS_JEI_INFO, "Use on dirt to turn it into a grass block.");
        english.add(TranslationKeys.WARPED_NYLIUM_SPORES_JEI_INFO, "Use on netherrack to turn it into a warped nylium block.");
        english.add(TranslationKeys.CRIMSON_NYLIUM_SPORES_JEI_INFO, "Use on netherrack to turn it into a crimson nylium block.");
        english.add(TranslationKeys.SCULK_CORE_JEI_INFO, "Use a sculk core on a Sculk Shrieker to enable it to spawn Wardens. Normally, Sculk Shriekers placed by players cannot spawn Wardens, so this item is useful for obtaining Sculk items in a SkyBlock world.");
        english.add(TranslationKeys.MECHANICAL_SIEVE_JEI_INFO, "The Mechanical Sieve is a machine that, when supplied with a mesh and Forge Energy (FE), will sift blocks without a player having to do it themselves. It also supports three different modes of redstone control. Since Ex Deorum does not provide a way to generate FE, you will need another mod to provide power.");
        english.add(TranslationKeys.MECHANICAL_HAMMER_JEI_INFO, "The Mechanical Hammer is a machine that, when supplied with Forge Energy (FE), will hammer blocks without a player having to do it themselves. It can operate without a hammer, but adding any hammer will double the speed, and efficiency enchantments on the hammer will further increase speed. It also supports three different modes of redstone control. Since Ex Deorum does not provide a way to generate FE, you will need another mod to provide power.");

        english.add(TranslationKeys.BARREL_COMPOST_CATEGORY_TITLE, "Barrel Compost");
        english.add(TranslationKeys.BARREL_COMPOST_RECIPE_VOLUME, "Compost: %s");
        english.add(TranslationKeys.BARREL_MIXING_CATEGORY_TITLE, "Barrel Mixing");
        english.add(TranslationKeys.BARREL_FLUID_MIXING_CATEGORY_TITLE, "Barrel Fluid Mixing");
        english.add(TranslationKeys.WATER_CRUCIBLE_CATEGORY_TITLE, "Water Crucible");
        english.add(TranslationKeys.LAVA_CRUCIBLE_CATEGORY_TITLE, "Lava Crucible");
        english.add(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_TITLE, "Crucible Heat Sources");
        english.add(TranslationKeys.CRUCIBLE_HEAT_SOURCE_CATEGORY_MULTIPLIER, "Melt Rate: %sx");
        english.add(TranslationKeys.HAMMER_CATEGORY_TITLE, "Hammer");
        english.add(TranslationKeys.SIEVE_CATEGORY_TITLE, "Sieve");
        english.add(TranslationKeys.SIEVE_RECIPE_CHANCE, "Chance: %s%%");
        english.add(TranslationKeys.SIEVE_RECIPE_AVERAGE_OUTPUT, "Avg. Output: %s");
        english.add(TranslationKeys.SIEVE_RECIPE_MIN_OUTPUT, "Min: %s");
        english.add(TranslationKeys.SIEVE_RECIPE_MAX_OUTPUT, "Max: %s");
        english.add(TranslationKeys.SIEVE_RECIPE_BY_HAND_ONLY, "Does not drop from Mechanical Sieve");

        english.add(TranslationKeys.MECHANICAL_SIEVE_SCREEN_TITLE, "Mechanical Sieve");
        english.add(TranslationKeys.REDSTONE_CONTROL_MODES[RedstoneControlWidget.REDSTONE_MODE_IGNORED], "Always");
        english.add(TranslationKeys.REDSTONE_CONTROL_MODES[RedstoneControlWidget.REDSTONE_MODE_UNPOWERED], "Unpowered");
        english.add(TranslationKeys.REDSTONE_CONTROL_MODES[RedstoneControlWidget.REDSTONE_MODE_POWERED], "Powered");
        english.add(TranslationKeys.REDSTONE_CONTROL_LABEL, "Redstone Mode");
        english.add(TranslationKeys.REDSTONE_CONTROL_MODE, "Mode: ");
        english.add(TranslationKeys.MECHANICAL_HAMMER_SCREEN_TITLE, "Mechanical Hammer");

        english.addBlock(EBlocks.VEXING_ARCHWOOD_CRUCIBLE, "Vexing Archwood Crucible");
        english.addBlock(EBlocks.CASCADING_ARCHWOOD_CRUCIBLE, "Cascading Archwood Crucible");
        english.addBlock(EBlocks.BLAZING_ARCHWOOD_CRUCIBLE, "Blazing Archwood Crucible");
        english.addBlock(EBlocks.FLOURISHING_ARCHWOOD_CRUCIBLE, "Flourishing Archwood Crucible");
    }
}
