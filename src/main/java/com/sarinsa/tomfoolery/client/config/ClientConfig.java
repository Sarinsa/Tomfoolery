package com.sarinsa.tomfoolery.client.config;

import fathertoast.crust.api.config.common.AbstractConfigCategory;
import fathertoast.crust.api.config.common.AbstractConfigFile;
import fathertoast.crust.api.config.common.ConfigManager;
import fathertoast.crust.api.config.common.field.BooleanField;
import fathertoast.crust.api.config.common.field.DoubleField;

public class ClientConfig extends AbstractConfigFile {
    public final Misc MISC;
    
    
    /** Builds the config spec that should be used for this config. */
    public ClientConfig( ConfigManager cfgManager, String cfgName ) {
        super( cfgManager, cfgName,
                "This config contains various client-sided settings."
        );
        MISC = new Misc( this );
    }
    
    public static class Misc extends AbstractConfigCategory<ClientConfig> {
        
        public final BooleanField overrideDeathFlip;
        public final DoubleField deathRotationDegrees;
        
        Misc( ClientConfig parent ) {
            super( parent, "misc",
                    "Misc settings." );
            
            overrideDeathFlip = SPEC.define( new BooleanField( "override_death_flip", true,
                    "If enabled, Tomfoolery will override vanilla Minecraft's death-flip animation that plays when an entity dies." ) );
            
            deathRotationDegrees = SPEC.define( new DoubleField( "death_rotation_degrees", 1000.0D, -10000.0D, 10000.0D,
                    "If 'rotate_on_death' is enabled, this value will override how many degrees entities rotate when they die." ) );
        }
    }
}
