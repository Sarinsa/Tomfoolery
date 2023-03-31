package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class GrenadeRoundItem extends Item {

    private final Supplier<GrenadeType> grenadeType;

    public GrenadeRoundItem(Supplier<GrenadeType> grenadeType) {
        super(new Item.Properties()
                .stacksTo(6)
                .tab(CreativeModeTab.TAB_COMBAT));

        this.grenadeType = grenadeType;
    }

    public final int getColor(int color) {
        return grenadeType.get().getColor(color);
    }

    public GrenadeType getGrenadeType() {
        return grenadeType.get();
    }
}
