package thedarkcolour.exnihiloreborn.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3i;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

// ExNihiloReborn comes with a precomputed list of vanilla colors, since textures don't exist on the server.
// However, modded textures usually DO exist on the server, so their colors can be computed by the server once
// and stored in a cache file which is regenerated whenever number of mods or versions of mods change.
public class CompostColors {
    public static final String VANILLA_COMPOST_COLORS_FILE = "vanilla_compost_colors.txt";

    public static final Object2ObjectMap<Item, Vector3i> COLORS = new Object2ObjectOpenHashMap<>();

    public static void loadColors() {
        COLORS.clear();

        loadVanilla();
        loadModded();
    }

    private static void loadModded() {

    }

    public static boolean isLoaded() {
        return !COLORS.isEmpty();
    }

    private static void loadVanilla() {
        var vanillaColors = ModList.get().getModFileById(ExNihiloReborn.ID).getFile().findResource(CompostColors.VANILLA_COMPOST_COLORS_FILE);

        if (!Files.exists(vanillaColors)) {
            if (ExNihiloReborn.DEBUG) {
                computeVanilla();
                exportVanilla();
            } else {
                ExNihiloReborn.LOGGER.error("Failed to load vanilla colors!");
            }
        } else {
            readVanilla(vanillaColors);
        }
    }

    private static void readVanilla(Path vanillaColors) {
        try (var stream = Files.newInputStream(vanillaColors)) {
            try (var streamReader = new InputStreamReader(stream)) {
                try (var reader = new BufferedReader(streamReader)) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        var tokenizer = new StringTokenizer(line, ", #");
                        var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tokenizer.nextToken()));
                        String token = tokenizer.nextToken();
                        var color = Integer.parseInt(token, 16);

                        if (item != null) {
                            COLORS.put(item, new Vector3i(
                                    (color >> 16) & 255,
                                    (color >> 8) & 255,
                                    (color) & 255
                            ));
                        }
                    }
                }
            }
        } catch (IOException e) {
            ExNihiloReborn.LOGGER.error("Error reading vanilla colors file", e);
        }
    }

    private static void computeVanilla() {
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
                        int r = 0;
                        int g = 0;
                        int b = 0;

                        for (int i = sprite.x; i < sprite.x + width; i++) {
                            for (int j = sprite.y; j < sprite.y + height; j++) {
                                int pixel = image.getPixelRGBA(i, j);
                                if (pixel != 0) {
                                    // bgr because Minecraft has the pixels backwards
                                    b += (pixel >> 16) & 0xff;
                                    g += (pixel >> 8) & 0xff;
                                    r += (pixel) & 0xff;
                                    pixels++;
                                }
                            }
                        }

                        if (pixels > 0 && (r | g | b) != 0) {
                            var tint = Minecraft.getInstance().getItemColors().getColor(new ItemStack(item), 0);
                            Color c = new Color(
                                    r / pixels * (tint >> 16) & 0xff,
                                    g / pixels * (tint >> 8) & 0xff,
                                    b / pixels * tint & 0xff
                            );
                            // do not brighten a tinted texture
                            if (tint == 0) c = c.brighter();

                            Vector3i color = new Vector3i(c.getRed(), c.getGreen(), c.getBlue());
                            ExNihiloReborn.LOGGER.debug("Item '{}' has color ({}, {}, {})\n", entry.getKey().location(), color.x, color.y, color.z);
                            CompostColors.COLORS.put(item, color);
                        }
                    }
                }
            }
        }
    }

    public static void exportVanilla() {
        var path = Paths.get(CompostColors.VANILLA_COMPOST_COLORS_FILE);
        try {
            var file = path.toFile();

            if ((file.exists() && file.delete()) || file.createNewFile()) {
                try (var fileWriter = new FileWriter(file)) {
                    try (var writer = new BufferedWriter(fileWriter)) {
                        for (var entry : CompostColors.COLORS.object2ObjectEntrySet()) {
                            var id = ForgeRegistries.ITEMS.getKey(entry.getKey());
                            if (id.getNamespace().equals("minecraft")) {
                                writer.write(id.getPath());
                                writer.write(", #");
                                var colorVec = entry.getValue();
                                writer.write(Integer.toHexString(new Color(colorVec.x, colorVec.y, colorVec.z).getRGB() & 0xffffff));
                                writer.write('\n');
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
