package com.extraBlocks.pedestal;

import javax.annotation.Nullable;

import com.extraBlocks.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPedestal extends TileEntity {
	
	public long lastChangeTime;
	String name = new String("Pedestal");
	
	public double motionMultX = 0.1, motionMultY = 0.0, motionMultZ = 0.0;
	public double motionSpeedX = 0.1, motionSpeedY = 0.0, motionSpeedZ = 0.0;
	public double motionTimeOffX = 0.0, motionTimeOffY = 0.0, motionTimeOffZ = 0.0;
	public double rotationSpeedX = 0.0, rotationSpeedY = 0.0, rotationSpeedZ = 0.0;
	public double rotationTimeOffX = 0.0, rotationTimeOffY = 0.0, rotationTimeOffZ = 0.0;
	public double positionOffsetX = 0.0, positionOffsetY = 0.0, positionOffsetZ = 0.0;
	
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				TileEntityPedestal.this.name = new TextComponentString(getStackInSlot(slot).getDisplayName()).getUnformattedComponentText();
				System.out.println(TileEntityPedestal.this.name);
				Main.network.sendToAllAround(new PacketUpdatePedestal(TileEntityPedestal.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0.1));
			}
		}
	};

	@Override
	public void onLoad() {
		if (world.isRemote) {
			Main.network.sendToServer(new PacketRequestUpdatePedestal(this));
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		
//		System.out.println("doing write");
		
//		System.out.println("1 : Key \"0001X\" was" + (compound.hasKey("0001X") ? " stored. Value is: " + compound.getDouble("0001X") : "n\'t stored"));
//		System.out.println(compound.getKeySet().toString());
		
		compound.setDouble("0001X", getMotionMultX());
		compound.setDouble("0002X", getMotionSpeedX());
		compound.setDouble("0003X", getMotionTimeOffX());
		compound.setDouble("0004X", getRotationSpeedX());
		compound.setDouble("0005X", getRotationTimeOffX());
		compound.setDouble("0006X", getPositionOffsetX());
		compound.setDouble("0001Y", getMotionMultY());
		compound.setDouble("0002Y", getMotionSpeedY());
		compound.setDouble("0003Y", getMotionTimeOffY());
		compound.setDouble("0004Y", getRotationSpeedY());
		compound.setDouble("0005Y", getRotationTimeOffY());
		compound.setDouble("0006Y", getPositionOffsetY());
		compound.setDouble("0001Z", getMotionMultZ());
		compound.setDouble("0002Z", getMotionSpeedZ());
		compound.setDouble("0003Z", getMotionTimeOffZ());
		compound.setDouble("0004Z", getRotationSpeedZ());
		compound.setDouble("0005Z", getRotationTimeOffZ());
		compound.setDouble("0006Z", getPositionOffsetZ());
		
//		System.out.println(compound.getKeySet().toString());
		
//		System.out.println("2 : Key \"0001X\" was" + (compound.hasKey("0001X") ? " stored. Value is: " + compound.getDouble("0001X") : "n\'t stored"));
		
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		
//		System.out.println("3 : Key \"0001X\" was" + (compound.hasKey("0001X") ? " stored. Value is: " + compound.getDouble("0001X") : "n\'t stored"));
		
		setMotionMultX(		compound.getDouble("0001X"));
		setMotionSpeedX(	compound.getDouble("0002X"));
		setMotionTimeOffX(	compound.getDouble("0003X"));
		setRotationSpeedX(	compound.getDouble("0004X"));
		setRotationTimeOffX(compound.getDouble("0005X"));
		setPositionOffsetX(	compound.getDouble("0006X"));
		setMotionMultY(		compound.getDouble("0001Y"));
		setMotionSpeedY(	compound.getDouble("0002Y"));
		setMotionTimeOffY(	compound.getDouble("0003Y"));
		setRotationSpeedY(	compound.getDouble("0004Y"));
		setRotationTimeOffY(compound.getDouble("0005Y"));
		setPositionOffsetY(	compound.getDouble("0006Y"));
		setMotionMultZ(		compound.getDouble("0001Z"));
		setMotionSpeedZ(	compound.getDouble("0002Z"));
		setMotionTimeOffZ(	compound.getDouble("0003Z"));
		setRotationSpeedZ(	compound.getDouble("0004Z"));
		setRotationTimeOffZ(compound.getDouble("0005Z"));
		setPositionOffsetZ(	compound.getDouble("0006Z"));
		
		super.readFromNBT(compound);
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
	
	public double getMotionMultX() {
		return motionMultX;
	}

	public void setMotionMultX(double motionMultX) {
		this.motionMultX = motionMultX;
	}

	public double getMotionMultY() {
		return motionMultY;
	}

	public void setMotionMultY(double motionMultY) {
		this.motionMultY = motionMultY;
	}

	public double getMotionMultZ() {
		return motionMultZ;
	}

	public void setMotionMultZ(double motionMultZ) {
		this.motionMultZ = motionMultZ;
	}

	public double getMotionSpeedX() {
		return motionSpeedX;
	}

	public void setMotionSpeedX(double motionSpeedX) {
		this.motionSpeedX = motionSpeedX;
	}

	public double getMotionSpeedY() {
		return motionSpeedY;
	}

	public void setMotionSpeedY(double motionSpeedY) {
		this.motionSpeedY = motionSpeedY;
	}

	public double getMotionSpeedZ() {
		return motionSpeedZ;
	}

	public void setMotionSpeedZ(double motionSpeedZ) {
		this.motionSpeedZ = motionSpeedZ;
	}

	public double getMotionTimeOffX() {
		return motionTimeOffX;
	}

	public void setMotionTimeOffX(double motionTimeOffX) {
		this.motionTimeOffX = motionTimeOffX;
	}

	public double getMotionTimeOffY() {
		return motionTimeOffY;
	}

	public void setMotionTimeOffY(double motionTimeOffY) {
		this.motionTimeOffY = motionTimeOffY;
	}

	public double getMotionTimeOffZ() {
		return motionTimeOffZ;
	}

	public void setMotionTimeOffZ(double motionTimeOffZ) {
		this.motionTimeOffZ = motionTimeOffZ;
	}

	public double getRotationSpeedX() {
		return rotationSpeedX;
	}

	public void setRotationSpeedX(double rotationSpeedX) {
		this.rotationSpeedX = rotationSpeedX;
	}

	public double getRotationSpeedY() {
		return rotationSpeedY;
	}

	public void setRotationSpeedY(double rotationSpeedY) {
		this.rotationSpeedY = rotationSpeedY;
	}

	public double getRotationSpeedZ() {
		return rotationSpeedZ;
	}

	public void setRotationSpeedZ(double rotationSpeedZ) {
		this.rotationSpeedZ = rotationSpeedZ;
	}

	public double getRotationTimeOffX() {
		return rotationTimeOffX;
	}

	public void setRotationTimeOffX(double rotationTimeOffX) {
		this.rotationTimeOffX = rotationTimeOffX;
	}

	public double getRotationTimeOffY() {
		return rotationTimeOffY;
	}

	public void setRotationTimeOffY(double rotationTimeOffY) {
		this.rotationTimeOffY = rotationTimeOffY;
	}

	public double getRotationTimeOffZ() {
		return rotationTimeOffZ;
	}

	public void setRotationTimeOffZ(double rotationTimeOffZ) {
		this.rotationTimeOffZ = rotationTimeOffZ;
	}

	public double getPositionOffsetX() {
		return positionOffsetX;
	}

	public void setPositionOffsetX(double positionOffsetX) {
		this.positionOffsetX = positionOffsetX;
	}

	public double getPositionOffsetY() {
		return positionOffsetY;
	}

	public void setPositionOffsetY(double positionOffsetY) {
		this.positionOffsetY = positionOffsetY;
	}

	public double getPositionOffsetZ() {
		return positionOffsetZ;
	}

	public void setPositionOffsetZ(double positionOffsetZ) {
		this.positionOffsetZ = positionOffsetZ;
	}

}