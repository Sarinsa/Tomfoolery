package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Function;
import java.util.function.Supplier;

public class TomArmorMaterial implements ArmorMaterial {
    
    private final Function<ArmorItem.Type, Integer> durabilityMultiplier;
    private final int[] defenseForSlot;
    private final int enchantmentValue;
    private final Supplier<SoundEvent> sound;
    private final Supplier<Ingredient> repairMaterial;
    private final String name;
    private final float toughness;
    private final float knockbackRes;
    
    
    public static final TomArmorMaterial NETHERAIGHT = new TomArmorMaterial(
            ( type ) -> 1,
            new int[] { 3, 6, 8, 3 },
            100,
            () -> SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of( Items.NETHERITE_INGOT ),
            "netherite",
            3.0F,
            0.1F
    );
    
    public static final TomArmorMaterial COOL_GLASSES = new TomArmorMaterial(
            ( type ) -> 40,
            new int[] { 1, 1, 1, 1 },
            100,
            () -> SoundEvents.GLASS_BREAK,
            () -> Ingredient.of( Items.DIRT ),
            name( "dirt_glasses" ),
            0.0F,
            -0.3F
    );
    
    
    private TomArmorMaterial( Function<ArmorItem.Type, Integer> durabilityMultiplier, int[] defenseForSlot, int enchantmentValue, Supplier<SoundEvent> sound, Supplier<Ingredient> repairMaterial, String name, float toughness, float knockbackRes ) {
        this.durabilityMultiplier = durabilityMultiplier;
        this.defenseForSlot = defenseForSlot;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.repairMaterial = repairMaterial;
        this.name = name;
        this.toughness = toughness;
        this.knockbackRes = knockbackRes;
    }
    
    @Override
    public int getDurabilityForType( ArmorItem.Type type ) {
        return durabilityMultiplier.apply( type );
    }
    
    @Override
    public int getDefenseForType( ArmorItem.Type type ) {
        return defenseForSlot[type.ordinal()];
    }
    
    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }
    
    @Override
    public SoundEvent getEquipSound() {
        return sound.get();
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public float getToughness() {
        return toughness;
    }
    
    @Override
    public float getKnockbackResistance() {
        return knockbackRes;
    }
    
    @SuppressWarnings( "SameParameterValue" )
    private static String name( String name ) {
        return Tomfoolery.rl( name ).toString();
    }
}
