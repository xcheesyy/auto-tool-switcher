package com.xcheesyy.switcher.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemWithDestroySpeed {
    public final ItemStack item;
    public final Block block;
    public final float destroySpeed;
    public final BlockState blockState;

    public ItemWithDestroySpeed(
        ItemStack item,
        Block block
    ) {
        this.item = item;
        this.block = block;
        this.destroySpeed = item.getDestroySpeed(block.defaultBlockState());
        this.blockState = block.defaultBlockState();
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getBlock() {
        return block;
    }

    public float getDestroySpeed() {
        return destroySpeed;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
