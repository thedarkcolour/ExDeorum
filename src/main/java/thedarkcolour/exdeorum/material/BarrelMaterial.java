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
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.block.BarrelBlock;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class BarrelMaterial {
    // Use a linked hash map to maintain insertion order
    public static final List<BarrelMaterial> REGISTERED_MATERIALS = new ArrayList<>();

    private static final Path CUSTOM_BARREL_MATERIAL_CONFIGS = Paths.get("config/exdeorum/barrel_materials");

    // The sound this block makes (a string corresponding to a field in SoundType, or an unqualified field reference like me.mymod.block.Sounds.MODDED_SOUND_TYPE)
    public final SoundType soundType;
    // The hardness of the barrel when harvesting
    public final float strength;
    // Whether this barrel needs a special tool to be harvested (ex. stone barrel only drops if mined with pickaxe)
    public final boolean needsCorrectTool;
    // Whether the barrel can hold hot fluids
    public final boolean fireproof;
    // Numeric ID of map color (these can be found on Minecraft Wiki as well as in MapColor.java)
    public final int mapColor;
    // ID of mod that should be present
    public final String requiredModId;
    // Whether fluids should be rendered with sides instead of just the top
    public final boolean transparent;

    private RegistryObject<BarrelBlock> block;
    private RegistryObject<BlockItem> item;

    BarrelMaterial(SoundType soundType, float strength, boolean needsCorrectTool, boolean fireproof, int mapColor, String requiredModId, boolean transparent) {
        this.soundType = soundType;
        this.strength = strength;
        this.needsCorrectTool = needsCorrectTool;
        this.fireproof = fireproof;
        this.mapColor = mapColor;
        this.requiredModId = requiredModId;
        this.transparent = transparent;
    }

    public static void findMaterials() {
        if (DatagenModLoader.isRunningDataGen()) {
            return;
        }
        if (CompostColors.createConfigFolder(CUSTOM_BARREL_MATERIAL_CONFIGS)) {
            var materialsFolder = CUSTOM_BARREL_MATERIAL_CONFIGS.toFile();
            var children = materialsFolder.list();

            if (children != null) {
                for (var child : children) {
                    if (child.endsWith(".json")) {
                        Path path = CUSTOM_BARREL_MATERIAL_CONFIGS.resolve(child);

                        try {
                            var json = JsonParser.parseString(Files.readString(path));
                            var material = parseJsonMaterial((JsonObject) json, path);

                            if (material != null) {
                                registerMaterial(child.substring(0, child.length() - 5), material);
                            }
                        } catch (IOException e) {
                            ExDeorum.LOGGER.error("Failed to read JSON custom barrel material at {}", path);
                        }
                    }
                }
            } else {
                ExDeorum.LOGGER.error("Failed to read custom barrel materials folder {}: not a directory", CUSTOM_BARREL_MATERIAL_CONFIGS);
            }
        }
    }

    // Returns either the material or a list of errors that occurred while parsing
    @Nullable
    private static BarrelMaterial parseJsonMaterial(JsonObject json, Path jsonPath) {
        boolean error = false;
        SoundType soundType = null;
        float strength = 0;
        boolean needsCorrectTool = false;
        boolean fireproof = false;
        int mapColor = 0;
        String requiredModId = ExDeorum.ID;
        boolean transparent = false;

        if (json.has("sound_type")) {
            var soundTypeJson = json.get("sound_type");

            if (soundTypeJson.isJsonPrimitive()) {
                String soundTypeString = soundTypeJson.getAsString();
                soundType = SoundTypeResolver.VANILLA_SOUND_TYPES.get(soundTypeString);

                if (soundType == null) {
                    ExDeorum.LOGGER.error("Unknown sound type \"{}\" for barrel material {}", soundTypeString, jsonPath);
                    error = true;
                }
            } else if (soundTypeJson instanceof JsonObject soundTypeObj) {
                // todo let users define sound types with registry names
            } else {
                ExDeorum.LOGGER.error("Unable to parse sound type for barrel material {}", jsonPath);
                error = true;
            }
        } else {
            ExDeorum.LOGGER.error("Missing sound_type property for barrel material {}", jsonPath);
            error = true;
        }
        if (json.has("strength")) {
            var strengthJson = json.get("strength");

            if (strengthJson.isJsonPrimitive()) {
                try {
                    strength = strengthJson.getAsFloat();
                } catch (NumberFormatException e) {
                    ExDeorum.LOGGER.error("Failed to parse strength property for barrel material {} with value {}", jsonPath, strengthJson.getAsString());
                    error = true;
                }
            } else {
                ExDeorum.LOGGER.error("Failed to parse strength property for barrel material {}: not a number", jsonPath);
                error = true;
            }
        } else {
            ExDeorum.LOGGER.error("Missing strength property for barrel material {}", jsonPath);
            error = true;
        }

        if (json.has("map_color")) {
            if (json.get("map_color") instanceof JsonPrimitive prim) {
                mapColor = prim.getAsInt();
                if (64 <= mapColor || mapColor < 0) {
                    ExDeorum.LOGGER.error("Failed to parse map_color property for barrel material {}: value must be in [0,64), found {}", jsonPath, mapColor);
                    error = true;
                }
            } else {
                ExDeorum.LOGGER.error("Failed to parse map_color property for barrel material {}: not a number", jsonPath);
                error = true;
            }
        } else {
            ExDeorum.LOGGER.error("Missing map_color property for barrel material {}", jsonPath);
            error = true;
        }

        if (json.get("needs_correct_tool") instanceof JsonPrimitive prim && prim.isBoolean()) {
            needsCorrectTool = prim.getAsBoolean();
        }
        if (json.get("fireproof") instanceof JsonPrimitive prim && prim.isBoolean()) {
            fireproof = prim.getAsBoolean();
        }
        if (json.get("required_mod_id") instanceof JsonPrimitive prim && prim.isString()) {
            requiredModId = prim.getAsString();
        }
        if (json.get("transparent") instanceof JsonPrimitive prim && prim.isBoolean()) {
            transparent = prim.getAsBoolean();
        }

        if (error) {
            return null;
        } else {
            return new BarrelMaterial(soundType, strength, needsCorrectTool, fireproof, mapColor, requiredModId, transparent);
        }
    }

    public static void registerMaterial(String name, BarrelMaterial material) {
        var id = name + "_barrel";
        ExDeorum.LOGGER.info("Added barrel \"{}\" for barrel material {}: {}", id, name + ".json", material);

        if (material.block != null) {
            throw new IllegalStateException("Tried to set block on material" + name + ", but block was already set!");
        }

        material.block = EBlocks.BLOCKS.register(id, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of().strength(material.strength).sound(material.soundType);
            if (!material.fireproof) props.ignitedByLava();
            if (material.needsCorrectTool) props.requiresCorrectToolForDrops();
            return new BarrelBlock(props);
        });
        material.item = EItems.registerItemBlock(material.block);
        REGISTERED_MATERIALS.add(material);
    }

    public static BlockEntityType<BarrelBlockEntity> createBlockEntityType() {
        HashSet<Block> validBlocks = new HashSet<>();

        for (var material : REGISTERED_MATERIALS) {
            validBlocks.add(material.block.get());
        }

        return new BlockEntityType<>(BarrelBlockEntity::new, validBlocks, null);
    }

    // Does not check if the mod is registered
    public Item getItem() {
        return this.item.get();
    }

    public Block getBlock() {
        return this.block.get();
    }
}
