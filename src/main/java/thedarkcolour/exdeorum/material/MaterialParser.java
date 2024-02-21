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

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;

import java.nio.file.Path;

public class MaterialParser {
    private final JsonObject json;
    private final Path jsonPath;
    @Nullable
    private final MaterialRegistry<?> registry;
    boolean error;

    public MaterialParser(JsonObject json, Path jsonPath, @Nullable MaterialRegistry<?> registry) {
        this.json = json;
        this.jsonPath = jsonPath;
        this.registry = registry;
    }

    public SoundType getSoundType() {
        if (this.json.has("sound_type")) {
            var soundTypeJson = this.json.get("sound_type");

            if (soundTypeJson.isJsonPrimitive()) {
                String soundTypeString = soundTypeJson.getAsString();
                var soundType = SoundTypeResolver.VANILLA_SOUND_TYPES.get(soundTypeString);

                if (soundType == null) {
                    ExDeorum.LOGGER.error("Unknown sound type \"{}\" for material {}", soundTypeString, this.jsonPath);
                    this.error = true;
                } else {
                    return soundType;
                }
            } else if (soundTypeJson instanceof JsonObject soundTypeObj) {
                if (soundTypeObj.has("break_sound") && soundTypeObj.has("step_sound") && soundTypeObj.has("place_sound") && soundTypeObj.has("hit_sound") && soundTypeObj.has("fall_sound")) {
                    return new ForgeSoundType(1.0f, 1.0f,
                            RegistryObject.create(new ResourceLocation(soundTypeObj.get("break_sound").getAsString()), Registries.SOUND_EVENT, ExDeorum.ID),
                            RegistryObject.create(new ResourceLocation(soundTypeObj.get("step_sound").getAsString()), Registries.SOUND_EVENT, ExDeorum.ID),
                            RegistryObject.create(new ResourceLocation(soundTypeObj.get("place_sound").getAsString()), Registries.SOUND_EVENT, ExDeorum.ID),
                            RegistryObject.create(new ResourceLocation(soundTypeObj.get("hit_sound").getAsString()), Registries.SOUND_EVENT, ExDeorum.ID),
                            RegistryObject.create(new ResourceLocation(soundTypeObj.get("fall_sound").getAsString()), Registries.SOUND_EVENT, ExDeorum.ID)
                    );
                }
            } else {
                ExDeorum.LOGGER.error("Unable to parse sound type for material {}", this.jsonPath);
                this.error = true;
            }
        } else {
            ExDeorum.LOGGER.error("Missing sound_type property for material {}", this.jsonPath);
            this.error = true;
        }

        return SoundType.WOOD;
    }

    public float getStrength() {
        if (this.json.has("strength")) {
            var strengthJson = this.json.get("strength");

            if (strengthJson.isJsonPrimitive()) {
                try {
                    return strengthJson.getAsFloat();
                } catch (NumberFormatException e) {
                    ExDeorum.LOGGER.error("Failed to parse strength property for material {} with value {}", this.jsonPath, strengthJson.getAsString());
                    this.error = true;
                }
            } else {
                ExDeorum.LOGGER.error("Failed to parse strength property for material {}: not a number", this.jsonPath);
                this.error = true;
            }
        } else {
            if (this.registry == DefaultMaterials.SIEVES) {
                return 2f;
            } else {
                ExDeorum.LOGGER.error("Missing strength property for material {}", this.jsonPath);
                this.error = true;
            }
        }

        return 0f;
    }

    public int getMapColor() {
        if (this.json.has("map_color")) {
            if (this.json.get("map_color") instanceof JsonPrimitive prim) {
                var mapColor = prim.getAsInt();
                if (64 <= mapColor || mapColor < 0) {
                    ExDeorum.LOGGER.error("Failed to parse map_color property for material {}: value must be in [0,64), found {}", this.jsonPath, mapColor);
                    this.error = true;
                } else {
                    return mapColor;
                }
            } else {
                ExDeorum.LOGGER.error("Failed to parse map_color property for material {}: not a number", this.jsonPath);
                this.error = true;
            }
        } else {
            ExDeorum.LOGGER.error("Missing map_color property for material {}", this.jsonPath);
            this.error = true;
        }

        return 0;
    }

    public boolean getOptionalBoolean(String key) {
        return this.json.get(key) instanceof JsonPrimitive prim && prim.isBoolean() && prim.getAsBoolean();
    }

    public String getRequiredModId() {
        if (this.json.get("required_mod_id") instanceof JsonPrimitive prim && prim.isString()) {
            return prim.getAsString();
        } else {
            return ExDeorum.ID;
        }
    }
}
