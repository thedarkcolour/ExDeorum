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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;

enum SieveComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof SieveLogic.Owner sieve) {
            var logic = sieve.getLogic();

            if (!logic.getContents().isEmpty()) {
                tooltip.add(Component.literal("Progress: ").append((Math.round(1000 * logic.getProgress()) / 10) + "%"));
            }
            if (accessor.getPlayer().isShiftKeyDown()) {
                var mesh = logic.getMesh();
                var element = IElementHelper.get().item(mesh);
                tooltip.add(element);
                tooltip.append(IElementHelper.get().text(Component.translatable(mesh.getDescriptionId())).translate(new Vec2(2f, 6f)));
                if (mesh.isEnchanted()) {
                    var list = new ObjectArrayList<Component>();
                    ItemStack.appendEnchantmentNames(list, mesh.getEnchantmentTags());

                    for (var component : list) {
                        tooltip.add(component);
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ExDeorumJadePlugin.SIEVE;
    }
}
