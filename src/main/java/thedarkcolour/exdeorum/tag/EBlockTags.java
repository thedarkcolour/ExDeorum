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

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import thedarkcolour.exdeorum.ExDeorum;

public class EBlockTags {
    public static final TagKey<Block> WATERING_CAN_TICKABLE = tag("watering_can_tickable");
    public static final TagKey<Block> MINEABLE_WITH_HAMMER = tag("mineable/hammer");

    public static final TagKey<Block> GRASS_SEEDS_SPREADABLES = tag("grass_seeds_spreadables");
    public static final TagKey<Block> MYCELIUM_SPORES_SPREADABLES = tag("mycelium_spores_spreadables");
    public static final TagKey<Block> WARPED_NYLIUM_SPORES_SPREADABLES = tag("warped_nylium_spores_spreadables");
    public static final TagKey<Block> CRIMSON_NYLIUM_SPORES_SPREADABLES = tag("crimson_nylium_spores_spreadables");

    public static TagKey<Block> tag(String name) {
        return BlockTags.create(ExDeorum.loc(name));
    }
}
