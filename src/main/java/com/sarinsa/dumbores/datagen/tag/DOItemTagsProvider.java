package com.sarinsa.dumbores.datagen.tag;

import com.sarinsa.dumbores.common.core.registry.DOBlocks;
import com.sarinsa.dumbores.common.core.DumbOres;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class DOItemTagsProvider extends ItemTagsProvider {

    public DOItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, DumbOres.MODID, existingFileHelper);
    }

    protected void addTags() {
        this.tag(Tags.Items.ORES).add(DOBlocks.ORE_ORE.get().asItem());
        this.tag(Tags.Items.ORES).add(DOBlocks.CAKE_ORE.get().asItem());
    }
}
