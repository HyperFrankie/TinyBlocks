package com.extraBlocks.pedestal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiPedestal extends GuiContainer {

	private InventoryPlayer playerInv;
	public static ResourceLocation BG_TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/pedestal_without_slots.png"), BG_TEXTURE_SLOTS = new ResourceLocation(Main.MODID, "textures/gui/pedestal_slots.png");
	public static ContainerPedestal container;
	public static TileEntityPedestal te;
	public static int offsetX = ContainerPedestal.offsetX, offsetY = ContainerPedestal.offsetY, extraWidth = ContainerPedestal.extraWidth;

	private GuiTextFieldCustom motionMultiplierX, motionTimeMultiplierX, motionTimeOffsetX, rotTimeMultiplierX, rotTimeOffsetX, posOffsetX;
	private GuiTextFieldCustom motionMultiplierY, motionTimeMultiplierY, motionTimeOffsetY, rotTimeMultiplierY, rotTimeOffsetY, posOffsetY;
	private GuiTextFieldCustom motionMultiplierZ, motionTimeMultiplierZ, motionTimeOffsetZ, rotTimeMultiplierZ, rotTimeOffsetZ, posOffsetZ;
	private int tb, sel;
	public ArrayList<GuiTextFieldCustom> tbs = new ArrayList<GuiTextFieldCustom>();

	/**
	 * This is a constructor that constructs a client side GuiPedestal, an extension of GuiContainer, 
	 * using a server side Container and a player's inventory. The Container used is a ContainerPedestal,
	 * which is an extension of Container.
	 * @param container
	 * @param playerInv
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3){
		super.drawScreen(mouseX, mouseY, par3);
		this.motionMultiplierX.drawTextBox(mouseX, mouseY);
		this.motionTimeMultiplierX.drawTextBox(mouseX, mouseY);
		this.motionTimeOffsetX.drawTextBox(mouseX, mouseY);
		this.rotTimeMultiplierX.drawTextBox(mouseX, mouseY);
		this.rotTimeOffsetX.drawTextBox(mouseX, mouseY);
		this.posOffsetX.drawTextBox(mouseX, mouseY);
		this.motionMultiplierY.drawTextBox(mouseX, mouseY);
		this.motionTimeMultiplierY.drawTextBox(mouseX, mouseY);
		this.motionTimeOffsetY.drawTextBox(mouseX, mouseY);
		this.rotTimeMultiplierY.drawTextBox(mouseX, mouseY);
		this.rotTimeOffsetY.drawTextBox(mouseX, mouseY);
		this.posOffsetY.drawTextBox(mouseX, mouseY);
		this.motionMultiplierZ.drawTextBox(mouseX, mouseY);
		this.motionTimeMultiplierZ.drawTextBox(mouseX, mouseY);
		this.motionTimeOffsetZ.drawTextBox(mouseX, mouseY);
		this.rotTimeMultiplierZ.drawTextBox(mouseX, mouseY);
		this.rotTimeOffsetZ.drawTextBox(mouseX, mouseY);
		this.posOffsetZ.drawTextBox(mouseX, mouseY);
	}

	public GuiPedestal(Container container, InventoryPlayer playerInv) {
		super(container);
		this.container = (ContainerPedestal) container;
		this.playerInv = playerInv;
		this.te = (TileEntityPedestal) Minecraft.getMinecraft().getIntegratedServer().getWorld(playerInv.player.dimension).getTileEntity(this.container.pos);
	}

	@Override
	public void initGui() {
		//You have to add this line for the Gui to function properly!
		super.initGui();
		createButtons();
		createTextFields();
		tb = -1;
		sel = -1;
	}

	private void createButtons() {
		GuiButtonExt b1 = new GuiButtonCustom(0, getGuiLeft() + 184, getGuiTop() - offsetY + 5, 20, 20, "");
		this.buttonList.add(b1);
	}

	public GuiTextFieldCustom createTextField(int i) {
		return new GuiTextFieldCustom(i, this.fontRenderer, getGuiLeft() + 185 + ((0 <= i && i <= 5) ? 0 : ((6 <= i && i <= 11) ? 1 : 2)) * 44, getGuiTop() - offsetY + 30 + (i % 6) * 24, 38, 18);
	}
	
	private void createTextFields() {
		
		this.motionMultiplierX = 		createTextField(0);
		tbs.add(this.motionMultiplierX);
		this.motionTimeMultiplierX = 	createTextField(1);
		tbs.add(this.motionTimeMultiplierX);
		this.motionTimeOffsetX = 		createTextField(2);
		tbs.add(this.motionTimeOffsetX);
		this.rotTimeMultiplierX = 		createTextField(3);
		tbs.add(this.rotTimeMultiplierX);
		this.rotTimeOffsetX = 			createTextField(4);
		tbs.add(this.rotTimeOffsetX);
		this.posOffsetX = 				createTextField(5);
		tbs.add(this.posOffsetX);
		this.motionMultiplierY = 		createTextField(6);
		tbs.add(this.motionMultiplierY);
		this.motionTimeMultiplierY = 	createTextField(7);
		tbs.add(this.motionTimeMultiplierY);
		this.motionTimeOffsetY = 		createTextField(8);
		tbs.add(this.motionTimeOffsetY);
		this.rotTimeMultiplierY = 		createTextField(9);
		tbs.add(this.rotTimeMultiplierY);
		this.rotTimeOffsetY = 			createTextField(10);
		tbs.add(this.rotTimeOffsetY);
		this.posOffsetY = 				createTextField(11);
		tbs.add(this.posOffsetY);
		this.motionMultiplierZ = 		createTextField(12);
		tbs.add(this.motionMultiplierZ);
		this.motionTimeMultiplierZ = 	createTextField(13);
		tbs.add(this.motionTimeMultiplierZ);
		this.motionTimeOffsetZ = 		createTextField(14);
		tbs.add(this.motionTimeOffsetZ);
		this.rotTimeMultiplierZ = 		createTextField(15);
		tbs.add(this.rotTimeMultiplierZ);
		this.rotTimeOffsetZ = 			createTextField(16);
		tbs.add(this.rotTimeOffsetZ);
		this.posOffsetZ = 				createTextField(17);
		tbs.add(this.posOffsetZ);

		
		
//		this.motionMultiplierX = 		new GuiTextFieldCustom(0, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 24, 38, 18);
//		tbs.add(this.motionMultiplierX);
//
//		this.motionTimeMultiplierX = 	new GuiTextFieldCustom(1, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 48, 38, 18);
//		tbs.add(this.motionTimeMultiplierX);
//
//		this.motionTimeOffsetX = 		new GuiTextFieldCustom(2, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 72, 38, 18);
//		tbs.add(this.motionTimeOffsetX);
//
//		this.rotTimeMultiplierX = 		new GuiTextFieldCustom(3, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 96, 38, 18);
//		tbs.add(this.rotTimeMultiplierX);
//
//		this.rotTimeOffsetX = 			new GuiTextFieldCustom(4, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 120, 38, 18);
//		tbs.add(this.rotTimeOffsetX);
//
//		this.posOffsetX = 				new GuiTextFieldCustom(5, this.fontRenderer, getGuiLeft() + 185, getGuiTop() - offsetY + 6 + 144, 38, 18);
//		tbs.add(this.posOffsetX);
//
//		this.motionMultiplierY = 		new GuiTextFieldCustom(6, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 24, 38, 18);
//		tbs.add(this.motionMultiplierY);
//
//		this.motionTimeMultiplierY = 	new GuiTextFieldCustom(7, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 48, 38, 18);
//		tbs.add(this.motionTimeMultiplierY);
//
//		this.motionTimeOffsetY = 		new GuiTextFieldCustom(8, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 72, 38, 18);
//		tbs.add(this.motionTimeOffsetY);
//
//		this.rotTimeMultiplierY = 		new GuiTextFieldCustom(9, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 96, 38, 18);
//		tbs.add(this.rotTimeMultiplierY);
//
//		this.rotTimeOffsetY = 			new GuiTextFieldCustom(10, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 120, 38, 18);
//		tbs.add(this.rotTimeOffsetY);
//
//		this.posOffsetY = 				new GuiTextFieldCustom(11, this.fontRenderer, getGuiLeft() + 229, getGuiTop() - offsetY + 6 + 144, 38, 18);
//		tbs.add(this.posOffsetY);
//
//		this.motionMultiplierZ = 		new GuiTextFieldCustom(12, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 24, 38, 18);
//		tbs.add(this.motionMultiplierZ);
//
//		this.motionTimeMultiplierZ = 	new GuiTextFieldCustom(13, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 48, 38, 18);
//		tbs.add(this.motionTimeMultiplierZ);
//
//		this.motionTimeOffsetZ = 		new GuiTextFieldCustom(14, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 72, 38, 18);
//		tbs.add(this.motionTimeOffsetZ);
//
//		this.rotTimeMultiplierZ = 		new GuiTextFieldCustom(15, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 96, 38, 18);
//		tbs.add(this.rotTimeMultiplierZ);
//
//		this.rotTimeOffsetZ = 			new GuiTextFieldCustom(16, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 120, 38, 18);
//		tbs.add(this.rotTimeOffsetZ);
//
//		this.posOffsetZ = 				new GuiTextFieldCustom(17, this.fontRenderer, getGuiLeft() + 273, getGuiTop() - offsetY + 6 + 144, 38, 18);
//		tbs.add(this.posOffsetZ);
		
		
		
//		double textFieldScale = 1 / 0.5;
//		
//		this.motionMultiplierX = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 24) - 1), 38, 18);
//		tbs.add(this.motionMultiplierX);
//
//		this.motionTimeMultiplierX = 	new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 48) - 1), 38, 18);
//		tbs.add(this.motionTimeMultiplierX);
//
//		this.motionTimeOffsetX = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 72) - 1), 38, 18);
//		tbs.add(this.motionTimeOffsetX);
//
//		this.rotTimeMultiplierX = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 96) - 1), 38, 18);
//		tbs.add(this.rotTimeMultiplierX);
//
//		this.rotTimeOffsetX = 			new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 120) - 1), 38, 18);
//		tbs.add(this.rotTimeOffsetX);
//
//		this.posOffsetX = 				new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 185) - 1), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 144) - 1), 38, 18);
//		tbs.add(this.posOffsetX);
//
//		this.motionMultiplierY = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 24)), 38, 18);
//		tbs.add(this.motionMultiplierY);
//
//		this.motionTimeMultiplierY = 	new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 48)), 38, 18);
//		tbs.add(this.motionTimeMultiplierY);
//
//		this.motionTimeOffsetY = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 72)), 38, 18);
//		tbs.add(this.motionTimeOffsetY);
//
//		this.rotTimeMultiplierY = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 96)), 38, 18);
//		tbs.add(this.rotTimeMultiplierY);
//
//		this.rotTimeOffsetY = 			new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 120)), 38, 18);
//		tbs.add(this.rotTimeOffsetY);
//
//		this.posOffsetY = 				new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 229)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 144)), 38, 18);
//		tbs.add(this.posOffsetY);
//
//		this.motionMultiplierZ = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 24)), 38, 18);
//		tbs.add(this.motionMultiplierZ);
//
//		this.motionTimeMultiplierZ = 	new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 48)), 38, 18);
//		tbs.add(this.motionTimeMultiplierZ);
//
//		this.motionTimeOffsetZ = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 72)), 38, 18);
//		tbs.add(this.motionTimeOffsetZ);
//
//		this.rotTimeMultiplierZ = 		new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 96)), 38, 18);
//		tbs.add(this.rotTimeMultiplierZ);
//
//		this.rotTimeOffsetZ = 			new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 120)), 38, 18);
//		tbs.add(this.rotTimeOffsetZ);
//
//		this.posOffsetZ = 				new GuiTextFieldCustom(0, this.fontRenderer, (int) (textFieldScale * (getGuiLeft() + 273)), (int) (textFieldScale * (getGuiTop() - offsetY + 6 + 144)), 38, 18);
//		tbs.add(this.posOffsetZ);
		
		for(int i = 0; i < tbs.size(); i++) {
			this.tbs.get(i).setTextColor(-1);
			this.tbs.get(i).setDisabledTextColour(-1);
			this.tbs.get(i).setDisabledTextColour(-1);
			this.tbs.get(i).setMaxStringLength(35);
		}

		retrieveValues(-1);
	}

	public void retrieveValues(int bt) {
		switch(bt) {
		case -1 :
			this.motionMultiplierX.setText(te.getMotionMultX() + "");
			this.motionTimeMultiplierX.setText(te.getMotionSpeedX() + "");
			this.motionTimeOffsetX.setText(te.getMotionTimeOffX() + "");
			this.rotTimeMultiplierX.setText(te.getRotationSpeedX() + "");
			this.rotTimeOffsetX.setText(te.getRotationTimeOffX() + "");
			this.posOffsetX.setText(te.getPositionOffsetX() + "");

			this.motionMultiplierY.setText(te.getMotionMultY() + "");
			this.motionTimeMultiplierY.setText(te.getMotionSpeedY() + "");
			this.motionTimeOffsetY.setText(te.getMotionTimeOffY() + "");
			this.rotTimeMultiplierY.setText(te.getRotationSpeedY() + "");
			this.rotTimeOffsetY.setText(te.getRotationTimeOffY() + "");
			this.posOffsetY.setText(te.getPositionOffsetY() + "");

			this.motionMultiplierZ.setText(te.getMotionMultZ() + "");
			this.motionTimeMultiplierZ.setText(te.getMotionSpeedZ() + "");
			this.motionTimeOffsetZ.setText(te.getMotionTimeOffZ() + "");
			this.rotTimeMultiplierZ.setText(te.getRotationSpeedZ() + "");
			this.rotTimeOffsetZ.setText(te.getRotationTimeOffZ() + "");
			this.posOffsetZ.setText(te.getPositionOffsetZ() + "");
			
			for(int i = 0; i < tbs.size(); i++) {
				this.tbs.get(i).setFocused(false);
			}
			break;
		case 0 :
			this.motionMultiplierX.setText(te.getMotionMultX() + "");
			this.motionMultiplierX.setFocused(false);
			break;
		case 1 :
			this.motionTimeMultiplierX.setText(te.getMotionSpeedX() + "");
			this.motionTimeMultiplierX.setFocused(false);
			break;
		case 2 :
			this.motionTimeOffsetX.setText(te.getMotionTimeOffX() + "");
			this.motionTimeOffsetX.setFocused(false);
			break;
		case 3 :
			this.rotTimeMultiplierX.setText(te.getRotationSpeedX() + "");
			this.rotTimeMultiplierX.setFocused(false);
			break;
		case 4 :
			this.rotTimeOffsetX.setText(te.getRotationTimeOffX() + "");
			this.rotTimeOffsetX.setFocused(false);
			break;
		case 5 :
			this.posOffsetX.setText(te.getPositionOffsetX() + "");
			this.posOffsetX.setFocused(false);
			break;
		case 6 :
			this.motionMultiplierY.setText(te.getMotionMultY() + "");
			this.motionMultiplierY.setFocused(false);
			break;
		case 7 :
			this.motionTimeMultiplierY.setText(te.getMotionSpeedY() + "");
			this.motionTimeMultiplierY.setFocused(false);
			break;
		case 8 :
			this.motionTimeOffsetY.setText(te.getMotionTimeOffY() + "");
			this.motionTimeOffsetY.setFocused(false);
			break;
		case 9 :
			this.rotTimeMultiplierY.setText(te.getRotationSpeedY() + "");
			this.rotTimeMultiplierY.setFocused(false);
			break;
		case 10 :
			this.rotTimeOffsetY.setText(te.getRotationTimeOffY() + "");
			this.rotTimeOffsetY.setFocused(false);
			break;
		case 11 :
			this.posOffsetY.setText(te.getPositionOffsetY() + "");
			this.posOffsetY.setFocused(false);
			break;
		case 12 :
			this.motionMultiplierZ.setText(te.getMotionMultZ() + "");
			this.motionMultiplierZ.setFocused(false);
			break;
		case 13 :
			this.motionTimeMultiplierZ.setText(te.getMotionSpeedZ() + "");
			this.motionTimeMultiplierZ.setFocused(false);
			break;
		case 14 :
			this.motionTimeOffsetZ.setText(te.getMotionTimeOffZ() + "");
			this.motionTimeOffsetZ.setFocused(false);
			break;
		case 15 :
			this.rotTimeMultiplierZ.setText(te.getRotationSpeedZ() + "");
			this.rotTimeMultiplierZ.setFocused(false);
			break;
		case 16 :
			this.rotTimeOffsetZ.setText(te.getRotationTimeOffZ() + "");
			this.rotTimeOffsetZ.setFocused(false);
			break;
		case 17 :
			this.posOffsetZ.setText(te.getPositionOffsetZ() + "");
			this.posOffsetZ.setFocused(false);
			break;
		}
	}

	@Override
	protected void actionPerformed(GuiButton b) { if(b.id == 0) { System.out.println("My Button is Clicked!"); } }

	@Override
	public int getXSize() { return 210; }

	@Override
	public int getYSize() { return 174; }

	@Override
	public int getGuiLeft() { return super.getGuiLeft() - 16; }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		//		this.setGuiSize(176, 174);
		int x = (width - this.getXSize()) / 2;
		int y = (height - this.getYSize()) / 2;
		
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		drawTexturedModalRect(x, y, 0, 0, this.getXSize(), this.getYSize());

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
		mc.getTextureManager().bindTexture(BG_TEXTURE_SLOTS);
		drawTexturedModalRect(x, y , 0, 0, this.getXSize(), this.getYSize());
		GL11.glPopMatrix();

		drawEntityOnScreen(x + 52, y + 72, 30, (float) (x + 52) - mouseX, (float) (y + 72) - mouseY, this.mc.player);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int x = (width - this.getXSize()) / 2;
		int y = (height - this.getYSize()) / 2;
		String name = I18n.format(ModBlocks.pedestal.getUnlocalizedName() + ".name");
		fontRenderer.drawString(name, offsetX + ((108 + (this.getXSize() - 108 - extraWidth) / 2) - fontRenderer.getStringWidth(name) / 2), offsetY + 2, 0x404040);
		fontRenderer.drawString(playerInv.player.getDisplayName().getUnformattedText(), offsetX + ((this.getXSize() - extraWidth) / 2 - fontRenderer.getStringWidth(playerInv.player.getDisplayName().getUnformattedText()) / 2), offsetY + this.getYSize() - 101, 0x404040);
	}

	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)posX, (float)posY, 50.0F);
		GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 25.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 90.0F;
		ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 50.0F;
		ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 50.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		for(int i = 0; i < tbs.size(); i++) {
			this.tbs.get(i).mouseClicked(mouseX, mouseY, mouseButton);
		}

		int oldtb = tb;
		tb = -1;
		sel = -1;
		
		for(int i = 0; i < tbs.size(); i++) {
			if(tbs.get(i).isFocused()) {	//Finds the focused tb and sets 'tb' to it
				tb = i;
				sel = i;
				tbs.get(i).setFocused(true);
				break;
			}
		}
		
		playerInv.player.sendMessage(new TextComponentString("tb: " + tb + ", " + sel));
		
		if(tb != oldtb && oldtb != -1) { saveTB(oldtb); }	//Saves contents of the previously focused tb if existent and if the newly focused tb is another
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private void saveTB(int i) {
		String regex = "(.)*(\\d)(.)*";      
		Pattern pattern = Pattern.compile(regex);
		
		switch(i) {
		case 0 :
			if(motionMultiplierX.getText().isEmpty() || !pattern.matcher(motionMultiplierX.getText()).matches()) {
				te.setMotionMultX(0.0);
				motionMultiplierX.setText("0.0");
			} else {
				te.setMotionMultX(Double.parseDouble(motionMultiplierX.getText()));
				motionMultiplierX.setFocused(false);
			}

			break;
		case 1 :
			if(motionTimeMultiplierX.getText().isEmpty() || !pattern.matcher(motionTimeMultiplierX.getText()).matches()) {
				te.setMotionSpeedX(0.0);
				motionTimeMultiplierX.setText("0.0");
			} else {
				te.setMotionSpeedX(Double.parseDouble(motionTimeMultiplierX.getText()));
				motionTimeMultiplierX.setFocused(false);
			}
			break;
		case 2 :
			if(motionTimeOffsetX.getText().isEmpty() || !pattern.matcher(motionTimeOffsetX.getText()).matches()) {
				te.setMotionTimeOffX(0.0);
				motionTimeOffsetX.setText("0.0");
			} else {
				te.setMotionTimeOffX(Double.parseDouble(motionTimeOffsetX.getText()));
				motionTimeOffsetX.setFocused(false);
			}
			break;
		case 3 :
			if(rotTimeMultiplierX.getText().isEmpty() || !pattern.matcher(rotTimeMultiplierX.getText()).matches()) {
				te.setRotationSpeedX(0.0);
				rotTimeMultiplierX.setText("0.0");
			} else {
				te.setRotationSpeedX(Double.parseDouble(rotTimeMultiplierX.getText()));
				rotTimeMultiplierX.setFocused(false);
			}
			break;
		case 4 :
			if(rotTimeOffsetX.getText().isEmpty() || !pattern.matcher(rotTimeOffsetX.getText()).matches()) {
				te.setRotationTimeOffX(0.0);
				rotTimeOffsetX.setText("0.0");
			} else {
				te.setRotationTimeOffX(Double.parseDouble(rotTimeOffsetX.getText()));
				rotTimeOffsetX.setFocused(false);
			}
			break;
		case 5 :
			if(posOffsetX.getText().isEmpty() || !pattern.matcher(posOffsetX.getText()).matches()) {
				te.setPositionOffsetX(0.0);
				posOffsetX.setText("0.0");
			} else {
				te.setPositionOffsetX(Double.parseDouble(posOffsetX.getText()));
				posOffsetX.setFocused(false);
			}
			break;
		case 6 :
			if(motionMultiplierY.getText().isEmpty() || !pattern.matcher(motionMultiplierY.getText()).matches()) {
				te.setMotionMultY(0.0);
				motionMultiplierY.setText("0.0");
			} else {
				te.setMotionMultY(Double.parseDouble(motionMultiplierY.getText()));
				motionMultiplierY.setFocused(false);
			}
			break;
		case 7 :
			if(motionTimeMultiplierY.getText().isEmpty() || !pattern.matcher(motionTimeMultiplierY.getText()).matches()) {
				te.setMotionSpeedY(0.0);
				motionTimeMultiplierY.setText("0.0");
			} else {
				te.setMotionSpeedY(Double.parseDouble(motionTimeMultiplierY.getText()));
				motionTimeMultiplierY.setFocused(false);
			}
			break;
		case 8 :
			if(motionTimeOffsetY.getText().isEmpty() || !pattern.matcher(motionTimeOffsetY.getText()).matches()) {
				te.setMotionTimeOffY(0.0);
				motionTimeOffsetY.setText("0.0");
			} else {
				te.setMotionTimeOffY(Double.parseDouble(motionTimeOffsetY.getText()));
				motionTimeOffsetY.setFocused(false);
			}
			break;
		case 9 :
			if(rotTimeMultiplierY.getText().isEmpty() || !pattern.matcher(rotTimeMultiplierY.getText()).matches()) {
				te.setRotationSpeedY(0.0);
				rotTimeMultiplierY.setText("0.0");
			} else {
				te.setRotationSpeedY(Double.parseDouble(rotTimeMultiplierY.getText()));
				rotTimeMultiplierY.setFocused(false);
			}
			break;
		case 10 :
			if(rotTimeOffsetY.getText().isEmpty() || !pattern.matcher(rotTimeOffsetY.getText()).matches()) {
				te.setRotationTimeOffY(0.0);
				rotTimeOffsetY.setText("0.0");
			} else {
				te.setRotationTimeOffY(Double.parseDouble(rotTimeOffsetY.getText()));
				rotTimeOffsetY.setFocused(false);
			}
			break;
		case 11 :
			if(posOffsetY.getText().isEmpty() || !pattern.matcher(posOffsetY.getText()).matches()) {
				te.setPositionOffsetY(0.0);
				posOffsetY.setText("0.0");
			} else {
				te.setPositionOffsetY(Double.parseDouble(posOffsetY.getText()));
				posOffsetY.setFocused(false);
			}
			break;
		case 12 :
			if(motionMultiplierZ.getText().isEmpty() || !pattern.matcher(motionMultiplierZ.getText()).matches()) {
				te.setMotionMultZ(0.0);
				motionMultiplierZ.setText("0.0");
			} else {
				te.setMotionMultZ(Double.parseDouble(motionMultiplierZ.getText()));
				motionMultiplierZ.setFocused(false);
			}
			break;
		case 13 :
			if(motionTimeMultiplierZ.getText().isEmpty() || !pattern.matcher(motionTimeMultiplierZ.getText()).matches()) {
				te.setMotionSpeedZ(0.0);
				motionTimeMultiplierZ.setText("0.0");
			} else {
				te.setMotionSpeedZ(Double.parseDouble(motionTimeMultiplierZ.getText()));
				motionTimeMultiplierZ.setFocused(false);
			}
			break;
		case 14 :
			if(motionTimeOffsetZ.getText().isEmpty() || !pattern.matcher(motionTimeOffsetZ.getText()).matches()) {
				te.setMotionTimeOffZ(0.0);
				motionTimeOffsetZ.setText("0.0");
			} else {
				te.setMotionTimeOffZ(Double.parseDouble(motionTimeOffsetZ.getText()));
				motionTimeOffsetZ.setFocused(false);
			}
			break;
		case 15 :
			if(rotTimeMultiplierZ.getText().isEmpty() || !pattern.matcher(rotTimeMultiplierZ.getText()).matches()) {
				te.setRotationSpeedZ(0.0);
				rotTimeMultiplierZ.setText("0.0");
			} else {
				te.setRotationSpeedZ(Double.parseDouble(rotTimeMultiplierZ.getText()));
				rotTimeMultiplierZ.setFocused(false);
			}
			break;
		case 16 :
			if(rotTimeOffsetZ.getText().isEmpty() || !pattern.matcher(rotTimeOffsetZ.getText()).matches()) {
				te.setRotationTimeOffZ(0.0);
				rotTimeOffsetZ.setText("0.0");
			} else {
				te.setRotationTimeOffZ(Double.parseDouble(rotTimeOffsetZ.getText()));
				rotTimeOffsetZ.setFocused(false);
			}
			break;
		case 17 :
			if(posOffsetZ.getText().isEmpty() || !pattern.matcher(posOffsetZ.getText()).matches()) {
				te.setRotationTimeOffZ(0.0);
				posOffsetZ.setText("0.0");
			} else {
				te.setPositionOffsetZ(Double.parseDouble(posOffsetZ.getText()));
				posOffsetZ.setFocused(false);
			}
			break;
		}
		retrieveValues(i);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		boolean alreadyExistingDot = false;
		boolean alreadyExistingNegativityIndicator = false;
		boolean mayPlaceNegativityIndicator = true;
		if(keyCode == 52) {
			for(int i = 0; i < tbs.size(); i++) {	//Loops through all tbs to check if it contains a period
				if(tbs.get(i).isFocused()) {
					if(tbs.get(i).getText().indexOf(".") != -1) alreadyExistingDot = true;
				}
			}
		} else if(keyCode == 12) {
			for(int i = 0; i < tbs.size(); i++) {	//Loops through all tbs to check if it contains a minus and to check if a minus may be placed at that index
				if(tbs.get(i).isFocused()) {
					if(tbs.get(i).getText().indexOf("-") != -1) alreadyExistingNegativityIndicator = true;
					if(tbs.get(i).getCursorPosition() != 0) mayPlaceNegativityIndicator = false;
				}
			}
		}

		playerInv.player.sendMessage(new TextComponentString("key code is: " + keyCode + "   tb is: " + tb));
		if(
				(tb != -1 || sel != -1)
				&& mayPlaceNegativityIndicator			//This prevents the user from putting a minus anywhere other then at the start of the string
				&& !alreadyExistingNegativityIndicator	//This prevents the user from putting a minus in a tb that already contains one
				&& !alreadyExistingDot					//This prevents the user from putting a period in a tb that already contains one
				)
		{
			playerInv.player.sendMessage(new TextComponentString("Made it through the if statement"));
			if(sel != -1) tbs.get(sel).setFocused(true);
			if		(this.motionMultiplierX		.textboxKeyTyped(typedChar, keyCode)) tb = 0;
			else if (this.motionTimeMultiplierX	.textboxKeyTyped(typedChar, keyCode)) tb = 1;
			else if (this.motionTimeOffsetX		.textboxKeyTyped(typedChar, keyCode)) tb = 2;
			else if (this.rotTimeMultiplierX	.textboxKeyTyped(typedChar, keyCode)) tb = 3;
			else if (this.rotTimeOffsetX		.textboxKeyTyped(typedChar, keyCode)) tb = 4;
			else if (this.posOffsetX			.textboxKeyTyped(typedChar, keyCode)) tb = 5;
			else if (this.motionMultiplierY		.textboxKeyTyped(typedChar, keyCode)) tb = 6;
			else if (this.motionTimeMultiplierY	.textboxKeyTyped(typedChar, keyCode)) tb = 7;
			else if (this.motionTimeOffsetY		.textboxKeyTyped(typedChar, keyCode)) tb = 8;
			else if (this.rotTimeMultiplierY	.textboxKeyTyped(typedChar, keyCode)) tb = 9;
			else if (this.rotTimeOffsetY		.textboxKeyTyped(typedChar, keyCode)) tb = 10;
			else if (this.posOffsetY			.textboxKeyTyped(typedChar, keyCode)) tb = 11;
			else if (this.motionMultiplierZ		.textboxKeyTyped(typedChar, keyCode)) tb = 12;
			else if (this.motionTimeMultiplierZ	.textboxKeyTyped(typedChar, keyCode)) tb = 13;
			else if (this.motionTimeOffsetZ		.textboxKeyTyped(typedChar, keyCode)) tb = 14;
			else if (this.rotTimeMultiplierZ	.textboxKeyTyped(typedChar, keyCode)) tb = 15;
			else if (this.rotTimeOffsetZ		.textboxKeyTyped(typedChar, keyCode)) tb = 16;
			else if (this.posOffsetZ			.textboxKeyTyped(typedChar, keyCode)) tb = 17;
			else if(keyCode == 28 && tb != -1) {
				playerInv.player.sendMessage(new TextComponentString("Pressed enter"));
				saveTB(tb);
				tb = -1;
				te.markDirty();

			} else if(keyCode == 1 && tb != -1) {
				retrieveValues(tb);
				tb = -1;
			} else if(keyCode == 15) {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("going to process tab key"));
				if (GuiScreen.isShiftKeyDown()) {
					if (GuiScreen.isCtrlKeyDown()) {
						if(sel - 6 < 0) {
							tbs.get(sel).setFocused(false);
							tbs.get(sel + 12).setFocused(true);
							sel += 12;
						} else {
							tbs.get(sel).setFocused(false);
							tbs.get(sel - 6).setFocused(true);
							sel -= 6;
						}
					} else {
						if(sel - 1 < 0) {
							tbs.get(sel).setFocused(false);
							tbs.get(tbs.size() - 1).setFocused(true);
							sel = tbs.size() - 1;
						} else {
							tbs.get(sel).setFocused(false);
							tbs.get(sel - 1).setFocused(true);
							sel -= 1;
						}
					}
				} else if (GuiScreen.isCtrlKeyDown()) {
					if(sel + 6 >= tbs.size()) {
						tbs.get(sel).setFocused(false);
						tbs.get(sel - 12).setFocused(true);
						sel -= 12;
					} else {
						tbs.get(sel).setFocused(false);
						tbs.get(sel + 6).setFocused(true);
						sel += 6;
					}
				} else {
					this.setFocused(false);
					if(sel + 1 >= tbs.size()) {
						tbs.get(sel).setFocused(false);
						tbs.get(0).setFocused(true);
						sel = 0;
					} else {
						tbs.get(sel).setFocused(false);
						tbs.get(sel + 1).setFocused(true);
						sel += 1;
					}
				}
				tb = sel;
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
		if(tb != -1 && (tbs.get(tb).getText().indexOf("'") != -1 || tbs.get(tb).getText().indexOf("\"") != -1 || tbs.get(tb).getText().indexOf("`") != -1 || tbs.get(tb).getText().indexOf("~") != -1)) {
			playerInv.player.sendMessage(new TextComponentString("Going to replace all characters that shouldn't be in the string"));
			tbs.get(tb).getText().replace("'", "");
			tbs.get(tb).getText().replace("\"", "");
			tbs.get(tb).getText().replace("`", "");
			tbs.get(tb).getText().replace("~", "");
		}
		
	}
}