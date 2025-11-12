package com.sarinsa.tomfoolery.datagen.tag;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.tags.TomItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TomItemTagsProvider extends ItemTagsProvider {
    
    public TomItemTagsProvider( DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper ) {
        super( dataGenerator.getPackOutput(), provider, blockTagProvider, Tomfoolery.MODID, existingFileHelper );
    }
    
    protected void addTags( HolderLookup.Provider provider ) {
        this.tag( Tags.Items.ORES ).add(
                TomBlocks.ORE_ORE.get().asItem(),
                TomBlocks.CAKE_ORE.get().asItem()
        );
        
        this.tag( TomItemTags.CAKES ).add(
                Items.CAKE
        );
    }
}
