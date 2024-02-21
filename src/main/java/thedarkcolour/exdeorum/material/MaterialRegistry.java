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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.data.loading.DatagenModLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.registry.EBlocks;
import thedarkcolour.exdeorum.registry.EItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class MaterialRegistry<M extends AbstractMaterial> implements Iterable<M> {
    private final List<M> values = new ArrayList<>();
    private final String suffix;

    // Suffix should NOT start with an underscore
    public MaterialRegistry(String suffix) {
        this.suffix = suffix;

        Preconditions.checkArgument(!suffix.startsWith("_"));
    }

    public void search(Function<MaterialParser, @Nullable M> materialRegistrar) {
        var configPath = Paths.get("config/exdeorum/" + this.suffix + "_materials");

        if (!DatagenModLoader.isRunningDataGen()) {
            if (CompostColors.createConfigFolder(configPath)) {
                var materialsFolder = configPath.toFile();
                var children = materialsFolder.list();

                if (children != null) {
                    for (var child : children) {
                        if (child.endsWith(".json")) {
                            Path jsonPath = configPath.resolve(child);

                            try {
                                var json = (JsonObject) JsonParser.parseString(Files.readString(jsonPath));
                                var material = materialRegistrar.apply(new MaterialParser(json, jsonPath, this));

                                if (material != null) {
                                    register(child.substring(0, child.length() - 5), material);
                                }
                            } catch (IOException e) {
                                ExDeorum.LOGGER.error("Failed to read JSON custom material at {}", jsonPath);
                            }
                        }
                    }
                } else {
                    ExDeorum.LOGGER.error("Failed to read JSON custom materials at {}: not a directory", configPath);
                }
            }
        }
    }

    public void register(String name, M material) {
        var id = name + "_" + this.suffix;
        ExDeorum.LOGGER.info("Registered \"{}\" for {} material {}.json", id, this.suffix, name);

        if (material.block != null) {
            throw new IllegalStateException(this.suffix + " material with name " + name + " already registered: duplicate material?");
        }

        material.block = EBlocks.BLOCKS.register(id, material::createBlock);
        material.item = EItems.registerItemBlock(material.block);
        this.values.add(material);
    }

    public <B extends BlockEntity> BlockEntityType<B> createBlockEntityType(BlockEntityType.BlockEntitySupplier<? extends B> factory) {
        ImmutableSet.Builder<Block> validBlocks = ImmutableSet.builder();

        for (var material : this.values) {
            validBlocks.add(material.block.get());
        }

        return new BlockEntityType<>(factory, validBlocks.build(), null);
    }

    @Override
    public Iterator<M> iterator() {
        return this.values.iterator();
    }

    public Stream<M> stream() {
        return this.values.stream();
    }
}
