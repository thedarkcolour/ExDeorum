package thedarkcolour.exnihiloreborn.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.modkit.data.MKBlockModelProvider;

class BlockModels {
    public static void addBlockModels(MKBlockModelProvider models) {
        models.simpleBlock(EBlocks.DUST.get());
        models.simpleBlock(EBlocks.CRUSHED_NETHERRACK.get());
        models.simpleBlock(EBlocks.CRUSHED_END_STONE.get());

        // Barrels
        barrel(models, EBlocks.OAK_BARREL.get(), Blocks.OAK_PLANKS);
        barrel(models, EBlocks.SPRUCE_BARREL.get(), Blocks.SPRUCE_PLANKS);
        barrel(models, EBlocks.BIRCH_BARREL.get(), Blocks.BIRCH_PLANKS);
        barrel(models, EBlocks.JUNGLE_BARREL.get(), Blocks.JUNGLE_PLANKS);
        barrel(models, EBlocks.ACACIA_BARREL.get(), Blocks.ACACIA_PLANKS);
        barrel(models, EBlocks.DARK_OAK_BARREL.get(), Blocks.DARK_OAK_PLANKS);
        barrel(models, EBlocks.MANGROVE_BARREL.get(), Blocks.MANGROVE_PLANKS);
        barrel(models, EBlocks.CHERRY_BARREL.get(), Blocks.CHERRY_PLANKS);
        barrel(models, EBlocks.BAMBOO_BARREL.get(), Blocks.BAMBOO_PLANKS);
        barrel(models, EBlocks.CRIMSON_BARREL.get(), Blocks.CRIMSON_PLANKS);
        barrel(models, EBlocks.WARPED_BARREL.get(), Blocks.WARPED_PLANKS);
        barrel(models, EBlocks.STONE_BARREL.get(), Blocks.STONE);

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
        crucible(models, EBlocks.UNFIRED_CRUCIBLE.get());
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

    public static void barrel(MKBlockModelProvider models, Block block, Block appearance) {
        singleModel(models, block)
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
