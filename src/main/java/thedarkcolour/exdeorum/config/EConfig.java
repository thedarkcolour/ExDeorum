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

package thedarkcolour.exdeorum.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
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
                    .comment("Whether the Void World type is set as the default world preset. (DEPRECATED - USE THE OPTION IN THE COMMON CONFIG INSTEAD)")
                    .define("set_void_world_as_default", true);

            builder.pop();
        }
    }

    // Needed because these configs are needed before Tags are loaded
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
        public final ConfigValue<String> preferredThoriumOre;
        public final ConfigValue<String> preferredMagnesiumOre;
        public final ConfigValue<String> preferredLithiumOre;
        public final ConfigValue<String> preferredBoronOre;

        public final BooleanValue setVoidWorldAsDefault;
        public final BooleanValue voidNetherGeneration;
        public final BooleanValue voidEndGeneration;

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
            this.preferredThoriumOre = preferredOreConfig(builder, "thorium_ore", airId);
            this.preferredMagnesiumOre = preferredOreConfig(builder, "magnesium_ore", airId);
            this.preferredLithiumOre = preferredOreConfig(builder, "lithium_ore", airId);
            this.preferredBoronOre = preferredOreConfig(builder, "boron_ore", airId);

            builder.pop();

            this.setVoidWorldAsDefault = builder
                    .comment("Whether the Void World type is set as the default world preset in world creation screen and server.properties.")
                    .define("set_void_world_as_default", true);
            this.voidNetherGeneration = builder
                    .comment("If the Void World type is selected, whether the Nether world generation is overridden to a void world. Changes take effect after reopening the world.")
                    .define("void_nether_generation", true);
            this.voidEndGeneration = builder
                    .comment("If the Void World type is selected, whether the End world generation is overridden to a void world. Changes take effect after reopening the world.")
                    .define("void_end_generation", true);

            builder.pop();
        }
    }

    public static class Server {
        public final BooleanValue startingTorch;
        public final BooleanValue startingWateringCan;
        public final BooleanValue simultaneousSieveUsage;
        public final IntValue simultaneousSieveUsageRange;
        public final BooleanValue automatedSieves;
        public final DoubleValue barrelProgressStep;
        public final BooleanValue witchWaterDirtGenerator;
        public final BooleanValue witchWaterNetherrackGenerator;
        public final ConfigValue<String> defaultSpawnTreeFeature;
        public final BooleanValue useBiomeAppropriateTree;
        public final BooleanValue limitMossSieveDrops;
        public final BooleanValue allowWaterBottleTransfer;
        public final BooleanValue allowWitchWaterEntityConversion;
        public final IntValue mechanicalSieveEnergyStorage;
        public final IntValue mechanicalSieveEnergyConsumption;
        public final IntValue mechanicalHammerEnergyStorage;
        public final IntValue mechanicalHammerEnergyConsumption;
        public final IntValue sieveIntervalTicks;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration for Ex Deorum").push("server");

            this.startingTorch = builder
                    .comment("Whether players in a void world start out with a torch or not.")
                    .define("starting_torch", true);
            this.startingWateringCan = builder
                    .comment("Whether players in a void world start out with a full wooden watering can.")
                    .define("starting_watering_can", true);
            this.simultaneousSieveUsage = builder
                    .comment("Whether players can use multiple sieves in a 3x3 or larger area at once.")
                    .define("simultaneous_sieve_usage", true);
            this.simultaneousSieveUsageRange = builder
                    .comment("The range from which simultaneous sieve usage can reach. 1 means a maximum of 3x3 sieves at once, 2 means a maximum of 5x5, 3 means maximum of 7x7 simultaneous sieves, and so on.")
                    .defineInRange("simultaneous_sieve_range", 2, 0, 6);
            this.automatedSieves = builder
                    .comment("Whether machines/fake players can interact with the Sieve.")
                    .define("automated_sieves", false);
            this.barrelProgressStep = builder
                    .comment("The progress to increment by each tick for barrel composting.")
                    .defineInRange("barrel_progress_step", 0.004, 0.0f, 1.0f);
            this.witchWaterDirtGenerator = builder
                    .comment("Whether Witch Water forms dirt when water flows into it, allowing for a dirt version of a cobblestone generator.")
                    .define("witch_water_dirt_generator", false);
            this.witchWaterNetherrackGenerator = builder
                    .comment("Whether Witch Water forms netherrack when lava flows into it, allowing for a netherrack version of a cobblestone generator.")
                    .define("witch_water_netherrack_generator", true);
            this.defaultSpawnTreeFeature = builder
                    .comment("The ID of the default tree feature to use when generating a spawn island (or when useBiomeAppropriateTree is true and the biome has no tree set). By default, minecraft:oak_tree_bees_005 is used.")
                    .define("default_spawn_tree_feature", ModIds.MINECRAFT + ":oak_tree_bees_005");
            this.useBiomeAppropriateTree = builder
                    .comment("Whether the Spawn Tree in the void world changes based on the biome it's in. If false, Oak Tree is always used.")
                        .define("use_biome_appropriate_tree", false);
            this.limitMossSieveDrops = builder
                    .comment("Whether to restrict Moss Block sieve drops to 1-2 items when sieving. May be useful when lots of mods add saplings and the sieve drops become spammy.")
                    .define("limit_moss_sieve_drops", true);
            this.allowWaterBottleTransfer = builder
                    .comment("Whether glass bottles can be used to transfer water between water crucibles and barrels.")
                    .define("allow_water_bottle_transfer", true);
            this.allowWitchWaterEntityConversion = builder
                    .comment("Whether the entity conversion mechanic of Witch Water is enabled. If enabled, when an entity steps into Witch Water, the following conversions may happen: Villager -> Zombie Villager, Cleric Villager -> Witch, Skeleton -> Wither Skeleton, Creeper -> Charged Creeper, Spider -> Cave Spider, Pig & Piglin -> Zombified Piglin, Squid -> Ghast, Mooshroom -> Brown Mooshroom, Axolotl -> Blue Axolotl, Rabbit -> Killer Rabbit, Pufferfish -> Guardian, Horse -> Skeleton/Zombie Horse")
                    .define("allow_witch_water_entity_conversion", true);
            this.mechanicalSieveEnergyStorage = builder
                    .comment("The maximum amount of FE the mechanical sieve can have in its energy storage.")
                    .defineInRange("mechanical_sieve_energy_storage", 40_000, 0, Integer.MAX_VALUE);
            this.mechanicalSieveEnergyConsumption = builder
                    .comment("The amount of FE/t a tick consumed by the mechanical sieve when sifting a block.")
                    .defineInRange("mechanical_sieve_energy_consumption", 40, 0, Integer.MAX_VALUE);
            this.mechanicalHammerEnergyStorage = builder
                    .comment("The maximum amount of FE the mechanical hammer can have in its energy storage.")
                    .defineInRange("mechanical_hammer_energy_storage", 40_000, 0, Integer.MAX_VALUE);
            this.mechanicalHammerEnergyConsumption = builder
                    .comment("The amount of FE/t a tick consumed by the mechanical hammer when crushing a block.")
                    .defineInRange("mechanical_hammer_energy_consumption", 20, 0, Integer.MAX_VALUE);
            this.sieveIntervalTicks = builder
                    .comment("The minimum number of ticks a player must wait between two sifting operations. Only affects sifting by hand. 0 means no limit.")
                    .defineInRange("sieve_interval", 1, 0, Integer.MAX_VALUE);
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
