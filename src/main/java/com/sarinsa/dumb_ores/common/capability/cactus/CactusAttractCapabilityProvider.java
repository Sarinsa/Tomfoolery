package com.sarinsa.dumb_ores.common.capability.cactus;

import com.sarinsa.dumb_ores.common.capability.TomCapabilities;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class CactusAttractCapabilityProvider implements ICapabilitySerializable<ByteNBT> {

    private final ICactusAttractCapability INSTANCE = TomCapabilities.CACTUS_ATTRACT_CAPABILITY.getDefaultInstance();
    private final LazyOptional<ICactusAttractCapability> optional = LazyOptional.of(() -> INSTANCE);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TomCapabilities.CACTUS_ATTRACT_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public ByteNBT serializeNBT() {
        return (ByteNBT) TomCapabilities.CACTUS_ATTRACT_CAPABILITY.getStorage().writeNBT(TomCapabilities.CACTUS_ATTRACT_CAPABILITY, INSTANCE, null);
    }

    @Override
    public void deserializeNBT(ByteNBT nbt) {
        TomCapabilities.CACTUS_ATTRACT_CAPABILITY.getStorage().readNBT(TomCapabilities.CACTUS_ATTRACT_CAPABILITY, INSTANCE, null, nbt);
    }
}
