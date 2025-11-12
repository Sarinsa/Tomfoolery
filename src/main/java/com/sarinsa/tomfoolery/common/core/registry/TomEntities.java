package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.*;
import com.sarinsa.tomfoolery.common.entity.living.Buffcat;
import com.sarinsa.tomfoolery.common.entity.living.Ghastinator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TomEntities {
    
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create( ForgeRegistries.ENTITY_TYPES, Tomfoolery.MODID );
    
    
    public static final RegistryObject<EntityType<HugeFireball>> HUGE_FIREBALL = register( "huge_fireball", EntityType.Builder.<HugeFireball>of( HugeFireball::new, MobCategory.MISC )
            .noSummon()
            .sized( 10.0F, 10.0F )
            .clientTrackingRange( 15 )
            .updateInterval( 10 ) );
    public static final RegistryObject<EntityType<CactusBlockEntity>> CACTUS_BLOCK_ENTITY = register( "cactus_block_entity", EntityType.Builder.<CactusBlockEntity>of( CactusBlockEntity::new, MobCategory.MISC )
            .noSummon()
            .sized( 1.0F, 1.0F )
            .clientTrackingRange( 10 )
            .updateInterval( 5 ) );
    public static final RegistryObject<EntityType<LaunchedTorch>> LAUNCHED_TORCH = register( "launched_torch", EntityType.Builder.<LaunchedTorch>of( LaunchedTorch::new, MobCategory.MISC )
            .noSummon()
            .sized( 1.0F, 1.0F )
            .clientTrackingRange( 10 )
            .updateInterval( 5 ) );
    public static final RegistryObject<EntityType<GrenadeRound>> GRENADE_ROUND = register( "grenade_round", EntityType.Builder.<GrenadeRound>of( GrenadeRound::new, MobCategory.MISC )
            .sized( 0.2F, 0.2F )
            .clientTrackingRange( 6 )
            .updateInterval( 20 ) );
    public static final RegistryObject<EntityType<InstaSapling>> INSTA_SAPLING = register( "insta_sapling", EntityType.Builder.<InstaSapling>of( InstaSapling::new, MobCategory.MISC )
            .sized( 0.2F, 0.2F )
            .clientTrackingRange( 6 )
            .updateInterval( 20 ) );
    public static final RegistryObject<EntityType<Buffcat>> BUFFCAT = register( "buffcat", EntityType.Builder.of( Buffcat::new, MobCategory.CREATURE )
            .sized( 1.4F, 2.0F )
            .noSummon() );
    public static final RegistryObject<EntityType<Ghastinator>> GHASTINATOR = register( "ghastinator", EntityType.Builder.of( Ghastinator::new, MobCategory.MONSTER )
            .sized( 1.0F, 1.0F )
            .fireImmune()
            .clientTrackingRange( 40 ) );
    
    
    /**
     * Called during the EntityAttributeCreationEvent.
     * Our entities' attributes are created here.
     */
    public static void createEntityAttributes( EntityAttributeCreationEvent event ) {
        event.put( BUFFCAT.get(), Buffcat.createBuffcatAttributes().build() );
        event.put( GHASTINATOR.get(), Ghastinator.createGhastinatorAttributes().build() );
    }
    
    /**
     * Called during the FMLCommonSetupEvent.
     * Our entities' spawn placements are registered here.
     */
    public static void registerEntitySpawnPlacement( SpawnPlacementRegisterEvent event ) {
    
    }
    
    private static <I extends Entity> RegistryObject<EntityType<I>> register( String name, EntityType.Builder<I> builder ) {
        return ENTITIES.register( name, () -> builder.build( name ) );
    }
}
