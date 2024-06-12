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

package thedarkcolour.exdeorum.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;

// These sounds are all copies of vanilla sounds, just with their own names so players can easily muffle them
public class ESounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, ExDeorum.ID);

    // original sound: SoundEvents.COMPOSTER_FILL
    public static final DeferredHolder<SoundEvent, SoundEvent> BARREL_ADD_COMPOST = SOUNDS.register("barrel_add_compost", () -> SoundEvent.createVariableRangeEvent(ESounds.BARREL_ADD_COMPOST.getId()));
    // original sound: SoundEvents.COMPOSTER_READY
    public static final DeferredHolder<SoundEvent, SoundEvent> BARREL_COMPOST = SOUNDS.register("barrel_compost_finish", () -> SoundEvent.createVariableRangeEvent(ESounds.BARREL_COMPOST.getId()));
    // original sound: SoundEvents.AMBIENT_UNDERWATER_EXIT
    public static final DeferredHolder<SoundEvent, SoundEvent> BARREL_MIXING = SOUNDS.register("barrel_mixing", () -> SoundEvent.createVariableRangeEvent(ESounds.BARREL_MIXING.getId()));
    // original sound: SoundEvents.BREWING_STAND_BREW
    public static final DeferredHolder<SoundEvent, SoundEvent> BARREL_FLUID_TRANSFORM = SOUNDS.register("barrel_fluid_transform", () -> SoundEvent.createVariableRangeEvent(ESounds.BARREL_FLUID_TRANSFORM.getId()));

    // original sound: SoundEvents.HONEY_BLOCK_PLACE
    public static final DeferredHolder<SoundEvent, SoundEvent> SILK_WORM_DROP = SOUNDS.register("silk_worm_drop", () -> SoundEvent.createVariableRangeEvent(ESounds.SILK_WORM_DROP.getId()));
    // original sound: SoundEvents.HONEY_BLOCK_HIT
    public static final DeferredHolder<SoundEvent, SoundEvent> SILK_WORM_INFEST = SOUNDS.register("silk_worm_infest", () -> SoundEvent.createVariableRangeEvent(ESounds.SILK_WORM_INFEST.getId()));
    // original sound: SoundEvents.WEEPING_VINES_PLACE
    public static final DeferredHolder<SoundEvent, SoundEvent> SILK_WORM_EAT = SOUNDS.register("silk_worm_eat", () -> SoundEvent.createVariableRangeEvent(ESounds.SILK_WORM_EAT.getId()));

    // original sound: SoundEvents.ROOTED_DIRT_PLACE
    public static final DeferredHolder<SoundEvent, SoundEvent> GRASS_SEEDS_PLACE = SOUNDS.register("grass_seeds_place", () -> SoundEvent.createVariableRangeEvent(ESounds.GRASS_SEEDS_PLACE.getId()));
    // original sound: SoundEvents.SCULK_SHRIEKER_SHRIEK
    public static final DeferredHolder<SoundEvent, SoundEvent> SCULK_CORE_ACTIVATE = SOUNDS.register("sculk_core_activate", () -> SoundEvent.createVariableRangeEvent(ESounds.SCULK_CORE_ACTIVATE.getId()));

    // original sound: SoundEvents.WEATHER_RAIN
    public static final DeferredHolder<SoundEvent, SoundEvent> WATERING_CAN_USE = SOUNDS.register("watering_can_use", () -> SoundEvent.createVariableRangeEvent(ESounds.WATERING_CAN_USE.getId()));
    // original sound: SoundEvents.BUCKET_FILL
    public static final DeferredHolder<SoundEvent, SoundEvent> WATERING_CAN_STOP = SOUNDS.register("watering_can_stop", () -> SoundEvent.createVariableRangeEvent(ESounds.WATERING_CAN_STOP.getId()));
}
