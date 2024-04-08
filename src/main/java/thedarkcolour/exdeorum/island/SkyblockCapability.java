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

package thedarkcolour.exdeorum.island;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;

import java.util.ArrayList;
import java.util.List;

public class SkyblockCapability implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation(ExDeorum.ID, "island_manager");
    public static final Capability<SkyblockCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final LazyOptional<SkyblockCapability> holder = LazyOptional.of(() -> this);
    private final List<SkyblockIsland> islands = new ArrayList<>();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();

        for (var island : this.islands) {
            listTag.add(island.serializeNbt());
        }
        nbt.put("islands", listTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.islands.clear();

        for (var islandNbt : nbt.getList("islands", Tag.TAG_COMPOUND)) {
            this.islands.add(new SkyblockIsland((CompoundTag) islandNbt));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == SkyblockCapability.CAPABILITY ? this.holder.cast() : LazyOptional.empty();
    }
}
