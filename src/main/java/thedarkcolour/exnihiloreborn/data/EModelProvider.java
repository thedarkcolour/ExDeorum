package thedarkcolour.exnihiloreborn.data;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EBlocks;
import thedarkcolour.exnihiloreborn.registry.EItems;

public class EModelProvider extends BlockStateProvider {
    public EModelProvider(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerBlockModels();
        registerItemModels();
    }

    public void registerBlockModels() {
        // Materials
        simpleCubeAll(EBlocks.DUST.get());
        simpleCubeAll(EBlocks.CRUSHED_NETHERRACK.get());
        simpleCubeAll(EBlocks.CRUSHED_END_STONE.get());

        // Compressed Blocks
        //simpleCubeAll(EBlocks.COMPRESSED_COBBLESTONE.get());
        //simpleCubeAll(EBlocks.COMPRESSED_DIRT.get());
        //simpleCubeAll(EBlocks.COMPRESSED_SAND.get());
        //simpleCubeAll(EBlocks.COMPRESSED_DUST.get());

        //simpleCubeAll(EBlocks.COMPRESSED_CRUSHED_NETHERRACK.get());
        //simpleCubeAll(EBlocks.COMPRESSED_CRUSHED_END_STONE.get());

        // Barrels
        barrel(EBlocks.OAK_BARREL.get(), Blocks.OAK_PLANKS);
        barrel(EBlocks.SPRUCE_BARREL.get(), Blocks.SPRUCE_PLANKS);
        barrel(EBlocks.BIRCH_BARREL.get(), Blocks.BIRCH_PLANKS);
        barrel(EBlocks.JUNGLE_BARREL.get(), Blocks.JUNGLE_PLANKS);
        barrel(EBlocks.ACACIA_BARREL.get(), Blocks.ACACIA_PLANKS);
        barrel(EBlocks.DARK_OAK_BARREL.get(), Blocks.DARK_OAK_PLANKS);
        barrel(EBlocks.CRIMSON_BARREL.get(), Blocks.CRIMSON_PLANKS);
        barrel(EBlocks.WARPED_BARREL.get(), Blocks.WARPED_PLANKS);
        barrel(EBlocks.STONE_BARREL.get(), Blocks.STONE);

        sieve(EBlocks.OAK_SIEVE.get(), Blocks.OAK_PLANKS);
        sieve(EBlocks.SPRUCE_SIEVE.get(), Blocks.SPRUCE_PLANKS);
        sieve(EBlocks.BIRCH_SIEVE.get(), Blocks.BIRCH_PLANKS);
        sieve(EBlocks.JUNGLE_SIEVE.get(), Blocks.JUNGLE_PLANKS);
        sieve(EBlocks.ACACIA_SIEVE.get(), Blocks.ACACIA_PLANKS);
        sieve(EBlocks.DARK_OAK_SIEVE.get(), Blocks.DARK_OAK_PLANKS);
        sieve(EBlocks.CRIMSON_SIEVE.get(), Blocks.CRIMSON_PLANKS);
        sieve(EBlocks.WARPED_SIEVE.get(), Blocks.WARPED_PLANKS);

        // Lava Crucible
        crucible(EBlocks.UNFIRED_CRUCIBLE.get());
        crucible(EBlocks.PORCELAIN_CRUCIBLE.get());
        crucible(EBlocks.CRIMSON_CRUCIBLE.get(), Blocks.CRIMSON_STEM);
        crucible(EBlocks.WARPED_CRUCIBLE.get(), Blocks.WARPED_STEM);

        // Water Crucible
        crucible(EBlocks.OAK_CRUCIBLE.get(), Blocks.OAK_LOG);
        crucible(EBlocks.SPRUCE_CRUCIBLE.get(), Blocks.SPRUCE_LOG);
        crucible(EBlocks.BIRCH_CRUCIBLE.get(), Blocks.BIRCH_LOG);
        crucible(EBlocks.JUNGLE_CRUCIBLE.get(), Blocks.JUNGLE_LOG);
        crucible(EBlocks.ACACIA_CRUCIBLE.get(), Blocks.ACACIA_LOG);
        crucible(EBlocks.DARK_OAK_CRUCIBLE.get(), Blocks.DARK_OAK_LOG);
    }

    public void registerItemModels() {
        generic2d(EItems.SILK_WORM);
        generic2d(EItems.COOKED_SILK_WORM);

        handheld(EItems.WOODEN_HAMMER);
        handheld(EItems.STONE_HAMMER);
        handheld(EItems.GOLDEN_HAMMER);
        handheld(EItems.IRON_HAMMER);
        handheld(EItems.DIAMOND_HAMMER);

        handheld(EItems.CROOK);
        handheld(EItems.BONE_CROOK);
    }

    public void simpleCubeAll(Block block) {
        blockItem(block);
        simpleBlock(block);
    }

    public void crucible(Block block) {
        crucible(block, block);
    }

    public void crucible(Block block, Block appearance) {
        ResourceLocation texture = blockTexture(appearance);

        blockItem(block);

        singleModel(block)
                .parent(modParent("template_crucible"))
                .texture("inside", texture)
                .texture("top", texture)
                .texture("bottom", texture)
                .texture("side", texture);
    }
    
    public void barrel(Block block, Block appearance) {
        ResourceLocation texture = blockTexture(appearance);

        blockItem(block);

        singleModel(block)
                .parent(modParent("template_barrel"))
                .texture("barrel", texture);
    }

    public void sieve(Block block, Block appearance) {
        ResourceLocation texture = blockTexture(appearance);

        blockItem(block);

        singleModel(block)
                .parent(modParent("template_sieve"))
                .texture("texture", texture);
    }

    public void blockItem(Block block) {
        simpleBlockItem(block, modParent(block.getRegistryName().getPath()));
    }

    public ModelFile modParent(String name) {
        return new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name));
    }

    public ResourceLocation blockTexture(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + id.getPath());
    }

    // Configures a block model with no block state properties
    public BlockModelBuilder singleModel(Block block) {
        BlockModelBuilder builder = blockModel(block);

        getVariantBuilder(block).partialState().addModels(new ConfiguredModel(builder));

        return builder;
    }

    public BlockModelBuilder blockModel(Block block) {
        return models().getBuilder(block.getRegistryName().getPath());
    }

    // Generic 2d item model like lantern or hopper. Requires Item form.
    private void generic2d(RegistryObject<? extends IItemProvider> supplier) {
        String path = supplier.getId().getPath();
        itemModels().getBuilder(path).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated"))).texture("layer0", modLoc("item/" + path));
    }

    private void handheld(RegistryObject<Item> item) {
        String itemName = item.getId().getPath();

        itemModels().singleTexture(itemName, mcLoc("item/handheld"), "layer0", modLoc("item/" + itemName));
    }
}
