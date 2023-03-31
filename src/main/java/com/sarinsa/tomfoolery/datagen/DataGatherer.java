package com.sarinsa.tomfoolery.datagen;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.datagen.loot_mod.TomLootModsProvider;
import com.sarinsa.tomfoolery.datagen.loot_table.TomLootTableProvider;
import com.sarinsa.tomfoolery.datagen.recipe.TomRecipeProvider;
import com.sarinsa.tomfoolery.datagen.tag.TomBlockTagsProvider;
import com.sarinsa.tomfoolery.datagen.tag.TomItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGatherer {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new TomBlockTagsProvider(dataGenerator, fileHelper);
            dataGenerator.addProvider(true, blockTagsProvider);
            dataGenerator.addProvider(true, new TomItemTagsProvider(dataGenerator, blockTagsProvider, fileHelper));
            dataGenerator.addProvider(true, new TomLootTableProvider(dataGenerator));
            dataGenerator.addProvider(true, new TomRecipeProvider(dataGenerator));
            dataGenerator.addProvider(true, new TomLootModsProvider(dataGenerator));
        }
        if (event.includeClient()) {

        }
    }
}
