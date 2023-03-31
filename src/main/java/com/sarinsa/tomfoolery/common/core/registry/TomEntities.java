package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.CactusBlockEntity;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.entity.InstaSaplingEntity;
import com.sarinsa.tomfoolery.common.entity.living.BuffcatEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TomEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Tomfoolery.MODID);

    public static final RegistryObject<EntityType<CactusBlockEntity>> CACTUS_BLOCK_ENTITY = register("cactus_block_entity", EntityType.Builder.<CactusBlockEntity>of(CactusBlockEntity::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0F, 1.0F)
            .clientTrackingRange(10)
            .updateInterval(5));
    public static final RegistryObject<EntityType<GrenadeRoundEntity>> GRENADE_ROUND = register("grenade_round", EntityType.Builder.<GrenadeRoundEntity>of(GrenadeRoundEntity::new, MobCategory.MISC)
            .sized(0.2F, 0.2F)
            .clientTrackingRange(6)
            .updateInterval(20));
    public static final RegistryObject<EntityType<InstaSaplingEntity>> INSTA_SAPLING = register("insta_sapling", EntityType.Builder.<InstaSaplingEntity>of(InstaSaplingEntity::new, MobCategory.MISC)
            .sized(0.2F, 0.2F)
            .clientTrackingRange(6)
            .updateInterval(20));

    public static final RegistryObject<EntityType<BuffcatEntity>> BUFFCAT = register("buffcat", EntityType.Builder.of(BuffcatEntity::new, MobCategory.CREATURE)
            .sized(1.4F, 2.0F)
            .noSummon());


    /**
     * Called during the EntityAttributeCreationEvent.
     * Our entities' attributes are created here.
     */
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BUFFCAT.get(), BuffcatEntity.createBuffcatAttributes().build());
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
