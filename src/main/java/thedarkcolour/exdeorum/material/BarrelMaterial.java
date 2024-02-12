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

import com.mojang.datafixers.util.Function6;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.SoundType;
import thedarkcolour.exdeorum.ExDeorum;

public record BarrelMaterial(
        // The sound this block makes (a string corresponding to a field in SoundType, or an unqualified field reference like me.mymod.block.Sounds.MODDED_SOUND_TYPE)
        SoundType soundType,
        // The hardness of the barrel when harvesting
        float strength,
        // Whether this barrel needs a special tool to be harvested (ex. stone barrel only drops if mined with pickaxe)
        boolean needsCorrectTool,
        // Whether the barrel can hold hot fluids
        boolean fireproof,
        // Numeric ID of map color (these can be found on Minecraft Wiki as well as in MapColor.java)
        int mapColor,
        // ID of mod that should be present
        String requiredModId
) {
    public static final Codec<BarrelMaterial> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("sound_type").xmap(BarrelMaterial::readSoundType, BarrelMaterial::writeSoundType).forGetter(BarrelMaterial::soundType),
                Codec.FLOAT.fieldOf("strength").forGetter(BarrelMaterial::strength),
                Codec.BOOL.fieldOf("needs_correct_tool").forGetter(BarrelMaterial::needsCorrectTool),
                Codec.BOOL.fieldOf("fireproof").forGetter(BarrelMaterial::fireproof),
                Codec.INT.fieldOf("map_color").forGetter(BarrelMaterial::mapColor),
                Codec.STRING.optionalFieldOf("required_mod_id", ExDeorum.ID).forGetter(BarrelMaterial::requiredModId)
        ).apply(builder, BarrelMaterial::new);
    });

    private static SoundType readSoundType(String soundType) {
        return null;
    }

    private static String writeSoundType(SoundType soundType) {
        return null;
    }
}
