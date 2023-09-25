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

package thedarkcolour.exdeorum.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.HashMap;
import java.util.Map;

public class EBiomeTags {
    public static final Map<TagKey<Biome>, RegistryObject<ConfiguredFeature<?, ?>>> TREE_TAGS = new HashMap<>();

    // Vanilla
    public static final TagKey<Biome> OAK_TREE_BIOMES = addTreeTag("oak_tree_biomes", TreeFeatures.OAK_BEES_005.location());
    public static final TagKey<Biome> SPRUCE_TREE_BIOMES = addTreeTag("spruce_tree_biomes", TreeFeatures.SPRUCE.location());
    public static final TagKey<Biome> BIRCH_TREE_BIOMES = addTreeTag("birch_tree_biomes", TreeFeatures.BIRCH_BEES_002.location());
    public static final TagKey<Biome> JUNGLE_TREE_BIOMES = addTreeTag("jungle_tree_biomes", TreeFeatures.JUNGLE_TREE_NO_VINE.location());
    public static final TagKey<Biome> ACACIA_TREE_BIOMES = addTreeTag("acacia_tree_biomes", TreeFeatures.ACACIA.location());
    public static final TagKey<Biome> CHERRY_TREE_BIOMES = addTreeTag("cherry_tree_biomes", TreeFeatures.CHERRY_BEES_005.location());
    public static final TagKey<Biome> DARK_OAK_TREE_BIOMES = addTreeTag("dark_oak_tree_biomes", TreeFeatures.DARK_OAK.location());
    public static final TagKey<Biome> MANGROVE_TREE_BIOMES = addTreeTag("mangrove_tree_biomes", TreeFeatures.MANGROVE.location());

    // Bop tags
    public static final TagKey<Biome> FLOWERING_OAK_TREE_BIOMES = addTreeTag("flowering_oak_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "flowering_oak_tree_bees"));
    public static final TagKey<Biome> MAHOGANY_TREE_BIOMES = addTreeTag("mahogany_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "mahogany_tree"));
    public static final TagKey<Biome> JACARANDA_TREE_BIOMES = addTreeTag("jacaranda_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "jacaranda_tree_bees"));
    public static final TagKey<Biome> PALM_TREE_BIOMES = addTreeTag("palm_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "palm_tree"));
    public static final TagKey<Biome> WILLOW_TREE_BIOMES = addTreeTag("willow_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "willow_tree"));
    public static final TagKey<Biome> DEAD_TREE_BIOMES = addTreeTag("dead_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "dead_tree_wasteland"));
    public static final TagKey<Biome> MAGIC_TREE_BIOMES = addTreeTag("magic_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "magic_tree"));
    public static final TagKey<Biome> UMBRAN_TREE_BIOMES = addTreeTag("umbran_tree_biomes", new ResourceLocation(ModIds.BIOMES_O_PLENTY, "umbran_tree"));

    public static TagKey<Biome> addTreeTag(String tagName, ResourceLocation id) {
        var tag = tag(tagName);
        if (TREE_TAGS.put(tag, RegistryObject.createOptional(id, Registries.CONFIGURED_FEATURE, ExDeorum.ID)) != null) {
            throw new IllegalStateException("Already added a tree tag under " + tag);
        }
        return tag;
    }

    public static TagKey<Biome> tag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(ExDeorum.ID, name));
    }
}
