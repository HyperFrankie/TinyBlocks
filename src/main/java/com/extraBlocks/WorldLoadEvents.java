package com.extraBlocks;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldLoadEvents extends WorldEvent.Load {

	public WorldLoadEvents(World world) {
		super(world);
	}

}
