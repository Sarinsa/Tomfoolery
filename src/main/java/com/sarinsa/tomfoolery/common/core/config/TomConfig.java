package com.sarinsa.tomfoolery.common.core.config;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import fathertoast.crust.api.config.common.ConfigManager;

public class TomConfig {
    
    /** General settings. */
    public static final GeneralConfig GENERAL = new GeneralConfig( ConfigManager.getRequired( Tomfoolery.MODID ), "general" );
    
    
    /** Performs initial loading of our configs. */
    public static void initialize() {
        ConfigManager manager = ConfigManager.getRequired( Tomfoolery.MODID );
        manager.freezeFileWatcher = true;
        
        GENERAL.SPEC.initialize();
        
        manager.freezeFileWatcher = false;
    }
}
