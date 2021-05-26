package com.sarinsa.dumb_ores.common.misc.mixin;

import com.sarinsa.dumb_ores.common.core.config.DOClientConfig;

public class ClientMixinHooks {

    public static float getRendererFlipDegrees(float original) {
        if (DOClientConfig.CLIENT.deathFlipDegreesEnabled()) {
            return DOClientConfig.CLIENT.getDeathFlipDegrees();
        }
        return original;
    }
}
