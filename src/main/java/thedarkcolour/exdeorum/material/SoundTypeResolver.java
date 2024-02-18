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

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.fml.loading.FMLLoader;
import thedarkcolour.exdeorum.ExDeorum;

import java.util.Map;

class SoundTypeResolver {
    static final Map<String, SoundType> VANILLA_SOUND_TYPES;

    static {
        if (ExDeorum.DEBUG && !FMLLoader.versionInfo().mcVersion().equals("1.20.1")) {
            throw new RuntimeException("Update the BarrelMaterial map");
        }
        // Very long line of put calls that put each SoundType field and its lowercase name into the map
        ImmutableMap.Builder<String, SoundType> temp = ImmutableMap.builder();
        temp.put("empty", SoundType.EMPTY);
        temp.put("wood", SoundType.WOOD);
        temp.put("gravel", SoundType.GRAVEL);
        temp.put("grass", SoundType.GRASS);
        temp.put("lily_pad", SoundType.LILY_PAD);
        temp.put("stone", SoundType.STONE);
        temp.put("metal", SoundType.METAL);
        temp.put("glass", SoundType.GLASS);
        temp.put("wool", SoundType.WOOL);
        temp.put("sand", SoundType.SAND);
        temp.put("snow", SoundType.SNOW);
        temp.put("powder_snow", SoundType.POWDER_SNOW);
        temp.put("ladder", SoundType.LADDER);
        temp.put("anvil", SoundType.ANVIL);
        temp.put("slime_block", SoundType.SLIME_BLOCK);
        temp.put("honey_block", SoundType.HONEY_BLOCK);
        temp.put("wet_grass", SoundType.WET_GRASS);
        temp.put("coral_block", SoundType.CORAL_BLOCK);
        temp.put("bamboo", SoundType.BAMBOO);
        temp.put("bamboo_sapling", SoundType.BAMBOO_SAPLING);
        temp.put("scaffolding", SoundType.SCAFFOLDING);
        temp.put("sweet_berry_bush", SoundType.SWEET_BERRY_BUSH);
        temp.put("crop", SoundType.CROP);
        temp.put("hard_crop", SoundType.HARD_CROP);
        temp.put("vine", SoundType.VINE);
        temp.put("nether_wart", SoundType.NETHER_WART);
        temp.put("lantern", SoundType.LANTERN);
        temp.put("stem", SoundType.STEM);
        temp.put("nylium", SoundType.NYLIUM);
        temp.put("fungus", SoundType.FUNGUS);
        temp.put("roots", SoundType.ROOTS);
        temp.put("shroomlight", SoundType.SHROOMLIGHT);
        temp.put("weeping_vines", SoundType.WEEPING_VINES);
        temp.put("twisting_vines", SoundType.TWISTING_VINES);
        temp.put("soul_sand", SoundType.SOUL_SAND);
        temp.put("soul_soil", SoundType.SOUL_SOIL);
        temp.put("basalt", SoundType.BASALT);
        temp.put("wart_block", SoundType.WART_BLOCK);
        temp.put("netherrack", SoundType.NETHERRACK);
        temp.put("nether_bricks", SoundType.NETHER_BRICKS);
        temp.put("nether_sprouts", SoundType.NETHER_SPROUTS);
        temp.put("nether_ore", SoundType.NETHER_ORE);
        temp.put("bone_block", SoundType.BONE_BLOCK);
        temp.put("netherite_block", SoundType.NETHERITE_BLOCK);
        temp.put("ancient_debris", SoundType.ANCIENT_DEBRIS);
        temp.put("lodestone", SoundType.LODESTONE);
        temp.put("chain", SoundType.CHAIN);
        temp.put("nether_gold_ore", SoundType.NETHER_GOLD_ORE);
        temp.put("gilded_blackstone", SoundType.GILDED_BLACKSTONE);
        temp.put("candle", SoundType.CANDLE);
        temp.put("amethyst", SoundType.AMETHYST);
        temp.put("amethyst_cluster", SoundType.AMETHYST_CLUSTER);
        temp.put("small_amethyst_bud", SoundType.SMALL_AMETHYST_BUD);
        temp.put("medium_amethyst_bud", SoundType.MEDIUM_AMETHYST_BUD);
        temp.put("large_amethyst_bud", SoundType.LARGE_AMETHYST_BUD);
        temp.put("tuff", SoundType.TUFF);
        temp.put("calcite", SoundType.CALCITE);
        temp.put("dripstone_block", SoundType.DRIPSTONE_BLOCK);
        temp.put("pointed_dripstone", SoundType.POINTED_DRIPSTONE);
        temp.put("copper", SoundType.COPPER);
        temp.put("cave_vines", SoundType.CAVE_VINES);
        temp.put("spore_blossom", SoundType.SPORE_BLOSSOM);
        temp.put("azalea", SoundType.AZALEA);
        temp.put("flowering_azalea", SoundType.FLOWERING_AZALEA);
        temp.put("moss_carpet", SoundType.MOSS_CARPET);
        temp.put("pink_petals", SoundType.PINK_PETALS);
        temp.put("moss", SoundType.MOSS);
        temp.put("big_dripleaf", SoundType.BIG_DRIPLEAF);
        temp.put("small_dripleaf", SoundType.SMALL_DRIPLEAF);
        temp.put("rooted_dirt", SoundType.ROOTED_DIRT);
        temp.put("hanging_roots", SoundType.HANGING_ROOTS);
        temp.put("azalea_leaves", SoundType.AZALEA_LEAVES);
        temp.put("sculk_sensor", SoundType.SCULK_SENSOR);
        temp.put("sculk_catalyst", SoundType.SCULK_CATALYST);
        temp.put("sculk", SoundType.SCULK);
        temp.put("sculk_vein", SoundType.SCULK_VEIN);
        temp.put("sculk_shrieker", SoundType.SCULK_SHRIEKER);
        temp.put("glow_lichen", SoundType.GLOW_LICHEN);
        temp.put("deepslate", SoundType.DEEPSLATE);
        temp.put("deepslate_bricks", SoundType.DEEPSLATE_BRICKS);
        temp.put("deepslate_tiles", SoundType.DEEPSLATE_TILES);
        temp.put("polished_deepslate", SoundType.POLISHED_DEEPSLATE);
        temp.put("froglight", SoundType.FROGLIGHT);
        temp.put("frogspawn", SoundType.FROGSPAWN);
        temp.put("mangrove_roots", SoundType.MANGROVE_ROOTS);
        temp.put("muddy_mangrove_roots", SoundType.MUDDY_MANGROVE_ROOTS);
        temp.put("mud", SoundType.MUD);
        temp.put("mud_bricks", SoundType.MUD_BRICKS);
        temp.put("packed_mud", SoundType.PACKED_MUD);
        temp.put("hanging_sign", SoundType.HANGING_SIGN);
        temp.put("nether_wood_hanging_sign", SoundType.NETHER_WOOD_HANGING_SIGN);
        temp.put("bamboo_wood_hanging_sign", SoundType.BAMBOO_WOOD_HANGING_SIGN);
        temp.put("bamboo_wood", SoundType.BAMBOO_WOOD);
        temp.put("nether_wood", SoundType.NETHER_WOOD);
        temp.put("cherry_wood", SoundType.CHERRY_WOOD);
        temp.put("cherry_sapling", SoundType.CHERRY_SAPLING);
        temp.put("cherry_leaves", SoundType.CHERRY_LEAVES);
        temp.put("cherry_wood_hanging_sign", SoundType.CHERRY_WOOD_HANGING_SIGN);
        temp.put("chiseled_bookshelf", SoundType.CHISELED_BOOKSHELF);
        temp.put("suspicious_sand", SoundType.SUSPICIOUS_SAND);
        temp.put("suspicious_gravel", SoundType.SUSPICIOUS_GRAVEL);
        temp.put("decorated_pot", SoundType.DECORATED_POT);
        temp.put("decorated_pot_cracked", SoundType.DECORATED_POT_CRACKED);

        VANILLA_SOUND_TYPES = temp.build();
    }
}
