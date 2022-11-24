package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.effect.CactusAttractionEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TomEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Tomfoolery.MODID);

    public static final RegistryObject<Effect> CACTUS_ATTRACTION = registerEffect("cactus_attraction", CactusAttractionEffect::new);


    private static <T extends Effect> RegistryObject<T> registerEffect(String name, Supplier<T> effectSuppler) {
        return EFFECTS.register(name, effectSuppler);
    }
}
