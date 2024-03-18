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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.modkit.data.MKBlockModelProvider;

import java.util.Objects;

class BlockModels {
    public static void addBlockModels(MKBlockModelProvider models) {
        models.simpleBlock(EBlocks.DUST.get());
        models.simpleBlock(EBlocks.CRUSHED_NETHERRACK.get());
        models.simpleBlock(EBlocks.CRUSHED_END_STONE.get());
        models.simpleBlock(EBlocks.CRUSHED_DEEPSLATE.get());
        models.simpleBlock(EBlocks.CRUSHED_BLACKSTONE.get());

        // Barrels
        barrel(models, DefaultMaterials.OAK_BARREL.getBlock(), Blocks.OAK_PLANKS);
        barrel(models, DefaultMaterials.SPRUCE_BARREL.getBlock(), Blocks.SPRUCE_PLANKS);
        barrel(models, DefaultMaterials.BIRCH_BARREL.getBlock(), Blocks.BIRCH_PLANKS);
        barrel(models, DefaultMaterials.JUNGLE_BARREL.getBlock(), Blocks.JUNGLE_PLANKS);
        barrel(models, DefaultMaterials.ACACIA_BARREL.getBlock(), Blocks.ACACIA_PLANKS);
        barrel(models, DefaultMaterials.DARK_OAK_BARREL.getBlock(), Blocks.DARK_OAK_PLANKS);
        barrel(models, DefaultMaterials.MANGROVE_BARREL.getBlock(), Blocks.MANGROVE_PLANKS);
        barrel(models, DefaultMaterials.CHERRY_BARREL.getBlock(), Blocks.CHERRY_PLANKS);
        barrel(models, DefaultMaterials.BAMBOO_BARREL.getBlock(), Blocks.BAMBOO_PLANKS);
        barrel(models, DefaultMaterials.CRIMSON_BARREL.getBlock(), Blocks.CRIMSON_PLANKS);
        barrel(models, DefaultMaterials.WARPED_BARREL.getBlock(), Blocks.WARPED_PLANKS);
        barrel(models, DefaultMaterials.STONE_BARREL.getBlock(), Blocks.STONE);

        sieve(models, DefaultMaterials.OAK_SIEVE.getBlock(), Blocks.OAK_PLANKS);
        sieve(models, DefaultMaterials.SPRUCE_SIEVE.getBlock(), Blocks.SPRUCE_PLANKS);
        sieve(models, DefaultMaterials.BIRCH_SIEVE.getBlock(), Blocks.BIRCH_PLANKS);
        sieve(models, DefaultMaterials.JUNGLE_SIEVE.getBlock(), Blocks.JUNGLE_PLANKS);
        sieve(models, DefaultMaterials.ACACIA_SIEVE.getBlock(), Blocks.ACACIA_PLANKS);
        sieve(models, DefaultMaterials.DARK_OAK_SIEVE.getBlock(), Blocks.DARK_OAK_PLANKS);
        sieve(models, DefaultMaterials.MANGROVE_SIEVE.getBlock(), Blocks.MANGROVE_PLANKS);
        sieve(models, DefaultMaterials.CHERRY_SIEVE.getBlock(), Blocks.CHERRY_PLANKS);
        sieve(models, DefaultMaterials.BAMBOO_SIEVE.getBlock(), Blocks.BAMBOO_PLANKS);
        sieve(models, DefaultMaterials.CRIMSON_SIEVE.getBlock(), Blocks.CRIMSON_PLANKS);
        sieve(models, DefaultMaterials.WARPED_SIEVE.getBlock(), Blocks.WARPED_PLANKS);

        // Lava Crucible
        crucible(models, EBlocks.UNFIRED_PORCELAIN_CRUCIBLE.get());
        crucible(models, DefaultMaterials.PORCELAIN_CRUCIBLE.getBlock());
        crucible(models, DefaultMaterials.CRIMSON_CRUCIBLE.getBlock(), Blocks.CRIMSON_STEM);
        crucible(models, DefaultMaterials.WARPED_CRUCIBLE.getBlock(), Blocks.WARPED_STEM);

        // Water Crucible
        crucible(models, DefaultMaterials.OAK_CRUCIBLE.getBlock(), Blocks.OAK_LOG);
        crucible(models, DefaultMaterials.SPRUCE_CRUCIBLE.getBlock(), Blocks.SPRUCE_LOG);
        crucible(models, DefaultMaterials.BIRCH_CRUCIBLE.getBlock(), Blocks.BIRCH_LOG);
        crucible(models, DefaultMaterials.JUNGLE_CRUCIBLE.getBlock(), Blocks.JUNGLE_LOG);
        crucible(models, DefaultMaterials.ACACIA_CRUCIBLE.getBlock(), Blocks.ACACIA_LOG);
        crucible(models, DefaultMaterials.DARK_OAK_CRUCIBLE.getBlock(), Blocks.DARK_OAK_LOG);
        crucible(models, DefaultMaterials.MANGROVE_CRUCIBLE.getBlock(), Blocks.MANGROVE_LOG);
        crucible(models, DefaultMaterials.CHERRY_CRUCIBLE.getBlock(), Blocks.CHERRY_LOG);
        crucible(models, DefaultMaterials.BAMBOO_CRUCIBLE.getBlock(), Blocks.BAMBOO_BLOCK);

        // Mod compat
        bopModels(models);
        arsNouveauModels(models);
        aetherModels(models);
        blueSkiesModels(models);
    }

