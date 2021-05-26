package com.sarinsa.dumb_ores.common.capability.cactus;

public class DefaultCactusAttractCapability implements ICactusAttractCapability {

    private boolean marked;

    @Override
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public boolean getMarked() {
        return this.marked;
    }
}
