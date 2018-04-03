package com.extraBlocks.pedestal;

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

import akka.actor.FSM.State;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockPedestal extends BlockTileEntity<TileEntityPedestal> {

	private World world;
	public ArrayList<AxisAlignedBB> objs = new ArrayList<AxisAlignedBB>();
	public AxisAlignedBB hit01 = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
	public AxisAlignedBB hit02 = new AxisAlignedBB(0.25, 0.25, 0.0, 1.0, 0.5, 1.0);
	public AxisAlignedBB hit03 = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 0.75, 1.0);
	public AxisAlignedBB hit04 = new AxisAlignedBB(0.75, 0.75, 0.0, 1.0, 1.0, 1.0);
	public AxisAlignedBB bounds = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

	public BlockPedestal() {
		super(Material.ROCK, "Pedestal");
		objs.add(hit01);
		objs.add(hit02);
		objs.add(hit03);
		objs.add(hit04);
		//		createBlockModel();
	}

	

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState){
		this.world = worldIn;
		addCollisionBoxToList(pos, entityBox, collidingBoxes, hit01);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, hit02);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, hit03);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, hit04);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounds;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return state.getBoundingBox(worldIn, pos).offset(pos);
	}

	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		AxisAlignedBB bb = new AxisAlignedBB(pos);
		return rayTrace(pos, start, end, hit01);

	}

	@Nullable
	protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
		return ray(pos, start, end, objs);
	}
	
	private RayTraceResult ray(BlockPos pos, Vec3d start, Vec3d end, ArrayList<AxisAlignedBB> boundingBoxes) {
		RayTraceResult raytraceresult = null;
		for(int i = 0; i < objs.size(); i++) {
			if(raytraceresult == null) {
				Vec3d vec3d = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
				Vec3d vec3d1 = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
				raytraceresult = boundingBoxes.get(i).calculateIntercept(vec3d, vec3d1);
				raytraceresult = raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
			}
		}
		return raytraceresult;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {

	}

	//	@Override
	//	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
	//		return hit;
	//	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityPedestal tile = (TileEntityPedestal) world.getTileEntity(pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			ItemStack heldItem = player.getHeldItemMainhand();
			if (player.isSneaking()) {
				//				ItemStack stack = itemHandler.getStackInSlot(0);
				//				if (!stack.isEmpty()) {
				//					String localized = Main.proxy.localize(stack.getUnlocalizedName() + ".name");
				//					player.sendMessage(new TextComponentString(stack.getCount() + "x " + localized));
				//				} else {
				//					player.sendMessage(new TextComponentString("Empty"));
				//				}

				if (heldItem.isEmpty()) {
					player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
				} else {
					player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
				}
				tile.markDirty();
			} else {
				//				Minecraft.getMinecraft().displayGuiScreen(new GuiPedestal(new ModGuiHandler().getServerGuiElement(ModGuiHandler.PEDESTAL, player, world, pos.getX(), pos.getY(), pos.getZ()), player.inventory));
				player.openGui(Main.instance, ModGuiHandler.PEDESTAL, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		if(!player.isCreative()) {
			TileEntityPedestal tile = (TileEntityPedestal) world.getTileEntity(pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
			ItemStack stack1 = itemHandler.getStackInSlot(0);
			if (!stack1.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
				world.spawnEntity(item);
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.pedestal)));
			}
		}

	}

	@Override
	public Class<TileEntityPedestal> getTileEntityClass() {
		return TileEntityPedestal.class;
	}

	@Nullable
	@Override
	public TileEntityPedestal createTileEntity(World world, IBlockState state) {
		return new TileEntityPedestal();
	}

	@Override
	public boolean isTranslucent(IBlockState i) {
		return true;
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

	//	@Override
	//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
	//		return hit;
	//		
	//	}

}