package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TomSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Tomfoolery.MODID);


    public static final RegistryObject<SoundEvent> LAUNCHER_THUMP = register("item.grenade_launcher.thump");


    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(Tomfoolery.resourceLoc(name)));
    }
}
