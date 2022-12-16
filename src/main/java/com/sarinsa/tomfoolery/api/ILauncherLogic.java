package com.sarinsa.tomfoolery.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ILauncherLogic {

    /**
     * Called when the grenade launcher is fired. This method
     * is responsible for the logic for the ammo item associated
     * with this ILauncherLogic.<br>
     * <br>
     *
     * @param player The player using the launcher.
     */
    void onLaunch(World world, PlayerEntity player, Hand hand);
}
