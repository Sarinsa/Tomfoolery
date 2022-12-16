package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.CactusBlockEntity;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.entity.InstaSaplingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TomEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Tomfoolery.MODID);

    public static final RegistryObject<EntityType<CactusBlockEntity>> CACTUS_BLOCK_ENTITY = register("cactus_block_entity", EntityType.Builder.<CactusBlockEntity>of(CactusBlockEntity::new, EntityClassification.MISC)
            .noSummon()
            .sized(1.0F, 1.0F)
            .clientTrackingRange(10)
            .updateInterval(5));
    public static final RegistryObject<EntityType<GrenadeRoundEntity>> GRENADE_ROUND = register("grenade_round", EntityType.Builder.<GrenadeRoundEntity>of(GrenadeRoundEntity::new, EntityClassification.MISC)
            .sized(0.2F, 0.2F)
            .clientTrackingRange(6)
            .updateInterval(20));
    public static final RegistryObject<EntityType<InstaSaplingEntity>> INSTA_SAPLING = register("insta_sapling", EntityType.Builder.<InstaSaplingEntity>of(InstaSaplingEntity::new, EntityClassification.MISC)
            .sized(0.2F, 0.2F)
            .clientTrackingRange(6)
            .updateInterval(20));

    /**
     * Called during the EntityAttributeCreationEvent.
     * Our entities' attributes are created here.
     */
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {

    }

    /**
     * Called during the FMLCommonSetupEvent.
     * Our entities' spawn placements are registered here.
     */
    public static void registerEntitySpawnPlacement() {

    }

    private static <I extends Entity> RegistryObject<EntityType<I>> register(String name, EntityType.Builder<I> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }
}
