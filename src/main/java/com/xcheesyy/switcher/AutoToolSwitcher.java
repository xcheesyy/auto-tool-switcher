package com.xcheesyy.switcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

		List<ItemStack> vanillaAxes = List.of(
			new ItemStack(Items.STONE_AXE),
			new ItemStack(Items.COPPER_AXE),
			new ItemStack(Items.GOLDEN_AXE),
			new ItemStack(Items.IRON_AXE),
			new ItemStack(Items.DIAMOND_AXE),
			new ItemStack(Items.NETHERITE_AXE)
		);

		List<ItemStack> vanillaShovels = List.of(
			new ItemStack(Items.STONE_SHOVEL),
			new ItemStack(Items.COPPER_SHOVEL),
			new ItemStack(Items.GOLDEN_SHOVEL),
			new ItemStack(Items.IRON_SHOVEL),
			new ItemStack(Items.DIAMOND_SHOVEL),
			new ItemStack(Items.NETHERITE_SHOVEL)
		);

		List<TagKey<Block>> blockTags = List.of(
			BlockTags.MINEABLE_WITH_AXE,
			BlockTags.MINEABLE_WITH_PICKAXE,
			BlockTags.MINEABLE_WITH_HOE,
			BlockTags.MINEABLE_WITH_SHOVEL
		);

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState state = world.getBlockState(pos);
			// System.out.println("Player started breaking: " + state.getBlock().getName().getString());
			// System.out.println("Hand: " + player.getInventory().getSelectedItem());

			// getDestroySpeed(player.getInventory().getSelectedItem(), state.getBlock());
			var optimalTool = state.getBlock()
				.defaultBlockState()
				.getTags()
				.filter(blockTags::contains)
				.collect(Collectors.toList());

			if (optimalTool.size() <= 0) {
				// already return here
			}

			System.out.println(optimalTool);

			// BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_PICKAXE
			// List<ItemStack> toolsInPosession = getItemsInInventory(vanillaPickaxes, player.getInventory());
			// System.out.println(checkForFastestTool(state.getBlock(), toolsInPosession, player));

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

	// TODO: change return type to float
	public void getDestroySpeed(ItemStack tool, Block block) {
		System.out.println("Destroyspeed of current tool" + tool.getDestroySpeed(block.defaultBlockState()));
		Holder<Enchantment> enchantment = (Holder<Enchantment>) Enchantments.EFFICIENCY;
		int efficiency = EnchantmentHelper.getItemEnchantmentLevel(enchantment, tool);
		System.out.println("Efficiency of currently held tool: " + efficiency);
	}
}