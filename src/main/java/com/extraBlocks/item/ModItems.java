package com.extraBlocks.item;

import com.extraBlocks.Main;
import com.extraBlocks.block.miniBlock.Bit;
import com.extraBlocks.item.tool.ItemAxeBase;
import com.extraBlocks.item.tool.ItemChiselBase;
import com.extraBlocks.item.tool.ItemHoeBase;
import com.extraBlocks.item.tool.ItemPickaxeBase;
import com.extraBlocks.item.tool.ItemShovelBase;
import com.extraBlocks.item.tool.ItemSwordBase;
import com.extraBlocks.item.tool.SaveModelBase.GuiSaveModel;
import com.extraBlocks.item.tool.SaveModelBase.SaveModelBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	public static ItemBase 			shadow_ingot = 				new ItemBase("shadow_ingot");
	public static ItemArmorBase 	armor_helmet_shadow = 		new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.HEAD,  "shadow_helmet");
	public static ItemArmorBase 	armor_chestplate_shadow = 	new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.CHEST, "shadow_chestplate");
	public static ItemArmorBase 	armor_leggings_shadow = 	new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.LEGS,  "shadow_leggings");
	public static ItemArmorBase 	armor_boots_shadow = 		new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.FEET,  "shadow_boots");
	public static ItemSwordBase 	shadow_sword = 				new ItemSwordBase(	Main.shadowToolMaterial,	"shadow_sword");
	public static ItemPickaxeBase 	shadow_pickaxe = 			new ItemPickaxeBase(Main.shadowToolMaterial,	"shadow_pickaxe");
	public static ItemAxeBase 		shadow_axe = 				new ItemAxeBase(	Main.shadowToolMaterial,	"shadow_axe");
	public static ItemShovelBase 	shadow_shovel = 			new ItemShovelBase(	Main.shadowToolMaterial,	"shadow_shovel");
	public static ItemHoeBase 		shadow_hoe = 				new ItemHoeBase(	Main.shadowToolMaterial,	"shadow_hoe");
	public static ItemChiselBase 	chisel_wood = 				new ItemChiselBase(	Main.chiselWood,			"wood_chisel");
	public static ItemChiselBase 	chisel_stone = 				new ItemChiselBase(	Main.chiselStone,			"stone_chisel");
	public static ItemChiselBase 	chisel_iron = 				new ItemChiselBase(	Main.chiselIron,			"iron_chisel");
	public static ItemChiselBase 	chisel_diamond = 			new ItemChiselBase(	Main.chiselDiamond,			"diamond_chisel");
	public static ItemChiselBase 	chisel_obsidian = 			new ItemChiselBase(	Main.chiselObsidian,		"obsidian_chisel");
	public static SaveModelBase		save_model = 				new SaveModelBase(	Main.shadowToolMaterial, 	"save_model");
	public static Bit				bit = 						new Bit("Bit");

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				shadow_ingot,
				armor_helmet_shadow,
				armor_chestplate_shadow,
				armor_leggings_shadow,
				armor_boots_shadow,
				shadow_sword,
				shadow_pickaxe,
				shadow_axe,
				shadow_shovel,
				shadow_hoe,
				chisel_wood,
				chisel_stone,
				chisel_iron,
				chisel_diamond,
				chisel_obsidian,
				save_model,
				bit
				);
	}

	public static void registerModels() {
		shadow_ingot.registerItemModel();
		armor_helmet_shadow.registerItemModel();
		armor_chestplate_shadow.registerItemModel();
		armor_leggings_shadow.registerItemModel();
		armor_boots_shadow.registerItemModel();
		shadow_sword.registerItemModel();
		shadow_pickaxe.registerItemModel();
		shadow_axe.registerItemModel();
		shadow_shovel.registerItemModel();
		shadow_hoe.registerItemModel();
		chisel_wood.registerItemModel();
		chisel_stone.registerItemModel();
		chisel_iron.registerItemModel();
		chisel_diamond.registerItemModel();
		chisel_obsidian.registerItemModel();
		save_model.registerItemModel();
		bit.registerItemModel();
	}

}
