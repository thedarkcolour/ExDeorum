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

package thedarkcolour.exdeorum.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.List;

public class EConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Client CLIENT;
    public static final Common COMMON;
    public static final Server SERVER;

    public static class Client {
        public final BooleanValue useFastInfestedLeaves;
        public final BooleanValue setVoidWorldAsDefault;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client configuration for Ex Deorum").push("client");

            this.useFastInfestedLeaves = builder
                    .comment("Whether to use a simplified renderer for infested leaves (reduces FPS lag with lots of infested trees)")
                    .define("use_fast_infested_leaves", false);
            this.setVoidWorldAsDefault = builder
                    .comment("Whether the Void World type is set as the default world preset in the world creation screen.")
                    .define("set_void_world_as_default", true);

            builder.pop();
        }
    }

    // Needed because common configs load before Tags
    public static class Common {
        public final ConfigValue<String> preferredAluminumOre;
        public final ConfigValue<String> preferredCobaltOre;
        public final ConfigValue<String> preferredSilverOre;
        public final ConfigValue<String> preferredLeadOre;
        public final ConfigValue<String> preferredPlatinumOre;
        public final ConfigValue<String> preferredNickelOre;
        public final ConfigValue<String> preferredUraniumOre;
        public final ConfigValue<String> preferredOsmiumOre;
        public final ConfigValue<String> preferredTinOre;
        public final ConfigValue<String> preferredZincOre;
        public final ConfigValue<String> preferredIridiumOre;

        public Common(ForgeConfigSpec.Builder builder) {
            // Preferred items
            builder.comment("Common configuration for Ex Deorum").push("common");

            builder.comment("For recipes automatically added by Ex Deorum for other mods, some mods may add two of the same item (ex. Tin Ore). When Ex Deorum adds a recipe for those kinds of items, you may choose which item of the two (or more) is chosen as the crafting result.").push("preferred_tag_items");

            var airId = ModIds.MINECRAFT + ":air";

            this.preferredAluminumOre = preferredOreConfig(builder, "aluminum_ore", airId);
            this.preferredCobaltOre = preferredOreConfig(builder, "cobalt_ore", ModIds.TINKERS_CONSTRUCT + ":cobalt_ore");
            this.preferredSilverOre = preferredOreConfig(builder, "silver_ore", airId);
            this.preferredLeadOre = preferredOreConfig(builder, "lead_ore", airId);
            this.preferredPlatinumOre = preferredOreConfig(builder, "platinum_ore", airId);
            this.preferredNickelOre = preferredOreConfig(builder, "nickel_ore", airId);
            this.preferredUraniumOre = preferredOreConfig(builder, "uranium_ore", airId);
            this.preferredOsmiumOre = preferredOreConfig(builder, "osmium_ore", airId);
            this.preferredTinOre = preferredOreConfig(builder, "tin_ore", airId);
            this.preferredZincOre = preferredOreConfig(builder, "zinc_ore", airId);
            this.preferredIridiumOre = preferredOreConfig(builder, "iridium_ore", airId);

            builder.pop(2);
        }
    }

    public static class Server {
        public final BooleanValue startingTorch;
        public final BooleanValue startingWateringCan;
        public final BooleanValue simultaneousSieveUsage;
        public final BooleanValue automatedSieves;
        public final DoubleValue barrelProgressStep;
        public final BooleanValue witchWaterNetherrackGenerator;
        public final BooleanValue setVoidWorldAsDefault;
        public final ConfigValue<String> defaultSpawnTreeFeature;
        public final BooleanValue useBiomeAppropriateTree;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration for Ex Deorum").push("server");

            this.startingTorch = builder
                    .comment("Whether players in a void world start out with a torch or not.")
                    .define("starting_torch", true);
            this.startingWateringCan = builder
                    .comment("Whether players in a void world start out with a full wooden watering can.")
                    .define("starting_watering_can", true);
            this.simultaneousSieveUsage = builder
                    .comment("Whether players can use multiple sieves in a 3x3 area at once.")
                    .define("simultaneous_sieve_usage", true);
            this.automatedSieves = builder
                    .comment("Whether machines/fake players can interact with the Sieve.")
                    .define("automated_sieves", false);
            this.barrelProgressStep = builder
                    .comment("The progress to increment by each tick for barrel composting and witch water transformation.")
                    .defineInRange("barrel_progress_step", 0.004, 0.0f, 1.0f);
            this.witchWaterNetherrackGenerator = builder
                    .comment("Whether Witch Water forms netherrack when lava flows into it, allowing for a netherrack version of a cobblestone generator.")
                    .define("witch_water_netherrack_generator", true);
            this.setVoidWorldAsDefault = builder
                    .comment("Whether the Void World type is used by default in the \"server.properties\" file when creating a server.")
                    .define("set_void_world_as_default", true);
            this.defaultSpawnTreeFeature = builder
                    .comment("The ID of the default tree feature to use when generating a spawn island (or when useBiomeAppropriateTree is true and the biome has no tree set). By default, minecraft:oak_tree_bees_005 is used.")
                    .define("default_spawn_tree_feature", ModIds.MINECRAFT + ":oak_tree_bees_005");
            this.useBiomeAppropriateTree = builder
                    .comment("Whether the Spawn Tree in the void world changes based on the biome it's in. If false, Oak Tree is always used.")
                        .define("use_biome_appropriate_tree", false);

            builder.pop();
        }
    }

    @SuppressWarnings("deprecation")
    private static ConfigValue<String> preferredOreConfig(ForgeConfigSpec.Builder builder, String name, String defaultId) {
        return builder
                .comment("The ID of the item to use for Ex Deorum recipes that craft into " + WordUtils.capitalize(name.replace('_', ' ')) + ". Leave as air for default preference, which chooses alphabetically by mod name.")
                .define(List.of("preferred_" + name), defaultId, o -> o != null && o.getClass() == String.class && ResourceLocation.isValidResourceLocation((String) o));
    }

    static {
        {
            Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
        {
            Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }
}
