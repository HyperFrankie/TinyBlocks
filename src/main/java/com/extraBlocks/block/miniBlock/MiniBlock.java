package com.extraBlocks.block.miniBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.extraBlocks.Main;
import com.extraBlocks.ModGuiHandler;
import com.extraBlocks.block.BlockTileEntity;
import com.extraBlocks.block.ModBlocks;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import akka.actor.FSM.State;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.WorldAccessContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MiniBlock extends BlockTileEntity<TileEntityMiniBlock> {

	public TileEntityMiniBlock te;
	public static AxisAlignedBB bounds = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
	public ArrayList<Obj> objs = new ArrayList<Obj>();
	public static boolean chiselHeld = false;
//	public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");
	public static final PropertyInteger TAGTHINGY = PropertyInteger.create("tagthingy", 0, 2);

	public MiniBlock() {
		super(Material.ROCK, "mini_block");
		this.setDefaultState(this.blockState.getBaseState().withProperty(TAGTHINGY, 0));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
    }
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TAGTHINGY});
	}
	
	public int getMetaFromState(IBlockState state) { return 0; }

	public IBlockState getStateFromMeta(int meta) { return this.getDefaultState(); }

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		TileEntityMiniBlock te = (TileEntityMiniBlock) worldIn.getTileEntity(pos);
//		System.out.println(te.objs.toString());
//		if(te.objs.isEmpty()) {
		if(objs.isEmpty()) {
			worldIn.removeTileEntity(pos);
		}
//		for(int i = 0; i < te.objs.size(); i++) {
		for(int i = 0; i < objs.size(); i++) {
//			addCollisionBoxToList(pos, entityBox, collidingBoxes, te.objs.get(i));
			addCollisionBoxToList(pos, entityBox, collidingBoxes, objs.get(i));
		}
		
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounds;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return state.getBoundingBox(worldIn, pos).offset(pos);
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
//		TileEntityMiniBlock te = (TileEntityMiniBlock) worldIn.getTileEntity(pos);
//		if(!te.objs.isEmpty()) {
//			return ray(worldIn, pos, blockState, start, end, te.objs);
//		}
		return ray(worldIn, pos, blockState, start, end, objs);
//		return null;
	}

	private RayTraceResult ray(World worldIn, BlockPos pos, IBlockState blockState, Vec3d start, Vec3d end, ArrayList<Obj> objs2) {
		RayTraceResult raytraceresultNearest = null;
		RayTraceResult raytraceresult = null;
		for(int i = 0; i < objs2.size(); i++) {
			Vec3d startVec = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			Vec3d endVec = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			raytraceresult = objs2.get(i).calculateIntercept(startVec, endVec);
			raytraceresult = raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
			
//			System.out.println(startVec + " , " + endVec + " , " + start + " , " + end + " ( " + boundingBoxes.get(i) + " )");
			
			if(raytraceresultNearest == null) {
				raytraceresultNearest = raytraceresult;
			} else if(raytraceresult != null && raytraceresultNearest != null) {
				if(raytraceresult.hitVec.distanceTo(start) < raytraceresultNearest.hitVec.distanceTo(start)) {
					raytraceresultNearest = raytraceresult;
				}
			}
		}
		return raytraceresultNearest;
	}

//	@Override
//	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//		if (!world.isRemote) {
//			TileEntityMiniBlock tile = (TileEntityMiniBlock) world.getTileEntity(pos);
//			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
//			ItemStack heldItem = player.getHeldItemMainhand();
//			if (player.isSneaking()) {
////				ItemStack stack = itemHandler.getStackInSlot(0);
////				if (!stack.isEmpty()) {
////					String localized = Main.proxy.localize(stack.getUnlocalizedName() + ".name");
////					player.sendMessage(new TextComponentString(stack.getCount() + "x " + localized));
////				} else {
////					player.sendMessage(new TextComponentString("Empty"));
////				}
//
//				if (heldItem.isEmpty()) {
//					player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
//				} else {
//					player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
//				}
//				tile.markDirty();
//			} else {
//
//			}
//		}
//		return true;
//	}

	@Override
	public Class<TileEntityMiniBlock> getTileEntityClass() {
		return TileEntityMiniBlock.class;
	}

	@Nullable
	@Override
	public TileEntityMiniBlock createTileEntity(World world, IBlockState state) {
		return new TileEntityMiniBlock();
	}

	@Override
	public boolean isTranslucent(IBlockState i) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}