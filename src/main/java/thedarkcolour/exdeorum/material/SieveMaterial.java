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
import thedarkcolour.exdeorum.block.SieveBlock;

public class SieveMaterial extends AbstractMaterial {
    public SieveMaterial(SoundType soundType, float strength, boolean needsCorrectTool, String requiredModId) {
        super(soundType, strength, needsCorrectTool, 0, requiredModId);
    }

    @Override
    protected Block createBlock() {
        return new SieveBlock(props().noOcclusion());
    }

    @Nullable
    public static SieveMaterial readFromJson(MaterialParser parser) {
        SoundType soundType = parser.getSoundType();
        float strength = parser.getStrength();
        boolean needsCorrectTool = parser.getOptionalBoolean("needs_correct_tool");
        String requiredModId = parser.getRequiredModId();

        if (parser.error) {
            return null;
        } else {
            return new SieveMaterial(soundType, strength, needsCorrectTool, requiredModId);
        }
    }
}
