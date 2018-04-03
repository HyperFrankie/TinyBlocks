package com.extraBlocks.pedestal;

import com.extraBlocks.pedestal.TileEntityPedestal;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdatePedestal implements IMessage {
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	private TileEntityPedestal te;
	
	public PacketUpdatePedestal(BlockPos pos, ItemStack stack, long lastChangeTime) {
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
	}
	
	public PacketUpdatePedestal(TileEntityPedestal te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime);
		this.te = te;
	}
	
	public PacketUpdatePedestal() {
		
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
	
	public static class Handler implements IMessageHandler<PacketUpdatePedestal, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdatePedestal message, MessageContext ctx) {
			Minecraft.getMinecraft().getIntegratedServer().addScheduledTask(() -> {
				IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
				World messageWorld = Minecraft.getMinecraft().world;
				WorldProvider wp = messageWorld.provider;
				int dimension = wp.getDimension();
				WorldServer world = server.getWorld(dimension);
				if(world.getTileEntity(message.pos) instanceof TileEntityPedestal) {
					TileEntityPedestal te = (TileEntityPedestal) world.getTileEntity(message.pos);
					te.inventory.setStackInSlot(0, message.stack);
					te.lastChangeTime = message.lastChangeTime;
				}
				
			});
			return null;
		}
	
	}

}