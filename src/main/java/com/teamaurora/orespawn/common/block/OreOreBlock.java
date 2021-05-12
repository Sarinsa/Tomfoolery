package com.teamaurora.orespawn.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OreOreBlock extends Block {

    public OreOreBlock(Properties properties) {
        super(properties);
    }

    /*
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ResourceLocation tableLocation = this.getLootTable();
        LootContext lootContext = builder.withParameter(LootParameters.BLOCK_STATE, state).create(LootParameterSets.BLOCK);
        ServerWorld serverWorld = lootContext.getLevel();

        if (tableLocation == LootTables.EMPTY) {
            return getRandomOres(serverWorld.random, lootContext.);
        }
        else {
            LootTable loottable = serverWorld.getServer().getLootTables().get(tableLocation);
            return loottable.getRandomItems(lootContext);
        }
    }

    private static List<ItemStack> getRandomOres(Random random, int lootingLevel) {
        List<ItemStack> ores = new ArrayList<>();
        int amount = random.nextInt(2 + lootingLevel) + 1;

        for (int i = 0; i < amount; i++) {
            ores.add(new ItemStack(Tags.Items.ORES.getRandomElement(random)));
        }
        return ores;
    }

     */
}
