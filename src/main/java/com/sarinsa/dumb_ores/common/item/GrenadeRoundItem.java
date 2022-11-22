package com.sarinsa.dumb_ores.common.item;

import com.sarinsa.dumb_ores.common.core.registry.types.GrenadeType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.function.Supplier;

public class GrenadeRoundItem extends Item {

    private final Supplier<GrenadeType> grenadeType;

    public GrenadeRoundItem(Supplier<GrenadeType> grenadeType) {
        super(new Item.Properties()
                .stacksTo(6)
                .tab(ItemGroup.TAB_COMBAT));

        this.grenadeType = grenadeType;
    }

    public final int getColor(int color) {
        return grenadeType.get().getColor(color);
    }

    public GrenadeType getGrenadeType() {
        return grenadeType.get();
    }
}
