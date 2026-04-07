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

package thedarkcolour.exdeorum.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.joml.Vector3i;
import thedarkcolour.exdeorum.ExDeorum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

// Server-safe compost color loader. Vanilla colors come from the bundled text file,
// while modded overrides are read from config/exdeorum/compost_colors.
public class CompostColors {
    public static final String VANILLA_COMPOST_COLORS_FILE = "vanilla_compost_colors.txt";
    public static final Path COMPOST_COLORS_CONFIGS = Paths.get("config/exdeorum/compost_colors");

    public static final Object2ObjectOpenHashMap<Item, Vector3i> COLORS = new Object2ObjectOpenHashMap<>();
    public static final Vector3i DEFAULT_COLOR = new Vector3i(53, 168, 42);

    public static void loadColors() {
        COLORS.clear();
        loadVanilla();
        loadModded();
    }

    public static boolean isLoaded() {
        return !COLORS.isEmpty();
    }

    private static void loadVanilla() {
        try (var stream = CompostColors.class.getClassLoader().getResourceAsStream(VANILLA_COMPOST_COLORS_FILE)) {
            if (stream == null) {
                ExDeorum.LOGGER.error("Failed to load bundled vanilla compost colors");
                return;
            }

            try (var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                readColorEntries("minecraft", reader, VANILLA_COMPOST_COLORS_FILE);
            }
        } catch (IOException exception) {
            ExDeorum.LOGGER.error("Failed to read bundled vanilla compost colors", exception);
        }
    }

    private static void loadModded() {
        var colorsFolder = COMPOST_COLORS_CONFIGS.toFile();
        if (!colorsFolder.exists() || !colorsFolder.isDirectory()) {
            return;
        }

        var children = colorsFolder.list();
        if (children == null) {
            return;
        }

        for (var child : children) {
            if (!child.endsWith(".txt")) {
                continue;
            }

            var modid = child.substring(0, child.length() - 4);
            var path = COMPOST_COLORS_CONFIGS.resolve(child);
            try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                readColorEntries(modid, reader, path.toString());
            } catch (IOException exception) {
                ExDeorum.LOGGER.error("Error reading compost colors file {}", path, exception);
            }
        }
    }

    private static void readColorEntries(String modid, BufferedReader reader, String source) throws IOException {
        int lineNumber = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.isBlank() || line.startsWith("//")) {
                continue;
            }

            var tokenizer = new StringTokenizer(line, ", #");
            try {
                var id = Identifier.fromNamespaceAndPath(modid, tokenizer.nextToken());
                var item = BuiltInRegistries.ITEM.get(id).map(reference -> reference.value()).orElse(Items.AIR);
                int color = Integer.parseInt(tokenizer.nextToken(), 16);

                if (item == Items.AIR) {
                    ExDeorum.LOGGER.error("Unknown item {} in compost colors source {} line {}", id, source, lineNumber);
                    continue;
                }

                COLORS.put(item, new Vector3i((color >> 16) & 255, (color >> 8) & 255, color & 255));
            } catch (IllegalArgumentException | NoSuchElementException exception) {
                ExDeorum.LOGGER.error("Invalid compost color entry in {} line {}", source, lineNumber, exception);
            }
        }
    }

    public static void debugCompute() {
        throw new UnsupportedOperationException("debugCompute is not ported to MC 26.x");
    }

    public static void export(String modid) {
        export(modid, COLORS.keySet().stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(modid))
                .sorted(Comparator.comparing(BuiltInRegistries.ITEM::getKey))
                .toList());
    }

    private static void export(String modid, List<Item> sortedToExport) {
        try {
            if (!createConfigFolder(COMPOST_COLORS_CONFIGS)) {
                ExDeorum.LOGGER.error("Unable to create compost color config folder for {}", modid);
                return;
            }

            var path = COMPOST_COLORS_CONFIGS.resolve(modid + ".txt");
            try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("// Compost colors for " + modid + ".\n");

                var alphabeticalItems = new ArrayList<>(sortedToExport);
                alphabeticalItems.sort(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).getPath()));

                for (var item : alphabeticalItems) {
                    var color = COLORS.get(item);
                    if (color == null) {
                        continue;
                    }

                    writer.write(BuiltInRegistries.ITEM.getKey(item).getPath());
                    writer.write(", #");
                    writer.write(String.format("%02x%02x%02x", color.x, color.y, color.z));
                    writer.write('\n');
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to export compost colors for " + modid, exception);
        }
    }

    public static boolean createConfigFolder(Path configPath) {
        var colorsFolder = configPath.toFile();
        var configFolder = configPath.getParent().toFile();
        return (configFolder.exists() || configFolder.mkdir()) && (colorsFolder.exists() || colorsFolder.mkdir());
    }
}
