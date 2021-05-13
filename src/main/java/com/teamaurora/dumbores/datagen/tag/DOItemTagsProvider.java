package com.teamaurora.dumbores.datagen.tag;

import com.teamaurora.dumbores.common.core.DumbOres;
import com.teamaurora.dumbores.common.core.registry.DOBlocks;
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