    private static void arsNouveauModels(MKBlockModelProvider models) {
        barrel(models, DefaultMaterials.ARCHWOOD_BARREL.getBlock(), ModCompatData.ARCHWOOD_PLANKS.get());

        sieve(models, DefaultMaterials.ARCHWOOD_SIEVE.getBlock(), ModCompatData.ARCHWOOD_PLANKS.get());

        crucible(models, DefaultMaterials.CASCADING_ARCHWOOD_CRUCIBLE.getBlock(), ModCompatData.CASCADING_ARCHWOOD_LOG.get());
        crucible(models, DefaultMaterials.BLAZING_ARCHWOOD_CRUCIBLE.getBlock(), ModCompatData.BLAZING_ARCHWOOD_LOG.get());
        crucible(models, DefaultMaterials.VEXING_ARCHWOOD_CRUCIBLE.getBlock(), ModCompatData.VEXING_ARCHWOOD_LOG.get());
        crucible(models, DefaultMaterials.FLOURISHING_ARCHWOOD_CRUCIBLE.getBlock(), ModCompatData.FLOURISHING_ARCHWOOD_LOG.get());
    }

    private static void aetherModels(MKBlockModelProvider models) {
        barrel(models, DefaultMaterials.SKYROOT_BARREL.getBlock(), ModCompatData.SKYROOT_PLANKS.get(), "construction/");

        sieve(models, DefaultMaterials.SKYROOT_SIEVE.getBlock(), ModCompatData.SKYROOT_PLANKS.get(), "construction/");

        crucible(models, DefaultMaterials.SKYROOT_CRUCIBLE.getBlock(), ModCompatData.SKYROOT_LOG.get(), "natural/", "");
        crucible(models, DefaultMaterials.GOLDEN_OAK_CRUCIBLE.getBlock(), ModCompatData.GOLDEN_OAK_LOG.get(), "natural/", "");
    }

