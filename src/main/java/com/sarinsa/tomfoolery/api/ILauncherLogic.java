package com.sarinsa.tomfoolery.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ILauncherLogic {

    /**
     * Called when the grenade launcher is fired. This method
     * is responsible for the logic for the ammo item associated
     * with this ILauncherLogic.<br>
     * <br>
     *
     * @param player The player using the launcher.
     */
    void onLaunch(Level level, Player player, InteractionHand hand);
}
