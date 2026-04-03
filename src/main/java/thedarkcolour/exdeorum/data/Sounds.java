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

package thedarkcolour.exdeorum.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.registry.ESounds;

class Sounds extends SoundDefinitionsProvider {
    protected Sounds(PackOutput output) {
        super(output, ExDeorum.ID);
    }

    @Override
    public void registerSounds() {
        add(ESounds.BARREL_ADD_COMPOST.getId(), withVariations(TranslationKeys.BARREL_ADD_COMPOST_SUBTITLE, "block/composter/fill", 4));
        add(ESounds.BARREL_COMPOST.getId(), withVariations(TranslationKeys.BARREL_COMPOST_SUBTITLE, "block/composter/ready", 4));
        add(ESounds.BARREL_MIXING.getId(), withVariations(TranslationKeys.BARREL_MIXING_SUBTITLE, ExDeorum.ID + ":block/barrel/mix", 3));
        add(ESounds.BARREL_FLUID_TRANSFORM.getId(), withVariations(TranslationKeys.BARREL_FLUID_TRANSFORM_SUBTITLE, "block/brewing_stand/brew", 2));
        add(ESounds.SILK_WORM_DROP.getId(), withVariations(TranslationKeys.SILK_WORM_DROP_SUBTITLE, "block/honeyblock/break", 5));
        add(ESounds.SILK_WORM_INFEST.getId(), withVariations(TranslationKeys.SILK_WORM_INFEST_SUBTITLE, "block/honeyblock/break", 5));
        add(ESounds.SILK_WORM_EAT.getId(), withVariations(TranslationKeys.SILK_WORM_EAT_SUBTITLE, "block/roots/break", 6));
        add(ESounds.GRASS_SEEDS_PLACE.getId(), withVariations(TranslationKeys.GRASS_SEEDS_PLACE_SUBTITLE, "block/rooted_dirt/break", 4));
        add(ESounds.SCULK_CORE_ACTIVATE.getId(), withVariations(TranslationKeys.SCULK_CORE_ACTIVATE_SUBTITLE, "block/sculk_shrieker/shriek", 5));
        add(ESounds.WATERING_CAN_USE.getId(), withVariations(TranslationKeys.WATERING_CAN_USE_SUBTITLE, "ambient/weather/rain", 8));
        add(ESounds.WATERING_CAN_STOP.getId(), withVariations(TranslationKeys.WATERING_CAN_STOP_SUBTITLE, "item/bucket/fill", 3));
    }

    private static SoundDefinition withVariations(String subtitleKey, String baseSoundPath, int variations) {
        var definition = definition().subtitle(subtitleKey);
        for (int i = 1; i <= variations; i++) {
            definition.with(sound(baseSoundPath + i));
        }

        return definition;
    }
}
