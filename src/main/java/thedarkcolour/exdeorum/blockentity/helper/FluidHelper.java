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

package thedarkcolour.exdeorum.blockentity.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.templates.FluidTank;

// Only changed behavior from FluidTank is that fluid stacks read from NBT are clamped and removed validator predicate.
public class FluidHelper extends FluidTank {
    public FluidHelper(int capacity) {
        super(capacity);
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.fluid.setAmount(Math.min(this.capacity, this.fluid.getAmount()));

        return this;
    }
}
