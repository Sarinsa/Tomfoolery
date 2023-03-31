package com.sarinsa.tomfoolery.common.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class TomItemTags {

    public static final TagKey<Item> CAKES = forgeTag("cakes");

    private static TagKey<Item> forgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

    public static void init() {}
}
