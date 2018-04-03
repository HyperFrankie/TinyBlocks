package com.extraBlocks.block.miniBlock;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import scala.tools.nsc.backend.icode.analysis.SemiLattice.IState;

public class miniBlockModel extends ArrayList<AxisAlignedBB> {

	public miniBlockModel(IBlockState blockState) {
		this(new ModelResourceLocation("extra_blocks:" + blockState.getBlock().getLocalizedName()));
	}
	
	public miniBlockModel(ModelResourceLocation modelResourceLocation) {
//		Minecraft.getMinecraft
	}
	
}
