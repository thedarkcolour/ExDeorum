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

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;

import java.util.function.Function;

public abstract class AbstractSieveBlockEntity extends EBlockEntity implements SieveLogic.Owner {
    protected final SieveLogic logic;

    public AbstractSieveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Function<SieveLogic.Owner, SieveLogic> logic) {
        super(type, pos, state);

        this.logic = logic.apply(this);
    }

    public static ItemStack singleCopy(ItemStack stack) {
        var copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    @Override
    public ServerLevel getServerLevel() {
        return (ServerLevel) this.level;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        this.logic.saveNbt(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.logic.loadNbt(nbt);
    }

    @Override
    public SieveLogic getLogic() {
        return this.logic;
    }

    @Override
    public void writeVisualData(FriendlyByteBuf buffer) {
        this.logic.writeVisualData(buffer);
    }

    @Override
    public void readVisualData(FriendlyByteBuf buffer) {
        this.logic.readVisualData(buffer);
    }

    @Override
    public void copyVisualData(BlockEntity fromIntegratedServer) {
        this.logic.copyVisualData(fromIntegratedServer);
    }
}
