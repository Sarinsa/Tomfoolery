package com.sarinsa.tomfoolery.common.core.config;

import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import fathertoast.crust.api.config.common.AbstractConfigCategory;
import fathertoast.crust.api.config.common.AbstractConfigFile;
import fathertoast.crust.api.config.common.ConfigManager;
import fathertoast.crust.api.config.common.field.BooleanField;
import fathertoast.crust.api.config.common.field.EnvironmentListField;
import fathertoast.crust.api.config.common.field.IntField;
import fathertoast.crust.api.config.common.field.collection.EntityMapField;
import fathertoast.crust.api.config.common.field.collection.RegistrySetField;
import fathertoast.crust.api.config.common.value.EnvironmentEntry;
import fathertoast.crust.api.config.common.value.EnvironmentList;
import fathertoast.crust.api.config.common.value.collection.EntityMap;
import fathertoast.crust.api.config.common.value.collection.RegistrySet;
import fathertoast.crust.api.config.common.value.collection.value.DoubleValueCodec;
import fathertoast.crust.api.config.common.value.environment.time.DayTimeEnvironment;
import fathertoast.crust.api.config.common.value.environment.time.MoonPhaseEnvironment;
import net.minecraft.world.entity.EntityType;

@SuppressWarnings( "UnstableApiUsage" )
public class GeneralConfig extends AbstractConfigFile {
    
    public final Rediculauncher RIDICULAUNCHER;
    public final Ghastinator GHASTINATOR;
    
    /** Builds the config spec that should be used for this config. */
    public GeneralConfig( ConfigManager cfgManager, String cfgName ) {
        super( cfgManager, cfgName,
                "This config contains general settings for the mod."
        );
        SPEC.describeEnvironmentListPart1of2();
        SPEC.fileOnlyNewLine();
        
        RIDICULAUNCHER = new Rediculauncher( this );
        GHASTINATOR = new Ghastinator( this );
        
        SPEC.fileOnlyNewLine();
        SPEC.describeEnvironmentListPart2of2();
        SPEC.fileOnlyNewLine();
    }
    
    public static class Rediculauncher extends AbstractConfigCategory<GeneralConfig> {
        
        public final RegistrySetField<GrenadeType> blacklistedGrenades;
        
        public final EntityMapField<Double> launcherWielders;
        
        
        Rediculauncher( GeneralConfig parent ) {
            super( parent, "ridiculauncher",
                    "Contains settings related to the Ridicu-launcher, grenades and other ammo types." );
            
            blacklistedGrenades = SPEC.define( new RegistrySetField<>( "blacklisted_grenades", createDefaultBlacklistedGrenades(),
                    "A list of grenade types that are blacklisted and cannot be used by the Ridicu-launcher." ) );
            
            SPEC.newLine();
            
            launcherWielders = SPEC.define( new EntityMapField<>( "launcher_wielders", createDefaultLauncherWielders(),
                    "A list of entity types that can spawn with a Redicu-launcher equipped and AI to use it, as well as the chance for them to spawn with one." ) );
            
            SPEC.newLine();
        }
        
        private RegistrySet<GrenadeType> createDefaultBlacklistedGrenades() {
            return new RegistrySet.Builder<>( TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get() )
                    .build();
        }
        
        private EntityMap<Double> createDefaultLauncherWielders() {
            return new EntityMap.Builder<>( DoubleValueCodec.PERCENT )
                    .put( EntityType.ZOMBIE, 0.02 )
                    .put( EntityType.SKELETON, 0.01 )
                    .put( EntityType.WITHER_SKELETON, 0.01 )
                    .build();
        }
    }
    
    public static class Ghastinator extends AbstractConfigCategory<GeneralConfig> {
        
        public final BooleanField spawnGhastinator;
        public final EnvironmentListField spawnConditions;
        public final EnvironmentListField despawnConditions;
        
        public final IntField explosionPower;
        
        
        Ghastinator( GeneralConfig parent ) {
            super( parent, "ghastinator",
                    "Contains settings related to the Ghastinator." );
            
            spawnGhastinator = SPEC.define( new BooleanField( "spawn_ghastinator", true,
                    "If enabled, the Ghastinator may spawn.",
                    "The Ghastinator is a gigantic, invincible, Ghast-like monster that observes players from far up in the sky.",
                    "It has a ridiculously long line of sight and will fire massive fireballs at any players it spots.",
                    "Ghastinators will not spawn near a player if one already exists within a 200 block radius of the player.",
                    "This feature exists solely to torture players." ) );
            
            spawnConditions = SPEC.define( new EnvironmentListField( "spawn_conditions", createDefaultSpawnConditions(),
                    "A list of environment conditions that must be met for the Ghastinator to spawn.",
                    "If conditions return a value greater than 0, conditions are considered met",
                    "If 'spawn_ghastinator' is disabled, this does nothing." ) );
            
            despawnConditions = SPEC.define( new EnvironmentListField( "despawn_conditions", createDefaultDespawnConditions(),
                    "A list of environment conditions that must be met for Ghastinators to despawn.",
                    "If conditions return a value greater than 0, conditions are considered met." ) );
            
            SPEC.newLine();
            
            explosionPower = SPEC.define( new IntField( "explosion_power", 10, 1, 100,
                    "The explosion power of the Ghastinator's fireballs.",
                    "Note that larger values will not only demolish players, but also their PC-s (heavy lag spike upon exploding)." ) );
        }
        
        private EnvironmentList createDefaultSpawnConditions() {
            return new EnvironmentList(
                    new EnvironmentEntry( 1,
                            new MoonPhaseEnvironment( MoonPhaseEnvironment.Value.NEW, false ),
                            new DayTimeEnvironment( DayTimeEnvironment.Value.NIGHT, false ),
                            new DayTimeEnvironment( DayTimeEnvironment.Value.SUNRISE, true )
                    )
            );
        }
        
        private EnvironmentList createDefaultDespawnConditions() {
            return new EnvironmentList(
                    new EnvironmentEntry( 1,
                            new DayTimeEnvironment( DayTimeEnvironment.Value.DAY, false )
                    ),
                    new EnvironmentEntry( 1,
                            new DayTimeEnvironment( DayTimeEnvironment.Value.SUNRISE, false )
                    )
            );
        }
    }
}
