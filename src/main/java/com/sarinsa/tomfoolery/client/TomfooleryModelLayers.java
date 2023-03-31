package com.sarinsa.tomfoolery.client;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class TomfooleryModelLayers {

    public static final ModelLayerLocation BUFFCAT = create("buffcat");
    public static final ModelLayerLocation GRENADE_ROUND = create("grenade_round");


    private static ModelLayerLocation create(String path) {
        return create(path, "main");
    }

    private static ModelLayerLocation create(String path, String layerName) {
        return new ModelLayerLocation(Tomfoolery.resourceLoc(path), layerName);
    }

    public static void init() {}

    private TomfooleryModelLayers() {}
}
