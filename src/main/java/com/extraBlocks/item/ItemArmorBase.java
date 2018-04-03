package com.extraBlocks.item;

import com.extraBlocks.Main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.world.World;

public class ItemArmorBase extends net.minecraft.item.ItemArmor {

	private String name;
	private int x = 0;
	private double speed = -1, oldSpeed;
	private float walkSpeed = 0.15f, rpn, rpo = 0;

	public ItemArmorBase(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
		super(material, 0, slot);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
		setCreativeTab(Main.shadowTab);
	}

	public void registerItemModel() {
		Main.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		// TODO Auto-generated method stub
		super.onArmorTick(world, player, itemStack);

		if(player.inventory.armorItemInSlot(0) != null){
			ItemStack boots = player.inventory.armorItemInSlot(0);
			if(boots.getItem() == ModItems.armor_boots_shadow) {
				oldSpeed = speed;
				speed = Math.sqrt(Math.pow(player.motionX, 2) + Math.pow(player.motionZ, 2));
				
				rpo = rpn;
				rpn = player.rotationYaw % 360;
				
				float r = dirChange();
				
				if(player.isSprinting()) {
					rpo = rpn;
					rpn = player.rotationYaw % 360;
					if(r == 0){
						x += 1;
					}
				} else if(walkSpeed != 0.15f) {
					if(player.collidedVertically) {
						walkSpeed = 0.15f;
					}
					
//					player.sendMessage(new TextComponentString("Going to decrease speed, you are not running anymore"));
					x -= (x >= 2) ? 2 : ((x >= 1) ? 1 : 0);
				}
				if(player.isSneaking()) {
					walkSpeed = 0.15f;
					x = 0;
				}
				walkSpeed = (float) (0.3 - Math.pow(0.999, 3 * x) * 0.15);
//				player.sendMessage(new TextComponentString(x + " , " + walkSpeed + " , " + speed + " , " + rpn + " , " + rpo));
				player.capabilities.setPlayerWalkSpeed(walkSpeed);
			}
		}
	}

	public float dirChange() {
		
		float d;
		if(rpn < 60 && rpo > 300) {
			d = (float) Math.sqrt(Math.pow((rpo - 360) - rpn, 2));
		} else if(rpn > 300 && rpo < 60) {
			d = (float) Math.sqrt(Math.pow((rpn - 360) - rpo, 2));
		} else {
			d = (float) (Math.sqrt(Math.pow(rpn - rpo, 2)) % 360);
		}
		
		if(Math.round(rpn * 3) / 3 != Math.round(rpo * 3) / 3) {
			x -= (float) (d / 360 * x);
		}
		return d;
	}
	
	@Override
	public ItemArmorBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

}