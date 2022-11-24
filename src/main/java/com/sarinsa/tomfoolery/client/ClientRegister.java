package com.sarinsa.tomfoolery.client;

import com.sarinsa.tomfoolery.client.render.entity.cactus.CactusEntityRenderer;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundRenderer;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.item.GrenadeRoundItem;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        setBlockRenderTypes();
        registerEntityRenderers();
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        for (Supplier<GrenadeRoundItem> itemSupplier : TomItems.GRENADE_AMMO) {
            itemColors.register((itemStack, color) -> itemSupplier.get().getColor(color), itemSupplier.get());
        }
    }

    private static void setBlockRenderTypes() {

    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(TomEntities.CACTUS_BLOCK_ENTITY.get(), CactusEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TomEntities.GRENADE_ROUND.get(), GrenadeRoundRenderer::new);
    }
}
