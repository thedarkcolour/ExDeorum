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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.modkit.data.MKBlockModelProvider;

import java.util.function.Supplier;

class BlockModels {
    public static void addBlockModels(MKBlockModelProvider models) {
        models.simpleBlock(EBlocks.DUST.get());
        models.simpleBlock(EBlocks.CRUSHED_NETHERRACK.get());
        models.simpleBlock(EBlocks.CRUSHED_END_STONE.get());
        models.simpleBlock(EBlocks.CRUSHED_DEEPSLATE.get());
        models.simpleBlock(EBlocks.CRUSHED_BLACKSTONE.get());

        // Barrels
        barrel(models, EBlocks.OAK_BARREL, Blocks.OAK_PLANKS);
        barrel(models, EBlocks.SPRUCE_BARREL, Blocks.SPRUCE_PLANKS);
        barrel(models, EBlocks.BIRCH_BARREL, Blocks.BIRCH_PLANKS);
        barrel(models, EBlocks.JUNGLE_BARREL, Blocks.JUNGLE_PLANKS);
        barrel(models, EBlocks.ACACIA_BARREL, Blocks.ACACIA_PLANKS);
        barrel(models, EBlocks.DARK_OAK_BARREL, Blocks.DARK_OAK_PLANKS);
        barrel(models, EBlocks.MANGROVE_BARREL, Blocks.MANGROVE_PLANKS);
        barrel(models, EBlocks.CHERRY_BARREL, Blocks.CHERRY_PLANKS);
        barrel(models, EBlocks.BAMBOO_BARREL, Blocks.BAMBOO_PLANKS);
        barrel(models, EBlocks.CRIMSON_BARREL, Blocks.CRIMSON_PLANKS);
        barrel(models, EBlocks.WARPED_BARREL, Blocks.WARPED_PLANKS);
        barrel(models, EBlocks.STONE_BARREL, Blocks.STONE);

        sieve(models, EBlocks.OAK_SIEVE.get(), Blocks.OAK_PLANKS);
        sieve(models, EBlocks.SPRUCE_SIEVE.get(), Blocks.SPRUCE_PLANKS);
        sieve(models, EBlocks.BIRCH_SIEVE.get(), Blocks.BIRCH_PLANKS);
        sieve(models, EBlocks.JUNGLE_SIEVE.get(), Blocks.JUNGLE_PLANKS);
        sieve(models, EBlocks.ACACIA_SIEVE.get(), Blocks.ACACIA_PLANKS);
        sieve(models, EBlocks.DARK_OAK_SIEVE.get(), Blocks.DARK_OAK_PLANKS);
        sieve(models, EBlocks.MANGROVE_SIEVE.get(), Blocks.MANGROVE_PLANKS);
        sieve(models, EBlocks.CHERRY_SIEVE.get(), Blocks.CHERRY_PLANKS);
        sieve(models, EBlocks.BAMBOO_SIEVE.get(), Blocks.BAMBOO_PLANKS);
        sieve(models, EBlocks.CRIMSON_SIEVE.get(), Blocks.CRIMSON_PLANKS);
        sieve(models, EBlocks.WARPED_SIEVE.get(), Blocks.WARPED_PLANKS);

        // Lava Crucible
        crucible(models, EBlocks.UNFIRED_PORCELAIN_CRUCIBLE.get());
        crucible(models, EBlocks.PORCELAIN_CRUCIBLE.get());
        crucible(models, EBlocks.CRIMSON_CRUCIBLE.get(), Blocks.CRIMSON_STEM);
        crucible(models, EBlocks.WARPED_CRUCIBLE.get(), Blocks.WARPED_STEM);

        // Water Crucible
        crucible(models, EBlocks.OAK_CRUCIBLE.get(), Blocks.OAK_LOG);
        crucible(models, EBlocks.SPRUCE_CRUCIBLE.get(), Blocks.SPRUCE_LOG);
        crucible(models, EBlocks.BIRCH_CRUCIBLE.get(), Blocks.BIRCH_LOG);
        crucible(models, EBlocks.JUNGLE_CRUCIBLE.get(), Blocks.JUNGLE_LOG);
        crucible(models, EBlocks.ACACIA_CRUCIBLE.get(), Blocks.ACACIA_LOG);
        crucible(models, EBlocks.DARK_OAK_CRUCIBLE.get(), Blocks.DARK_OAK_LOG);
        crucible(models, EBlocks.MANGROVE_CRUCIBLE.get(), Blocks.MANGROVE_LOG);
        crucible(models, EBlocks.CHERRY_CRUCIBLE.get(), Blocks.CHERRY_LOG);
        crucible(models, EBlocks.BAMBOO_CRUCIBLE.get(), Blocks.BAMBOO_BLOCK);

        // Mod compat
        bopModels(models);
        arsNouveauModels(models);
        aetherModels(models);
        blueSkiesModels(models);
    }

