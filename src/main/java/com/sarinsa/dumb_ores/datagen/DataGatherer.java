package com.sarinsa.dumb_ores.datagen;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.datagen.loot_mod.TomLootModsProvider;
import com.sarinsa.dumb_ores.datagen.loot_table.TomLootTableProvider;
import com.sarinsa.dumb_ores.datagen.recipe.TomRecipeProvider;
import com.sarinsa.dumb_ores.datagen.tag.TomBlockTagsProvider;
import com.sarinsa.dumb_ores.datagen.tag.TomItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGatherer {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new TomBlockTagsProvider(dataGenerator, fileHelper);
            dataGenerator.addProvider(blockTagsProvider);
            dataGenerator.addProvider(new TomItemTagsProvider(dataGenerator, blockTagsProvider, fileHelper));
            dataGenerator.addProvider(new TomLootTableProvider(dataGenerator));
            dataGenerator.addProvider(new TomRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new TomLootModsProvider(dataGenerator));
        }
        if (event.includeClient()) {

        }
    }
}
