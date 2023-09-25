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

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.function.Consumer;
import java.util.function.Function;

// registers dummy items so that data generation can reference modded items without needing those mods installed.
public class ModCompatData {
    // Ender IO
    public static RegistryObject<Item> GRAINS_OF_INFINITY;
    // Bigger reactors
    public static RegistryObject<Item> YELLORIUM_DUST;
    // Biomes O' Plenty
    public static RegistryObject<Block> FIR_PLANKS;
    public static RegistryObject<Block> REDWOOD_PLANKS;
    public static RegistryObject<Block> MAHOGANY_PLANKS;
    public static RegistryObject<Block> JACARANDA_PLANKS;
    public static RegistryObject<Block> PALM_PLANKS;
    public static RegistryObject<Block> WILLOW_PLANKS;
    public static RegistryObject<Block> DEAD_PLANKS;
    public static RegistryObject<Block> MAGIC_PLANKS;
    public static RegistryObject<Block> UMBRAN_PLANKS;
    public static RegistryObject<Block> HELLBARK_PLANKS;
    public static RegistryObject<Block> FIR_LOG;
    public static RegistryObject<Block> REDWOOD_LOG;
    public static RegistryObject<Block> MAHOGANY_LOG;
    public static RegistryObject<Block> JACARANDA_LOG;
    public static RegistryObject<Block> PALM_LOG;
    public static RegistryObject<Block> WILLOW_LOG;
    public static RegistryObject<Block> DEAD_LOG;
    public static RegistryObject<Block> MAGIC_LOG;
    public static RegistryObject<Block> UMBRAN_LOG;
    public static RegistryObject<Block> HELLBARK_LOG;
    public static RegistryObject<Item> ORIGIN_SAPLING;
    public static RegistryObject<Item> FLOWERING_OAK_SAPLING;
    public static RegistryObject<Item> SNOWBLOSSOM_SAPLING;
    public static RegistryObject<Item> RAINBOW_BIRCH_SAPLING;
    public static RegistryObject<Item> YELLOW_AUTUMN_SAPLING;
    public static RegistryObject<Item> ORANGE_AUTUMN_SAPLING;
    public static RegistryObject<Item> MAPLE_SAPLING;
    public static RegistryObject<Item> FIR_SAPLING;
    public static RegistryObject<Item> REDWOOD_SAPLING;
    public static RegistryObject<Item> MAHOGANY_SAPLING;
    public static RegistryObject<Item> JACARANDA_SAPLING;
    public static RegistryObject<Item> PALM_SAPLING;
    public static RegistryObject<Item> WILLOW_SAPLING;
    public static RegistryObject<Item> DEAD_SAPLING;
    public static RegistryObject<Item> MAGIC_SAPLING;
    public static RegistryObject<Item> UMBRAN_SAPLING;
    public static RegistryObject<Item> HELLBARK_SAPLING;
    // Applied Energistics 2
    public static RegistryObject<Item> CERTUS_QUARTZ_CRYSTAL;
    public static RegistryObject<Item> CHARGED_CERTUS_QUARTZ_CRYSTAL;
    public static RegistryObject<Item> CERTUS_QUARTZ_DUST;

    public static void registerModData() {
        registerModItems(ModIds.ENDERIO, addItem -> GRAINS_OF_INFINITY = addItem.apply("grains_of_infinity"));
        registerModItems(ModIds.EXTREME_REACTORS, addItem -> YELLORIUM_DUST = addItem.apply("yellorium_dust"));
        registerModBlocks(ModIds.BIOMES_O_PLENTY, addBlock -> {
            FIR_PLANKS = addBlock.apply("fir_planks");
            REDWOOD_PLANKS = addBlock.apply("redwood_planks");
            MAHOGANY_PLANKS = addBlock.apply("mahogany_planks");
            JACARANDA_PLANKS = addBlock.apply("jacaranda_planks");
            PALM_PLANKS = addBlock.apply("palm_planks");
            WILLOW_PLANKS = addBlock.apply("willow_planks");
            DEAD_PLANKS = addBlock.apply("dead_planks");
            MAGIC_PLANKS = addBlock.apply("magic_planks");
            UMBRAN_PLANKS = addBlock.apply("umbran_planks");
            HELLBARK_PLANKS = addBlock.apply("hellbark_planks");
            FIR_LOG = addBlock.apply("fir_log");
            REDWOOD_LOG = addBlock.apply("redwood_log");
            MAHOGANY_LOG = addBlock.apply("mahogany_log");
            JACARANDA_LOG = addBlock.apply("jacaranda_log");
            PALM_LOG = addBlock.apply("palm_log");
            WILLOW_LOG = addBlock.apply("willow_log");
            DEAD_LOG = addBlock.apply("dead_log");
            MAGIC_LOG = addBlock.apply("magic_log");
            UMBRAN_LOG = addBlock.apply("umbran_log");
            HELLBARK_LOG = addBlock.apply("hellbark_log");
        });
        registerModItems(ModIds.BIOMES_O_PLENTY, addItem -> {
            ORIGIN_SAPLING = addItem.apply("origin_sapling");
            FLOWERING_OAK_SAPLING = addItem.apply("flowering_oak_sapling");
            SNOWBLOSSOM_SAPLING = addItem.apply("snowblossom_sapling");
            RAINBOW_BIRCH_SAPLING = addItem.apply("rainbow_birch_sapling");
            YELLOW_AUTUMN_SAPLING = addItem.apply("yellow_autumn_sapling");
            ORANGE_AUTUMN_SAPLING = addItem.apply("orange_autumn_sapling");
            MAPLE_SAPLING = addItem.apply("maple_sapling");
            FIR_SAPLING = addItem.apply("fir_sapling");
            REDWOOD_SAPLING = addItem.apply("redwood_sapling");
            MAHOGANY_SAPLING = addItem.apply("mahogany_sapling");
            JACARANDA_SAPLING = addItem.apply("jacaranda_sapling");
            PALM_SAPLING = addItem.apply("palm_sapling");
            WILLOW_SAPLING = addItem.apply("willow_sapling");
            DEAD_SAPLING = addItem.apply("dead_sapling");
            MAGIC_SAPLING = addItem.apply("magic_sapling");
            UMBRAN_SAPLING = addItem.apply("umbran_sapling");
            HELLBARK_SAPLING = addItem.apply("hellbark_sapling");
        });
        registerModItems(ModIds.APPLIED_ENERGISTICS_2, addItem -> {
            CERTUS_QUARTZ_CRYSTAL = addItem.apply("certus_quartz_crystal");
            CHARGED_CERTUS_QUARTZ_CRYSTAL = addItem.apply("charged_certus_quartz_crystal");
            CERTUS_QUARTZ_DUST = addItem.apply("certus_quartz_dust");
        });
    }

    private static void registerModItems(String modid, Consumer<Function<String, RegistryObject<Item>>> addItems) {
        var deferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());

        addItems.accept(name -> deferredRegister.register(name, () -> new Item(new Item.Properties())));
    }

    @SuppressWarnings("SameParameterValue")
    private static void registerModBlocks(String modid, Consumer<Function<String, RegistryObject<Block>>> addBlocks) {
        var deferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());

        addBlocks.accept(name -> deferredRegister.register(name, () -> new Block(BlockBehaviour.Properties.of())));
    }
}
