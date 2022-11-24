package com.sarinsa.tomfoolery.datagen.loot_mod;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomLootMods;
import com.sarinsa.tomfoolery.common.loot_mods.SimpleAddLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class TomLootModsProvider extends GlobalLootModifierProvider {

    public TomLootModsProvider(DataGenerator gen) {
        super(gen, Tomfoolery.MODID);
    }

    @Override
    protected void start() {
        add("add_launcher", TomLootMods.SIMPLE_ADD.get(), new SimpleAddLootModifier(
                new ILootCondition[] {},
                new ResourceLocation[] {
                        new ResourceLocation("chests/abandoned_mineshaft"),
                        new ResourceLocation("chests/bastion_treasure"),
                        new ResourceLocation("chests/simple_dungeon")

                },
                TomItems.GRENADE_LAUNCHER,
                0.04,
                1,
                1
        ));
        add("add_launcher_igloo", TomLootMods.SIMPLE_ADD.get(), new SimpleAddLootModifier(
                new ILootCondition[] {},
                new ResourceLocation[] {
                        new ResourceLocation("chests/igloo_chest")
                },
                TomItems.GRENADE_LAUNCHER,
                0.25,
                1,
                1
        ));
    }
}
