package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Function;
import java.util.function.Supplier;

public class TomArmorMaterial implements IArmorMaterial {

    private final Function<EquipmentSlotType, Integer> durabilityMultiplier;
    private final int[] defenseForSlot;
    private final int enchantmentValue;
    private final Supplier<SoundEvent> sound;
    private final Supplier<Ingredient> repairMaterial;
    private final String name;
    private final float toughness;
    private final float knockbackRes;




    public static final TomArmorMaterial NETHERAIGHT = new TomArmorMaterial(
            (equipmentSlotType) -> 1,
            new int[] {3, 6, 8, 3},
            100,
            () -> SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(Items.NETHERITE_INGOT),
            "netherite",
            3.0F,
            0.1F
    );




    private TomArmorMaterial(Function<EquipmentSlotType, Integer> durabilityMultiplier, int[] defenseForSlot, int enchantmentValue, Supplier<SoundEvent> sound, Supplier<Ingredient> repairMaterial, String name, float toughness, float knockbackRes) {
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
    public int getDurabilityForSlot(EquipmentSlotType equipmentSlotType) {
        return durabilityMultiplier.apply(equipmentSlotType);
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType equipmentSlotType) {
        return defenseForSlot[equipmentSlotType.getIndex()];
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

    private static String name(String name) {
        return Tomfoolery.resourceLoc(name).toString();
    }
}
