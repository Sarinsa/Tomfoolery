package com.sarinsa.tomfoolery.common.misc.mixin;

import com.sarinsa.tomfoolery.client.ClientRegister;

public class ClientMixinHooks {
    
    public static float getRendererFlipDegrees( float original ) {
        if( ClientRegister.CLIENT_CONFIG.MISC.overrideDeathFlip.get() ) {
            return ClientRegister.CLIENT_CONFIG.MISC.deathRotationDegrees.getFloat();
        }
        return original;
    }
}
