package com.xcheesyy.switcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xcheesyy.switcher.util.ItemWithDestroySpeed;

public class AutoToolSwitcher implements ModInitializer {
	public static final String MOD_ID = "auto-tool-switcher";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		List<ItemStack> vanillaPickaxes = List.of(
			new ItemStack(Items.STONE_PICKAXE),
			new ItemStack(Items.COPPER_PICKAXE),
			new ItemStack(Items.GOLDEN_PICKAXE),
			new ItemStack(Items.IRON_PICKAXE),
			new ItemStack(Items.DIAMOND_PICKAXE),
			new ItemStack(Items.NETHERITE_PICKAXE)
		);

		Set<Item> vanillaAxes = Set.of(
			Items.STONE_AXE,
			Items.COPPER_AXE,
			Items.GOLDEN_AXE,
			Items.IRON_AXE,
			Items.DIAMOND_AXE,
			Items.NETHERITE_AXE
		);

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState state = world.getBlockState(pos);
			System.out.println("Player started breaking: " + state.getBlock().getName().getString());
			System.out.println("Hand: " + player.getInventory().getSelectedItem());
			
			List<ItemStack> toolsInPosession = getItemsInInventory(vanillaPickaxes, player.getInventory());
			System.out.println(checkForFastestTool(state.getBlock(), toolsInPosession, player));

			// You can cancel the breaking
			return InteractionResult.PASS;
		});
	}

	public List<ItemStack> getItemsInInventory(List<ItemStack> items, Inventory inventory) {
		List<ItemStack> itemsInInventory = new ArrayList<>();

		for (ItemStack item : items) {
			if (inventory.contains(item)) {
				itemsInInventory.add(item);
			}
		}

		return itemsInInventory;
	}

	public ItemStack checkForFastestTool(Block block, List<ItemStack> tools, Player player) {
		if (tools.isEmpty()) {
			return player.getInventory().getSelectedItem();
		}

		if (tools.size() == 1) {
			return tools.getFirst();
		}

		List<ItemWithDestroySpeed> itemsWithDestroySpeeds = new ArrayList<>();

		for (ItemStack tool : tools) {
			itemsWithDestroySpeeds.add(new ItemWithDestroySpeed(tool, block));
		}

		ItemWithDestroySpeed fastestItem = itemsWithDestroySpeeds
			.stream()
			.max(Comparator.comparing(ItemWithDestroySpeed::getDestroySpeed))
			.orElse(null);

		return fastestItem.getItem();
	}
}