package com.sarinsa.dumb_ores.common.capability.cactus;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CactusAttractCapabilityStorage implements Capability.IStorage<ICactusAttractCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ICactusAttractCapability> capability, ICactusAttractCapability instance, Direction side) {
        return ByteNBT.valueOf(instance.getMarked());
    }

    @Override
    public void readNBT(Capability<ICactusAttractCapability> capability, ICactusAttractCapability instance, Direction side, INBT nbt) {
        if (nbt.getType() != ByteNBT.TYPE) {
            Tomfoolery.LOGGER.error("Failed to read difficulty capability data! The parsed data must be of type ByteNBT");
            return;
        }
        ByteNBT tag = (ByteNBT) nbt;
        instance.setMarked(tag.getAsByte() == 1);
    }
}