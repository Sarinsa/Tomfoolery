package com.sarinsa.dumb_ores.common.misc.mixin;

import com.sarinsa.dumb_ores.common.core.config.TomClientConfig;

public class ClientMixinHooks {

    public static float getRendererFlipDegrees(float original) {
        if (TomClientConfig.CLIENT.deathFlipDegreesEnabled()) {
            return TomClientConfig.CLIENT.getDeathFlipDegrees();
        }
        return original;
    }
}