    private static void arsNouveauModels(MKBlockModelProvider models) {
        barrel(models, EBlocks.ARCHWOOD_BARREL, ModCompatData.ARCHWOOD_PLANKS.get());

        sieve(models, EBlocks.ARCHWOOD_SIEVE.get(), ModCompatData.ARCHWOOD_PLANKS.get());

        crucible(models, EBlocks.CASCADING_ARCHWOOD_CRUCIBLE.get(), ModCompatData.CASCADING_ARCHWOOD_LOG.get());
        crucible(models, EBlocks.BLAZING_ARCHWOOD_CRUCIBLE.get(), ModCompatData.BLAZING_ARCHWOOD_LOG.get());
        crucible(models, EBlocks.VEXING_ARCHWOOD_CRUCIBLE.get(), ModCompatData.VEXING_ARCHWOOD_LOG.get());
        crucible(models, EBlocks.FLOURISHING_ARCHWOOD_CRUCIBLE.get(), ModCompatData.FLOURISHING_ARCHWOOD_LOG.get());
    }

    private static void aetherModels(MKBlockModelProvider models) {
        barrel(models, EBlocks.SKYROOT_BARREL, ModCompatData.SKYROOT_PLANKS.get());

        sieve(models, EBlocks.SKYROOT_SIEVE.get(), ModCompatData.SKYROOT_PLANKS.get());

        crucible(models, EBlocks.SKYROOT_CRUCIBLE.get(), ModCompatData.SKYROOT_LOG.get());
        crucible(models, EBlocks.GOLDEN_OAK_CRUCIBLE.get(), ModCompatData.GOLDEN_OAK_LOG.get());
    }

    private static void blueSkiesModels(MKBlockModelProvider models) {
        barrel(models, EBlocks.BLUEBRIGHT_BARREL, ModCompatData.BLUEBRIGHT_PLANKS.get());
        barrel(models, EBlocks.STARLIT_BARREL, ModCompatData.STARLIT_PLANKS.get());
        barrel(models, EBlocks.FROSTBRIGHT_BARREL, ModCompatData.FROSTBRIGHT_PLANKS.get());
        barrel(models, EBlocks.COMET_BARREL, ModCompatData.COMET_PLANKS.get());
        barrel(models, EBlocks.LUNAR_BARREL, ModCompatData.LUNAR_PLANKS.get());
        barrel(models, EBlocks.DUSK_BARREL, ModCompatData.DUSK_PLANKS.get());
        barrel(models, EBlocks.MAPLE_BARREL, ModCompatData.MAPLE_PLANKS.get());
        barrel(models, EBlocks.CRYSTALLIZED_BARREL, ModCompatData.CRYSTALLIZED_PLANKS.get());

        barrel(models, EBlocks.BLUEBRIGHT_SIEVE.get(), ModCompatData.BLUEBRIGHT_PLANKS.get());
        barrel(models, EBlocks.STARLIT_SIEVE.get(), ModCompatData.STARLIT_PLANKS.get());
        barrel(models, EBlocks.FROSTBRIGHT_SIEVE.get(), ModCompatData.FROSTBRIGHT_PLANKS.get());
        barrel(models, EBlocks.COMET_SIEVE.get(), ModCompatData.COMET_PLANKS.get());
        barrel(models, EBlocks.LUNAR_SIEVE.get(), ModCompatData.LUNAR_PLANKS.get());
        barrel(models, EBlocks.DUSK_SIEVE.get(), ModCompatData.DUSK_PLANKS.get());
        barrel(models, EBlocks.MAPLE_SIEVE.get(), ModCompatData.MAPLE_PLANKS.get());
        barrel(models, EBlocks.CRYSTALLIZED_SIEVE.get(), ModCompatData.CRYSTALLIZED_PLANKS.get());

        crucible(models, EBlocks.BLUEBRIGHT_CRUCIBLE.get(), ModCompatData.BLUEBRIGHT_LOG.get());
        crucible(models, EBlocks.STARLIT_CRUCIBLE.get(), ModCompatData.STARLIT_LOG.get());
        crucible(models, EBlocks.FROSTBRIGHT_CRUCIBLE.get(), ModCompatData.FROSTBRIGHT_LOG.get());
        crucible(models, EBlocks.COMET_CRUCIBLE.get(), ModCompatData.COMET_LOG.get());
        crucible(models, EBlocks.LUNAR_CRUCIBLE.get(), ModCompatData.LUNAR_LOG.get());
        crucible(models, EBlocks.DUSK_CRUCIBLE.get(), ModCompatData.DUSK_LOG.get());
        crucible(models, EBlocks.MAPLE_CRUCIBLE.get(), ModCompatData.MAPLE_LOG.get());
        crucible(models, EBlocks.CRYSTALLIZED_CRUCIBLE.get(), ModCompatData.CRYSTALLIZED_LOG.get());
    }

