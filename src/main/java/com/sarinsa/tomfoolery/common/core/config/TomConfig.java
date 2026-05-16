package com.sarinsa.tomfoolery.common.core.config;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import fathertoast.crust.api.config.common.ConfigManager;

public class TomConfig {
    
    /** General settings. */
    public static GeneralConfig GENERAL;
    
    
    /** Performs initial loading of our configs. */
    public static void initialize() {
        GENERAL = new GeneralConfig( ConfigManager.getRequired( Tomfoolery.MODID ), "general" );
        GENERAL.SPEC.initialize();
    }
}
