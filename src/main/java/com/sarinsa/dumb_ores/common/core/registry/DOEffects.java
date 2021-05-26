package com.sarinsa.dumb_ores.common.core.registry;

import com.sarinsa.dumb_ores.common.core.DumbOres;
import com.sarinsa.dumb_ores.common.effect.CactusAttractionEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DOEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, DumbOres.MODID);

    public static final RegistryObject<Effect> CACTUS_ATTRACTION = registerEffect("cactus_attraction", CactusAttractionEffect::new);


    private static <T extends Effect> RegistryObject<T> registerEffect(String name, Supplier<T> effectSuppler) {
        return EFFECTS.register(name, effectSuppler);
    }
}
