package thedarkcolour.exnihiloreborn.data;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.modkit.data.DataHelper;

// these two annotations basically mean modEventBus.addListener(Data::generateData)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        // Two things used by data generators
        var gen = event.getGenerator(); // writes to json
        var output = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        var helper = event.getExistingFileHelper(); // reads existing files like pngs and parent models

        var dataHelper = new DataHelper(ExNihiloReborn.ID, event);
        dataHelper.createEnglish(true, English::addTranslations);
        dataHelper.createBlockModels(BlockModels::addBlockModels);
        dataHelper.createItemModels(true, true, false, ItemModels::addItemModels);

        dataHelper.createRecipes(Recipes::addRecipes);

        var blockTags = new BlockTags(output, lookup, helper);

        gen.addProvider(true, new LootTables(output));
        gen.addProvider(true, blockTags);
        gen.addProvider(true, new ItemTags(output, lookup, blockTags.contentsGetter(), helper));
        gen.addProvider(true, new StructureTags(output, lookup, helper));
        gen.addProvider(true, new WorldPresetTags(output, lookup, helper));
        gen.addProvider(true, new Advancements(output, lookup, helper));
    }
}
