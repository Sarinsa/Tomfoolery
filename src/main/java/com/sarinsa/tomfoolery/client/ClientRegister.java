package com.sarinsa.tomfoolery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sarinsa.tomfoolery.client.render.entity.buffcat.BuffcatEntityModel;
import com.sarinsa.tomfoolery.client.render.entity.buffcat.BuffcatEntityRenderer;
import com.sarinsa.tomfoolery.client.render.entity.cactus.CactusEntityRenderer;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundModel;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundRenderer;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.item.GrenadeRoundItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        TomfooleryModelLayers.init();
        setBlockRenderTypes();
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (Supplier<GrenadeRoundItem> itemSupplier : TomItems.GRENADE_AMMO) {
            event.register((itemStack, color) -> itemSupplier.get().getColor(color), itemSupplier.get());
        }
    }

    private static void setBlockRenderTypes() {

    }

    @SubscribeEvent
    public static void registerLayerDefs(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TomfooleryModelLayers.GRENADE_ROUND, GrenadeRoundModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TomEntities.CACTUS_BLOCK_ENTITY.get(), CactusEntityRenderer::new);
        event.registerEntityRenderer(TomEntities.GRENADE_ROUND.get(), GrenadeRoundRenderer::new);
        event.registerEntityRenderer(TomEntities.INSTA_SAPLING.get(), (context) -> new ThrownItemRenderer<>(context, 1.75F, true));
    }
}
