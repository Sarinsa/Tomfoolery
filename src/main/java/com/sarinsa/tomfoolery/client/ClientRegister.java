package com.sarinsa.tomfoolery.client;

import com.sarinsa.tomfoolery.client.config.ClientConfig;
import com.sarinsa.tomfoolery.client.render.entity.TomArmPoses;
import com.sarinsa.tomfoolery.client.render.entity.cactus.CactusEntityRenderer;
import com.sarinsa.tomfoolery.client.render.entity.ghastinator.GhastinatorModel;
import com.sarinsa.tomfoolery.client.render.entity.ghastinator.GhastinatorRenderer;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundModel;
import com.sarinsa.tomfoolery.client.render.entity.grenade.GrenadeRoundRenderer;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.item.GrenadeRoundItem;
import fathertoast.crust.api.config.client.ClientConfigUtil;
import fathertoast.crust.api.config.common.ConfigManager;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber( value = Dist.CLIENT, modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ClientRegister {
    
    /** Tomfoolery's client config. NOT AVAILABLE until client setup. */
    public static ClientConfig CLIENT_CONFIG;
    
    @SubscribeEvent
    public static void onClientSetup( FMLClientSetupEvent event ) {
        CLIENT_CONFIG = new ClientConfig(
                ConfigManager.getRequired( Tomfoolery.MODID ), "client_settings" );
        
        CLIENT_CONFIG.SPEC.initialize();
        
        ClientConfigUtil.registerConfigButtonAsEditScreen( ModList.get().getModContainerById( Tomfoolery.MODID ).orElseThrow() );
        
        TomfooleryModelLayers.init();
        TomArmPoses.init();
    }
    
    @SubscribeEvent
    public static void registerItemColors( RegisterColorHandlersEvent.Item event ) {
        for( Supplier<GrenadeRoundItem> itemSupplier : TomItems.GRENADE_AMMO ) {
            event.register( ( itemStack, color ) -> itemSupplier.get().getColor( color ), itemSupplier.get() );
        }
    }
    
    
    @SubscribeEvent
    public static void registerLayerDefs( EntityRenderersEvent.RegisterLayerDefinitions event ) {
        event.registerLayerDefinition( TomfooleryModelLayers.GRENADE_ROUND, GrenadeRoundModel::createBodyLayer );
        event.registerLayerDefinition( TomfooleryModelLayers.GHASTINATOR, GhastinatorModel::createBodyLayer );
    }
    
    @SubscribeEvent
    public static void registerEntityRenderers( EntityRenderersEvent.RegisterRenderers event ) {
        event.registerEntityRenderer( TomEntities.CACTUS_BLOCK_ENTITY.get(), CactusEntityRenderer::new );
        event.registerEntityRenderer( TomEntities.GRENADE_ROUND.get(), GrenadeRoundRenderer::new );
        event.registerEntityRenderer( TomEntities.LAUNCHED_TORCH.get(), ( context ) -> new ThrownItemRenderer<>( context, 1.75F, true ) );
        event.registerEntityRenderer( TomEntities.INSTA_SAPLING.get(), ( context ) -> new ThrownItemRenderer<>( context, 1.75F, true ) );
        event.registerEntityRenderer( TomEntities.GHASTINATOR.get(), GhastinatorRenderer::new );
        event.registerEntityRenderer( TomEntities.HUGE_FIREBALL.get(), ( context ) -> new ThrownItemRenderer<>( context, 20.0F, true ) );
    }
}
