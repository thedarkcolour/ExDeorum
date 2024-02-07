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

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.AbstractCrucibleBlock;
import thedarkcolour.exdeorum.block.BarrelBlock;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.block.SieveBlock;

@WailaPlugin
public class ExDeorumJadePlugin implements IWailaPlugin {
    static final ResourceLocation INFESTED_LEAVES = new ResourceLocation(ExDeorum.ID, "infested_leaves");
    static final ResourceLocation BARREL = new ResourceLocation(ExDeorum.ID, "barrel");
    static final ResourceLocation SIEVE = new ResourceLocation(ExDeorum.ID, "sieve");
    static final ResourceLocation CRUCIBLE = new ResourceLocation(ExDeorum.ID, "crucible");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(InfestedLeavesComponentProvider.INSTANCE, InfestedLeavesBlock.class);
        registration.registerBlockComponent(BarrelComponentProvider.INSTANCE, BarrelBlock.class);
        registration.registerBlockComponent(SieveComponentProvider.INSTANCE, SieveBlock.class);
        registration.registerBlockComponent(CrucibleComponentProvider.INSTANCE, AbstractCrucibleBlock.class);
    }
}
