package com.extraBlocks.item.tool.SaveModelBase;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.lwjgl.opengl.GL11;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;
import com.extraBlocks.block.miniBlock.TileEntityMiniBlock;
import com.extraBlocks.pedestal.GuiButtonCustom;
import com.extraBlocks.pedestal.GuiTextFieldCustom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import scala.sys.process.ProcessBuilder.FileBuilder;

public class GuiSaveModel extends GuiContainer {

	public static ResourceLocation BG_TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/guiSaveModel.png");
	
	public ContainerSaveModel container;
	public TileEntityMiniBlock te;
	
	public ArrayList<GuiTextFieldCustom> tbs = new ArrayList<GuiTextFieldCustom>();
	public GuiTextFieldCustom name;
	public GuiCheckBox checkBox;
	
	public static int offsetY = ContainerSaveModel.offsetY, offsetX = ContainerSaveModel.offsetX, extraWidth = ContainerSaveModel.extraWidth;
	
	private int tb, sel;

	private boolean saveButtonSelected, override;
	
	public GuiSaveModel(ContainerSaveModel container, EntityPlayer player) {
		super(container);
		this.container = (ContainerSaveModel) container;
		this.te = (TileEntityMiniBlock) Minecraft.getMinecraft().getIntegratedServer().getWorld(player.dimension).getTileEntity(this.container.pos);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3){
		super.drawScreen(mouseX, mouseY, par3);
		name.drawTextBox(mouseX, mouseY);
		checkBox.drawButton(mc, mouseX, mouseY, 0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		int x = (width - this.getXSize()) / 2;
		int y = (height - this.getYSize()) / 2;

		BG_TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/guisavemodel_with_save_cancel_checkbox.png");
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		drawTexturedModalRect(x, y, 0, 0, this.getXSize(), this.getYSize());
		GL11.glPopMatrix();
		
//		GL11.glEnable(GL11.GL_BLEND);
//		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
//		mc.getTextureManager().bindTexture(BG_TEXTURE);
//		drawTexturedModalRect(x, y , 0, 0, this.getXSize(), this.getYSize());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int x = (width - this.getXSize()) / 2;
		int y = (height - this.getYSize()) / 2;
		String name = I18n.format(ModBlocks.pedestal.getUnlocalizedName() + ".name");
	}
	
	@Override
	public void initGui() {
		//You have to add this line for the Gui to function properly!
		super.initGui();
		createButtons();
		createTextFields();
		createCheckboxes();
		tbs.get(0).setFocused(true);
		tb = 0;
		tbs.get(tb).setCursorPosition("Model name".length());
		tbs.get(tb).setSelectionPos(0);
	}
	
	private void createTextFields() {
		this.name = new CustomTextBox(0, this.fontRenderer, getGuiLeft() - offsetX + 8, getGuiTop() - offsetY + 8, 160, 16);
//		name.setEnableBackgroundDrawing(true);
		tbs.add(this.name);
		name.setText("Model name");
	}
	
	private void createButtons() {
		GuiButtonExt b1 = new GuiButtonCustom(0, getGuiLeft() + 46, getGuiTop() - offsetY + 44, 40, 18, "Save");
		this.buttonList.add(b1);
	}
	
	private void createCheckboxes() {
		GuiCheckBox checkBox = new GuiCheckBox(0, getGuiLeft() + 7, getGuiTop() - offsetY + 29, "Override existing file", true);
		checkBox.enabled = true;
//		checkBox.packedFGColour = 16777120;
		this.checkBox = checkBox;
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(sel != -1) tbs.get(sel).setFocused(true);
		if(tb != -1 || sel != -1) {
			if(this.name.textboxKeyTyped(typedChar, keyCode)) tb = 0;
			else if(keyCode == 28) { //Enter
				if(tb == -1) { //If this, then close window and save
					saveModel();
					this.mc.player.closeScreen();
				} else {
					tbs.get(tb).setFocused(false);
					tb = -1;
				}
			} else if(keyCode == 1 && tb != -1) { //Escape
				name.setText("Model name");
				tb = -1;
			} else if(keyCode == 15 && tb != -1) { //Tab
				if(sel != -1) {
					tbs.get(sel).setFocused(false);
					sel = -1;
					saveButtonSelected = true;
				} else if(saveButtonSelected) {
					saveButtonSelected = false;
					sel = 0;
					tbs.get(sel).setFocused(true);
				}
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
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
				if(tbs.get(i).getText() == "Model name") {
					tbs.get(i).setCursorPosition("Model name".length());
					tbs.get(i).setSelectionPos(0);
				}
				break;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void saveModel() {
		List<String> lines = Arrays.asList("The first line", "The second line");
		
		Desktop d = Desktop.getDesktop();
		String path = Minecraft.getMinecraft().getIntegratedServer().getDataDirectory().toPath().toAbsolutePath().getParent().resolve("assets").resolve("Extra Blocks").toString().replaceAll(Pattern.quote("%20"), " ");
		File f1 = new File(path, tbs.get(0).getText() + ".txt");
		f1.getParentFile().mkdirs();
		try {
			boolean created = f1.createNewFile();
			if(created || (!created && override == true)) {
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			d.open(new File(path));
//			System.out.println(new File(Minecraft.getMinecraft().getIntegratedServer().getDataDirectory().toPath().toAbsolutePath().getParent().resolve("assets").resolve("Extra Blocks").toString().replaceAll(Pattern.quote("%20"), " ")));
//			d.open(new File("G:\\files\\Eclipse projects\\forge modding\\forge-1.12.2-14.23.2.2618-mdk\\bin\\assets\\extra_blocks\\models\\chiseledBlockModels"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		try {
//			File f = new File(path.toUri());
//			System.out.println(f);
//			if(!f.exists()) {
////				Files.createFile(path);
//				Files.createDirectory(path);
//				FileWriterWithEncoding fw = new FileWriterWithEncoding(path.toFile(), "UTF-8");
//				fw.write("testString");
//				fw.close();
//			}
////			if(f.canWrite()) {
////				Files.write(path, lines, Charset.forName("UTF-8"));
////			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Override
	protected void actionPerformed(GuiButton b) {
		if(b.id == 0) {
			saveModel();
			this.mc.player.closeScreen();
		}
	}
}