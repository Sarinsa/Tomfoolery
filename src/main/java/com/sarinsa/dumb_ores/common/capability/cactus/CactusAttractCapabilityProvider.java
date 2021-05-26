package com.sarinsa.dumb_ores.common.capability.cactus;

import com.sarinsa.dumb_ores.common.capability.DOCapabilities;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class CactusAttractCapabilityProvider implements ICapabilitySerializable<ByteNBT> {

    private final ICactusAttractCapability INSTANCE = DOCapabilities.CACTUS_ATTRACT_CAPABILITY.getDefaultInstance();
    private final LazyOptional<ICactusAttractCapability> optional = LazyOptional.of(() -> INSTANCE);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == DOCapabilities.CACTUS_ATTRACT_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public ByteNBT serializeNBT() {
        return (ByteNBT) DOCapabilities.CACTUS_ATTRACT_CAPABILITY.getStorage().writeNBT(DOCapabilities.CACTUS_ATTRACT_CAPABILITY, INSTANCE, null);
    }

    @Override
    public void deserializeNBT(ByteNBT nbt) {
        DOCapabilities.CACTUS_ATTRACT_CAPABILITY.getStorage().readNBT(DOCapabilities.CACTUS_ATTRACT_CAPABILITY, INSTANCE, null, nbt);
    }
}
