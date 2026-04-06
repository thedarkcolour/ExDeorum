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

package thedarkcolour.exdeorum.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.registries.DeferredHolder;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.HashMap;
import java.util.Map;

public class EBiomeTags {
    public static final Map<TagKey<Biome>, DeferredHolder<ConfiguredFeature<?, ?>, ConfiguredFeature<?, ?>>> TREE_TAGS = new HashMap<>();

    static {
        // Vanilla
        addTreeTag("oak_tree_biomes", TreeFeatures.OAK_BEES_005.identifier());
        addTreeTag("spruce_tree_biomes", TreeFeatures.SPRUCE.identifier());
        addTreeTag("birch_tree_biomes", TreeFeatures.BIRCH_BEES_002.identifier());
        addTreeTag("jungle_tree_biomes", TreeFeatures.JUNGLE_TREE_NO_VINE.identifier());
        addTreeTag("acacia_tree_biomes", TreeFeatures.ACACIA.identifier());
        addTreeTag("cherry_tree_biomes", TreeFeatures.CHERRY_BEES_005.identifier());
        addTreeTag("dark_oak_tree_biomes", TreeFeatures.DARK_OAK.identifier());
        addTreeTag("mangrove_tree_biomes", TreeFeatures.MANGROVE.identifier());
        // Bop tags
        addTreeTag("flowering_oak_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "flowering_oak_tree_bees"));
        addTreeTag("mahogany_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "mahogany_tree"));
        addTreeTag("jacaranda_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "jacaranda_tree_bees"));
        addTreeTag("palm_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "palm_tree"));
        addTreeTag("willow_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "willow_tree"));
        addTreeTag("dead_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "dead_tree_wasteland"));
        addTreeTag("magic_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "magic_tree"));
        addTreeTag("umbran_tree_biomes", Identifier.fromNamespaceAndPath(ModIds.BIOMES_O_PLENTY, "umbran_tree"));
    }

    private static void addTreeTag(String tagName, Identifier id) {
        var tag = tag(tagName);
        if (TREE_TAGS.put(tag, DeferredHolder.create(Registries.CONFIGURED_FEATURE, id)) != null) {
            throw new IllegalStateException("Already added a tree tag under " + tag);
        }
    }

    private static TagKey<Biome> tag(String name) {
        return TagKey.create(Registries.BIOME, ExDeorum.loc(name));
    }
}
