package com.sarinsa.tomfoolery.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class CoolBlockReplacerGlassesItem extends CoolGlassesItem {

    private final Supplier<Block> blockSupplier;

    public CoolBlockReplacerGlassesItem(Supplier<Block> blockSupplier) {
        super();
        this.blockSupplier = blockSupplier;
    }

    @Override
    public void gaze(Player player, Level level, BlockHitResult hitResult) {
        BlockPos lookAtPos = hitResult.getBlockPos();
        BlockState lookAtState = level.getBlockState(lookAtPos);

        if (!lookAtState.isAir() && lookAtState.getBlock() != blockSupplier.get() && !lookAtState.getCollisionShape(level, lookAtPos).isEmpty()) {
            if (level.getBlockEntity(lookAtPos) != null) {
                BlockEntity blockEntity = level.getBlockEntity(lookAtPos);

                if (blockEntity instanceof Container) {
                    Containers.dropContents(level, lookAtPos, (Container) blockEntity);
                }
            }
            level.setBlock(lookAtPos, blockSupplier.get().defaultBlockState(), 2);
        }
    }

    public Supplier<Block> getBlockSupplier() {
        return this.blockSupplier;
    }
}
