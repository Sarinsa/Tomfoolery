package com.sarinsa.tomfoolery.common.core.config;

import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import fathertoast.crust.api.config.common.AbstractConfigCategory;
import fathertoast.crust.api.config.common.AbstractConfigFile;
import fathertoast.crust.api.config.common.ConfigManager;
import fathertoast.crust.api.config.common.field.*;
import fathertoast.crust.api.config.common.value.*;
import fathertoast.crust.api.config.common.value.environment.time.DayTimeEnvironment;
import fathertoast.crust.api.config.common.value.environment.time.MoonPhaseEnvironment;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class GeneralConfig extends AbstractConfigFile {
    
    public final Rediculauncher REDICULAUNCHER;
    public final Ghastinator GHASTINATOR;
    
    /** Builds the config spec that should be used for this config. */
    public GeneralConfig( ConfigManager cfgManager, String cfgName ) {
        super( cfgManager, cfgName,
                "This config contains general settings for the mod."
        );
        
        SPEC.fileOnlyNewLine();
        SPEC.describeRegistryEntryList();
        SPEC.fileOnlyNewLine();
        SPEC.describeEntityList();
        SPEC.fileOnlyNewLine();
        SPEC.describeEnvironmentListPart1of2();
        SPEC.fileOnlyNewLine();
        
        REDICULAUNCHER = new Rediculauncher( this );
        GHASTINATOR = new Ghastinator( this );
        
        SPEC.fileOnlyNewLine();
        SPEC.describeEnvironmentListPart2of2();
        SPEC.fileOnlyNewLine();
    }
    
    public static class Rediculauncher extends AbstractConfigCategory<GeneralConfig> {
        
        public final RegistryEntryListField<GrenadeType> blacklistedGrenades;
        
        public final EntityListField launcherWielders;
        
        
        Rediculauncher( GeneralConfig parent ) {
            super( parent, "grenades",
                    "Contains settings related to the Redicu-launcher, grenades and other ammo types." );
            
            blacklistedGrenades = SPEC.define( new RegistryEntryListField<>( "blacklisted_grenades", createDefaultBlacklistedGrenades(),
                    "A list of grenade types that are blacklisted and cannot be used or crafted." ) );
            
            SPEC.newLine();
            
            launcherWielders = SPEC.define( new EntityListField( "launcher_wielders", createDefaultLauncherWielders(),
                    "A list of entity types that can spawn with a Redicu-launcher equipped and AI to use it, as well as the chance for them to spawn with one." ) );
            
            SPEC.newLine();
        }
        
        private RegistryEntryList<GrenadeType> createDefaultBlacklistedGrenades() {
            return new RegistryEntryList<>( TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get() );
        }
        
        private EntityList createDefaultLauncherWielders() {
            return new EntityList( null,
                    new EntityEntry( EntityType.ZOMBIE, 0.02 ),
                    new EntityEntry( EntityType.SKELETON, 0.01 ),
                    new EntityEntry( EntityType.WITHER_SKELETON, 0.01 )
            );
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
                    new EnvironmentEntry( 1, List.of(
                            new MoonPhaseEnvironment( MoonPhaseEnvironment.Value.NEW, false ),
                            new DayTimeEnvironment( DayTimeEnvironment.Value.NIGHT, false )
                    ) )
            );
        }
        
        private EnvironmentList createDefaultDespawnConditions() {
            return new EnvironmentList(
                    new EnvironmentEntry( 1, List.of(
                            new DayTimeEnvironment( DayTimeEnvironment.Value.DAY, false )
                    ) )
            );
        }
    }
}
