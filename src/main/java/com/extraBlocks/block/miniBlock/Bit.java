package com.extraBlocks.block.miniBlock;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.item.ItemBase;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Bit extends ItemBlock {

	public String name;
	
	public Bit(String name) {
		super(ModBlocks.mini_block);
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.shadowTab);
		this.setMaxStackSize(4096);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        BlockPos newPos = pos;
        
        if (!block.isReplaceable(worldIn, pos))
        {
            newPos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if(worldIn.getBlockState(pos).getBlock() instanceof MiniBlock) {
        	if(worldIn.getTileEntity(pos) != null) {
        		if(worldIn.getTileEntity(pos) instanceof TileEntityMiniBlock) {
        			TileEntityMiniBlock te = (TileEntityMiniBlock) worldIn.getTileEntity(pos);
        			Vec3d v = new Vec3d(hitX, hitY, hitZ);
					EnumFacing s = facing;
					// Side 0 = down
					// Side 1 = up
					// Side 2 = north
					// Side 3 = south
					// Side 4 = west
					// Side 5 = east
					AxisAlignedBB hitbox = MiniBlock.bounds;
					AxisAlignedBB targetPos = hitbox.offset(	(s == EnumFacing.EAST) ? 1.0 / 16.0 : (s == EnumFacing.WEST) ? -1.0 / 16.0 : 0.0, 
																(s == EnumFacing.UP) ? 1.0 / 16.0 : (s == EnumFacing.DOWN) ? -1.0 / 16.0 : 0.0,
																(s == EnumFacing.SOUTH) ? 1.0 / 16.0 : (s == EnumFacing.NORTH) ? -1.0 / 16.0 : 0.0
															);
        			te.addBitToBlock(targetPos, Block.getBlockById(itemstack.getMetadata()));
        		}
        	}
        	
        	return EnumActionResult.SUCCESS;
        } else {
        	if (!itemstack.isEmpty() && player.canPlayerEdit(newPos, facing, itemstack) && worldIn.mayPlace(this.block, newPos, false, facing, (Entity) null)) {
        		int i = this.getMetadata(itemstack.getMetadata());
        		IBlockState iblockstate1 = this.block.getDefaultState();
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
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
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
