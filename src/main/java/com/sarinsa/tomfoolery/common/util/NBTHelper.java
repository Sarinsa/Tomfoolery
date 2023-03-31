package com.sarinsa.tomfoolery.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class NBTHelper {

    private static final String MOD_DATA_KEY = "TomfooleryModData";

    public static void markEntityCactusAttr(LivingEntity livingEntity, boolean marked) {
        if (livingEntity == null)
            return;

        if (livingEntity.getPersistentData().contains(MOD_DATA_KEY, Tag.TAG_COMPOUND)) {
            livingEntity.getPersistentData().getCompound(MOD_DATA_KEY).putBoolean("CactusMarked", marked);
        }
        else {
            CompoundTag modData = new CompoundTag();
            modData.putBoolean("CactusMarked", marked);
            livingEntity.getPersistentData().put(MOD_DATA_KEY, modData);
        }
    }

    public static boolean isEntityCactusMarked(LivingEntity livingEntity) {
        if (livingEntity.getPersistentData().contains(MOD_DATA_KEY, Tag.TAG_COMPOUND)) {
            return livingEntity.getPersistentData().getCompound(MOD_DATA_KEY).contains("CactusMarked", Tag.TAG_BYTE)
                    && livingEntity.getPersistentData().getCompound(MOD_DATA_KEY).getBoolean("CactusMarked");
        }
        return false;
    }
}
