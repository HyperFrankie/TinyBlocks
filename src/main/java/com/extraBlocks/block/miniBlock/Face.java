package com.extraBlocks.block.miniBlock;

import java.util.ArrayList;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Face {

	public double minU, minV, maxU, maxV, baseU, baseV, minX, minY, minZ, maxX, maxY, maxZ, r01, r02, r03, r04, r05, r06, r07, r08, r09, r10, r11, r12;
	public EnumFacing face;
	public String iconName;
	
//	public Face(double minUOffset, double minVOffset, double maxUOffset, double maxVOffset, EnumFacing face, IBlockState blockState) {
//		this.blockState = blockState;
//		this.iconName = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState).getParticleTexture().getIconName();
//		this.baseU = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinU();
//		this.baseV = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinV();
//		this.minU = this.baseU + minUOffset;
//		this.minV = this.baseV + minVOffset;
//		this.maxU = this.baseU + maxUOffset;
//		this.maxV = this.baseV + maxVOffset;
//		this.face = face;
//	}
	
	public Face(Vec3d minVec, Vec3d maxVec, EnumFacing face, IBlockState blockState, boolean oppositeSide) {
		this(minVec, maxVec, face, Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState).getParticleTexture().getIconName(), oppositeSide);
		iconName = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState).getParticleTexture().getIconName();
	}
	
	public Face(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, EnumFacing face, IBlockState blockState, boolean oppositeSide) {
		this(minX, minY, minZ, maxX, maxY, maxZ, face, Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState).getParticleTexture().getIconName(), oppositeSide);
		this.iconName = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState).getParticleTexture().getIconName();
	}
	
	public Face(Vec3d minVec, Vec3d maxVec, EnumFacing face, String iconName, boolean oppositeSide) {
		if(oppositeSide) {
			face = face.getOpposite();
		}
		switch(face) {
		case UP :
			if(minVec.y != maxVec.y) {
				minVec = new Vec3d(minVec.x, maxVec.y, minVec.z);
			}
			break;
		case DOWN:
			if(maxVec.y != minVec.y) {
				maxVec = new Vec3d(maxVec.x, minVec.y, maxVec.z);
			}
			break;
		case NORTH:
			if(maxVec.z != minVec.z) {
				maxVec = new Vec3d(maxVec.x, maxVec.y, minVec.z);
			}
			break;
		case EAST:
			if(maxVec.x != minVec.x) {
				minVec = new Vec3d(maxVec.x, minVec.y, minVec.z);
			}
			break;
		case SOUTH:
			if(maxVec.z != minVec.z) {
				minVec = new Vec3d(minVec.x, minVec.y, maxVec.z);
			}
			break;
		case WEST:
			if(maxVec.x != minVec.x) {
				maxVec = new Vec3d(minVec.x, maxVec.y, maxVec.z);
			}
			break;
		}
		if(oppositeSide) {
			face = face.getOpposite();
		}
		if(
			((face == EnumFacing.NORTH || face == EnumFacing.SOUTH) && minVec.x != maxVec.x && minVec.y != maxVec.y && minVec.z == maxVec.z) ||
			((face == EnumFacing.UP || face == EnumFacing.DOWN) && minVec.x != maxVec.x && minVec.y == maxVec.y && minVec.z != maxVec.z) ||
			((face == EnumFacing.EAST || face == EnumFacing.WEST) && minVec.x == maxVec.x && minVec.y != maxVec.y && minVec.z != maxVec.z)
		) {
			this.iconName = iconName;
			this.baseU = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinU();
			this.baseV = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinV();
			this.face = face;
			this.minU = this.baseU + getminUFromC(minVec, maxVec, face);
			this.minV = this.baseV + getminVFromC(minVec, maxVec, face);
			this.maxU = this.baseU + getmaxUFromC(minVec, maxVec, face);
			this.maxV = this.baseV + getmaxVFromC(minVec, maxVec, face);
			this.minX = minVec.x;
			this.minY = minVec.y;
			this.minZ = minVec.z;
			this.maxX = maxVec.x;
			this.maxY = maxVec.y;
			this.maxZ = maxVec.z;
			generateRenderCoordinates();
		}
	}
	
	public Face(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, EnumFacing face, String iconName, boolean oppositeSide) {
		if(oppositeSide) {
			face = face.getOpposite();
		}
		switch(face) {
		case UP :
			if(minY != maxY) {
				minY = maxY;
			}
			break;
		case DOWN:
			if(maxY != minY) {
				maxY = minY;
			}
			break;
		case NORTH:
			if(maxZ != minZ) {
				maxZ = minZ;
			}
			break;
		case EAST:
			if(maxX != minX) {
				minX = maxX;
			}
			break;
		case SOUTH:
			if(maxZ != minZ) {
				minZ = maxZ;
			}
			break;
		case WEST:
			if(maxX != minX) {
				maxX = minX;
			}
			break;
		}
		if(oppositeSide) {
			face = face.getOpposite();
		}
		if(
			((face == EnumFacing.NORTH || face == EnumFacing.SOUTH) && minX != maxX && minY != maxY && minZ == maxZ) ||
			((face == EnumFacing.UP || face == EnumFacing.DOWN) && minX != maxX && minY == maxY && minZ != maxZ) ||
			((face == EnumFacing.EAST || face == EnumFacing.WEST) && minX == maxX && minY != maxY && minZ != maxZ)
		) {
			Vec3d minVec = new Vec3d(minX, minY, minZ);
			Vec3d maxVec = new Vec3d(maxX, maxY, maxZ);
			this.iconName = iconName;
			this.baseU = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinU();
			this.baseV = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iconName).getMinV();
			this.face = face;
			this.minU = this.baseU + getminUFromC(minVec, maxVec, face);
			this.minV = this.baseV + getminVFromC(minVec, maxVec, face);
			this.maxU = this.baseU + getmaxUFromC(minVec, maxVec, face);
			this.maxV = this.baseV + getmaxVFromC(minVec, maxVec, face);
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
			generateRenderCoordinates();
		}
	}
	
	public double getminUFromC(Vec3d minVec, Vec3d maxVec, EnumFacing f) {
		switch(f) {
		case UP :
			return minVec.x / 32;
		case DOWN:
			return minVec.x / 32;
		case NORTH:
			return (1.0 - maxVec.x) / 32;
		case EAST:
			return (1.0 - maxVec.z) / 32;
		case SOUTH:
			return minVec.x / 32;
		case WEST:
			return minVec.z / 32;
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return 0.0;
	}
	
	public double getmaxUFromC(Vec3d minVec, Vec3d maxVec, EnumFacing f) {
		switch(f) {
		case UP :
			return maxVec.x / 32;
		case DOWN:
			return maxVec.x / 32;
		case NORTH:
			return (1.0 - minVec.x) / 32;
		case EAST:
			return (1.0 - minVec.z) / 32;
		case SOUTH:
			return maxVec.x / 32;
		case WEST:
			return maxVec.z / 32;
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return 0.0;
	}
	
	public double getminVFromC(Vec3d minVec, Vec3d maxVec, EnumFacing f) {
		switch(f) {
		case UP :
			return minVec.z / 32;
		case DOWN:
			return (1.0 - maxVec.z) / 32;
		case NORTH:
			return (1.0 - maxVec.y) / 32;
		case EAST:
			return (1.0 - maxVec.y) / 32;
		case SOUTH:
			return (1.0 - maxVec.y) / 32;
		case WEST:
			return (1.0 - maxVec.y) / 32;
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return 0.0;
	}

	public double getmaxVFromC(Vec3d minVec, Vec3d maxVec, EnumFacing f) {
		switch(f) {
		case UP :
			return maxVec.z / 32;
		case DOWN:
			return (1.0 - minVec.z) / 32;
		case NORTH:
			return (1.0 - minVec.y) / 32;
		case EAST:
			return (1.0 - minVec.y) / 32;
		case SOUTH:
			return (1.0 - minVec.y) / 32;
		case WEST:
			return (1.0 - minVec.y) / 32;
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return 0.0;
	}
	
	public static Face getFullFaceFromEnumFacing(EnumFacing f, IBlockState blockState) {
		System.out.println(f);
		switch(f) {
		case UP :
			return new Face(0.0, 1.0, 0.0, 1.0, 1.0, 1.0, f, blockState, false);
		case DOWN:
			return new Face(0.0, 0.0, 0.0, 1.0, 0.0, 1.0, f, blockState, false);
		case NORTH:
			return new Face(0.0, 0.0, 0.0, 1.0, 1.0, 0.0, f, blockState, false);
		case EAST:
			return new Face(1.0, 0.0, 0.0, 1.0, 1.0, 1.0, f, blockState, false);
		case SOUTH:
			return new Face(0.0, 0.0, 1.0, 1.0, 1.0, 1.0, f, blockState, false);
		case WEST:
			return new Face(0.0, 0.0, 0.0, 0.0, 1.0, 1.0, f, blockState, false);
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return null;
	}

	public static IBlockState getBlockNextToFace(World world, BlockPos pos, EnumFacing f) {
		switch(f) {
		case UP :
			return world.getBlockState(pos.add(0, 1, 0));
		case DOWN:
			return world.getBlockState(pos.add(0, -1, 0));
		case NORTH:
			return world.getBlockState(pos.add(0, 0, -1));
		case EAST:
			return world.getBlockState(pos.add(1, 0, 0));
		case SOUTH:
			return world.getBlockState(pos.add(0, 0, 1));
		case WEST:
			return world.getBlockState(pos.add(-1, 0, 0));
		}
		System.out.println("This should never happen! If you see this, something is horribly wrong...");
		return null;
	}
	
	public void generateRenderCoordinates() {
		switch(this.face) {
		case UP :
			r01 = maxX;
			r02 = maxY;
			r03 = maxZ;
			r04 = maxX;
			r05 = maxY;
			r06 = minZ;
			r07 = minX;
			r08 = maxY;
			r09 = minZ;
			r10 = minX;
			r11 = maxY;
			r12 = maxZ;
			break;
		case DOWN:
			r01 = maxX;
			r02 = minY;
			r03 = minZ;
			r04 = maxX;
			r05 = minY;
			r06 = maxZ;
			r07 = minX;
			r08 = minY;
			r09 = maxZ;
			r10 = minX;
			r11 = minY;
			r12 = minZ;
			break;
		case NORTH:
			r01 = minX;
			r02 = minY;
			r03 = minZ;
			r04 = minX;
			r05 = maxY;
			r06 = minZ;
			r07 = maxX;
			r08 = maxY;
			r09 = minZ;
			r10 = maxX;
			r11 = minY;
			r12 = minZ;
			break;
		case EAST:
			r01 = maxX;
			r02 = minY;
			r03 = minZ;
			r04 = maxX;
			r05 = maxY;
			r06 = minZ;
			r07 = maxX;
			r08 = maxY;
			r09 = maxZ;
			r10 = maxX;
			r11 = minY;
			r12 = maxZ;
			break;
		case SOUTH:
			r01 = maxX;
			r02 = minY;
			r03 = maxZ;
			r04 = maxX;
			r05 = maxY;
			r06 = maxZ;
			r07 = minX;
			r08 = maxY;
			r09 = maxZ;
			r10 = minX;
			r11 = minY;
			r12 = maxZ;
			break;
		case WEST:
			r01 = maxX;
			r02 = minY;
			r03 = maxZ;
			r04 = maxX;
			r05 = maxY;
			r06 = maxZ;
			r07 = maxX;
			r08 = maxY;
			r09 = minZ;
			r10 = maxX;
			r11 = minY;
			r12 = minZ;
			break;
		}
	}

	public String string() {
		return "Face[ " + 
	r01 + " |/| " + 
	r02 + " |/| " + 
	r03 + " |/| " + 
	r04 + " |/| " + 
	r05 + " |/| " + 
	r06 + " |/| " + 
	r07 + " |/| " + 
	r08 + " |/| " + 
	r09 + " |/| " + 
	r10 + " |/| " + 
	r11 + " |/| " + 
	r12 + " |/| " +
	baseU + " |/| " +
	baseV + " |/| " +
	minU + " |/| " +
	minV + " |/| " +
	maxU + " |/| " +
	maxV + " |/| " +
	minX + " |/| " +
	minY + " |/| " +
	minZ + " |/| " +
	maxX + " |/| " +
	maxY + " |/| " +
	maxZ + " |/| " +
	face.name() + " |/| " +
	iconName +
	" ]";
	}
	
	public Face(String stringToBuildFaceFrom) {
		int startI = 6;
		int stopI = stringToBuildFaceFrom.length() - 2;
		String sub = stringToBuildFaceFrom.substring(startI, stopI);
		String[] strings = sub.split(Pattern.quote(" |/| "));
		if(strings.length != 0) {
			r01 = Double.parseDouble(strings[0]);
			r02 = Double.parseDouble(strings[1]);
			r03 = Double.parseDouble(strings[2]);
			r04 = Double.parseDouble(strings[3]);
			r05 = Double.parseDouble(strings[4]);
			r06 = Double.parseDouble(strings[5]);
			r07 = Double.parseDouble(strings[6]);
			r08 = Double.parseDouble(strings[7]);
			r09 = Double.parseDouble(strings[8]);
			r10 = Double.parseDouble(strings[9]);
			r11 = Double.parseDouble(strings[10]);
			r12 = Double.parseDouble(strings[11]);
			baseU = Double.parseDouble(strings[12]);
			baseV = Double.parseDouble(strings[13]);
			minU = Double.parseDouble(strings[14]);
			minV = Double.parseDouble(strings[15]);
			maxU = Double.parseDouble(strings[16]);
			maxV = Double.parseDouble(strings[17]);
			minX = Double.parseDouble(strings[18]);
			minY = Double.parseDouble(strings[19]);
			minZ = Double.parseDouble(strings[20]);
			maxX = Double.parseDouble(strings[21]);
			maxY = Double.parseDouble(strings[22]);
			maxZ = Double.parseDouble(strings[23]);
			face = EnumFacing.byName(strings[24]);
			iconName = strings[25];
		}
	}
}
