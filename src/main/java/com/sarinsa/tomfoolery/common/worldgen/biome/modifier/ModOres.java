package com.sarinsa.tomfoolery.common.worldgen.biome.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum ModOres implements StringRepresentable {
    ORE_ORE("ore_ore"),
    CAKE("cake");

    ModOres(String name) {
        this.name = name;
    }

    public static final Codec<ModOres> CODEC = StringRepresentable.fromEnum(ModOres::values);
    private final String name;

    @Override
    public String getSerializedName() {
        return name;
    }
}
