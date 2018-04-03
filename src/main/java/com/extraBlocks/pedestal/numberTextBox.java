package com.extraBlocks.pedestal;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class numberTextBox extends Gui {
	public float value;

	private final FontRenderer fontRenderer;
	private final int xPosition;
	private final int yPosition;

	private final int width;
	private final int height;


	private boolean canLoseFocus = true;

	private boolean isFocused = false;

	private boolean isEnabled = true;

	private boolean visible = true;
	private String text = "";

	public boolean restrict;
	public float min;
	public float max;


	public numberTextBox(FontRenderer fontRenderer1, int x, int y, int width, int height, float f) {
		this.fontRenderer = fontRenderer1;
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		setValue(f);
	}

	public numberTextBox(FontRenderer fontRenderer1, int x, int y, int width, int height, float f, float g, float h) {
		this(fontRenderer1, x, y, width, height, f);
		this.min = g;
		this.max = h;
		this.restrict = true;
	}


	public void setValue(float f) {
		this.text = Float.toString(f);
		this.value = f;
		if (restrict) {
			if (value > max) setValue(max);
			if (value < min) setValue(min);
		}
	}


	public float getValue() {
		if (this.text.length() > 0)
			try {
				this.value = Float.parseFloat(this.text);
			} catch (NumberFormatException ex) {
				this.setValue(1.0f);
			}
		else this.value = 0;
		return this.value;
	}

	public boolean isNumber(char c) {
		return ((c >= 48 && c <= 57) || c == 45);
	}

	public String filerNumbers(String par0Str) {
		StringBuilder stringbuilder = new StringBuilder();
		char[] achar = par0Str.toCharArray();
		int i = achar.length;

		for (int j = 0; j < i; ++j) {
			char c0 = achar[j];

			if (this.isNumber(c0)) {
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}


	public void writeText(String str) {
		this.text += filerNumbers(str);
		try {
			this.value = Float.parseFloat(text);
		} catch (NumberFormatException ex) {
			this.value = 0;
		}
		if (restrict) {
			if (value > max) setValue(max);
			if (value < min) setValue(min);
		}
	}


	public boolean textboxKeyTyped(char letter, int id) {
		if (!this.isFocused) {
			return false;
		} else {
			switch (id) {
			case 14:
				if (this.isEnabled) {
					this.deleteChar();
				}
				return true;

			default:
				if (this.isNumber(letter)) {
					if (this.isEnabled) {
						if ((int) letter == 45 && text.length() > 0) return false;
						this.writeText(Character.toString(letter));
					}

					return true;
				} else {
					return false;
				}
			}

		}
	}

	private void deleteChar() {
		if (this.text.length() > 0) {
			this.text = text.substring(0, this.text.length() - 1);
		} else this.text = "0";
	}

	public boolean isHovering(int x, int y){
		return x >= this.xPosition && x < this.xPosition + this.width && y >= this.yPosition && y < this.yPosition + this.height;
	}

	public boolean isDownHovering(int x, int y){
		return isHovering(x,y) && x > this.xPosition + this.width - 15 && y > this.yPosition + this.height / 2;
	}

	public boolean isUpHovering(int x, int y){
		return isHovering(x,y) && x > this.xPosition + this.width - 15 && y <= this.yPosition + this.height / 2;

	}

	public void mouseClicked(int x, int y, int button) {
		boolean flag = isHovering(x,y);
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(flag + ""));
		System.out.println(flag);
		if (this.canLoseFocus) {
			this.setFocused(flag);
		}

		if(isUpHovering(x, y)) setValue(getValue() + 0.01f);
		else if(isDownHovering(x,y)) setValue(getValue() - 0.01f);


	}


	public void drawTextBox(int x, int y) {
		if (this.getVisible()) {

			drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.isFocused ? 0xFFBBBBBB : 0xFF787878);
			drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width -1, this.yPosition + this.height - 1, -16777216);

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("extra_blocks:textures/gui/icon01.png"));

			if(isUpHovering(x,y)) GL11.glColor4f(1F, 1F, 1F, 1F); else GL11.glColor4f(0.8F, 0.8F, 0.8F, 1F);
			drawRect(this.xPosition + this.getWidth() - 15, this.yPosition, this.xPosition + this.width, 20, 0xFFBBBBBB);

			if(isDownHovering(x, y)) GL11.glColor4f(1F, 1F, 1F, 1F); else GL11.glColor4f(0.8F, 0.8F, 0.8F, 1F);
			drawRect(this.xPosition + this.getWidth() - 15, this.yPosition + 10, this.xPosition + this.width, 20, 0xFF787878);

			if (text.length() > 0) {

//				this.fontRenderer.drawString(text, this.xPosition + 4, this.yPosition + this.height / 2 - 4, this.isFocused ? 0xFFFFFFFF : 0xFF646464);
				this.drawCenteredString(this.fontRenderer, text, this.xPosition + 10, this.yPosition + this.height / 2 - 4, this.isFocused ? 0xFFFFFFFF : 0xFF646464);
			}

		}
	}

	public void setFocused(boolean f) {
		this.isFocused = f;
	}


	public boolean isFocused() {
		return this.isFocused;
	}

	public void setEnabled(boolean e) {
		this.isEnabled = e;
	}


	public int getWidth() {
		return this.width;
	}


	public void setCanLoseFocus(boolean is) {
		this.canLoseFocus = is;
	}


	public boolean getVisible() {
		return this.visible;
	}


	public void setVisible(boolean p_146189_1_) {
		this.visible = p_146189_1_;
	}
}