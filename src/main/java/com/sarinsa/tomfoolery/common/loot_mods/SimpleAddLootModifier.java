package com.sarinsa.tomfoolery.common.loot_mods;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomLootMods;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class SimpleAddLootModifier extends LootModifier {

    private static final ListCodec<ResourceLocation> RL_LIST_CODEC = new ListCodec<>(ResourceLocation.CODEC);

    private final List<ResourceLocation> targetLootTables;
    private final Item itemToAdd;
    private final double chance;
    private final int minAmount;
    private final int maxAmount;

    public static final Supplier<Codec<SimpleAddLootModifier>> CODEC = () -> RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst)
            .and(inst.group(
                            ForgeRegistries.ITEMS.getCodec()
                                    .fieldOf("item")
                                    .forGetter(m -> m.itemToAdd),
                            Codec.DOUBLE.fieldOf("chance")
                                    .forGetter(m -> m.chance),
                            Codec.INT.fieldOf("maxCount")
                                    .forGetter(m -> m.maxAmount),
                            Codec.INT.fieldOf("minCount")
                                    .forGetter(m -> m.minAmount),
                            RL_LIST_CODEC
                                    .fieldOf("lootTable")
                                    .forGetter(m -> m.targetLootTables)
                    )
            )
            .apply(inst, SimpleAddLootModifier::new)
    );

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public SimpleAddLootModifier(LootItemCondition[] conditionsIn, Item itemToAdd, double chance, int minAmount, int maxAmount, List<ResourceLocation> targetLootTables) {
        super(conditionsIn);

        if (minAmount < 1 || minAmount > maxAmount || chance > 1 || chance < 0) {
            throw new IllegalArgumentException("SimpleAddLootModifier does not support minAmount below 1 or minAmount being greater than maxAmount. Chance must also be between 0-1.");
        }
        this.targetLootTables = targetLootTables;
        this.itemToAdd = itemToAdd;
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ResourceLocation rl : targetLootTables) {
            if (context.getQueriedLootTableId().equals(rl)) {
                RandomSource random = context.getRandom();

                if (random.nextDouble() <= chance) {
                    int bonusAmount = maxAmount == minAmount ? 0 : random.nextInt(maxAmount - minAmount);
                    generatedLoot.add(new ItemStack(itemToAdd, minAmount + bonusAmount));
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return TomLootMods.SIMPLE_ADD.get();
    }
}

