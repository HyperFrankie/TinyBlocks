package com.extraBlocks;

import com.extraBlocks.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ExtraBlocksTab extends CreativeTabs {

	public ExtraBlocksTab() {
		super(Main.MODID);
		setBackgroundImageName("item_search.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.shadow_ingot);
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}

}