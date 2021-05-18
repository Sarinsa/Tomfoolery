package com.sarinsa.dumbores.common.core.registry;

import com.sarinsa.dumbores.common.core.DumbOres;
import com.sarinsa.dumbores.common.entity.CactusBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DOEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, DumbOres.MODID);

    public static final RegistryObject<EntityType<CactusBlockEntity>> CACTUS_BLOCK_ENTITY = register("cactus_block_entity", EntityType.Builder.<CactusBlockEntity>of(CactusBlockEntity::new, EntityClassification.MISC).noSummon().sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(5));

    /**
     * Initializing entity types for living entities in the mod class
     * constructor so that the entity types can be used for the spawn egg items
     * before entity types are registered.
     * Probably better ways of doing this, but it works.
     */
    public static void initTypes() {

    }


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

    private static <I extends Entity> RegistryObject<EntityType<I>> register(String name, Supplier<EntityType<I>> entityTypeSupplier) {
        return ENTITIES.register(name, entityTypeSupplier);
    }

    private static <I extends Entity> EntityType<I> create(String name, EntityType.Builder<I> builder) {
        return builder.build(name);
    }
}