    private static void blueSkiesModels(MKBlockModelProvider models) {
        final String woodPrefix = "wood/";
        final String logSuffix = "_side";

        barrel(models, DefaultMaterials.BLUEBRIGHT_BARREL.getBlock(), ModCompatData.BLUEBRIGHT_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.STARLIT_BARREL.getBlock(), ModCompatData.STARLIT_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.FROSTBRIGHT_BARREL.getBlock(), ModCompatData.FROSTBRIGHT_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.COMET_BARREL.getBlock(), ModCompatData.COMET_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.LUNAR_BARREL.getBlock(), ModCompatData.LUNAR_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.DUSK_BARREL.getBlock(), ModCompatData.DUSK_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.MAPLE_BARREL.getBlock(), ModCompatData.MAPLE_PLANKS.get(), woodPrefix);
        barrel(models, DefaultMaterials.CRYSTALLIZED_BARREL.getBlock(), ModCompatData.CRYSTALLIZED_PLANKS.get(), woodPrefix).renderType("translucent");

        sieve(models, DefaultMaterials.BLUEBRIGHT_SIEVE.getBlock(), ModCompatData.BLUEBRIGHT_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.STARLIT_SIEVE.getBlock(), ModCompatData.STARLIT_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.FROSTBRIGHT_SIEVE.getBlock(), ModCompatData.FROSTBRIGHT_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.COMET_SIEVE.getBlock(), ModCompatData.COMET_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.LUNAR_SIEVE.getBlock(), ModCompatData.LUNAR_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.DUSK_SIEVE.getBlock(), ModCompatData.DUSK_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.MAPLE_SIEVE.getBlock(), ModCompatData.MAPLE_PLANKS.get(), woodPrefix);
        sieve(models, DefaultMaterials.CRYSTALLIZED_SIEVE.getBlock(), ModCompatData.CRYSTALLIZED_PLANKS.get(), woodPrefix).renderType("translucent");

        crucible(models, DefaultMaterials.BLUEBRIGHT_CRUCIBLE.getBlock(), ModCompatData.BLUEBRIGHT_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.STARLIT_CRUCIBLE.getBlock(), ModCompatData.STARLIT_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.FROSTBRIGHT_CRUCIBLE.getBlock(), ModCompatData.FROSTBRIGHT_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.COMET_CRUCIBLE.getBlock(), ModCompatData.COMET_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.LUNAR_CRUCIBLE.getBlock(), ModCompatData.LUNAR_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.DUSK_CRUCIBLE.getBlock(), ModCompatData.DUSK_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.MAPLE_CRUCIBLE.getBlock(), ModCompatData.MAPLE_LOG.get(), woodPrefix, logSuffix);
        crucible(models, DefaultMaterials.CRYSTALLIZED_CRUCIBLE.getBlock(), ModCompatData.CRYSTALLIZED_LOG.get(), woodPrefix, logSuffix).renderType("translucent");
    }

    private static void bopModels(MKBlockModelProvider models) {
        barrel(models, DefaultMaterials.FIR_BARREL.getBlock(), ModCompatData.FIR_PLANKS.get());
        barrel(models, DefaultMaterials.REDWOOD_BARREL.getBlock(), ModCompatData.REDWOOD_PLANKS.get());
        barrel(models, DefaultMaterials.MAHOGANY_BARREL.getBlock(), ModCompatData.MAHOGANY_PLANKS.get());
        barrel(models, DefaultMaterials.JACARANDA_BARREL.getBlock(), ModCompatData.JACARANDA_PLANKS.get());
        barrel(models, DefaultMaterials.PALM_BARREL.getBlock(), ModCompatData.PALM_PLANKS.get());
        barrel(models, DefaultMaterials.WILLOW_BARREL.getBlock(), ModCompatData.WILLOW_PLANKS.get());
        barrel(models, DefaultMaterials.DEAD_BARREL.getBlock(), ModCompatData.DEAD_PLANKS.get());
        barrel(models, DefaultMaterials.MAGIC_BARREL.getBlock(), ModCompatData.MAGIC_PLANKS.get());
        barrel(models, DefaultMaterials.UMBRAN_BARREL.getBlock(), ModCompatData.UMBRAN_PLANKS.get());
        barrel(models, DefaultMaterials.HELLBARK_BARREL.getBlock(), ModCompatData.HELLBARK_PLANKS.get());

        sieve(models, DefaultMaterials.FIR_SIEVE.getBlock(), ModCompatData.FIR_PLANKS.get());
        sieve(models, DefaultMaterials.REDWOOD_SIEVE.getBlock(), ModCompatData.REDWOOD_PLANKS.get());
        sieve(models, DefaultMaterials.MAHOGANY_SIEVE.getBlock(), ModCompatData.MAHOGANY_PLANKS.get());
        sieve(models, DefaultMaterials.JACARANDA_SIEVE.getBlock(), ModCompatData.JACARANDA_PLANKS.get());
        sieve(models, DefaultMaterials.PALM_SIEVE.getBlock(), ModCompatData.PALM_PLANKS.get());
        sieve(models, DefaultMaterials.WILLOW_SIEVE.getBlock(), ModCompatData.WILLOW_PLANKS.get());
        sieve(models, DefaultMaterials.DEAD_SIEVE.getBlock(), ModCompatData.DEAD_PLANKS.get());
        sieve(models, DefaultMaterials.MAGIC_SIEVE.getBlock(), ModCompatData.MAGIC_PLANKS.get());
        sieve(models, DefaultMaterials.UMBRAN_SIEVE.getBlock(), ModCompatData.UMBRAN_PLANKS.get());
        sieve(models, DefaultMaterials.HELLBARK_SIEVE.getBlock(), ModCompatData.HELLBARK_PLANKS.get());

        crucible(models, DefaultMaterials.FIR_CRUCIBLE.getBlock(), ModCompatData.FIR_LOG.get());
        crucible(models, DefaultMaterials.REDWOOD_CRUCIBLE.getBlock(), ModCompatData.REDWOOD_LOG.get());
        crucible(models, DefaultMaterials.MAHOGANY_CRUCIBLE.getBlock(), ModCompatData.MAHOGANY_LOG.get());
        crucible(models, DefaultMaterials.JACARANDA_CRUCIBLE.getBlock(), ModCompatData.JACARANDA_LOG.get());
        crucible(models, DefaultMaterials.PALM_CRUCIBLE.getBlock(), ModCompatData.PALM_LOG.get());
        crucible(models, DefaultMaterials.WILLOW_CRUCIBLE.getBlock(), ModCompatData.WILLOW_LOG.get());
        crucible(models, DefaultMaterials.DEAD_CRUCIBLE.getBlock(), ModCompatData.DEAD_LOG.get());
        crucible(models, DefaultMaterials.MAGIC_CRUCIBLE.getBlock(), ModCompatData.MAGIC_LOG.get());
        crucible(models, DefaultMaterials.UMBRAN_CRUCIBLE.getBlock(), ModCompatData.UMBRAN_LOG.get());
        crucible(models, DefaultMaterials.HELLBARK_CRUCIBLE.getBlock(), ModCompatData.HELLBARK_LOG.get());
    }

