package com.extraBlocks.block.miniBlock;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.collection.immutable.List;

public class PacketUpdateMiniBlock implements IMessage {
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	private TileEntityMiniBlock te;
	public ArrayList<Obj> objs = new ArrayList<Obj>();
	
	public PacketUpdateMiniBlock(BlockPos pos, ItemStack stack, long lastChangeTime, ArrayList<Obj> objs) {
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
		this.objs = objs;
	}
	
	public PacketUpdateMiniBlock(TileEntityMiniBlock te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime, te.objs);
		this.te = te;
	}
	
	public PacketUpdateMiniBlock() {
		
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
	
	public static class Handler implements IMessageHandler<PacketUpdateMiniBlock, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateMiniBlock message, MessageContext ctx) {
			Minecraft.getMinecraft().getIntegratedServer().addScheduledTask(() -> {
				IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
				World messageWorld = Minecraft.getMinecraft().world;
				WorldProvider wp = messageWorld.provider;
				int dimension = wp.getDimension();
				WorldServer world = server.getWorld(dimension);
				if(world.getTileEntity(message.pos) instanceof TileEntityMiniBlock) {
					TileEntityMiniBlock te = (TileEntityMiniBlock) world.getTileEntity(message.pos);
					te.inventory.setStackInSlot(0, message.stack);
					te.objs = message.objs;
					te.lastChangeTime = message.lastChangeTime;
				}
			});
			return null;
		}
	
	}

}