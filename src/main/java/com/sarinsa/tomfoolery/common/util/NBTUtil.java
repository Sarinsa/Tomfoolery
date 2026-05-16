package com.sarinsa.tomfoolery.common.util;

import fathertoast.crust.api.lib.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

public class NBTUtil {
    
    private static final String KEY_MOD_DATA = "TomfooleryModData";
    
    public static final String KEY_LAUNCHER_MOB = "IsLauncherMob";
    
    
    /** @return True if the given mob is flagged as a Ridicu-launcher wielder mob. */
    public static boolean isLauncherMob( Mob mob ) {
        if( NBTHelper.containsCompound( mob.getPersistentData(), KEY_MOD_DATA ) ) {
            return NBTHelper.containsNumber( mob.getPersistentData().getCompound( KEY_MOD_DATA ), KEY_LAUNCHER_MOB );
        }
        return false;
    }
    
    /** Flags the given mob as being a Ridicu-launcher wielder. */
    public static void setLauncherMob( Mob mob ) {
        CompoundTag modData = NBTHelper.getOrCreateCompound( mob.getPersistentData(), KEY_MOD_DATA );
        modData.putBoolean( KEY_LAUNCHER_MOB, true );
    }
}
