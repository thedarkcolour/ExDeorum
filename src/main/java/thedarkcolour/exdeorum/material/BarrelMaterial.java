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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.block.BarrelBlock;

import java.util.HashSet;
import java.util.Set;

public final class BarrelMaterial extends AbstractMaterial {
    public static final Set<Block> TRANSPARENT_BARRELS = new HashSet<>();

    // Whether the barrel can hold hot fluids
    public final boolean fireproof;
    // Whether fluids should be rendered with sides instead of just the top
    public final boolean transparent;

    BarrelMaterial(SoundType soundType, float strength, boolean needsCorrectTool, boolean fireproof, int mapColor, String requiredModId, boolean transparent) {
        super(soundType, strength, needsCorrectTool, mapColor, requiredModId);

        this.fireproof = fireproof;
        this.transparent = transparent;
    }

    @Override
    protected Block createBlock() {
        var props = props();
        if (!this.fireproof) props.ignitedByLava();
        return new BarrelBlock(props);
    }

    @Nullable
    public static BarrelMaterial readFromJson(MaterialParser parser) {
        SoundType soundType = parser.getSoundType();
        float strength = parser.getStrength();
        int mapColor = parser.getMapColor();
        boolean needsCorrectTool = parser.getOptionalBoolean("needs_correct_tool");
        boolean fireproof = parser.getOptionalBoolean("fireproof");
        String requiredModId = parser.getRequiredModId();
        boolean transparent = parser.getOptionalBoolean("transparent");

        if (parser.error) {
            return null;
        } else {
            return new BarrelMaterial(soundType, strength, needsCorrectTool, fireproof, mapColor, requiredModId, transparent);
        }
    }

    public static void loadTransparentBlocks() {
        for (var material : DefaultMaterials.BARRELS) {
            if (material.transparent) {
                TRANSPARENT_BARRELS.add(material.getBlock());
            }
        }
    }
}
