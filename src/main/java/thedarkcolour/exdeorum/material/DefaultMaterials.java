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

package thedarkcolour.exdeorum.material;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;

@SuppressWarnings("unused")
public class DefaultMaterials {
    public static final MaterialRegistry<BarrelMaterial> BARRELS = new MaterialRegistry<>("barrel");
    public static final MaterialRegistry<SieveMaterial> SIEVES = new MaterialRegistry<>("sieve");

    // Ex Deorum
    public static final BarrelMaterial OAK_BARREL = addDefaultWoodBarrel("oak", SoundType.WOOD, false, MapColor.WOOD, ExDeorum.ID);
    public static final BarrelMaterial SPRUCE_BARREL = addDefaultWoodBarrel("spruce", SoundType.WOOD, false, MapColor.PODZOL, ExDeorum.ID);
    public static final BarrelMaterial BIRCH_BARREL = addDefaultWoodBarrel("birch", SoundType.WOOD, false, MapColor.SAND, ExDeorum.ID);
    public static final BarrelMaterial JUNGLE_BARREL = addDefaultWoodBarrel("jungle", SoundType.WOOD, false, MapColor.DIRT, ExDeorum.ID);
    public static final BarrelMaterial ACACIA_BARREL = addDefaultWoodBarrel("acacia", SoundType.WOOD, false, MapColor.COLOR_ORANGE, ExDeorum.ID);
    public static final BarrelMaterial DARK_OAK_BARREL = addDefaultWoodBarrel("dark_oak", SoundType.WOOD, false, MapColor.COLOR_BROWN, ExDeorum.ID);
    public static final BarrelMaterial MANGROVE_BARREL = addDefaultWoodBarrel("mangrove", SoundType.WOOD, false, MapColor.COLOR_RED, ExDeorum.ID);
    public static final BarrelMaterial CHERRY_BARREL = addDefaultWoodBarrel("cherry", SoundType.CHERRY_WOOD, false, MapColor.TERRACOTTA_WHITE, ExDeorum.ID);
    public static final BarrelMaterial BAMBOO_BARREL = addDefaultWoodBarrel("bamboo", SoundType.BAMBOO_WOOD, false, MapColor.COLOR_YELLOW, ExDeorum.ID);
    public static final BarrelMaterial CRIMSON_BARREL = addDefaultWoodBarrel("crimson", SoundType.NETHER_WOOD, true, MapColor.CRIMSON_STEM, ExDeorum.ID);
    public static final BarrelMaterial WARPED_BARREL = addDefaultWoodBarrel("warped", SoundType.NETHER_WOOD, true, MapColor.WARPED_STEM, ExDeorum.ID);
    public static final BarrelMaterial STONE_BARREL = addDefaultBarrel("stone", SoundType.STONE, 4.0f, true, true, MapColor.STONE, ExDeorum.ID, false);
    // BOP
    public static final BarrelMaterial FIR_BARREL = addDefaultWoodBarrel("fir", SoundType.WOOD, false, MapColor.TERRACOTTA_WHITE, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial REDWOOD_BARREL = addDefaultWoodBarrel("redwood", SoundType.WOOD, false, MapColor.TERRACOTTA_ORANGE, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial MAHOGANY_BARREL = addDefaultWoodBarrel("mahogany", SoundType.WOOD, false, MapColor.TERRACOTTA_PINK, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial JACARANDA_BARREL = addDefaultWoodBarrel("jacaranda", SoundType.WOOD, false, MapColor.QUARTZ, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial PALM_BARREL = addDefaultWoodBarrel("palm", SoundType.WOOD, false, MapColor.TERRACOTTA_YELLOW, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial WILLOW_BARREL = addDefaultWoodBarrel("willow", SoundType.WOOD, false, MapColor.TERRACOTTA_LIGHT_GREEN, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial DEAD_BARREL = addDefaultWoodBarrel("dead", SoundType.WOOD, false, MapColor.STONE, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial MAGIC_BARREL = addDefaultWoodBarrel("magic", SoundType.WOOD, false, MapColor.COLOR_BLUE, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial UMBRAN_BARREL = addDefaultWoodBarrel("umbran", SoundType.WOOD, false, MapColor.TERRACOTTA_BLUE, ModIds.BIOMES_O_PLENTY);
    public static final BarrelMaterial HELLBARK_BARREL = addDefaultWoodBarrel("hellbark", SoundType.WOOD, true, MapColor.TERRACOTTA_GRAY, ModIds.BIOMES_O_PLENTY);
    // Ars Nouveau
    public static final BarrelMaterial ARCHWOOD_BARREL = addDefaultWoodBarrel("archwood", SoundType.WOOD, false, MapColor.COLOR_GRAY, ModIds.ARS_NOUVEAU);
    // Aether
    public static final BarrelMaterial SKYROOT_BARREL = addDefaultWoodBarrel("skyroot", SoundType.WOOD, false, MapColor.WOOD, ModIds.AETHER);
    // Blue Skies
    public static final BarrelMaterial BLUEBRIGHT_BARREL = addDefaultWoodBarrel("bluebright", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial STARLIT_BARREL = addDefaultWoodBarrel("starlit", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial FROSTBRIGHT_BARREL = addDefaultWoodBarrel("frostbright", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial COMET_BARREL = addDefaultWoodBarrel("comet", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial LUNAR_BARREL = addDefaultWoodBarrel("lunar", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial DUSK_BARREL = addDefaultWoodBarrel("dusk", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial MAPLE_BARREL = addDefaultWoodBarrel("maple", SoundType.WOOD, false, MapColor.WOOD, ModIds.BLUE_SKIES);
    public static final BarrelMaterial CRYSTALLIZED_BARREL = addDefaultBarrel("crystallized", SoundType.GLASS, 4.0f, true, true, MapColor.TERRACOTTA_WHITE, ModIds.BLUE_SKIES, true);

    // Ex Deorum
    public static final SieveMaterial OAK_SIEVE = addDefaultSieve("oak", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial SPRUCE_SIEVE = addDefaultSieve("spruce", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial BIRCH_SIEVE = addDefaultSieve("birch", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial JUNGLE_SIEVE = addDefaultSieve("jungle", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial ACACIA_SIEVE = addDefaultSieve("acacia", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial DARK_OAK_SIEVE = addDefaultSieve("dark_oak", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial MANGROVE_SIEVE = addDefaultSieve("mangrove", SoundType.WOOD, ExDeorum.ID);
    public static final SieveMaterial CHERRY_SIEVE = addDefaultSieve("cherry", SoundType.CHERRY_WOOD, ExDeorum.ID);
    public static final SieveMaterial BAMBOO_SIEVE = addDefaultSieve("bamboo", SoundType.BAMBOO_WOOD, ExDeorum.ID);
    public static final SieveMaterial CRIMSON_SIEVE = addDefaultSieve("crimson", SoundType.NETHER_WOOD, ExDeorum.ID);
    public static final SieveMaterial WARPED_SIEVE = addDefaultSieve("warped", SoundType.NETHER_WOOD, ExDeorum.ID);
    // Biomes O' Plenty
    public static final SieveMaterial FIR_SIEVE = addDefaultSieve("fir", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial REDWOOD_SIEVE = addDefaultSieve("redwood", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial MAHOGANY_SIEVE = addDefaultSieve("mahogany", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial JACARANDA_SIEVE = addDefaultSieve("jacaranda", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial PALM_SIEVE = addDefaultSieve("palm", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial WILLOW_SIEVE = addDefaultSieve("willow", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial DEAD_SIEVE = addDefaultSieve("dead", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial MAGIC_SIEVE = addDefaultSieve("magic", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial UMBRAN_SIEVE = addDefaultSieve("umbran", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    public static final SieveMaterial HELLBARK_SIEVE = addDefaultSieve("hellbark", SoundType.WOOD, ModIds.BIOMES_O_PLENTY);
    // Ars Nouveau
    public static final SieveMaterial ARCHWOOD_SIEVE = addDefaultSieve("archwood", SoundType.WOOD, ModIds.ARS_NOUVEAU);
    // Aether
    public static final SieveMaterial SKYROOT_SIEVE = addDefaultSieve("skyroot", SoundType.WOOD, ModIds.AETHER);
    // Blue Skies
    public static final SieveMaterial BLUEBRIGHT_SIEVE = addDefaultSieve("bluebright", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial STARLIT_SIEVE = addDefaultSieve("starlit", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial FROSTBRIGHT_SIEVE = addDefaultSieve("frostbright", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial COMET_SIEVE = addDefaultSieve("comet", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial LUNAR_SIEVE = addDefaultSieve("lunar", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial DUSK_SIEVE = addDefaultSieve("dusk", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial MAPLE_SIEVE = addDefaultSieve("maple", SoundType.WOOD, ModIds.BLUE_SKIES);
    public static final SieveMaterial CRYSTALLIZED_SIEVE = addDefaultSieve("crystallized", SoundType.GLASS, true, ModIds.BLUE_SKIES);

    private static BarrelMaterial addDefaultWoodBarrel(String name, SoundType soundType, boolean fireproof, MapColor color, String requiredModId) {
        return addDefaultBarrel(name, soundType, 2.0f, false, fireproof, color, requiredModId, false);
    }

    private static BarrelMaterial addDefaultBarrel(String name, SoundType soundType, float strength, boolean needsCorrectTool, boolean fireproof, MapColor color, String requiredModId, boolean transparent) {
        var material = new BarrelMaterial(soundType, strength, needsCorrectTool, fireproof, color.id, requiredModId, transparent);
        BARRELS.register(name, material);
        return material;
    }

    private static SieveMaterial addDefaultSieve(String name, SoundType soundType, String requiredModId) {
        return addDefaultSieve(name, soundType, false, requiredModId);
    }

    private static SieveMaterial addDefaultSieve(String name, SoundType soundType, boolean needsCorrectTool, String requiredModID) {
        var material = new SieveMaterial(soundType, 2.0f, needsCorrectTool, requiredModID);
        SIEVES.register(name, material);
        return material;
    }

    // This call initializes the DefaultMaterials fields as well as searching for user-defined ones in the appropriate places
    public static void registerMaterials() {
        BARRELS.search(BarrelMaterial::readFromJson);
        SIEVES.search(SieveMaterial::readFromJson);
    }
}
