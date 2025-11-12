package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TomSounds {
    
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create( ForgeRegistries.SOUND_EVENTS, Tomfoolery.MODID );
    
    
    public static final RegistryObject<SoundEvent> LAUNCHER_THUMP = register( "item.grenade_launcher.thump" );
    
    
    @SuppressWarnings( "SameParameterValue" )
    private static RegistryObject<SoundEvent> register( String name ) {
        return SOUNDS.register( name, () -> SoundEvent.createVariableRangeEvent( Tomfoolery.rl( name ) ) );
    }
}
