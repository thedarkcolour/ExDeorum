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

import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCrucibleMaterial extends AbstractMaterial {
    public AbstractCrucibleMaterial(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId) {
        super(soundType, strength, needsCorrectTool, mapColor, requiredModId);
    }

    @Nullable
    public static <T extends AbstractCrucibleMaterial> T readFromJson(MaterialParser parser, Factory<T> factory) {
        SoundType soundType = parser.getSoundType();
        float strength = parser.getStrength();
        boolean needsCorrectTool = parser.getOptionalBoolean("needs_correct_tool");
        int mapColor = parser.getMapColor();
        String requiredModId = parser.getRequiredModId();

        if (parser.error) {
            return null;
        } else {
            return factory.create(soundType, strength, needsCorrectTool, mapColor, requiredModId);
        }
    }

    public interface Factory<T> {
        T create(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId);
    }
}
