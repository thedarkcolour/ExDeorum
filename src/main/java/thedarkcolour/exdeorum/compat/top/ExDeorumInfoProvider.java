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

package thedarkcolour.exdeorum.compat.top;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.blockentity.SieveBlockEntity;
import thedarkcolour.exdeorum.registry.EBlocks;

public class ExDeorumInfoProvider implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(ExDeorum.ID, "info_provider");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player playerEntity, Level level, BlockState state, IProbeHitData data) {
        var te = level.getBlockEntity(data.getPos());

        if (state.getBlock() == EBlocks.INFESTED_LEAVES.get()) {
            if (te instanceof InfestedLeavesBlockEntity) {
                int progress = (int) (((InfestedLeavesBlockEntity) te).getProgress() * 100.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Progress: ").style(TextStyleClass.WARNING).text(progress + "%"));
            }
        } else if (te instanceof BarrelBlockEntity barrel) {
            short volume = barrel.compost;

            if (volume == 1000 || barrel.isBrewing()) {
                int progress = (int) (barrel.progress * 100.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Progress: ").style(TextStyleClass.WARNING).text(progress + "%"));
            } else if (volume > 0) {
                int volumePercent = (int) (volume / 10.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Volume: ").style(TextStyleClass.WARNING).text(volumePercent + "%"));
            } else if (barrel.isBurning()) {
                int progress = 300 - (int) (barrel.progress * 300.0f);

                info.text(CompoundText.create().style(TextStyleClass.ERROR).text("Burning! ").style(TextStyleClass.WARNING).text(progress / 20 + "s"));
            }
            if (playerEntity.isShiftKeyDown()) {
                info.tank(barrel.getTank());
            }
        } else if (te instanceof AbstractCrucibleBlockEntity crucible) {
            info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Rate: ").style(TextStyleClass.WARNING).text(crucible.getMeltingRate() + "x"));
            if (playerEntity.isShiftKeyDown()) {
                info.tank(crucible.getTank());
            }
        } else if (te instanceof SieveBlockEntity sieve) {
            if (!sieve.getContents().isEmpty()) {
                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Progress: ").style(TextStyleClass.WARNING).text((Math.round(1000 * sieve.getProgress()) / 10) + "%"));
            }
            if (playerEntity.isShiftKeyDown()) {
                var mesh = sieve.getMesh();
                info.horizontal(info.defaultLayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(sieve.getMesh(), info.defaultItemStyle().width(16).height(16))
                        .text(CompoundText.create().info(sieve.getMesh().getDescriptionId()));
                if (mesh.isEnchanted()) {
                    var list = new ObjectArrayList<Component>();
                    var style = info.defaultTextStyle().height(10);
                    ItemStack.appendEnchantmentNames(list, mesh.getEnchantmentTags());

                    for (var component : list) {
                        info.text(component, style);
                    }
                }
            }
        }
    }
}
