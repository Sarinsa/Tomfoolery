package com.sarinsa.tomfoolery.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class CoolBlockReplacerGlassesItem extends CoolGlassesItem {

    private final Supplier<Block> blockSupplier;

    public CoolBlockReplacerGlassesItem(Supplier<Block> blockSupplier) {
        super();
        this.blockSupplier = blockSupplier;
    }

    @Override
    public void gaze(PlayerEntity player, World world, BlockRayTraceResult traceResult) {
        BlockPos lookAtPos = traceResult.getBlockPos();
        BlockState lookAtState = world.getBlockState(lookAtPos);

        if (!lookAtState.isAir(world, lookAtPos) && lookAtState.getBlock() != blockSupplier.get() && !lookAtState.getCollisionShape(world, lookAtPos).isEmpty()) {
            if (world.getBlockEntity(lookAtPos) != null) {
                TileEntity tileEntity = world.getBlockEntity(lookAtPos);

                if (tileEntity instanceof IInventory) {
                    InventoryHelper.dropContents(world, lookAtPos, (IInventory) tileEntity);
                }
            }
            world.setBlock(lookAtPos, blockSupplier.get().defaultBlockState(), 2);
        }
    }

    public Supplier<Block> getBlockSupplier() {
        return this.blockSupplier;
    }
}
