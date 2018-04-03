package com.extraBlocks.proxy;

import com.extraBlocks.item.ItemBase;

import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;

public class CommonProxy {

	public void registerItemRenderer(ItemBase item, int meta, String id) {
		// TODO Auto-generated method stub
		
	}

	public void registerItemRenderer(Item itemBase, int meta, String name) {
		// TODO Auto-generated method stub
		
	}
	
	public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
	
	public void registerRenderers() {
	
	}

}
