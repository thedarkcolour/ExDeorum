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

package thedarkcolour.exdeorum.compat;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.tag.EItemTags;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class PreferredOres {
    private static final Map<TagKey<Item>, Item> PREFERRED_ORE_ITEMS = new Object2ObjectOpenHashMap<>(11, Hash.DEFAULT_LOAD_FACTOR);

    static {
        putPreferredOre(EItemTags.ORES_ALUMINUM, EConfig.COMMON.preferredAluminumOre, getDefaultAluminumOre());
        putPreferredOre(EItemTags.ORES_COBALT, EConfig.COMMON.preferredCobaltOre, getDefaultCobaltOre());
        putPreferredOre(EItemTags.ORES_SILVER, EConfig.COMMON.preferredSilverOre, getDefaultSilverOre());
        putPreferredOre(EItemTags.ORES_LEAD, EConfig.COMMON.preferredLeadOre, getDefaultLeadOre());
        putPreferredOre(EItemTags.ORES_PLATINUM, EConfig.COMMON.preferredPlatinumOre, getDefaultPlatinumOre());
        putPreferredOre(EItemTags.ORES_NICKEL, EConfig.COMMON.preferredNickelOre, getDefaultNickelOre());
        putPreferredOre(EItemTags.ORES_URANIUM, EConfig.COMMON.preferredUraniumOre, getDefaultUraniumOre());
        putPreferredOre(EItemTags.ORES_OSMIUM, EConfig.COMMON.preferredOsmiumOre, getDefaultOsmiumOre());
        putPreferredOre(EItemTags.ORES_TIN, EConfig.COMMON.preferredTinOre, getDefaultTinOre());
        putPreferredOre(EItemTags.ORES_ZINC, EConfig.COMMON.preferredZincOre, getDefaultZincOre());
        putPreferredOre(EItemTags.ORES_IRIDIUM, EConfig.COMMON.preferredIridiumOre, getDefaultIridiumOre());
        putPreferredOre(EItemTags.ORES_THORIUM, EConfig.COMMON.preferredThoriumOre, getDefaultThoriumOre());
        putPreferredOre(EItemTags.ORES_MAGNESIUM, EConfig.COMMON.preferredMagnesiumOre, getDefaultMagnesiumOre());
        putPreferredOre(EItemTags.ORES_LITHIUM, EConfig.COMMON.preferredLithiumOre, getDefaultLithiumOre());
        putPreferredOre(EItemTags.ORES_BORON, EConfig.COMMON.preferredBoronOre, getDefaultBoronOre());
    }

    /**
     * Determines which mod's ore should be crafted by Ex Deorum ore chunks.
     *
     * @param tag        The tag which contains a list of choices for the desired ore. Might be empty.
     * @param config     A config which holds an override value chosen by the user. Could even be something that isn't an ore.
     * @param defaultOre The default ore choice, picked by Ex Deorum based on which mod is the "best" choice according to thedarkcolour.
     */
    @SuppressWarnings("deprecation")
    private static void putPreferredOre(TagKey<Item> tag, ForgeConfigSpec.ConfigValue<String> config, Item defaultOre) {
        var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.get()));

        if (item == Items.AIR) {
            item = defaultOre;
            ExDeorum.LOGGER.debug("No preferred ore was set for tag {}. Using default choice {}", tag.location(), item.builtInRegistryHolder().key().location());
        }
        PREFERRED_ORE_ITEMS.put(tag, defaultOre);
    }

    /**
     * This method might not work properly during world load, since tags are unavailable.
     *
     * @return The preferred (as specified by config, or by alphabetical order)
     * item from the given tag or {@link Items#AIR} if the tag is empty.
     */
    @SuppressWarnings("deprecation")
    public static Item getPreferredOre(TagKey<Item> tag) {
        Item preferred = PREFERRED_ORE_ITEMS.get(tag);

        if (preferred != null && preferred != Items.AIR) {
            return preferred;
        } else {
            var collection = Lists.newArrayList(BuiltInRegistries.ITEM.getTagOrEmpty(tag));

            if (collection.isEmpty()) {
                return Items.AIR;
            } else {
                collection.sort(Comparator.comparing(holder -> BuiltInRegistries.ITEM.getKey(holder.get())));

                // todo should the PREFERRED_ORE map be updated with this value?
                return collection.get(0).get();
            }
        }
    }

    public static Item getDefaultAluminumOre() {
        return defaultItem("aluminum_ore", ModIds.ALL_THE_ORES);
    }

    public static Item getDefaultCobaltOre() {
        return defaultItem("cobalt_ore", ModIds.TINKERS_CONSTRUCT, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultSilverOre() {
        return defaultItem("silver_ore", ModIds.ALL_THE_ORES, ModIds.OCCULTISM, ModIds.RAILCRAFT, ModIds.FACTORIUM, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultLeadOre() {
        return defaultItem("lead_ore", ModIds.ALL_THE_ORES, ModIds.GREG, ModIds.MEKANISM, ModIds.RAILCRAFT, ModIds.FACTORIUM, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultPlatinumOre() {
        return defaultItem("platinum_ore", ModIds.ALL_THE_ORES, ModIds.GREG, ModIds.FACTORIUM, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultNickelOre() {
        return defaultItem("nickel_ore", ModIds.ALL_THE_ORES, ModIds.GREG, ModIds.THERMAL, ModIds.RAILCRAFT, ModIds.FACTORIUM);
    }

    public static Item getDefaultUraniumOre() {
        var item = defaultItem("uranium_ore", ModIds.ALL_THE_ORES, ModIds.MEKANISM, ModIds.BIGGER_REACTORS, ModIds.NUCLEARCRAFT_NEOTERIC);

        if (item == Items.AIR && ModList.get().isLoaded(ModIds.EXTREME_REACTORS)) {
            item = defaultItem("yellorite_ore", ModIds.EXTREME_REACTORS);
        }

        return item;
    }

    public static Item getDefaultOsmiumOre() {
        return defaultItem("osmium_ore", ModIds.ALL_THE_ORES, ModIds.MEKANISM);
    }

    public static Item getDefaultTinOre() {
        return defaultItem("tin_ore", ModIds.ALL_THE_ORES, ModIds.GREG, ModIds.THERMAL, ModIds.MEKANISM, ModIds.RAILCRAFT, ModIds.FACTORIUM, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultZincOre() {
        return defaultItem("zinc_ore", ModIds.ALL_THE_ORES, ModIds.GREG, ModIds.CREATE, ModIds.RAILCRAFT, ModIds.FACTORIUM, ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultIridiumOre() {
        return defaultItem("iridium_ore", ModIds.ALL_THE_ORES);
    }

    public static Item getDefaultThoriumOre() {
        return defaultItem("thorium_ore", ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultMagnesiumOre() {
        return defaultItem("magnesium_ore", ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultLithiumOre() {
        return defaultItem("lithium_ore", ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    public static Item getDefaultBoronOre() {
        return defaultItem("boron_ore", ModIds.NUCLEARCRAFT_NEOTERIC);
    }

    private static Item defaultItem(String path, String... modIds) {
        var modId = getFirstAvailableModId(modIds);

        if (modId != null) {
            if (modId.equals(ModIds.FACTORIUM)) {
                return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, "mat_" + path));
            } else {
                return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, path));
            }
        } else {
            return Items.AIR;
        }
    }

    private static @Nullable String getFirstAvailableModId(String... modids) {
        for (var modid : modids) {
            if (ModList.get().isLoaded(modid)) {
                return modid;
            }
        }

        ExDeorum.LOGGER.debug("None of the specified mods were found: {}", Arrays.toString(modids));
        return null;
    }
}
