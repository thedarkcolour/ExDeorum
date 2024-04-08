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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyblockIsland {
    public final int id;
    @Nullable
    public final UUID creator;
    public final BlockPos origin;
    public final List<UUID> owners = new ArrayList<>();
    public String name;

    public SkyblockIsland(int id, @Nullable UUID creator, BlockPos origin, String name) {
        this.id = id;
        this.creator = creator;
        this.origin = origin;
        this.name = name;
    }

    public SkyblockIsland(CompoundTag nbt) {
        this.id = nbt.getInt("id");
        this.creator = nbt.contains("creator") ? nbt.getUUID("creator") : null;
        this.origin = NbtUtils.readBlockPos(nbt.getCompound("origin"));
        this.name = nbt.getString("name");

        for (var ownerNbt : nbt.getList("owners", Tag.TAG_INT_ARRAY)) {
            this.owners.add(NbtUtils.loadUUID(ownerNbt));
        }
    }

    public CompoundTag serializeNbt() {
        CompoundTag data = new CompoundTag();
        data.putInt("id", this.id);
        if (this.creator != null) {
            data.putUUID("creator", this.creator);
        }
        data.put("origin", NbtUtils.writeBlockPos(this.origin));

        return data;
    }

    public boolean isUnassigned() {
        return this.owners.isEmpty();
    }
}
