package thedarkcolour.exnihiloreborn.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.client.ter.BarrelRenderer;
import thedarkcolour.exnihiloreborn.client.ter.CrucibleRenderer;
import thedarkcolour.exnihiloreborn.client.ter.InfestedLeavesRenderer;
import thedarkcolour.exnihiloreborn.client.ter.SieveRenderer;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EFluids;

public class ClientHandler {
    public static void register() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(ClientHandler::clientSetup);
        modBus.addListener(ClientHandler::stitchTextures);
        modBus.addListener(ClientHandler::postStitchTextures);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        // not sure why this isn't in render layers?
        bindTers();

        event.enqueueWork(ClientHandler::setRenderLayers);
    }

    private static void stitchTextures(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {
            event.addSprite(BarrelRenderer.COMPOST_DIRT_TEXTURE);
        }
    }

    @SuppressWarnings("resource")
    private static void postStitchTextures(TextureStitchEvent.Post event) {
        if (event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {
            int x =
            try (NativeImage image = new NativeImage(x, y, false)) {
                GlStateManager._bindTexture(event.getMap().getId());
                // alpha doesn't matter, only RGB
                image.downloadTexture(0, false);

                for (Item item : ForgeRegistries.ITEMS) {
                    IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(Items.HOPPER);

                    if (model != null) {
                        TextureAtlasSprite sprite = model.getParticleTexture(EmptyModelData.INSTANCE);

                        if (sprite.atlas() == event.getMap()) {

                            continue;
                        }
                    }
                    // put default color
                }
            }
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBlockEntities.INFESTED_LEAVES.get(), InfestedLeavesRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.BARREL.get(), BarrelRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.LAVA_CRUCIBLE.get(), CrucibleRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.WATER_CRUCIBLE.get(), CrucibleRenderer::new);
        event.registerBlockEntityRenderer(EBlockEntities.SIEVE.get(), SieveRenderer::new);
    }

    private static void setRenderLayers() {
        // Fluids
        RenderTypeLookup.setRenderLayer(EFluids.WITCH_WATER.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(EFluids.WITCH_WATER_FLOWING.get(), RenderType.translucent());
    }
}
