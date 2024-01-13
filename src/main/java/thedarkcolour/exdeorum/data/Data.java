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

import net.minecraft.core.registries.Registries;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.data.recipe.Recipes;
import thedarkcolour.modkit.data.DataHelper;

// these two annotations are equivalent to modEventBus.addListener(Data::generateData)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
    static {
        if (DatagenModLoader.isRunningDataGen()) {
            ModCompatData.registerModData();
        }
    }

    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        // Two things used by data generators
        var gen = event.getGenerator(); // writes to json
        var output = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        // reads existing files like pngs and parent models
        var helper = event.getExistingFileHelper();

        var dataHelper = new DataHelper(ExDeorum.ID, event);
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
