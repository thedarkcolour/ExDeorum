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

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.IdentityHashMap;
import java.util.Map;

// Mocks modded items so that data generation can reference modded items without needing those mods installed.
public class ModCompatData {
    // Identity maps because keys are just constants from ModIds
    private static final Map<String, DeferredRegister<Item>> itemRegistries = new IdentityHashMap<>();
    private static final Map<String, DeferredRegister<Block>> blockRegistries = new IdentityHashMap<>();

    @SuppressWarnings("DataFlowIssue")
    private static RegistryObject<Item> item(String modid, String name) {
        if (DatagenModLoader.isRunningDataGen()) {
            DeferredRegister<Item> registry = itemRegistries.computeIfAbsent(modid, key -> DeferredRegister.create(Registries.ITEM, key));
            return registry.register(name, () -> new Item(new Item.Properties()));
        } else {
            return null;
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private static RegistryObject<Block> block(String modid, String name) {
        if (DatagenModLoader.isRunningDataGen()) {
            DeferredRegister<Block> registry = blockRegistries.computeIfAbsent(modid, key -> DeferredRegister.create(Registries.BLOCK, key));
            return registry.register(name, () -> new Block(BlockBehaviour.Properties.of()));
        } else {
            return null;
        }
    }

    // Ender IO
    public static final RegistryObject<Item>
            GRAINS_OF_INFINITY = item(ModIds.ENDERIO, "grains_of_infinity");
    // Bigger reactors
    public static final RegistryObject<Item>
            YELLORIUM_DUST = item(ModIds.BIGGER_REACTORS, "yellorium_dust");
    // Biomes O' Plenty
    public static final RegistryObject<Block>
            FIR_PLANKS = block(ModIds.BIOMES_O_PLENTY, "fir_planks"),
            REDWOOD_PLANKS = block(ModIds.BIOMES_O_PLENTY, "redwood_planks"),
            MAHOGANY_PLANKS = block(ModIds.BIOMES_O_PLENTY, "mahogany_planks"),
            JACARANDA_PLANKS = block(ModIds.BIOMES_O_PLENTY, "jacaranda_planks"),
            PALM_PLANKS = block(ModIds.BIOMES_O_PLENTY, "palm_planks"),
            WILLOW_PLANKS = block(ModIds.BIOMES_O_PLENTY, "willow_planks"),
            DEAD_PLANKS = block(ModIds.BIOMES_O_PLENTY, "dead_planks"),
            MAGIC_PLANKS = block(ModIds.BIOMES_O_PLENTY, "magic_planks"),
            UMBRAN_PLANKS = block(ModIds.BIOMES_O_PLENTY, "umbran_planks"),
            HELLBARK_PLANKS = block(ModIds.BIOMES_O_PLENTY, "hellbark_planks"),
            FIR_LOG = block(ModIds.BIOMES_O_PLENTY, "fir_log"),
            REDWOOD_LOG = block(ModIds.BIOMES_O_PLENTY, "redwood_log"),
            MAHOGANY_LOG = block(ModIds.BIOMES_O_PLENTY, "mahogany_log"),
            JACARANDA_LOG = block(ModIds.BIOMES_O_PLENTY, "jacaranda_log"),
            PALM_LOG = block(ModIds.BIOMES_O_PLENTY, "palm_log"),
            WILLOW_LOG = block(ModIds.BIOMES_O_PLENTY, "willow_log"),
            DEAD_LOG = block(ModIds.BIOMES_O_PLENTY, "dead_log"),
            MAGIC_LOG = block(ModIds.BIOMES_O_PLENTY, "magic_log"),
            UMBRAN_LOG = block(ModIds.BIOMES_O_PLENTY, "umbran_log"),
            HELLBARK_LOG = block(ModIds.BIOMES_O_PLENTY, "hellbark_log");
    public static final RegistryObject<Item>
            FIR_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "fir_planks"),
            REDWOOD_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "redwood_planks"),
            MAHOGANY_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "mahogany_planks"),
            JACARANDA_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "jacaranda_planks"),
            PALM_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "palm_planks"),
            WILLOW_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "willow_planks"),
            DEAD_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "dead_planks"),
            MAGIC_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "magic_planks"),
            UMBRAN_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "umbran_planks"),
            HELLBARK_PLANKS_ITEM = item(ModIds.BIOMES_O_PLENTY, "hellbark_planks"),
            FIR_SLAB = item(ModIds.BIOMES_O_PLENTY, "fir_slab"),
            REDWOOD_SLAB = item(ModIds.BIOMES_O_PLENTY, "redwood_slab"),
            MAHOGANY_SLAB = item(ModIds.BIOMES_O_PLENTY, "mahogany_slab"),
            JACARANDA_SLAB = item(ModIds.BIOMES_O_PLENTY, "jacaranda_slab"),
            PALM_SLAB = item(ModIds.BIOMES_O_PLENTY, "palm_slab"),
            WILLOW_SLAB = item(ModIds.BIOMES_O_PLENTY, "willow_slab"),
            DEAD_SLAB = item(ModIds.BIOMES_O_PLENTY, "dead_slab"),
            MAGIC_SLAB = item(ModIds.BIOMES_O_PLENTY, "magic_slab"),
            UMBRAN_SLAB = item(ModIds.BIOMES_O_PLENTY, "umbran_slab"),
            HELLBARK_SLAB = item(ModIds.BIOMES_O_PLENTY, "hellbark_slab"),
            FIR_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "fir_log"),
            REDWOOD_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "redwood_log"),
            MAHOGANY_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "mahogany_log"),
            JACARANDA_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "jacaranda_log"),
            PALM_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "palm_log"),
            WILLOW_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "willow_log"),
            DEAD_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "dead_log"),
            MAGIC_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "magic_log"),
            UMBRAN_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "umbran_log"),
            HELLBARK_LOG_ITEM = item(ModIds.BIOMES_O_PLENTY, "hellbark_log"),
            ORIGIN_SAPLING = item(ModIds.BIOMES_O_PLENTY, "origin_sapling"),
            FLOWERING_OAK_SAPLING = item(ModIds.BIOMES_O_PLENTY, "flowering_oak_sapling"),
            SNOWBLOSSOM_SAPLING = item(ModIds.BIOMES_O_PLENTY, "snowblossom_sapling"),
            RAINBOW_BIRCH_SAPLING = item(ModIds.BIOMES_O_PLENTY, "rainbow_birch_sapling"),
            YELLOW_AUTUMN_SAPLING = item(ModIds.BIOMES_O_PLENTY, "yellow_autumn_sapling"),
            ORANGE_AUTUMN_SAPLING = item(ModIds.BIOMES_O_PLENTY, "orange_autumn_sapling"),
            MAPLE_SAPLING = item(ModIds.BIOMES_O_PLENTY, "maple_sapling"),
            FIR_SAPLING = item(ModIds.BIOMES_O_PLENTY, "fir_sapling"),
            REDWOOD_SAPLING = item(ModIds.BIOMES_O_PLENTY, "redwood_sapling"),
            MAHOGANY_SAPLING = item(ModIds.BIOMES_O_PLENTY, "mahogany_sapling"),
            JACARANDA_SAPLING = item(ModIds.BIOMES_O_PLENTY, "jacaranda_sapling"),
            PALM_SAPLING = item(ModIds.BIOMES_O_PLENTY, "palm_sapling"),
            WILLOW_SAPLING = item(ModIds.BIOMES_O_PLENTY, "willow_sapling"),
            DEAD_SAPLING = item(ModIds.BIOMES_O_PLENTY, "dead_sapling"),
            MAGIC_SAPLING = item(ModIds.BIOMES_O_PLENTY, "magic_sapling"),
            UMBRAN_SAPLING = item(ModIds.BIOMES_O_PLENTY, "umbran_sapling"),
            HELLBARK_SAPLING = item(ModIds.BIOMES_O_PLENTY, "hellbark_sapling");
    // Applied Energistics 2
    public static final RegistryObject<Item>
            CERTUS_QUARTZ_CRYSTAL = item(ModIds.APPLIED_ENERGISTICS_2, "certus_quartz_crystal"),
            CHARGED_CERTUS_QUARTZ_CRYSTAL = item(ModIds.APPLIED_ENERGISTICS_2, "charged_certus_quartz_crystal"),
            CERTUS_QUARTZ_DUST = item(ModIds.APPLIED_ENERGISTICS_2, "certus_quartz_dust"),
            SKY_STONE_DUST = item(ModIds.APPLIED_ENERGISTICS_2, "sky_dust");
    // Ars Nouveau
    public static final RegistryObject<Block>
            CASCADING_ARCHWOOD_LOG = block(ModIds.ARS_NOUVEAU, "blue_archwood_log"),
            BLAZING_ARCHWOOD_LOG = block(ModIds.ARS_NOUVEAU, "red_archwood_log"),
            VEXING_ARCHWOOD_LOG = block(ModIds.ARS_NOUVEAU, "purple_archwood_log"),
            FLOURISHING_ARCHWOOD_LOG = block(ModIds.ARS_NOUVEAU, "green_archwood_log"),
            ARCHWOOD_PLANKS = block(ModIds.ARS_NOUVEAU, "archwood_planks");
    public static final RegistryObject<Item>
            BLUE_ARCHWOOD_SAPLING = item(ModIds.ARS_NOUVEAU, "blue_archwood_sapling"),
            RED_ARCHWOOD_SAPLING = item(ModIds.ARS_NOUVEAU, "red_archwood_sapling"),
            PURPLE_ARCHWOOD_SAPLING = item(ModIds.ARS_NOUVEAU, "purple_archwood_sapling"),
            GREEN_ARCHWOOD_SAPLING = item(ModIds.ARS_NOUVEAU, "green_archwood_sapling"),
            SOURCEBERRY = item(ModIds.ARS_NOUVEAU, "sourceberry_bush"),
            CASCADING_ARCHWOOD_LOG_ITEM = item(ModIds.ARS_NOUVEAU, "blue_archwood_log"),
            BLAZING_ARCHWOOD_LOG_ITEM = item(ModIds.ARS_NOUVEAU, "red_archwood_log"),
            VEXING_ARCHWOOD_LOG_ITEM = item(ModIds.ARS_NOUVEAU, "purple_archwood_log"),
            FLOURISHING_ARCHWOOD_LOG_ITEM = item(ModIds.ARS_NOUVEAU, "green_archwood_log"),
            ARCHWOOD_SLAB = item(ModIds.ARS_NOUVEAU, "archwood_slab"),
            ARCHWOOD_PLANKS_ITEM = item(ModIds.ARS_NOUVEAU, "archwood_planks");
    // Aether
    public static final RegistryObject<Block>
            SKYROOT_PLANKS = block(ModIds.AETHER, "skyroot_planks"),
            SKYROOT_LOG = block(ModIds.AETHER, "skyroot_log"),
            GOLDEN_OAK_LOG = block(ModIds.AETHER, "golden_oak_log");
    public static final RegistryObject<Item>
            SKYROOT_SLAB = item(ModIds.AETHER, "skyroot_slab"),
            SKYROOT_PLANKS_ITEM = item(ModIds.AETHER, "skyroot_planks"),
            GOLDEN_OAK_LOG_ITEM = item(ModIds.AETHER, "golden_oak_log"),
            SKYROOT_LOG_ITEM = item(ModIds.AETHER, "skyroot_log");
    // Blue Skies
    public static final RegistryObject<Block>
            BLUEBRIGHT_PLANKS = block(ModIds.BLUE_SKIES, "bluebright_planks"),
            STARLIT_PLANKS = block(ModIds.BLUE_SKIES, "starlit_planks"),
            FROSTBRIGHT_PLANKS = block(ModIds.BLUE_SKIES, "frostbright_planks"),
            COMET_PLANKS = block(ModIds.BLUE_SKIES, "comet_planks"),
            LUNAR_PLANKS = block(ModIds.BLUE_SKIES, "lunar_planks"),
            DUSK_PLANKS = block(ModIds.BLUE_SKIES, "dusk_planks"),
            MAPLE_PLANKS = block(ModIds.BLUE_SKIES, "maple_planks"),
            CRYSTALLIZED_PLANKS = block(ModIds.BLUE_SKIES, "crystallized_planks"),
            BLUEBRIGHT_LOG = block(ModIds.BLUE_SKIES, "bluebright_log"),
            STARLIT_LOG = block(ModIds.BLUE_SKIES, "starlit_log"),
            FROSTBRIGHT_LOG = block(ModIds.BLUE_SKIES, "frostbright_log"),
            COMET_LOG = block(ModIds.BLUE_SKIES, "comet_log"),
            LUNAR_LOG = block(ModIds.BLUE_SKIES, "lunar_log"),
            DUSK_LOG = block(ModIds.BLUE_SKIES, "dusk_log"),
            MAPLE_LOG = block(ModIds.BLUE_SKIES, "maple_log"),
            CRYSTALLIZED_LOG = block(ModIds.BLUE_SKIES, "crystallized_log");
    public static final RegistryObject<Item>
            BLUEBRIGHT_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "bluebright_planks"),
            STARLIT_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "starlit_planks"),
            FROSTBRIGHT_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "frostbright_planks"),
            COMET_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "comet_planks"),
            LUNAR_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "lunar_planks"),
            DUSK_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "dusk_planks"),
            MAPLE_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "maple_planks"),
            CRYSTALLIZED_PLANKS_ITEM = item(ModIds.BLUE_SKIES, "crystallized_planks"),
            BLUEBRIGHT_SLAB = item(ModIds.BLUE_SKIES, "bluebright_slab"),
            STARLIT_SLAB = item(ModIds.BLUE_SKIES, "starlit_slab"),
            FROSTBRIGHT_SLAB = item(ModIds.BLUE_SKIES, "frostbright_slab"),
            COMET_SLAB = item(ModIds.BLUE_SKIES, "comet_slab"),
            LUNAR_SLAB = item(ModIds.BLUE_SKIES, "lunar_slab"),
            DUSK_SLAB = item(ModIds.BLUE_SKIES, "dusk_slab"),
            MAPLE_SLAB = item(ModIds.BLUE_SKIES, "maple_slab"),
            CRYSTALLIZED_SLAB = item(ModIds.BLUE_SKIES, "crystallized_slab"),
            BLUEBRIGHT_LOG_ITEM = item(ModIds.BLUE_SKIES, "bluebright_log"),
            STARLIT_LOG_ITEM = item(ModIds.BLUE_SKIES, "starlit_log"),
            FROSTBRIGHT_LOG_ITEM = item(ModIds.BLUE_SKIES, "frostbright_log"),
            COMET_LOG_ITEM = item(ModIds.BLUE_SKIES, "comet_log"),
            LUNAR_LOG_ITEM = item(ModIds.BLUE_SKIES, "lunar_log"),
            DUSK_LOG_ITEM = item(ModIds.BLUE_SKIES, "dusk_log"),
            MAPLE_LOG_ITEM = item(ModIds.BLUE_SKIES, "maple_log"),
            CRYSTALLIZED_LOG_ITEM = item(ModIds.BLUE_SKIES, "crystallized_log");

    public static final ResourceLocation[] PAMS_CROPS;

    static {
        // copy and pasted from the GitHub
        String[] names = { "pamagavecrop", "pamamaranthcrop", "pamarrowrootcrop", "pamartichokecrop", "pamasparaguscrop", "pambarleycrop", "pambeancrop", "pambellpeppercrop", "pamblackberrycrop", "pamblueberrycrop", "pambroccolicrop", "pambrusselsproutcrop", "pamcabbagecrop", "pamcactusfruitcrop", "pamcandleberrycrop", "pamcantaloupecrop", "pamcassavacrop", "pamcauliflowercrop", "pamcelerycrop", "pamchickpeacrop", "pamchilipeppercrop", "pamcoffeebeancrop", "pamcorncrop", "pamcottoncrop", "pamcranberrycrop", "pamcucumbercrop", "pameggplantcrop", "pamelderberrycrop", "pamflaxcrop", "pamgarliccrop", "pamgingercrop", "pamgrapecrop", "pamgreengrapecrop", "pamhuckleberrycrop", "pamjicamacrop", "pamjuniperberrycrop", "pamjutecrop", "pamkalecrop", "pamkenafcrop", "pamkiwicrop", "pamkohlrabicrop", "pamleekcrop", "pamlentilcrop", "pamlettucecrop", "pammilletcrop", "pammulberrycrop", "pammustardseedscrop", "pamoatscrop", "pamokracrop", "pamonioncrop", "pamparsnipcrop", "pampeanutcrop", "pampeascrop", "pampineapplecrop", "pamquinoacrop", "pamradishcrop", "pamraspberrycrop", "pamrhubarbcrop", "pamricecrop", "pamrutabagacrop", "pamryecrop", "pamscallioncrop", "pamsesameseedscrop", "pamsisalcrop", "pamsoybeancrop", "pamspiceleafcrop", "pamspinachcrop", "pamstrawberrycrop", "pamsweetpotatocrop", "pamtarocrop", "pamtealeafcrop", "pamtomatillocrop", "pamtomatocrop", "pamturnipcrop", "pamwaterchestnutcrop", "pamwhitemushroomcrop", "pamwintersquashcrop", "pamzucchinicrop"};
        PAMS_CROPS = new ResourceLocation[names.length];

        for (int i = 0, namesLength = names.length; i < namesLength; i++) {
            PAMS_CROPS[i] = new ResourceLocation(ModIds.PAMS_HARVESTCRAFT_CROPS, names[i]);
        }
    }

    public static void registerModData() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        for (var registry : itemRegistries.values()) {
            registry.register(modBus);
        }
        for (var registry : blockRegistries.values()) {
            registry.register(modBus);
        }
    }
}
