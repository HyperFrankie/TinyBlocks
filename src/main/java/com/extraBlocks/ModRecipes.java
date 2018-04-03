package com.extraBlocks;

import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void init() {
		GameRegistry.addSmelting(ModBlocks.ore_shadow, new ItemStack(ModItems.shadow_ingot, 1), 0.7f);
	}

}