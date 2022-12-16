package com.sarinsa.tomfoolery.api.impl;

import com.sarinsa.tomfoolery.api.IRegistryHelper;
import com.sarinsa.tomfoolery.api.ITomfooleryApi;

public class TomfooleryAPI implements ITomfooleryApi {

    private final IRegistryHelper registryHelper = new RegistryHelper();

    @Override
    public IRegistryHelper getRegistryHelper() {
        return registryHelper;
    }
}
