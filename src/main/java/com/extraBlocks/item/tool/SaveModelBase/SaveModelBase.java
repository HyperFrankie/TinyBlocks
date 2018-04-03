package com.extraBlocks.item.tool.SaveModelBase;

import java.util.ArrayList;

import com.extraBlocks.Main;
import com.extraBlocks.ModGuiHandler;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.block.miniBlock.MiniBlock;
import com.extraBlocks.block.miniBlock.Obj;
import com.extraBlocks.block.miniBlock.TileEntityMiniBlock;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaveModelBase extends net.minecraft.item.ItemTool {

	private String name;
	private static final java.util.Set<Block> EFFECTIVE_ON = Sets.newHashSet(ModBlocks.mini_block);
	public IBlockState selectedBlock;
	TileEntityMiniBlock te = null;
	ArrayList<Obj> objs = null;

	public SaveModelBase(ToolMaterial material, String name) {
		super(1, 1, material, EFFECTIVE_ON);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
		setCreativeTab(Main.shadowTab);
	}

	public void registerItemModel() {
		Main.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public SaveModelBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	public boolean canSaveBlock(IBlockState blockState) {
		return !blockState.isFullCube();
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		System.out.println("item used");
		if(worldIn.getBlockState(pos).getBlock() instanceof MiniBlock) {
			if(worldIn.getTileEntity(pos) != null) {
				if(worldIn.getTileEntity(pos) instanceof TileEntityMiniBlock) {
					te = (TileEntityMiniBlock) worldIn.getTileEntity(pos);
					objs = te.objs;
					player.openGui(Main.instance, ModGuiHandler.SAVEBLOCKGUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
        return EnumActionResult.PASS;
    }
	
}
