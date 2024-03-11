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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

// Changed behavior from FluidTank:
// - fluid stacks read from NBT are clamped.
// - removed validator predicate
// - fixed incorrect fill implementation that caused dupes with the barrel
public class FluidHelper extends FluidTank {
    public FluidHelper(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (this.fluid.isEmpty()) {
                return Math.min(this.capacity, resource.getAmount());
            }
            if (!this.fluid.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(this.capacity - this.fluid.getAmount(), resource.getAmount());
        }
        if (this.fluid.isEmpty()) {
            // fix forge's implementation to avoid dupes
            int amount = Math.min(this.capacity, resource.getAmount());
            this.fluid = new FluidStack(resource, Math.min(this.capacity, amount));
            onContentsChanged();
            return amount;
        }
        if (!this.fluid.isFluidEqual(resource))
        {
            return 0;
        }
        int filled = this.capacity - this.fluid.getAmount();

        if (resource.getAmount() < filled) {
            this.fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            this.fluid.setAmount(this.capacity);
        }
        if (filled > 0) {
            onContentsChanged();
        }
        return filled;
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        if (!this.fluid.isEmpty()) {
            this.fluid.setAmount(Math.min(this.capacity, this.fluid.getAmount()));
        }

        return this;
    }
}