    // Only used in Ex Deorum
    public static void crucible(MKBlockModelProvider models, Block block) {
        crucible(models, block, block);
    }

    public static void crucible(MKBlockModelProvider models, Block block, Block appearance) {
        crucible(models, block, appearance, "", "");
    }

    public static BlockModelBuilder crucible(MKBlockModelProvider models, Block block, Block appearance, String pathPrefix, String pathSuffix) {
        var texture = texture(appearance, pathPrefix, pathSuffix);

        return singleModel(models, block)
                .parent(models.modFile("template_crucible"))
                .texture("inside", texture)
                .texture("top", texture)
                .texture("bottom", texture)
                .texture("side", texture);
    }

    private static ResourceLocation texture(Block block, String prefix, String suffix) {
        var key = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
        return new ResourceLocation(key.getNamespace(), "block/" + prefix + key.getPath() + suffix);
    }

    public static void barrel(MKBlockModelProvider models, Block block, Block appearance) {
        barrel(models, block, appearance, "");
    }

    public static BlockModelBuilder barrel(MKBlockModelProvider models, Block block, Block appearance, String pathPrefix) {
        return singleModel(models, block)
                .parent(models.modFile("template_barrel"))
                .texture("barrel", texture(appearance, pathPrefix, ""));
    }

    public static void sieve(MKBlockModelProvider models, Block block, Block appearance) {
        sieve(models, block, appearance, "");
    }

    public static BlockModelBuilder sieve(MKBlockModelProvider models, Block block, Block appearance, String pathPrefix) {
        return singleModel(models, block)
                .parent(models.modFile("template_sieve"))
                .texture("texture", texture(appearance, pathPrefix, ""));
    }

    public static BlockModelBuilder singleModel(MKBlockModelProvider models, Block block) {
        BlockModelBuilder builder = blockModel(models, block);

        models.getVariantBuilder(block).partialState().addModels(new ConfiguredModel(builder));

        return builder;
    }

    public static BlockModelBuilder blockModel(MKBlockModelProvider models, Block block) {
        return models.models().getBuilder(models.name(block));
    }
}
