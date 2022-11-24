package com.sarinsa.tomfoolery.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class CoolGlassesItem extends ArmorItem {

    public CoolGlassesItem() {
        super(TomArmorMaterial.COOL_GLASSES, EquipmentSlotType.HEAD, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC).rarity(Rarity.UNCOMMON));
    }

    public abstract void gaze(PlayerEntity player, World world, BlockRayTraceResult traceResult);
}
