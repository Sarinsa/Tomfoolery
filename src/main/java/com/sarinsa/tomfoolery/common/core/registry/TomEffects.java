package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.mob_effect.CactusAttractionEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TomEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Tomfoolery.MODID);

    public static final RegistryObject<MobEffect> CACTUS_ATTRACTION = registerEffect("cactus_attraction", CactusAttractionEffect::new);


    private static <T extends MobEffect> RegistryObject<T> registerEffect(String name, Supplier<T> effectSuppler) {
        return EFFECTS.register(name, effectSuppler);
    }
}
