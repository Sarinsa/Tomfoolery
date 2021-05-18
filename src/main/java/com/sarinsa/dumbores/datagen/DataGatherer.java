package com.sarinsa.dumbores.datagen;

import com.sarinsa.dumbores.common.core.DumbOres;
import com.sarinsa.dumbores.datagen.loot_table.DOLootTableProvider;
import com.sarinsa.dumbores.datagen.recipe.DORecipeProvider;
import com.sarinsa.dumbores.datagen.tag.DOBlockTagsProvider;
import com.sarinsa.dumbores.datagen.tag.DOItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = DumbOres.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGatherer {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new DOBlockTagsProvider(dataGenerator, existingFileHelper);
            dataGenerator.addProvider(blockTagsProvider);
            dataGenerator.addProvider(new DOItemTagsProvider(dataGenerator, blockTagsProvider, existingFileHelper));
            dataGenerator.addProvider(new DOLootTableProvider(dataGenerator));
            dataGenerator.addProvider(new DORecipeProvider(dataGenerator));
        }
        if (event.includeClient()) {

        }
    }
}
