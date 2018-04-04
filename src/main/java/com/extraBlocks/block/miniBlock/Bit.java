package com.extraBlocks.block.miniBlock;

import java.util.List;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.item.ItemBase;
import com.extraBlocks.item.ModItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Bit extends ItemBase {

	public String name;
	public static final PropertyInteger BLOCKID = PropertyInteger.create("BlockID", 0, 100000);
	
	public Bit(String name) {
		super(name);
		this.name = name;
		this.setMaxStackSize(4096);
		this.setHasSubtypes(true);
	}

	public void getSubItems(Block itemID, CreativeTabs tabs, List list) {
		for(int i = 0; i < Block.REGISTRY.getKeys().size(); ++i){
			list.add(new ItemStack(ModItems.bit, 1, i));
		}
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		BlockPos newPos = pos;

		if (!block.isReplaceable(worldIn, pos)) {
			newPos = pos.offset(facing);
		}

		ItemStack itemstack = player.getHeldItem(hand);

		if(worldIn.getBlockState(pos).getBlock() instanceof MiniBlock) {
			if(worldIn.getTileEntity(pos) != null) {
				if(worldIn.getTileEntity(pos) instanceof TileEntityMiniBlock) {
					TileEntityMiniBlock te = (TileEntityMiniBlock) worldIn.getTileEntity(pos);
					EnumFacing s = facing;
					AxisAlignedBB hitbox = MiniBlock.bounds;
					AxisAlignedBB targetPos = hitbox.offset(	
							(s == EnumFacing.EAST) 	? 1.0 / 16.0 : (s == EnumFacing.WEST) 	? 	-1.0 / 16.0 : 0.0, 
							(s == EnumFacing.UP) 	? 1.0 / 16.0 : (s == EnumFacing.DOWN) 	? 	-1.0 / 16.0 : 0.0,
							(s == EnumFacing.SOUTH) ? 1.0 / 16.0 : (s == EnumFacing.NORTH) 	? 	-1.0 / 16.0 : 0.0
							);
					te.addBitToBlock(targetPos, Block.getBlockById(itemstack.getMetadata()).getDefaultState());
				}
			}

			return EnumActionResult.SUCCESS;
		} else {
			if (!itemstack.isEmpty() && player.canPlayerEdit(newPos, facing, itemstack) && worldIn.mayPlace(Block.getBlockById(player.getHeldItem(hand).getMetadata()), newPos, true, facing, (Entity) null)) {
				int blockID = this.getMetadata(itemstack.getMetadata());
				IBlockState iblockstate1 = Block.getBlockById(player.getHeldItem(hand).getMetadata()).getDefaultState();
				if (placeBlockAt(itemstack, player, worldIn, newPos, facing, hitX, hitY, hitZ, iblockstate1)) {
					iblockstate1 = worldIn.getBlockState(newPos);
					SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, newPos, player);
					worldIn.playSound(player, newPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
				}

				return EnumActionResult.SUCCESS;
			}
			else {
				return EnumActionResult.FAIL;
			}
		}
	}

	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		IBlockState stateNew = ModBlocks.mini_block.getDefaultState();
		if (!world.setBlockState(pos, stateNew, 11)) return false;

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != Block.getBlockById(stack.getMetadata()).getDefaultState()) {
			((TileEntityMiniBlock) world.getTileEntity(pos)).addBitToBlock(MiniBlock.bounds, newState);
			if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
		}

		return true;
	}

	public void registerItemModel() {
		Main.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public Bit setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

}
