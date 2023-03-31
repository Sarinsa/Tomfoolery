package com.sarinsa.tomfoolery.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class CoolGlassesItem extends ArmorItem {

    public CoolGlassesItem() {
        super(TomArmorMaterial.COOL_GLASSES, EquipmentSlot.HEAD, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.UNCOMMON));
    }

    /**
     * Called from {@link com.sarinsa.tomfoolery.common.event.EntityEvents#onPlayerUpdate(LivingEvent.LivingTickEvent)}
     * every tick when the player is wearing this item on their head.
     */
    public abstract void gaze(Player player, Level level, BlockHitResult hitResult);

    /**
     * @return The effective range of these glasses in blocks.
     */
    public double getRange() {
        return 50;
    }
}
