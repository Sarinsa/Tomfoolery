package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.common.event.EntityEventsListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class CoolGlassesItem extends ArmorItem {
    
    public CoolGlassesItem() {
        super( TomArmorMaterial.COOL_GLASSES, Type.HELMET, new Item.Properties().stacksTo( 1 ).rarity( Rarity.UNCOMMON ) );
    }
    
    /**
     * Called from {@link EntityEventsListener#onPlayerUpdate(LivingEvent.LivingTickEvent)}
     * every tick when the player is wearing this item on their head.
     */
    public abstract void gaze( Player player, Level level, BlockHitResult hitResult );
    
    /**
     * @return The effective range of these glasses in blocks.
     */
    public double getRange() {
        return 50;
    }
}
