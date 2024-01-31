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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import thedarkcolour.exdeorum.ExDeorum;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

// ExDeorum comes with a precomputed list of vanilla colors, since textures don't exist on the server.
// However, modded textures usually DO exist on the server, so their colors can be computed by the server once
// and stored in a file which can be configured by the user after the fact.
public class CompostColors {
    public static final String VANILLA_COMPOST_COLORS_FILE = "vanilla_compost_colors.txt";
    public static final Path COMPOST_COLORS_CONFIGS = Paths.get("config/exdeorum/compost_colors");

    public static final Object2ObjectMap<Item, Vector3i> COLORS = new Object2ObjectOpenHashMap<>();
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
        var vanillaColors = ModList.get().getModFileById(ExDeorum.ID).getFile().findResource(CompostColors.VANILLA_COMPOST_COLORS_FILE);

        if (!Files.exists(vanillaColors)) {
            if (ExDeorum.DEBUG) {
                debugCompute();
                export("minecraft");
            } else {
                ExDeorum.LOGGER.error("Failed to load vanilla colors!");
            }
        } else {
            readColorFile("minecraft", vanillaColors);
        }
    }

    // Instead of reading from files, this method pulls colors directly from the texture atlas.
    private static void debugCompute() {
        var atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
        int atlasWidth = atlas.width;
        int atlasHeight = atlas.height;

        try (var image = new NativeImage(atlasWidth, atlasHeight, false)) {
            // should already be bound but just in case
            GlStateManager._bindTexture(atlas.getId());
            // alpha doesn't matter, only RGB
            image.downloadTexture(0, false);

            for (var entry : ForgeRegistries.ITEMS.getEntries()) {
                var item = entry.getValue();
                var model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item);

                if (model != null) {
                    var sprite = model.getParticleIcon(ModelData.EMPTY);

                    if (sprite.atlasLocation().equals(InventoryMenu.BLOCK_ATLAS)) {
                        int width = sprite.contents().width();
                        int height = sprite.contents().height();
                        int pixels = 0;
                        int totalR = 0;
                        int totalG = 0;
                        int totalB = 0;

                        for (int i = sprite.x; i < sprite.x + width; i++) {
                            for (int j = sprite.y; j < sprite.y + height; j++) {
                                int pixel = image.getPixelRGBA(i, j);
                                if (pixel != 0) {
                                    // bgr because Minecraft has the pixels backwards
                                    totalB += (pixel >> 16) & 0xff;
                                    totalG += (pixel >> 8) & 0xff;
                                    totalR += (pixel) & 0xff;
                                    pixels++;
                                }
                            }
                        }

                        putColor(pixels, totalR, totalG, totalB, item);
                    }
                }
            }
        }
    }

    private static void loadModded() {
        var readMods = readModdedColorFiles();

        for (var entry : ForgeRegistries.ITEMS.getEntries()) {
            var modid = entry.getKey().location().getNamespace();

            if (!readMods.contains(modid)) {
                var id = entry.getKey().location().getPath();
                var modFile = ModList.get().getModFileById(modid);
                if (modFile == null)
                    continue;
                var jarFile = modFile.getFile();
                var modelPath = jarFile.findResource("assets/" + modid + "/models/item/" + id + ".json");

                if (Files.exists(modelPath)) {
                    JsonObject modelJson = parseModelJson(modelPath, modid, id);

                    if (modelJson != null) {
                        var textures = modelJson.get("textures");

                        if (textures instanceof JsonObject textureMap) {
                            String texture = findFirstTexture(textureMap);

                            if (texture != null) {
                                // Best case scenario, we are in a plain old 2D item.
                                var texturePath = jarFile.findResource("assets/" + modid + "/textures/" + texture + ".png");

                                if (Files.exists(texturePath)) {
                                    try (var stream = Files.newInputStream(texturePath)) {
                                        var img = ImageIO.read(stream);
                                        int width = img.getWidth();
                                        int height = img.getHeight();
                                        int pixels = 0;
                                        int totalR = 0;
                                        int totalG = 0;
                                        int totalB = 0;

                                        for (int x = 0; x < width; x++) {
                                            for (int y = 0; y < height; y++) {
                                                int pixel = img.getRGB(x, y);
                                                if (pixel != 0) {
                                                    totalR += (pixel >> 16) & 0xff;
                                                    totalG += (pixel >> 8) & 0xff;
                                                    totalB += (pixel) & 0xff;
                                                    pixels++;
                                                }
                                            }
                                        }

                                        putColor(pixels, totalR, totalG, totalB, entry.getValue());

                                        if (ExDeorum.DEBUG) {
                                            ExDeorum.LOGGER.debug("Item {}:{} has color {}", modid, id, COLORS.get(entry.getValue()));
                                        }
                                    } catch (IOException exception) {
                                        ExDeorum.LOGGER.error("Failed to read texture file for item {}:{}", modid, id);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        @SuppressWarnings("DataFlowIssue")
        var entries = COLORS.keySet().stream().sorted(Comparator.comparing(ForgeRegistries.ITEMS::getKey)).collect(Collectors.groupingBy(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace()));

        for (var entry : entries.entrySet()) {
            if (!readMods.contains(entry.getKey())) {
                export(entry.getKey(), entry.getValue());
            }
        }
    }

    @Nullable
    private static String findFirstTexture(JsonObject textureMap) {
        if (textureMap.get("layer0") instanceof JsonPrimitive primitive) {
            return new ResourceLocation(primitive.getAsString()).getPath();
        }

        return null;
    }

    // Returns a set of the mod ids that were read
    private static ObjectSet<String> readModdedColorFiles() {
        var colorsFolder = COMPOST_COLORS_CONFIGS.toFile();

        // Minecraft is hardcoded in the Ex Deorum jar file
        var readMods = new ObjectOpenHashSet<String>();
        readMods.add("minecraft");

        if (colorsFolder.exists() && colorsFolder.isDirectory()) {
            var children = colorsFolder.list();

            if (children != null) {
                // child should be "modid.txt"
                for (var child : children) {
                    if (child.endsWith(".txt")) {
                        var modid = child.replace(".txt", "");

                        if (ModList.get().isLoaded(modid) && !modid.equals("minecraft")) {
                            if (readColorFile(modid, COMPOST_COLORS_CONFIGS.resolve(child))) {
                                readMods.add(modid);
                            }
                        }
                    }
                }
            }
        }

        return readMods;
    }

    @Nullable
    private static JsonObject parseModelJson(Path modelPath, String modid, String id) {
        try (var stream = Files.newInputStream(modelPath)) {
            try (var streamReader = new InputStreamReader(stream)) {
                try {
                    return GsonHelper.parse(IOUtils.toString(streamReader));
                } catch (JsonParseException exception) {
                    ExDeorum.LOGGER.error("Failed to parse model file for item {}:{}", modid, id);
                }
            }
        } catch (IOException exception) {
            ExDeorum.LOGGER.error("Failed to read model file for item {}:{}", modid, id);
        }

        return null;
    }

    private static void putColor(int pixels, int totalR, int totalG, int totalB, Item item) {
        if (pixels > 0 && (totalR | totalG | totalB) != 0) {
            var tint = getTint(item);
            Color c;
            if (tint == 0) {
                c = new Color(totalR / pixels, totalG / pixels, totalB / pixels).brighter();
            } else {
                c = new Color(
                        ((float) totalR / pixels / 255f) * ((tint >> 16 & 0xff) / 255f),
                        ((float) totalG / pixels / 255f) * ((tint >> 8 & 0xff) / 255f),
                        ((float) totalB / pixels / 255f) * ((tint & 0xff) / 255f)
                );
            }

            Vector3i color = new Vector3i(c.getRed(), c.getGreen(), c.getBlue());
            CompostColors.COLORS.put(item, color);
        }
    }

    private static int getTint(Item item) {
        if (ExDeorum.DEBUG && FMLEnvironment.dist == Dist.CLIENT) {
            return Minecraft.getInstance().getItemColors().getColor(new ItemStack(item), 0);
        } else {
            return 0;
        }
    }

    private static boolean readColorFile(String modid, Path path) {
        try (var stream = Files.newInputStream(path)) {
            try (var streamReader = new InputStreamReader(stream)) {
                try (var reader = new BufferedReader(streamReader)) {
                    int readColors = 0;
                    int lineNumber = 0;
                    String line;

                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (line.startsWith("//")) continue;

                        var tokenizer = new StringTokenizer(line, ", #");
                        try {
                            var id = new ResourceLocation(modid, tokenizer.nextToken());
                            var item = ForgeRegistries.ITEMS.getValue(id);
                            String token = tokenizer.nextToken();
                            var color = Integer.parseInt(token, 16);

                            if (item != null && item != Items.AIR) {
                                readColors++;

                                COLORS.put(item, new Vector3i(
                                        (color >> 16) & 255,
                                        (color >> 8) & 255,
                                        (color) & 255
                                ));
                            } else {
                                ExDeorum.LOGGER.error("Failed to read line {} of compost colors file {} - Unknown item {}", lineNumber, path, id);
                            }
                        } catch (NumberFormatException | NoSuchElementException e) {
                            ExDeorum.LOGGER.error("Failed to read line {} of compost colors file {} - Invalid format: {}", lineNumber, path, e.getMessage());
                        }
                    }

                    if (readColors > 0) {
                        ExDeorum.LOGGER.debug("Read {} compost colors from compost colors file {}", readColors, path);
                        return true;
                    } else {
                        ExDeorum.LOGGER.debug("Ignoring empty compost colors file {}", path);
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            ExDeorum.LOGGER.error("Error reading colors file {} : {}", path, e);
        }

        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void export(String modid) {
        export(modid, COLORS.keySet().stream().filter(key -> ForgeRegistries.ITEMS.getKey(key).getNamespace().equals(modid)).sorted(Comparator.comparing(ForgeRegistries.ITEMS::getKey)).toList());
    }

    // The given list should be sorted
    private static void export(String modid, List<Item> sortedToExport) {
        try {
            if (createConfigFolder()) {
                var path = COMPOST_COLORS_CONFIGS.resolve(modid + ".txt");
                var file = path.toFile();

                if ((file.exists() && file.delete()) || file.createNewFile()) {
                    try (var fileWriter = new FileWriter(file)) {
                        try (var writer = new BufferedWriter(fileWriter)) {
                            // sort file entries alphabetically
                            var alphabeticalItems = new ArrayList<>(sortedToExport);
                            alphabeticalItems.sort(Comparator.comparing(item -> ForgeRegistries.ITEMS.getKey(item).getPath()));

                            writer.write("// Compost colors for " + modid + ". You may add your own colors, change existing ones, or remove colors that aren't needed.\n");

                            for (var item : alphabeticalItems) {
                                if (COLORS.containsKey(item)) {
                                    writer.write(ForgeRegistries.ITEMS.getKey(item).getPath());
                                    writer.write(", #");
                                    var colorVec = COLORS.get(item);
                                    writer.write(Integer.toHexString(new Color(colorVec.x, colorVec.y, colorVec.z).getRGB() & 0xffffff));
                                    writer.write('\n');
                                }
                            }

                            // Skips the error message
                            return;
                        }
                    }
                }
            }

            ExDeorum.LOGGER.error("Unable to save compost colors for mod \"{}\"", modid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean createConfigFolder() {
        var colorsFolder = COMPOST_COLORS_CONFIGS.toFile();
        var configFolder = COMPOST_COLORS_CONFIGS.getParent().toFile();

        return (configFolder.exists() || configFolder.mkdir()) && (colorsFolder.exists() || colorsFolder.mkdir());
    }
}
