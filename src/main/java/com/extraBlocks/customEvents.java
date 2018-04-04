package com.extraBlocks;

import com.extraBlocks.block.BlockTileEntity;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.block.miniBlock.Bit;
import com.extraBlocks.block.miniBlock.Face;
import com.extraBlocks.block.miniBlock.MiniBlock;
import com.extraBlocks.block.miniBlock.Obj;
import com.extraBlocks.block.miniBlock.TileEntityMiniBlock;
import com.extraBlocks.item.ModItems;
import com.extraBlocks.item.tool.ItemChiselBase;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.OpenGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import scala.tools.nsc.backend.icode.Primitives.Logical;

public class customEvents {

	@SubscribeEvent
	public void lefClickBlock(LeftClickBlock e) {
		EntityPlayer p = e.getEntityPlayer();
		IBlockState blockState = e.getWorld().getBlockState(e.getPos());
		AxisAlignedBB bb = blockState.getSelectedBoundingBox(e.getWorld(), e.getPos()).offset(-e.getPos().getX(), -e.getPos().getY(), -e.getPos().getZ());
		Block block = blockState.getBlock();
		ItemStack stack = p.getActiveItemStack();
		stack = p.getHeldItemMainhand();

		double x = Math.round(16 * (e.getHitVec().x - e.getPos().getX()) - 0.5);
		double y = Math.round(16 * (e.getHitVec().y - e.getPos().getY()) - 0.5);
		double z = Math.round(16 * (e.getHitVec().z - e.getPos().getZ()) - 0.5);

		p.sendMessage(new TextComponentString("Left clicking:").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
		p.sendMessage(new TextComponentString("Block: ").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)).appendSibling(new TextComponentString(block.toString())));
		p.sendMessage(new TextComponentString("At: ").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)).appendSibling(new TextComponentString(x + " , " + y + " , " + z + " , " + e.getFace())));
		p.sendMessage(new TextComponentString("With: " + stack).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
		ITextComponent message;
		if(stack.canHarvestBlock(blockState)) {
			message = new TextComponentString("Which ").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)).appendSibling(new TextComponentString("can").setStyle(new Style().setColor(TextFormatting.GREEN)).appendSibling(new TextComponentString(" be be used to break this block").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE))));
		} else {
			message = new TextComponentString("Which ").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)).appendSibling(new TextComponentString("can\'t").setStyle(new Style().setColor(TextFormatting.RED)).appendSibling(new TextComponentString(" be be used to break this block").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE))));
		}
		p.sendMessage(message);

		if(stack.getItem() instanceof Bit) {
			getBestChiselInHotbar(e.getEntityPlayer());
		}
		
		if(
				(stack.getItem() instanceof ItemChiselBase && stack.canHarvestBlock(blockState)) 
				|| (stack.getItem() instanceof Bit && ItemChiselBase.currentChisel.canHarvestBlock(blockState))
				) {
			System.out.println("Trying to break with chisel");
			if(stack.canHarvestBlock(blockState)) {
				if (!e.getWorld().isRemote) {
					if(block instanceof MiniBlock) {
						TileEntityMiniBlock te = (TileEntityMiniBlock) e.getWorld().getTileEntity(e.getPos());
						te.removeBitFromBlock(e.getEntityPlayer(), MiniBlock.bounds, e.getWorld(), e.getPos(), true);
					} else {
//						if(!Config.onlyBreakFullBlocksWithChisel || block.isFullBlock(blockState)) {
						ResourceLocation recourse = block.getRegistryName();
						e.getWorld().destroyBlock(e.getPos(), false);
						e.getWorld().setBlockState(e.getPos(), ModBlocks.mini_block.getDefaultState());
						TileEntityMiniBlock te = (TileEntityMiniBlock) e.getWorld().getTileEntity(e.getPos());
//						((MiniBlock) e.getWorld().getBlockState(e.getPos()).getBlock()).objs.clear();
//						((MiniBlock) e.getWorld().getBlockState(e.getPos()).getBlock()).objs.add(new Obj(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, blockState));
						te.objs.clear();
//						te.objs.add(new Obj(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, blockState));
						te.objs.add(new Obj(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, blockState));
						for(EnumFacing f : EnumFacing.VALUES) {
							te.addToRenderMap(te, new Face(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, f, blockState, false), false);
						}

//						te.renderMap.clear();
//						for(EnumFacing f : EnumFacing.VALUES) {
//							IBlockState s = Face.getBlockNextToFace(e.getWorld(), e.getPos(), f);
//							if(!s.isOpaqueCube() && !s.isSideSolid(e.getWorld(), e.getPos(), f.getOpposite())) {
//								te.renderMap.add(Face.getFullFaceFromEnumFacing(f, blockState));
//							}
//						}
//						if(block.isFullBlock(blockState)) {
//							te.addFullFaces(blockState);
//						}
						te.removeBitFromBlock(e.getEntityPlayer(), MiniBlock.bounds, e.getWorld(), e.getPos(), true);
					}
				}
				e.setCanceled(true);
			} else {
				e.setCanceled(true);
			}
		}
	}
	
	public ItemStack getBestChiselInHotbar(EntityPlayer p) {
		for(int i : new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8}) {
			ItemStack s = p.inventory.getStackInSlot(i);
			System.out.println("stack " + i + " is: " + s);
			if(s.getItem() instanceof ItemChiselBase) {
				ItemStack currentCurrentChisel = ItemChiselBase.currentChisel;
				int level = 0;
				if(currentCurrentChisel.getItem() instanceof ItemChiselBase) {
					level = ((ItemChiselBase) currentCurrentChisel.getItem()).getHarvestLevelForChisel();
				}
				if(((ItemChiselBase) s.getItem()).getHarvestLevelForChisel() > level) {
					ItemChiselBase.currentChisel = s;
				}
			}
		}
		return new ItemStack(Blocks.AIR);
	}
	
	@SubscribeEvent
	public void drawBlockHighlight(DrawBlockHighlightEvent e) {
		EntityPlayer p = e.getPlayer();
		ItemStack stack = p.getHeldItemMainhand();
		World world = p.getEntityWorld();
		if(e.getPlayer().getEntityWorld().isRemote) {
			if(e.getTarget().getBlockPos() != null) {
				IBlockState blockState = p.getEntityWorld().getBlockState(e.getTarget().getBlockPos());
				Block block = blockState.getBlock();
				if(stack.getItem() instanceof ItemChiselBase || stack.getItem() instanceof Bit) {
					Vec3d v = e.getTarget().hitVec;
					int s = e.getTarget().sideHit.getIndex();
					// Side 0 = down
					// Side 1 = up
					// Side 2 = north
					// Side 3 = south
					// Side 4 = west
					// Side 5 = east
					double x1 = Math.round(16.0 * (v.x - e.getTarget().getBlockPos().getX() - ((s == 5) ? (3.0 / 32.0) : (1.0 / 32)))) / 16.0;
					double y1 = Math.round(16.0 * (v.y - e.getTarget().getBlockPos().getY() - ((s == 1) ? (3.0 / 32.0) : (1.0 / 32)))) / 16.0;
					double z1 = Math.round(16.0 * (v.z - e.getTarget().getBlockPos().getZ() - ((s == 3) ? (3.0 / 32.0) : (1.0 / 32)))) / 16.0;
					double x2 = Math.round(16.0 * (v.x - e.getTarget().getBlockPos().getX() + ((s == 5) ? (-1.0 / 32.0) : (1.0 / 32)))) / 16.0;
					double y2 = Math.round(16.0 * (v.y - e.getTarget().getBlockPos().getY() + ((s == 1) ? (-1.0 / 32.0) : (1.0 / 32)))) / 16.0;
					double z2 = Math.round(16.0 * (v.z - e.getTarget().getBlockPos().getZ() + ((s == 3) ? (-1.0 / 32.0) : (1.0 / 32)))) / 16.0;
					if(!ModBlocks.mini_block.chiselHeld || !ModBlocks.mini_block.bounds.equals(new AxisAlignedBB(x1, y1, z1, x2, y2, z2))) {
//						System.out.println(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
						ModBlocks.mini_block.bounds = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
						ModBlocks.mini_block.chiselHeld = true;
					}

				} else {
					
					if(world.getBlockState(e.getTarget().getBlockPos()).getBlock() instanceof BlockTileEntity<?>) {
						if(((BlockTileEntity<?>) world.getBlockState(e.getTarget().getBlockPos()).getBlock()) != null) {
							if(((BlockTileEntity<?>) world.getBlockState(e.getTarget().getBlockPos()).getBlock()).getTileEntityClass() == TileEntityMiniBlock.class) {
								TileEntityMiniBlock te = (TileEntityMiniBlock) world.getTileEntity(e.getTarget().getBlockPos());
							}
						}
					}
					
					MiniBlock.bounds = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
					if(!(stack.getItem() instanceof ItemChiselBase)) {
						ModBlocks.mini_block.chiselHeld = false;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void sendMessage(BreakEvent e) {
		if(!e.getWorld().isRemote) {
			EntityPlayer player = e.getPlayer();
			if(!player.isCreative()) {
				if(e.getWorld().getTileEntity(e.getPos()) instanceof TileEntityMiniBlock) {
					World world = e.getWorld();
					BlockPos pos = e.getPos();
					TileEntityMiniBlock tile = (TileEntityMiniBlock) world.getTileEntity(pos);
					IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
					ItemStack stack1 = itemHandler.getStackInSlot(0);
					if (!stack1.isEmpty()) {
						EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
						world.spawnEntity(item);
						world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.mini_block)));
					}
				}
			}
			Block block = e.getState().getBlock();
			String name1 = block.getLocalizedName();
			String name2 = block.getUnlocalizedName();
			player.sendMessage(new TextComponentString("you broke:").setStyle(new Style().setColor(TextFormatting.AQUA)));
			player.sendMessage(new TextComponentString(block + ""));
			player.sendMessage(new TextComponentString("name: " + name1));
			player.sendMessage(new TextComponentString("tile: " + name2));
		}
		
	}
	
}
