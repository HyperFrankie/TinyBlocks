package com.extraBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.extraBlocks.Config.CONFIG_TYPES;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.block.miniBlock.PacketRequestUpdateMiniBlock;
import com.extraBlocks.block.miniBlock.PacketUpdateMiniBlock;
import com.extraBlocks.item.ModItems;
import com.extraBlocks.pedestal.PacketRequestUpdatePedestal;
import com.extraBlocks.pedestal.PacketUpdatePedestal;
import com.extraBlocks.proxy.CommonProxy;
import com.extraBlocks.world.ShadowWorldGeneration;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, dependencies = "required-after:FML")

public class Main {

	public static final String MODID = "extra_blocks";
	public static final String NAME = "Extra Blocks";
	public static final String VERSION = "1.0.0";

	@Instance(MODID)
	public static Main instance;

	@SidedProxy(serverSide = "com.extraBlocks.proxy.CommonProxy", clientSide = "com.extraBlocks.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static final ExtraBlocksTab shadowTab = new ExtraBlocksTab();

	public static final ItemArmor.ArmorMaterial shadowArmorMaterial = 	EnumHelper.addArmorMaterial("SHADOW", MODID + ":shadow", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static final Item.ToolMaterial shadowToolMaterial = 			EnumHelper.addToolMaterial("SHADOW",		2, 500, 6, 2, 14);
	public static final Item.ToolMaterial chiselWood = 		EnumHelper.addToolMaterial("chiselWood",	0,	4096,	0, 1, 0);
	public static final Item.ToolMaterial chiselStone = 	EnumHelper.addToolMaterial("chiselStone",	1,	16384,	0, 1, 0);
	public static final Item.ToolMaterial chiselIron = 		EnumHelper.addToolMaterial("chiselIron",	2,	65536,	0, 1, 0);
	public static final Item.ToolMaterial chiselDiamond = 	EnumHelper.addToolMaterial("chiselDiamond",	3,	262144,	0, 1, 0);
	public static final Item.ToolMaterial chiselObsidian = 	EnumHelper.addToolMaterial("chiselObsidian",3,	1048576,0, 1, 0);
	
	public static ArrayList<String> resourceLocationMap = new ArrayList<String>();
	
	public static SimpleNetworkWrapper network;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(this);
		
		GameRegistry.registerWorldGenerator(new ShadowWorldGeneration(), 3);
//		MinecraftForge.TERRAIN_GEN_BUS.register(new FirstWorldGenStructure());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		network.registerMessage(new PacketUpdateMiniBlock.Handler(), PacketUpdateMiniBlock.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateMiniBlock.Handler(), PacketRequestUpdateMiniBlock.class, 1, Side.SERVER);
		network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, 1, Side.SERVER);
		proxy.registerRenderers();

		logger = event.getModLog();
		logger.info(NAME + " is loading!");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModRecipes.init();
		MinecraftForge.EVENT_BUS.register(new customEvents());
//		ModelBakery.registerItemVariants(ModItems.armor_boots_shadow, new ResourceLocation(MODID ,"block/tinyBlock"));
		logger.debug("Old: {}", CONFIG_TYPES.bool);
        CONFIG_TYPES.bool = !CONFIG_TYPES.bool;
        logger.debug("New: {}", CONFIG_TYPES.bool);
        ConfigManager.sync(MODID, Type.INSTANCE);
        logger.debug("After sync: {}", CONFIG_TYPES.bool);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logger.info(NAME + " is almost done loading!");
	}
	
	@SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID))
        {
            ConfigManager.sync(MODID, Type.INSTANCE);
        }
    }
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModItems.register(event.getRegistry());
			ModBlocks.registerItemBlocks(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerItems(ModelRegistryEvent event) {
			ModItems.registerModels();
			ModBlocks.registerModels();
		}

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			ModBlocks.register(event.getRegistry());
//			ModBlocks.registerBlockPaths(event.getRegistry());
		}
	}
	
//	public static void syncConfig() { // Gets called from preInit
//	    try {
//	        // Load config
//	        config.load();
//
//	        // Read props from config
//	        Property isOnlyFullBlocks = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
//	                "enable_only_full_blocks", // Property name
//	                "true", // Default value
//	                "Whether only full blocks can be broken with the chisel"); // Comment
//
//	        configFullBlock = isOnlyFullBlocks.getBoolean(); // Get the boolean value, also set the property value to boolean
//	    } catch (Exception e) {
//	        // Failed reading/writing, just continue
//	    } finally {
//	        // Save props to config IF config changed
//	        if (config.hasChanged()) config.save();
//	    }
//	}
}