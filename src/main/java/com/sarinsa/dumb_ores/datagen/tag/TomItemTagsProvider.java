package com.sarinsa.dumb_ores.datagen.tag;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.core.registry.TomBlocks;
import com.sarinsa.dumb_ores.common.tags.TomItemTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TomItemTagsProvider extends ItemTagsProvider {

    public TomItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, Tomfoolery.MODID, existingFileHelper);
    }

    protected void addTags() {
        this.tag(Tags.Items.ORES).add(
                TomBlocks.ORE_ORE.get().asItem(),
                TomBlocks.CAKE_ORE.get().asItem()
        );

        this.tag(TomItemTags.CAKES).add(
                Items.CAKE
        );
    }
}
