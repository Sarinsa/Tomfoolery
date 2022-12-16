package com.sarinsa.tomfoolery.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class CoolGlassesItem extends ArmorItem {

    public CoolGlassesItem() {
        super(TomArmorMaterial.COOL_GLASSES, EquipmentSlotType.HEAD, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC).rarity(Rarity.UNCOMMON));
    }

    /**
     * Called from {@link com.sarinsa.tomfoolery.common.event.EntityEvents#onPlayerUpdate(LivingEvent.LivingUpdateEvent)}
     * every tick when the player is wearing this item on their head.
     */
    public abstract void gaze(PlayerEntity player, World world, BlockRayTraceResult traceResult);

    /**
     * @return The effective range of these glasses in blocks.
     */
    public double getRange() {
        return 50;
    }
}
