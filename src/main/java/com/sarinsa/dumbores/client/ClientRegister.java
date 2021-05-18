package com.sarinsa.dumbores.client;

import com.sarinsa.dumbores.common.core.DumbOres;
import com.sarinsa.dumbores.common.core.registry.DOEntities;
import com.sarinsa.dumbores.client.render.entity.cactus.CactusEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DumbOres.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        setBlockRenderTypes();
        registerEntityRenderers();
    }

    private static void setBlockRenderTypes() {

    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(DOEntities.CACTUS_BLOCK_ENTITY.get(), CactusEntityRenderer::new);
    }
}
