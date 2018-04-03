package com.extraBlocks.block.miniBlock;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateMiniBlock implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateMiniBlock(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateMiniBlock(TileEntityMiniBlock te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateMiniBlock() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateMiniBlock, PacketUpdateMiniBlock> {
	
		@Override
		public PacketUpdateMiniBlock onMessage(PacketRequestUpdateMiniBlock message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			if(Minecraft.getMinecraft().getIntegratedServer().getWorld(message.dimension).getTileEntity(message.pos) instanceof TileEntityMiniBlock) {
				TileEntityMiniBlock te = (TileEntityMiniBlock) Minecraft.getMinecraft().getIntegratedServer().getWorld(message.dimension).getTileEntity(message.pos);
				te.dim = message.dimension;
				return new PacketUpdateMiniBlock(te);
			} else {
				return null;
			}
			
		}
	
	}

}