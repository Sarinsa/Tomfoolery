package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;

public class TomDamageTypes {
    
    public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create( Registries.DAMAGE_TYPE, Tomfoolery.MODID );
    
    
    public static final ResourceKey<DamageType> GRENADE = create( "grenade" );
    
    
    public static DamageSource of( Level level, ResourceKey<DamageType> key ) {
        return new DamageSource( level.registryAccess().registryOrThrow( Registries.DAMAGE_TYPE ).getHolderOrThrow( key ) );
    }
    
    public static DamageSource of( Level level, ResourceKey<DamageType> key, Entity entity, @Nullable Entity shooter ) {
        return new DamageSource( level.registryAccess().registryOrThrow( Registries.DAMAGE_TYPE ).getHolderOrThrow( key ), entity, shooter );
    }
    
    @SuppressWarnings( "SameParameterValue" )
    private static ResourceKey<DamageType> create( String name ) {
        return ResourceKey.create( Registries.DAMAGE_TYPE, Tomfoolery.rl( name ) );
    }
    
    public static void bootstrap( BootstapContext<DamageType> context ) {
        register( context, GRENADE, new DamageType( msg( "quicksand" ), 0.0F ) );
    }
    
    protected static void register( BootstapContext<DamageType> context, ResourceKey<DamageType> damageTypeKey, DamageType damageType ) {
        context.register( damageTypeKey, damageType );
    }
    
    @SuppressWarnings( "SameParameterValue" )
    private static String msg( String name ) {
        return Tomfoolery.MODID + "." + name;
    }
}
