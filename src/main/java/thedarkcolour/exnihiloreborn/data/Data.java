package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

// these two annotations basically mean modEventBus.addListener(Data::generateData)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        // Two things used by data generators
        DataGenerator gen = event.getGenerator(); // writes to json
        ExistingFileHelper helper = event.getExistingFileHelper(); // reads existing files like pngs and parent models

        if (event.includeServer()) {
            EBlockTagsProvider blockTags = new EBlockTagsProvider(gen, helper);

            gen.addProvider(new ERecipeProvider(gen));
            gen.addProvider(new ELootProvider(gen));
            gen.addProvider(blockTags);
            gen.addProvider(new EItemTagProvider(gen, blockTags, helper));
        }
        if (event.includeClient()) {
            gen.addProvider(new EModelProvider(gen, helper));
            gen.addProvider(new ELangProvider(gen));
        }
    }
}
