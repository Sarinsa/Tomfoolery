package com.sarinsa.tomfoolery.client;

import com.sarinsa.tomfoolery.client.render.entity.cactus.CactusEntityRenderer;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundModel;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundRenderer;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.item.GrenadeRoundItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IArmPoseTransformer;
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
        event.registerEntityRenderer(TomEntities.LAUNCHED_TORCH.get(), (context) -> new ThrownItemRenderer<>(context, 1.75F, true));
        event.registerEntityRenderer(TomEntities.INSTA_SAPLING.get(), (context) -> new ThrownItemRenderer<>(context, 1.75F, true));
    }
}
