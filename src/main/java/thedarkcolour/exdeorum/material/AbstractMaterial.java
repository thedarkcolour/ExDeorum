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

package thedarkcolour.exdeorum.material;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public abstract class AbstractMaterial implements ItemLike {
    // The sound this block makes (a string corresponding to a field in SoundType or a JSON object with the five sound events used to create a sound type)
    public final SoundType soundType;
    // The hardness of the barrel when harvesting
    public final float strength;
    // Whether this barrel needs a special tool to be harvested (ex. stone barrel only drops if mined with pickaxe)
    public final boolean needsCorrectTool;
    // Numeric ID of map color (these can be found on Minecraft Wiki as well as in MapColor.java)
    public final int mapColor;
    // ID of mod that should be present
    public final String requiredModId;

    RegistryObject<Block> block;
    RegistryObject<BlockItem> item;

    protected AbstractMaterial(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId) {
        this.soundType = soundType;
        this.strength = strength;
        this.needsCorrectTool = needsCorrectTool;
        this.mapColor = mapColor;
        this.requiredModId = requiredModId;
    }

    protected abstract Block createBlock();

    protected BlockBehaviour.Properties props() {
        var properties = BlockBehaviour.Properties.of().strength(this.strength).sound(this.soundType);
        if (this.needsCorrectTool) properties.requiresCorrectToolForDrops();
        return properties;
    }

    public Item getItem() {
        return this.item.get();
    }

    public Block getBlock() {
        return this.block.get();
    }

    @Override
    public Item asItem() {
        return this.item.get();
    }
}
