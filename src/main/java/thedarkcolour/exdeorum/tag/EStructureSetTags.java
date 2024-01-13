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

package thedarkcolour.exdeorum.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import thedarkcolour.exdeorum.ExDeorum;

public class EStructureSetTags {
    public static final TagKey<StructureSet> OVERWORLD_VOID_STRUCTURES = tag("overworld_void_structure_sets");
    public static final TagKey<StructureSet> THE_NETHER_VOID_STRUCTURES = tag("the_nether_void_structure_sets");
    public static final TagKey<StructureSet> THE_END_VOID_STRUCTURES = tag("the_end_void_structure_sets");

    public static TagKey<StructureSet> tag(String name) {
        return TagKey.create(Registries.STRUCTURE_SET, new ResourceLocation(ExDeorum.ID, name));
    }
}
