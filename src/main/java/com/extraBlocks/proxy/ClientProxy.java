package com.extraBlocks.proxy;

import com.extraBlocks.Main;
import com.extraBlocks.block.miniBlock.TESRMiniBlock;
import com.extraBlocks.block.miniBlock.TileEntityMiniBlock;
import com.extraBlocks.item.ItemBase;
import com.extraBlocks.pedestal.TESRPedestal;
import com.extraBlocks.pedestal.TileEntityPedestal;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	public void registerItemRenderer(ItemBase item, int meta, String id) {
		// TODO Auto-generated method stub
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Main.MODID + ":" + id, "inventory"));
	}

	public void registerItemRenderer(Item item, int meta, String id) {
		// TODO Auto-generated method stub
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Main.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}
	
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new TESRPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiniBlock.class, new TESRMiniBlock());
	}

}
