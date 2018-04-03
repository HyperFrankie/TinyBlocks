package com.extraBlocks.item.tool.SaveModelBase;

import com.extraBlocks.block.miniBlock.TileEntityMiniBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public class ContainerSaveModel extends Container {

	public static int offsetX = 0, offsetY = -68, extraWidth = 0, x, y, z;
	public BlockPos pos;
	public TileEntityMiniBlock te;
	
	public ContainerSaveModel(EntityPlayer player, TileEntityMiniBlock te) {
		this.pos = te.getPos();
		this.te = te;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
