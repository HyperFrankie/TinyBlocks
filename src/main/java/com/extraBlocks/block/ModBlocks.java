package com.extraBlocks.block;

import java.util.Collection;

import com.extraBlocks.Main;
import com.extraBlocks.block.counter.BlockCounter;
import com.extraBlocks.block.miniBlock.MiniBlock;
import com.extraBlocks.pedestal.BlockPedestal;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	public static BlockOre ore_shadow = new BlockOre("shadow_ore");
	public static BlockCounter counter = new BlockCounter();
	public static BlockPedestal pedestal = new BlockPedestal();
	public static MiniBlock mini_block = new MiniBlock();

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				ore_shadow,
				counter,
				pedestal,
				mini_block
				);
		GameRegistry.registerTileEntity(counter.getTileEntityClass(), counter.getRegistryName().toString());
		GameRegistry.registerTileEntity(pedestal.getTileEntityClass(), pedestal.getRegistryName().toString());
		GameRegistry.registerTileEntity(mini_block.getTileEntityClass(), mini_block.getRegistryName().toString());
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				ore_shadow.createItemBlock(ore_shadow.name),
				counter.createItemBlock(counter.name),
				pedestal.createItemBlock(pedestal.name),
				mini_block.createItemBlock(mini_block.name)
				);
	}

	public static void registerModels() {
		ore_shadow.registerItemModel(Item.getItemFromBlock(ore_shadow));
		counter.registerItemModel(Item.getItemFromBlock(counter));
		pedestal.registerItemModel(Item.getItemFromBlock(pedestal));
		mini_block.registerItemModel(Item.getItemFromBlock(mini_block));
	}

	public static void registerBlockPaths(IForgeRegistry<Block> registry) {
		// TODO Auto-generated method stub
		Collection<Block> collection = registry.getValuesCollection();
		for(Block block : collection) {
			IBlockState b = block.getDefaultState();
			String path = block.getRegistryName().getResourceDomain() + ":blocks/" + block.getRegistryName().getResourcePath();
			for(Integer i : new int[] {0, 1, 2, 3, 4, 5}) Main.resourceLocationMap.add(getPath(path, i, b));
		}
	}
	
	public static String getPath(String basePath, int face, IBlockState blockState) {
		if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath) == Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
			if(face == 2 || face == 3 || face == 4 || face == 5) {
				if(getPathWithModifiers(basePath, "_side", blockState) != basePath) {
					return getPathWithModifiers(basePath, "_side", blockState);
				} else {
					switch(face) {
					case 2 :
						if(getPathWithModifiers(basePath, "_north", blockState) != basePath) {
							return getPathWithModifiers(basePath, "_north", blockState);
						}
						break;
					case 3 :
						if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_east") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
							return basePath + "_east";
						}
						break;
					case 4 :
						if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_south") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
							return basePath + "_south";
						}
						break;
					case 5 :
						if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_west") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
							return basePath + "_west";
						}
						break;
					}
				}
			} else if(face == 0) {
				if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_top") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
					return basePath + "_top";
				}
			} else if(face == 1) {
				if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_bottom") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
					return basePath + "_bottom";
				}
			}
			if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + "_normal") != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
				return basePath + "_normal";
			}
		}
		return basePath;
	}
	
	public static String getPathWithModifiers(String basePath, String modifier, IBlockState blockState) {
		ImmutableMap<IProperty<?>, Comparable<?>> m = blockState.getProperties();
		if(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(basePath + modifier) != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
			return basePath + modifier;
		} else if(m.containsKey("color")) {
			if(getPathWithColor(basePath, modifier, blockState) != basePath) {
				System.out.println(blockState + " contains color property");
				return basePath + modifier;
			}
		}
		return basePath;
	}
	
	public static String getPathWithColor(String basePath, String modifier, IBlockState blockState) {
//		blockState.getProperties().get("color");
//		for(int i = 0; i < 16; i++) {
//			
//		}
		return basePath;
	}

}