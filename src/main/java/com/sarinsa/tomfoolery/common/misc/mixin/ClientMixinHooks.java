package com.sarinsa.tomfoolery.common.misc.mixin;

import com.sarinsa.tomfoolery.common.core.config.TomClientConfig;

public class ClientMixinHooks {

    public static float getRendererFlipDegrees(float original) {
        if (TomClientConfig.CLIENT.deathFlipDegreesEnabled()) {
            return TomClientConfig.CLIENT.getDeathFlipDegrees();
        }
        return original;
    }
}
