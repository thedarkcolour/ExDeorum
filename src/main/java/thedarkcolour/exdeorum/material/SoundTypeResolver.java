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

import java.util.HashMap;
import java.util.Locale;

class SoundTypeResolver {
    private static final HashMap<String, SoundType> VANILLA_SOUND_TYPES = new HashMap<>();

    @Nullable
    static SoundType resolve(String name) {
        return VANILLA_SOUND_TYPES.computeIfAbsent(name.toUpperCase(Locale.ROOT), (fieldName) -> {
            try {
                var field = SoundType.class.getDeclaredField(fieldName);
                return (SoundType) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                return null;
            }
        });
    }
}