    private static void bopModels(MKBlockModelProvider models) {
        barrel(models, EBlocks.FIR_BARREL, ModCompatData.FIR_PLANKS.get());
        barrel(models, EBlocks.REDWOOD_BARREL, ModCompatData.REDWOOD_PLANKS.get());
        barrel(models, EBlocks.MAHOGANY_BARREL, ModCompatData.MAHOGANY_PLANKS.get());
        barrel(models, EBlocks.JACARANDA_BARREL, ModCompatData.JACARANDA_PLANKS.get());
        barrel(models, EBlocks.PALM_BARREL, ModCompatData.PALM_PLANKS.get());
        barrel(models, EBlocks.WILLOW_BARREL, ModCompatData.WILLOW_PLANKS.get());
        barrel(models, EBlocks.DEAD_BARREL, ModCompatData.DEAD_PLANKS.get());
        barrel(models, EBlocks.MAGIC_BARREL, ModCompatData.MAGIC_PLANKS.get());
        barrel(models, EBlocks.UMBRAN_BARREL, ModCompatData.UMBRAN_PLANKS.get());
        barrel(models, EBlocks.HELLBARK_BARREL, ModCompatData.HELLBARK_PLANKS.get());

        sieve(models, EBlocks.FIR_SIEVE.get(), ModCompatData.FIR_PLANKS.get());
        sieve(models, EBlocks.REDWOOD_SIEVE.get(), ModCompatData.REDWOOD_PLANKS.get());
        sieve(models, EBlocks.MAHOGANY_SIEVE.get(), ModCompatData.MAHOGANY_PLANKS.get());
        sieve(models, EBlocks.JACARANDA_SIEVE.get(), ModCompatData.JACARANDA_PLANKS.get());
        sieve(models, EBlocks.PALM_SIEVE.get(), ModCompatData.PALM_PLANKS.get());
        sieve(models, EBlocks.WILLOW_SIEVE.get(), ModCompatData.WILLOW_PLANKS.get());
        sieve(models, EBlocks.DEAD_SIEVE.get(), ModCompatData.DEAD_PLANKS.get());
        sieve(models, EBlocks.MAGIC_SIEVE.get(), ModCompatData.MAGIC_PLANKS.get());
        sieve(models, EBlocks.UMBRAN_SIEVE.get(), ModCompatData.UMBRAN_PLANKS.get());
        sieve(models, EBlocks.HELLBARK_SIEVE.get(), ModCompatData.HELLBARK_PLANKS.get());

        crucible(models, EBlocks.FIR_CRUCIBLE.get(), ModCompatData.FIR_LOG.get());
        crucible(models, EBlocks.REDWOOD_CRUCIBLE.get(), ModCompatData.REDWOOD_LOG.get());
        crucible(models, EBlocks.MAHOGANY_CRUCIBLE.get(), ModCompatData.MAHOGANY_LOG.get());
        crucible(models, EBlocks.JACARANDA_CRUCIBLE.get(), ModCompatData.JACARANDA_LOG.get());
        crucible(models, EBlocks.PALM_CRUCIBLE.get(), ModCompatData.PALM_LOG.get());
        crucible(models, EBlocks.WILLOW_CRUCIBLE.get(), ModCompatData.WILLOW_LOG.get());
        crucible(models, EBlocks.DEAD_CRUCIBLE.get(), ModCompatData.DEAD_LOG.get());
        crucible(models, EBlocks.MAGIC_CRUCIBLE.get(), ModCompatData.MAGIC_LOG.get());
        crucible(models, EBlocks.UMBRAN_CRUCIBLE.get(), ModCompatData.UMBRAN_LOG.get());
        crucible(models, EBlocks.HELLBARK_CRUCIBLE.get(), ModCompatData.HELLBARK_LOG.get());
    }

    public static void crucible(MKBlockModelProvider models, Block block) {
        crucible(models, block, block);
    }

    public static void crucible(MKBlockModelProvider models, Block block, Block appearance) {
        var texture = models.blockTexture(appearance);

        singleModel(models, block)
                .parent(models.modFile("template_crucible"))
                .texture("inside", texture)
                .texture("top", texture)
                .texture("bottom", texture)
                .texture("side", texture);
    }

    public static void barrel(MKBlockModelProvider models, Supplier<? extends Block> block, Block appearance) {
        singleModel(models, block.get())
                .parent(models.modFile("template_barrel"))
                .texture("barrel", models.blockTexture(appearance));
    }

    public static void sieve(MKBlockModelProvider models, Block block, Block appearance) {
        singleModel(models, block)
                .parent(models.modFile("template_sieve"))
                .texture("texture", models.blockTexture(appearance));
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
