package com.sarinsa.dumbores.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class DOItemTags {

    public static final Tags.IOptionalNamedTag<Item> CAKES = forgeTag("cakes");

    private static Tags.IOptionalNamedTag<Item> forgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation("forge", path));
    }

    public static void init() {}
}
