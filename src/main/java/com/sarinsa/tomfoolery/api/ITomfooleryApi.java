package com.sarinsa.tomfoolery.api;

/**
 * Interface representing the Tomfoolery API.
 * All methods, shared fields and whatnot from the
 * API implementation should be exposed here.
 */
public interface ITomfooleryApi {

    /**
     * @return The IRegistryHelper instance from Tomfoolery
     */
    IRegistryHelper getRegistryHelper();
}
