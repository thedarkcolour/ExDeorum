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

package thedarkcolour.exdeorum.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElementHelper;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;

enum BarrelComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        if (accessor.getBlockEntity() instanceof BarrelBlockEntity barrel) {
            short volume = barrel.compost;

            if (volume == 1000 || barrel.isBrewing()) {
                int progress = (int) (barrel.progress * 100.0f);

                tooltip.add(Component.literal("Progress: ").append(progress + "%"));
            } else if (volume > 0) {
                int volumePercent = (int) (volume / 10.0f);

                tooltip.add(Component.literal("Volume: ").append(volumePercent + "%"));
            } else if (barrel.isBurning()) {
                int progress = 300 - (int) (barrel.progress * 300.0f);

                tooltip.add(Component.literal("Burning! ").append(progress / 20 + "s"));
            }
            if (accessor.getPlayer().isShiftKeyDown()) {
                var fluid = barrel.getTank().getFluid();
                tooltip.add(IElementHelper.get().fluid(JadeFluidObject.of(fluid.getFluid(), fluid.getAmount())));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ExDeorumJadePlugin.BARREL;
    }
}
