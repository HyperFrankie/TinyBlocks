package com.extraBlocks.pedestal;

import javax.annotation.Nullable;

import com.extraBlocks.block.ModBlocks;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * This is a class that creates the server side Container for an inventory GUI
 * @author lenar_000
 *
 */
public class ContainerPedestal extends Container {

	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

	public static int offsetX = -17, offsetY = 4, extraWidth = 34, x, y, z;
	public BlockPos pos;
	public TileEntityPedestal pedestal;
	
	/**
	 * This is a method that creates the server side ContainerPedestal, an extension of Container, for an inventory GUI
	 * @param playerInv
	 * @param pedestal
	 */
	public ContainerPedestal(InventoryPlayer playerInv, final TileEntityPedestal pedestal) {
		this.pos = pedestal.getPos();
		this.x = pedestal.getPos().getX();
		this.y = pedestal.getPos().getY();
		this.z = pedestal.getPos().getZ();
		this.pedestal = pedestal;
		IItemHandler inventory = pedestal.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		addSlotToContainer(new SlotItemHandler(inventory, 0, offsetX + 134, offsetY + 18) {	//Pedestal slot
			@Override
			public void onSlotChanged() {
				pedestal.markDirty();
			}
		});

		for (int k = 0; k < 9; k++) {									//Hotbar
			addSlotToContainer(new Slot(playerInv, k, offsetX + 8 + k * 18, offsetY + 142));
		}

		for (int i = 0; i < 3; i++) {									//Main inventory
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, offsetX + 8 + j * 18, offsetY + 84 + i * 18));
			}
		}

		for (int k = 0; k < 4; ++k) {									//Armor
			final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
			this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), offsetX + 8, offsetY + k * 18) {
				/**
				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
				 * in the case of armor slots)
				 */
				public int getSlotStackLimit() {
					return 1;
				}
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace
				 * fuel.
				 */
				public boolean isItemValid(ItemStack stack) {
					return stack.getItem().isValidArmor(stack, entityequipmentslot, playerInv.player);
				}
				/**
				 * Return whether this slot's stack can be taken from this slot.
				 */
				public boolean canTakeStack(EntityPlayer playerIn) {
					ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}
				@Nullable
				@SideOnly(Side.CLIENT)
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
				}
			});
		}

		addSlotToContainer(new Slot(playerInv, 40, offsetX + 80, offsetY + 54) {			//Offhand slot
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture()
			{
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return true; }

	/**Method that checks whether the given ItemStack can be inserted into one of the slots in range: 
	 * <pre>
	 * startIndex - endIndex.
	 * 
	 * startIndex = slot index + 1
	 * endIndex = slot index +2
	 * 
	 * Range 1 - 10	are Hotbar slots
	 * Range 10 - 37	are Main inventory slots
	 * Range 37 - 41	are Armor slots
	 * Range 41 - 42	is Offhand slot
	 * </pre>
	 */
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		//		player.sendMessage(new TextComponentString(slot.toString()));

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				int bi;
				boolean b = false;
//				for(int i = 0; i < 4; i++) {
//					final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[i];
//					b = itemstack.getItem().isValidArmor(itemstack, entityequipmentslot, player);
//					if(b) { bi = i; break; }
//				}
//				if(!this.mergeItemStack(itemstack1, 36, 40, true)) {
//					player.sendMessage(new TextComponentString("trying to fit in armor slots"));
//					return ItemStack.EMPTY;
				/*} else */if(!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
