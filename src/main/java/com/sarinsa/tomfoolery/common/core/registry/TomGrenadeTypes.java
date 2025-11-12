package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.grenades.DoomGrenadeType;
import com.sarinsa.tomfoolery.common.grenades.ExplosiveGrenadeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TomGrenadeTypes {
    
    public static final DeferredRegister<GrenadeType> GRENADE_TYPES = DeferredRegister.create( Tomfoolery.rl( "grenade_types" ), Tomfoolery.MODID );
    
    public static final Supplier<IForgeRegistry<GrenadeType>> GRENADE_TYPE_REGISTRY = GRENADE_TYPES.makeRegistry(
            () -> (new RegistryBuilder<GrenadeType>())
                    .setDefaultKey( Tomfoolery.rl( "empty" ) ) );
    
    
    public static RegistryObject<GrenadeType> EXPLOSIVE = register( "explosive", ExplosiveGrenadeType::new );
    public static RegistryObject<GrenadeType> DOOM = register( "doom", DoomGrenadeType::new );
    
    
    private static RegistryObject<GrenadeType> register( String name, Supplier<GrenadeType> supplier ) {
        return GRENADE_TYPES.register( name, supplier );
    }
    
    public static GrenadeType getOrDefault( ResourceLocation id ) {
        for( GrenadeType type : GRENADE_TYPE_REGISTRY.get().getValues() ) {
            // noinspection ConstantConditions
            if( GRENADE_TYPE_REGISTRY.get().getKey( type ).equals( id ) )
                return type;
        }
        return EXPLOSIVE.get();
    }
}
