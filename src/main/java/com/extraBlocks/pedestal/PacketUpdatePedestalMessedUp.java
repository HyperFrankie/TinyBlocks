package com.extraBlocks.pedestal;

import com.extraBlocks.pedestal.TileEntityPedestal;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.swing.TextComponent;

public class PacketUpdatePedestalMessedUp implements IMessage {
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	private double d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, d18;
	
	public PacketUpdatePedestalMessedUp(BlockPos pos, ItemStack stack, long lastChangeTime, double d01, double d02, double d03, double d04, double d05, double d06, double d07, double d08, double d09, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17, double d18) {
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
		this.d1 = d01;
		this.d2 = d02;
		this.d3 = d03;
		this.d4 = d04;
		this.d5 = d05;
		this.d6 = d06;
		this.d7 = d07;
		this.d8 = d08;
		this.d9 = d09;
		this.d10 = d10;
		this.d11 = d11;
		this.d12 = d12;
		this.d13 = d13;
		this.d14 = d14;
		this.d15 = d15;
		this.d16 = d16;
		this.d17 = d17;
		this.d18 = d18;
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString("0006X inside PacketUpdatePedestal: " + d06));
	}
	
	public PacketUpdatePedestalMessedUp(TileEntityPedestal te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime, te.getMotionMultX(), te.getMotionSpeedX(), te.getMotionTimeOffX(), te.getRotationSpeedX(), te.getRotationTimeOffX(), te.getPositionOffsetX(), te.getMotionMultY(), te.getMotionSpeedY(), te.getMotionTimeOffY(), te.getRotationSpeedY(), te.getRotationTimeOffY(), te.getPositionOffsetY(), te.getMotionMultZ(), te.getMotionSpeedZ(), te.getMotionTimeOffZ(), te.getRotationSpeedZ(), te.getRotationTimeOffZ(), te.getPositionOffsetZ());
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString("the value of 0006X in PacketUpdatePedestal EC is: "  + te.getPositionOffsetX()));
	}
	
	public PacketUpdatePedestalMessedUp() {
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeLong(lastChangeTime);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		stack = ByteBufUtils.readItemStack(buf);
		lastChangeTime = buf.readLong();
	}
	
	public static class Handler implements IMessageHandler<PacketUpdatePedestalMessedUp, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdatePedestalMessedUp message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityPedestal te = (TileEntityPedestal)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack);
				te.lastChangeTime = message.lastChangeTime;
				
				te.setMotionMultX(message.d1);
				te.setMotionSpeedX(message.d2);
				te.setMotionTimeOffX(message.d3);
				te.setRotationSpeedX(message.d4);
				te.setRotationTimeOffX(message.d5);
				te.setPositionOffsetX(message.d6);
				te.setMotionMultY(message.d7);
				te.setMotionSpeedY(message.d8);
				te.setMotionTimeOffY(message.d9);
				te.setRotationSpeedY(message.d10);
				te.setRotationTimeOffY(message.d11);
				te.setPositionOffsetY(message.d12);
				te.setMotionMultZ(message.d13);
				te.setMotionSpeedZ(message.d14);
				te.setMotionTimeOffZ(message.d15);
				te.setRotationSpeedZ(message.d16);
				te.setRotationTimeOffZ(message.d17);
				te.setPositionOffsetZ(message.d18);
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Message Handler found the value of 0001X to be: " + message.d6));
//				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("PackedUpdatePedestal: " + message.stack));
				
			});
			return null;
		}
	
	}

}