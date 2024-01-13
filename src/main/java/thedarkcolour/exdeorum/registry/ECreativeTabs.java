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

package thedarkcolour.exdeorum.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.data.TranslationKeys;

public class ECreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExDeorum.ID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> {
        var builder = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0);
        ECreativeTabs.mainTab(builder);
        return builder.build();
    });

    private static void mainTab(CreativeModeTab.Builder builder) {
        builder.icon(() -> new ItemStack(EItems.CROOK.get()));
        builder.title(Component.translatable(TranslationKeys.MAIN_CREATIVE_TAB));
        builder.withTabsBefore(CreativeModeTabs.SPAWN_EGGS);
        builder.displayItems((enabledFeatures, output) -> EItems.addItemsToMainTab(output));
    }
}
