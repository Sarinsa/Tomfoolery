package com.sarinsa.tomfoolery.api;


import net.minecraft.resources.ResourceLocation;

/**
 * Modders must implement this interface in order
 * to make a valid Tomfoolery mod plugin.<br>
 * <br>
 * A mod plugin also requires the {@link TomfooleryPlugin} annotation,
 * to be detected and loaded by Tomfoolery.
 */
public interface ITomfooleryPlugin {

    /**
     * Called when mod plugins are loaded ({@link net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent})<br>
     * <br>
     * @param apiInstance Tomfoolery's API implementation
     */
    void onLoad(ITomfooleryApi apiInstance);

    /**
     * This should return a unique ID
     * with your mod's namespace in it.
     */
    ResourceLocation getPluginId();
}
