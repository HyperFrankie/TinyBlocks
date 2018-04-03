package com.extraBlocks.item.tool.SaveModelBase;

import com.extraBlocks.pedestal.GuiPedestal;
import com.extraBlocks.pedestal.GuiTextFieldCustom;

import net.minecraft.client.gui.FontRenderer;

public class CustomTextBox extends GuiTextFieldCustom {

	public CustomTextBox(int componentId, FontRenderer fontrendererObj, int x, int y, int Width, int Height, GuiPedestal parent) {
		super(componentId, fontrendererObj, x, y, Width, Height, parent);
	}

	public CustomTextBox(int componentId, FontRenderer fontrendererObj, int x, int y, int Width, int Height) {
		super(componentId, fontrendererObj, x, y, Width, Height);
	}
	
	@Override
	public boolean isCharacterAllowed(char character) {
		return 
				Character.isAlphabetic(character)	//Alpabethic characters
				|| character == '_'					//Underscore
				|| character == '-'					//Hyphen
				;
	}

}
