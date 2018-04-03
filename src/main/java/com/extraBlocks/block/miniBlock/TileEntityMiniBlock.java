package com.extraBlocks.block.miniBlock;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import com.extraBlocks.Main;
import com.extraBlocks.block.BlockTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMiniBlock extends TileEntity {
	
	public long lastChangeTime;
	public MiniBlock m;
	public ArrayList<Obj> objs = new ArrayList<Obj>();
	public ArrayList<Face> renderMap;
	public ArrayList<String> renderMapString;
	public int dim;
	public final String boolClass = new String("" + PropertyBool.class.getName()), intClass = new String("" + PropertyInteger.class.getName()), dirClass = new String("" + PropertyDirection.class.getName());
	
	public Obj getBoundContainingBit(AxisAlignedBB bb, World world, BlockPos pos) {
		Obj o = null;
		if(world.getBlockState(pos).getBlock() instanceof BlockTileEntity<?>) {
			System.out.println("Is instance of BlockTileEntity");
			if(((BlockTileEntity<?>) world.getBlockState(pos).getBlock()) != null) {
				System.out.println("Is not null");
				if(((BlockTileEntity<?>) world.getBlockState(pos).getBlock()).getTileEntityClass() == TileEntityMiniBlock.class) {
					
					System.out.println("going to cast block to MiniBlock");
					m = ((MiniBlock) world.getBlockState(pos).getBlock().getExtendedState(world.getBlockState(pos), world, pos).getBlock());
					m.objs = this.objs;
					m.te = this;
					for(int i = 0; i < this.objs.size(); i++) {
						System.out.println("testing if: \r\n" + bb + " fits inside: \r\n" + this.objs.get(i));
						AxisAlignedBB check = this.objs.get(i);
						if(doesBitOverlapWithBound(bb, check)) {
							o = this.objs.get(i);
						}
					}
				}
			}
		}
		System.out.println((o != null) ? o : "THIS SHOULD NOT HAPPEN! NO OBJECT FOUND IN OBJS ARRAYLIST!");
		return o;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
	}
	
	public boolean isBitInsideBounds(AxisAlignedBB bit, AxisAlignedBB bounds) {
		return 
				bounds.minX <= bit.minX && 
				bounds.minY <= bit.minY && 
				bounds.minZ <= bit.minZ &&  
				bounds.maxX >= bit.maxX && 
				bounds.maxY >= bit.maxY && 
				bounds.maxZ >= bit.maxZ;
	}
	
	public boolean doesBitOverlapWithBound(AxisAlignedBB bit, Obj bound) { return doesBitOverlapWithBound(bit, new AxisAlignedBB(bound.minX, bound.minY, bound.minZ, bound.maxX, bound.maxY, bound.maxZ)); }
	
	public boolean doesBitOverlapWithBound(AxisAlignedBB bit, AxisAlignedBB bound) {
		return 
				bit.maxX > bound.minX && 
				bit.minX < bound.maxX && 
				bit.maxY > bound.minY && 
				bit.minY < bound.maxY &&
				bit.maxZ > bound.minZ &&
				bit.minZ < bound.maxZ;
	}
	


	public void addBitToBlock(AxisAlignedBB bb, Block block) {
		Obj o = new Obj(bb, block);
		this.objs.add(o);
		addFacesFromAddingBit(o);
		this.joinBounds();
		this.markDirty();
	}
	
	public void removeBitFromBlock(AxisAlignedBB bb, World world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock() instanceof BlockTileEntity<?>) {
			if(((BlockTileEntity<?>) world.getBlockState(pos).getBlock()) != null) {
				if(((BlockTileEntity<?>) world.getBlockState(pos).getBlock()).getTileEntityClass() == TileEntityMiniBlock.class) {
					m = ((MiniBlock) world.getBlockState(pos).getBlock().getExtendedState(world.getBlockState(pos), world, pos).getBlock());
					m.objs = this.objs;
					m.te = this;
					// Looping through all the objects of this block and check if any of them overlap with the bit that is to be removed.
					// If that is the case, it will split the bounds that the bit overlaps with in a maximum of 6 pieces, where a piece will always 
					// have a volume at least 0.0625 * 0.0625 * 0.0625 and be within the space of the old bound.
					for(int i = 0; i < this.objs.size(); i++) {
						Obj old = this.objs.get(i);
						if(doesBitOverlapWithBound(bb, old)) {
							if(bb.minX > old.minX) {
								Obj newWestFromRemoval = new Obj(old.minX, old.minY, old.minZ, bb.minX, old.maxY, old.maxZ, old.blockType);
								this.objs.add(newWestFromRemoval);
							}
							if(bb.maxX < old.maxX) {
								Obj newEastFromRemoval = new Obj(bb.maxX, old.minY, old.minZ, old.maxX, old.maxY, old.maxZ, old.blockType);
								this.objs.add(newEastFromRemoval);
							}
							if(bb.minZ > old.minZ) {
								Obj newNorthFromRemoval = new Obj((bb.minX >= old.minX) ? bb.minX : old.minX, old.minY, old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.maxY, bb.minZ, old.blockType);
								this.objs.add(newNorthFromRemoval);
							}
							if(bb.maxZ < old.maxZ) {
								Obj newSouthFromRemoval = new Obj((bb.minX >= old.minX) ? bb.minX : old.minX, old.minY, bb.maxZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.maxY, old.maxZ, old.blockType);
								this.objs.add(newSouthFromRemoval);
							}
							if(bb.maxY < old.maxY) {
								Obj newAboveRemoval = new Obj((bb.minX >= old.minX) ? bb.minX : old.minX, bb.maxY, (bb.minZ >= old.minZ) ? bb.minZ : old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.maxY, (bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ, old.blockType);
								this.objs.add(newAboveRemoval);
							}
							if(bb.minY > old.minY) {
								Obj newBelowRemoval = new Obj((bb.minX >= old.minX) ? bb.minX : old.minX, bb.minY, (bb.minZ >= old.minZ) ? bb.minZ : old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.minY, (bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ, old.blockType);
								this.objs.add(newBelowRemoval);
							}
							this.objs.remove(old);
						}
					}
					
					removeBitFromFaces(bb, world, pos);
					this.joinBounds();
					this.markDirty();
					
				}
			}
		}
	}
	
	public void addFullFaces(IBlockState oldBlockState) {
		TileEntityMiniBlock te = (TileEntityMiniBlock) world.getTileEntity(pos);
		for(EnumFacing f : EnumFacing.VALUES) {
			IBlockState s = Face.getBlockNextToFace(world, pos, f);
			if(!s.isOpaqueCube() && !s.isSideSolid(world, pos, f.getOpposite())) {
				addToRenderMap(te, Face.getFullFaceFromEnumFacing(f, oldBlockState), false);
			}
		}
		updateFacesString(te);
	}
	
	public void updateFacesString(TileEntityMiniBlock te) {
		if(te.renderMapString == null) {
			te.renderMapString = new ArrayList<String>();
		}
		for(int i = 0; i < te.renderMap.size(); i++) {
			if(!te.renderMapString.contains(te.renderMap.get(i).string())) {
				te.renderMapString.add(te.renderMap.get(i).string());
			}
		}
	}
	
	public boolean addToRenderMap(TileEntityMiniBlock te, Face f, boolean withStringUpdate) {
		if(te.renderMap == null) {
			te.renderMap = new ArrayList<Face>();
		}
		if(te.renderMap.contains(f)) {
			return false;
		} else {
			te.renderMap.add(f);
			if(withStringUpdate) { te.updateFacesString(te); }
			return true;
		}
	}
	
	public boolean removeFaceFromString(String s) {
		if(renderMapString != null) {
			return renderMapString.remove(s);
		}
		return false;
	}
	
	public boolean removeFromRenderMap(Face f, boolean withStringUpdate) {
		if(renderMap != null) {
			if(withStringUpdate) {
				removeFaceFromString(f.string());
			}
			return renderMap.remove(f);
		}
		return false;
	}
	
	/**Used to make the new Face objects and remove the ones that cannot be seen anymore when a bit is added to the block
	 * @param bb
	 */
	private void addFacesFromAddingBit(Obj bb) {
		
		for(EnumFacing f : EnumFacing.VALUES) {
			Face existing = doesBitOppositeFaceAlreadyExist(bb, f);
			if(existing == null 
//					|| Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(existing.iconName)
					) {
				Obj neighbour = getNeighborBound(bb, f);
				if(
						neighbour == null || !neighbour.blockType.isOpaqueCube() 
						|| neighbour.blockType.getBlock() instanceof BlockFluidClassic
						|| neighbour.blockType.getBlock() instanceof BlockFluidBase
						|| neighbour.blockType.getBlock() instanceof BlockFluidFinite
						|| neighbour.blockType.getBlock() instanceof BlockLiquid
						|| neighbour.blockType.getBlock() instanceof BlockDynamicLiquid
						|| neighbour.blockType.getBlock() instanceof BlockStaticLiquid
						|| neighbour.blockType.getBlock() instanceof BlockGlass
						|| neighbour.blockType.getBlock() instanceof BlockStainedGlass
						|| neighbour.blockType.getBlock() instanceof BlockStainedGlassPane
						|| neighbour.blockType.getBlock() instanceof BlockIce
						|| neighbour.blockType.getBlock() instanceof BlockSlime
						) {
					addToRenderMap(this, new Face(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, f.getOpposite(), neighbour.blockType, true), false);
					removePartOfFace(bb, f);
				}
			}
		}
		updateFacesString(this);
	}
	
	private void removePartOfFace(Obj bb, EnumFacing f) {
		// TODO Auto-generated method stub
		
	}

	/**Used to make the new Face objects and remove the ones that cannot be seen anymore when a bit is removed from the block
	 * @param bb
	 * @param world
	 * @param pos
	 */
	private void removeBitFromFaces(AxisAlignedBB bb, World world, BlockPos pos) {
		ArrayList<Face> oldRenderMap = (ArrayList<Face>) renderMap.clone();
		for(int i = 0; i < oldRenderMap.size(); i++) {
			Face f = oldRenderMap.get(i);
			if(f.face != null) {
				switch(f.face) {
				case UP :
					if(bb.maxY == f.maxY && bb.maxX > f.minX && bb.minX < f.maxX && bb.maxZ > f.minZ && bb.minZ < f.maxZ) { //Check if faces overlap
						Face old = f;
						if(bb.minX > f.minX) {
							Face newWestFromRemoval = 	new Face(old.minX, 	old.minY, old.minZ, 	bb.minX, 	old.maxY, old.maxZ, EnumFacing.UP, old.iconName, false);
							renderMap.add(newWestFromRemoval);
						}
						if(bb.maxX < f.maxX) {
							Face newEastFromRemoval = 	new Face(bb.maxX, 	old.minY, old.minZ, 	old.maxX, 	old.maxY, old.maxZ, EnumFacing.UP, old.iconName, false);
							renderMap.add(newEastFromRemoval);
						}
						if(bb.minZ > f.minZ) {
							Face newNorthFromRemoval = 	new Face((bb.minX >= old.minX) ? bb.minX : old.minX, 	old.minY, old.minZ,	(bb.maxX <= old.maxX) ? bb.maxX : old.maxX,	old.maxY, bb.minZ,	EnumFacing.UP, old.iconName, false);
							renderMap.add(newNorthFromRemoval);
						}
						if(bb.maxZ < f.maxZ) {
							Face newSouthFromRemoval = 	new Face((bb.minX >= old.minX) ? bb.minX : old.minX, 	old.minY, bb.maxZ,	(bb.maxX <= old.maxX) ? bb.maxX : old.maxX,	old.maxY, old.maxZ, EnumFacing.UP, old.iconName, false);
							renderMap.add(newSouthFromRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				case DOWN:
					if(bb.minY == f.minY && bb.maxX > f.minX && bb.minX < f.maxX && bb.maxZ > f.minZ && bb.minZ < f.maxZ) { //Check if faces overlap
						Face old = f;
						if(bb.minX > f.minX) {
							Face newWestFromRemoval = 	new Face(old.minX, 	old.minY, old.minZ, 	bb.minX, 	old.maxY, old.maxZ, EnumFacing.DOWN, old.iconName, false);
							renderMap.add(newWestFromRemoval);
						}
						if(bb.maxX < f.maxX) {
							Face newEastFromRemoval = 	new Face(bb.maxX, 	old.minY, old.minZ, 	old.maxX, 	old.maxY, old.maxZ, EnumFacing.DOWN, old.iconName, false);
							renderMap.add(newEastFromRemoval);
						}
						if(bb.minZ > f.minZ) {
							Face newNorthFromRemoval = 	new Face((bb.minX >= old.minX) ? bb.minX : old.minX, 	old.minY, old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.maxY, bb.minZ, 	EnumFacing.DOWN, old.iconName, false);
							renderMap.add(newNorthFromRemoval);
						}
						if(bb.maxZ < f.maxZ) {
							Face newSouthFromRemoval = 	new Face((bb.minX >= old.minX) ? bb.minX : old.minX, 	old.minY, bb.maxZ, 	(bb.maxX <= old.maxX) ? bb.maxX : old.maxX, old.maxY, old.maxZ, EnumFacing.DOWN, old.iconName, false);
							renderMap.add(newSouthFromRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				case NORTH:
					if(bb.minZ == f.minZ && bb.maxX > f.minX && bb.minX < f.maxX && bb.maxY > f.minY && bb.minY < f.maxY) { //Check if faces overlap
						Face old = f;
						if(bb.minX > f.minX) {
							Face newWestFromRemoval = 	new Face(old.minX, 	old.minY, 	old.minZ, bb.minX, 	old.maxY, old.maxZ, EnumFacing.NORTH, old.iconName, false);
							renderMap.add(newWestFromRemoval);
						}
						if(bb.maxX < f.maxX) {
							Face newEastFromRemoval = 	new Face(bb.maxX, 	old.minY, 	old.minZ, old.maxX, old.maxY, old.maxZ, EnumFacing.NORTH, old.iconName, false);
							renderMap.add(newEastFromRemoval);
						}
						if(bb.maxY < f.maxY) {
							Face newAboveRemoval = 		new Face((bb.minX >= old.minX) ? bb.minX : old.minX,	bb.maxY,	old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX,	old.maxY, old.maxZ, EnumFacing.NORTH, old.iconName, false);
							renderMap.add(newAboveRemoval);
						}
						if(bb.minY > f.minY) {
							Face newBelowRemoval = 		new Face((bb.minX >= old.minX) ? bb.minX : old.minX, 	old.minY, 	old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX, 	bb.minY, old.maxZ, EnumFacing.NORTH, old.iconName, false);
							renderMap.add(newBelowRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				case EAST:
					if(bb.maxX == f.maxX && bb.maxZ > f.minZ && bb.minZ < f.maxZ && bb.maxY > f.minY && bb.minY < f.maxY) { //Check if faces overlap
						Face old = f;
						if(bb.minZ > f.minZ) {
							Face newNorthFromRemoval = 	new Face(old.minX, old.minY, old.minZ, 	old.maxX, old.maxY, bb.minZ, 	EnumFacing.EAST, old.iconName, false);
							renderMap.add(newNorthFromRemoval);
						}
						if(bb.maxZ < f.maxZ) {
							Face newSouthFromRemoval = 	new Face(old.minX, old.minY, bb.maxZ, 	old.maxX, old.maxY, old.maxZ, 	EnumFacing.EAST, old.iconName, false);
							renderMap.add(newSouthFromRemoval);
						}
						if(bb.maxY < f.maxY) {
							Face newAboveRemoval = 		new Face(old.minX, bb.maxY,	(bb.minZ >= old.minZ) ? bb.minZ : old.minZ,	old.maxX, old.maxY, (bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ,	EnumFacing.EAST, old.iconName, false);
							renderMap.add(newAboveRemoval);
						}
						if(bb.minY > f.minY) {
							Face newBelowRemoval = 		new Face(old.minX, old.minY,(bb.minZ >= old.minZ) ? bb.minZ : old.minZ,	old.maxX, bb.minY, (bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ, 	EnumFacing.EAST, old.iconName, false);
							renderMap.add(newBelowRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				case SOUTH:
					if(bb.maxZ == f.maxZ && bb.maxX > f.minX && bb.minX < f.maxX && bb.maxY > f.minY && bb.minY < f.maxY) { //Check if faces overlap
						Face old = f;
						if(bb.minX > f.minX) {
							Face newWestFromRemoval =	new Face(old.minX, 	old.minY, 	old.minZ, bb.minX, 	old.maxY, old.maxZ, EnumFacing.SOUTH, old.iconName, false);
							renderMap.add(newWestFromRemoval);
						}
						if(bb.maxX < f.maxX) {
							Face newEastFromRemoval = 	new Face(bb.maxX, 	old.minY, 	old.minZ, old.maxX, old.maxY, old.maxZ, EnumFacing.SOUTH, old.iconName, false);
							renderMap.add(newEastFromRemoval);
						}
						if(bb.maxY < f.maxY) {
							Face newAboveRemoval = 		new Face((bb.minX >= old.minX) ? bb.minX : old.minX,	bb.maxY,	old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX,	old.maxY, 	old.maxZ, EnumFacing.SOUTH, old.iconName, false);
							renderMap.add(newAboveRemoval);
						}
						if(bb.minY > f.minY) {
							Face newBelowRemoval = 		new Face((bb.minX >= old.minX) ? bb.minX : old.minX,	old.minY,	old.minZ, (bb.maxX <= old.maxX) ? bb.maxX : old.maxX,	bb.minY, 	old.maxZ, EnumFacing.SOUTH, old.iconName, false);
							renderMap.add(newBelowRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				case WEST:
					if(bb.minX == f.minX && bb.maxZ > f.minZ && bb.minZ < f.maxZ && bb.maxY > f.minY && bb.minY < f.maxY) { //Check if faces overlap
						Face old = f;
						if(bb.minZ > f.minZ) {
							Face newNorthFromRemoval = 	new Face(old.minX, old.minY,	old.minZ, 	old.maxX, old.maxY, bb.minZ, 	EnumFacing.WEST, old.iconName, false);
							renderMap.add(newNorthFromRemoval);
						}
						if(bb.maxZ < f.maxZ) {
							Face newSouthFromRemoval = 	new Face(old.minX, old.minY,	bb.maxZ, 	old.maxX, old.maxY, old.maxZ, 	EnumFacing.WEST, old.iconName, false);
							renderMap.add(newSouthFromRemoval);
						}
						if(bb.maxY < f.maxY) {
							Face newAboveRemoval = 		new Face(old.minX, bb.maxY,		(bb.minZ >= old.minZ) ? bb.minZ : old.minZ,	old.maxX, old.maxY, (bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ,	EnumFacing.WEST, old.iconName, false);
							renderMap.add(newAboveRemoval);
						}
						if(bb.minY > f.minY) {
							Face newBelowRemoval = 		new Face(old.minX, old.minY,	(bb.minZ >= old.minZ) ? bb.minZ : old.minZ,	old.maxX, bb.minY,	(bb.maxZ <= old.maxZ) ? bb.maxZ : old.maxZ,	EnumFacing.WEST, old.iconName, false);
							renderMap.add(newBelowRemoval);
						}
						removeFromRenderMap(old, true);
//						renderMap.remove(old);
//						renderMapString.remove(old.string());
					}
					break;
				}
			} else {
				removeFromRenderMap(f, true);
//				renderMap.remove(f);
//				renderMapString.remove(f.string());
			}
		}
		
		for(EnumFacing f : EnumFacing.VALUES) {
			Face existing = doesBitOppositeFaceAlreadyExist(bb, f);
			if(existing == null) {
				System.out.println("face does not exist yet. Going to add a new one");
				Obj neighbour = getNeighborBound(bb, f);
				if(neighbour != null) {
					System.out.println("There is a neighbour for side " + f.name());
					addToRenderMap(this, new Face(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, f.getOpposite(), neighbour.blockType, true), false);
//					renderMap.add(new Face(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, f.getOpposite(), neighbour.blockType, true));
				}
			}
		}
		updateFacesString(this);
	}
	
	@Nullable
	public Obj getNeighborBound(AxisAlignedBB bit, EnumFacing facing) {
		ArrayList<Obj> objs = (ArrayList<Obj>) this.objs.clone();
		switch(facing) {
		case UP :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.minY == bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.minZ) {
					return check;
				}
			}
			break;
		case DOWN :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.maxY == bit.minY && check.minZ <= bit.minZ && check.maxZ >= bit.minZ) {
					return check;
				}
			}
			break;
		case NORTH :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.maxZ == bit.minZ) {
					return check;
				}
			}
			break;
		case EAST :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.minX == bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.maxZ) {
					return check;
				}
			}
			break;
		case SOUTH :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ == bit.maxZ) {
					return check;
				}
			}
			break;
		case WEST :
			for(int i = 0; i < objs.size(); i++) {
				Obj check = objs.get(i);
				if(check.maxX == bit.minX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.maxZ) {
					return check;
				}
			}
			break;
		}
		return null;
	}
	
	@Nullable
	public Face doesBitOppositeFaceAlreadyExist(AxisAlignedBB bit, EnumFacing facing) {
		ArrayList<Face> oldRenderMap = (ArrayList<Face>) renderMap.clone();
		switch(facing) {
		case UP :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.UP.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.minY == bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.minZ) {
						return check;
					}
				}
			}
			break;
		case DOWN :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.DOWN.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.maxY == bit.minY && check.minZ <= bit.minZ && check.maxZ >= bit.minZ) {
						return check;
					}
				}
			}
			break;
		case NORTH :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.NORTH.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.minX <= bit.minX && check.maxX >= bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.maxZ == bit.minZ) {
						return check;
					}
				}
			}
			break;
		case EAST :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.EAST.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.minX == bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.maxZ) {
						return check;
					}
				}
			}
			break;
		case SOUTH :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.SOUTH.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.maxX <= bit.minX && check.maxX >= bit.maxX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ == bit.maxZ) {
						return check;
					}
				}
			}
			break;
		case WEST :
			for(int i = 0; i < oldRenderMap.size(); i++) {
				if(oldRenderMap.get(i).face == EnumFacing.WEST.getOpposite()) {
					Face check = oldRenderMap.get(i);
					if(check.maxX == bit.minX && check.minY <= bit.minY && check.maxY >= bit.maxY && check.minZ <= bit.minZ && check.maxZ >= bit.maxZ) {
						return check;
					}
				}
			}
			break;
		}
		return null;
	}

	public void joinBounds() {
		ArrayList<Obj> potentialObjs = cloneList(this.objs);
		boolean New = true;
		boolean changed = false;
		while(!(potentialObjs.equals(this.objs)) || New) {
//			System.out.println("/// Going through the combination algorithm...");
			New = false;
			changed = false;
			this.objs = cloneList(potentialObjs);
			for(int i = 0; i < this.objs.size(); i++) {
				for(int j = 0; j < this.objs.size(); j++) {
					if(!changed) {
						if(i != j && this.objs.get(i).blockType == this.objs.get(j).blockType) {
							Obj o1 = this.objs.get(i);
							Obj o2 = this.objs.get(j);
//							System.out.println("Comparing: " + o1.minX + " -> " + o1.maxX + " || " + o2.minX + " -> " + o2.maxX + "        " + o1.minY + " -> " + o1.maxY + " || " + o2.minY + " -> " + o2.maxY + "        " + o1.minZ + " -> " + o1.maxZ + " || " + o2.minZ + " -> " + o2.maxZ);
							int lowI = (i < j) ? i : j;
							int highI = (j < i) ? i : j;
							double minX = (o1.minX < o2.minX) ? o1.minX : o2.minX;
							double minY = (o1.minY < o2.minY) ? o1.minY : o2.minY;
							double minZ = (o1.minZ < o2.minZ) ? o1.minZ : o2.minZ;
							double maxX = (o1.maxX > o2.maxX) ? o1.maxX : o2.maxX;
							double maxY = (o1.maxY > o2.maxY) ? o1.maxY : o2.maxY;
							double maxZ = (o1.maxZ > o2.maxZ) ? o1.maxZ : o2.maxZ;
							if(o1.minX == o2.minX && o1.maxX == o2.maxX) { // Checks for same x size
								if(o1.minY == o2.minY && o1.maxY == o2.maxY) { // Checks for same y size
									if(o1.minZ == o2.maxZ || o1.minZ == o2.maxZ) { // Checks if the bounds are neighbours on the z axis
										System.out.println("-- Combining: " + o1 + " and " + o2 + " to: " + new Obj(minX, minY, minZ, maxX, maxY, maxZ, o1.blockType));
										potentialObjs = this.replaceBoundswithBound(potentialObjs, highI, lowI, new Obj(minX, minY, minZ, maxX, maxY, maxZ, o1.blockType));
										if(j == highI) j--;
										if(i == highI) i--;
										changed = true;
									}
								} else if(o1.minZ == o2.minZ && o1.maxZ == o2.maxZ) { // Checks for same z size
									if(o1.minY == o2.maxY || o1.minY == o2.maxY) { // Checks if the bounds are neighbours on the y axis
//										System.out.println("-- Combining: " + o1 + " and " + o2);
										potentialObjs = this.replaceBoundswithBound(potentialObjs, highI, lowI, new Obj(minX, minY, minZ, maxX, maxY, maxZ, o1.blockType));
										if(j == highI) j--;
										if(i == highI) i--;
										changed = true;
									}
								}
							} else if(o1.minY == o2.minY && o1.maxY == o2.maxY) { // Checks for same y size
								if(o1.minZ == o2.minZ && o1.maxZ == o2.maxZ) { // Checks for same z size
									if(o1.minX == o2.maxX || o1.minX == o2.maxX) { // Checks if the bounds are neighbours on the x axis
//										System.out.println("-- Combining: " + o1 + " and " + o2);
										potentialObjs = this.replaceBoundswithBound(potentialObjs, highI, lowI, new Obj(minX, minY, minZ, maxX, maxY, maxZ, o1.blockType));
										if(j == highI) j--;
										if(i == highI) i--;
										changed = true;
									}
								}
							}
						}
					}
				}
			}
		}
		
		m.objs = this.objs;
		
		System.out.println("---------------------------------------------------");
		for(int i = 0; i < this.objs.size(); i++) {
			System.out.println(this.objs.get(i));
		}
		System.out.println("---------------------------------------------------");
	}
	
	public ArrayList<Obj> cloneList(ArrayList<Obj> list) {
		ArrayList<Obj> clone = new ArrayList<Obj>(list.size());
	    for(Obj item : list) clone.add(item.clone());
	    return clone;
	}
	
	public ArrayList<Obj> replaceBoundswithBound(ArrayList<Obj> a, int toBeRemoved, int toBeReplaced, Obj replacement) {
		a.set(toBeReplaced, replacement);
		a.remove(toBeRemoved);
		return a;
	}
	
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				Main.network.sendToAllAround(new PacketUpdateMiniBlock(TileEntityMiniBlock.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 1));
			}
		}
	};

	@Override
	public void onLoad() {
		if (world.isRemote) {
			Main.network.sendToServer(new PacketRequestUpdateMiniBlock(this));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		int[] blockIDs = new int[4095];
		for(int i = 0; i < objs.size(); i++) {
			Obj o = objs.get(i);
			compound.setString("objs" + i, o.toString());
			compound.setString("blockType" + i, blockStateToString(o.blockType));
			blockIDs[i] = o.blockType.getBlock().getIdFromBlock(o.blockType.getBlock());
		}
		compound.setIntArray("blockIDs", blockIDs);
		if(renderMapString != null) {
			for(int i = 0; i < renderMapString.size(); i++) {
				compound.setString("renderMap" + i, renderMapString.get(i));
			}	
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		
		if(compound.hasKey("objs0") && compound.hasKey("blockIDs") && compound.hasKey("blockType0")) {
			if(!compound.getString("objs0").isEmpty()) {
				ArrayList<String> objsString = new ArrayList<String>();
				for(int i = 0; compound.hasKey("objs" + i); i++) {
					objsString.add(compound.getString("objs" + i));
				}
				ArrayList<String> blockTypes = new ArrayList<String>();
				for(int i = 0; compound.hasKey("blockType" + i); i++) {
					blockTypes.add(compound.getString("blockType" + i));
				}
				this.objs = ObjArrayFromString(objsString, blockTypes, compound.getIntArray("blockIDs"));
			}
		}
		
		if(compound.hasKey("renderMap0")) {
			if(!compound.getString("renderMap0").isEmpty()) {
				for(int i = 0; compound.hasKey("renderMap" + i); i++) {
					addToRenderMap(this, new Face(compound.getString("renderMap" + i)), false);
				}
				updateFacesString(this);
			}
		}
		super.readFromNBT(compound);
	}
	
	public ArrayList<Obj> ObjArrayFromString(ArrayList<String> bounds, ArrayList<String> blockTypeStrings, int[] blockIDs) {
		ArrayList<Obj> objs2 = new ArrayList<Obj>();
		for(int i = 0; i < bounds.size(); i++) {
			String s = bounds.get(i);
			int beginI1 = 5;
			int beginI2 = s.indexOf("->") + 2;
			int endI1 = s.indexOf("->") - 1;
			int endI2 = s.length() - 1;
			String from = s.substring(beginI1, endI1);
			String to = s.substring(beginI2, endI2);
			String[] fromList = from.split(Pattern.quote(", "));
			String[] toList = to.split(Pattern.quote(", "));
			ArrayList<Double> values = new ArrayList<Double>();
			for(int j = 0; j < fromList.length; j++) {
				values.add(Double.parseDouble(fromList[j]));
			}
			for(int j = 0; j < toList.length; j++) {
				values.add(Double.parseDouble(toList[j]));
			}
			objs2.add(new Obj(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5), blockStateFromStringAndBaseState(Block.getBlockById(blockIDs[i]).getDefaultState(), blockTypeStrings.get(i))));
		}
		return objs2;
	}
	
	public void createRenderMapFromString(String renderMapStrings) {
		int beginIndex = 1;
		int endIndex = renderMapStrings.length() - 1;
		String sub = renderMapStrings.substring(beginIndex, endIndex);
		String[] faces = sub.split(Pattern.quote(", "));
		renderMap = new ArrayList<Face>();
		renderMapString = new ArrayList<String>();
		for(int i = 0; i < faces.length; i++) {
			renderMap.add(new Face(faces[i]));
		}
		updateFacesString(this);
	}
	
	public String blockStateToString(IBlockState blockState) {
		String s= "";
		for(IProperty p : blockState.getPropertyKeys()) {
			s = s + "{" + p.getClass() + ", name=" + p.getName().toString() + ", value=" + blockState.getProperties().get(p).toString() + ", values=" + p.getAllowedValues() + "}, ";
		}
		return s.substring(0, s.length() - 3);
	}
	
	public IBlockState blockStateFromStringAndBaseState(IBlockState baseState, String blockStateString) {
		String[] propsR = blockStateString.split("}, ");
		IBlockState newState = baseState;
		for(String propR : propsR) {
			String prop = propR.substring(1, propR.length());
			System.out.println("Property: " + prop);
			String type = prop.substring(0, prop.indexOf(", name"));
			String name = prop.substring(prop.indexOf("name") + 5, prop.indexOf("value") - 2);
			String value = prop.substring(prop.indexOf("value") + 6, prop.indexOf("values") - 2);
			System.out.println(name + ": " + value);
			String values = prop.substring(prop.indexOf("values") + 7, prop.length());
			System.out.println("possible values: " + values);
			switch (type) {
			case "class net.minecraft.block.properties.PropertyDirection":
				String s = values.substring(1, values.length() - 1);
				String[] valuesList = s.split(", ");
				ArrayList<EnumFacing> p = new ArrayList<EnumFacing>();
				for(String s1 : valuesList) {
					p.add(EnumFacing.byName(s1));
				}
				PropertyDirection d = PropertyDirection.create(name, p);
				if(EnumFacing.byName(value) != null) {
					EnumFacing facing = EnumFacing.byName(value);
					newState = baseState.withProperty(d, facing);
				}
				break;
			case "class net.minecraft.block.properties.PropertyBool":
				PropertyBool b = PropertyBool.create(name);
				if(value == "true" || value == "false") {
					boolean Boolean = (value == "true") ? true : false;
					newState = baseState.withProperty(b, Boolean);
				}
				break;
			case "class net.minecraft.block.properties.PropertyInteger":
				PropertyInteger i = PropertyInteger.create(name, 0, 1400000000);
				if(NumberUtils.isCreatable(value)) {
					int integer = Integer.parseInt(value);
					newState = baseState.withProperty(i, integer);
				}
				break;
			default:
				break;
			}
		}
		return newState;
	}
	
	public IBakedModel bakeModel() {
		
		
		
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(capability, facing);
	}
}