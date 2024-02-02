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
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.MechanicalHammerMenu;
import thedarkcolour.exdeorum.menu.MechanicalSieveMenu;

public class EMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ExDeorum.ID);

    public static final RegistryObject<MenuType<MechanicalSieveMenu>> MECHANICAL_SIEVE = MENUS.register("mechanical_sieve", () -> new MenuType<>((IContainerFactory<MechanicalSieveMenu>) MechanicalSieveMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final RegistryObject<MenuType<MechanicalHammerMenu>> MECHANICAL_HAMMER = MENUS.register("mechanical_hammer", () -> new MenuType<>((IContainerFactory<MechanicalHammerMenu>) MechanicalHammerMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
