package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.modkit.data.DataHelper;

// these two annotations are equivalent to modEventBus.addListener(Data::generateData)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        // Two things used by data generators
        var gen = event.getGenerator(); // writes to json
        var output = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        // reads existing files like pngs and parent models
        var helper = event.getExistingFileHelper();

        var dataHelper = new DataHelper(ExNihiloReborn.ID, event);
        dataHelper.createEnglish(true, English::addTranslations);
        dataHelper.createBlockModels(BlockModels::addBlockModels);
        dataHelper.createItemModels(true, true, false, ItemModels::addItemModels);

        dataHelper.createRecipes(Recipes::addRecipes);
        dataHelper.createTags(Registries.BLOCK, ModTags::createBlockTags);
        dataHelper.createTags(Registries.ITEM, ModTags::createItemTags);
        dataHelper.createTags(Registries.FLUID, ModTags::createFluidTags);
        dataHelper.createTags(Registries.STRUCTURE_SET, ModTags::createStructureSetTags);
        dataHelper.createTags(Registries.WORLD_PRESET, ModTags::createWorldPresetTags);

        gen.addProvider(true, new LootTables(output));
        gen.addProvider(true, new Advancements(output, lookup, helper));
    }
}
