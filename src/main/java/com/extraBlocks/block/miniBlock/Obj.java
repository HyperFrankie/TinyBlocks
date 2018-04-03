package com.extraBlocks.block.miniBlock;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import scala.Cloneable;

public class Obj extends AxisAlignedBB implements Cloneable {

	public IBlockState blockType;
	
	public Obj(double x1, double y1, double z1, double x2, double y2, double z2, @Nullable IBlockState blockState) {
		super(x1, y1, z1, x2, y2, z2);
		blockType = blockState;
	}
	
	public Obj(AxisAlignedBB bb, Block block) {
		this(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, block.getDefaultState());
	}

	@Override
	public Obj clone() {
		return this;
	}

}
