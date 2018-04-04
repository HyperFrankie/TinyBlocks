package com.extraBlocks.item.tool;

import java.util.ArrayList;
import java.util.Collection;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.block.miniBlock.MiniBlock;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.collection.Iterator;
import scala.collection.mutable.Set;

public class ItemChiselBase extends net.minecraft.item.ItemTool {

	public static ItemStack currentChisel = new ItemStack(Item.getItemFromBlock(Blocks.AIR));
	private String name;
	private static final java.util.Set<Block> EFFECTIVE_ON = Sets.newHashSet(ModBlocks.mini_block);

	public ItemChiselBase(ToolMaterial material, String name) {
		super(1, 1, material, EFFECTIVE_ON);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
		setCreativeTab(Main.shadowTab);
	}

	public int getHarvestLevelForChisel() {
		return this.toolMaterial.getHarvestLevel();
	}
	
	public void registerItemModel() {
		Main.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ItemChiselBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		if(blockIn.getBlock().getHarvestLevel(blockIn) <= this.toolMaterial.getHarvestLevel()) {
			return true;
		}
		return false;
	}

}