package com.extraBlocks.pedestal;

import com.extraBlocks.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonCustom extends GuiButtonExt {
	
	public GuiButtonCustom(int id, int xPos, int yPos, String displayString) {
		super(id, xPos, yPos, displayString);
	}

	public GuiButtonCustom(int id, int xPos, int yPos, int width, int height, String displayString) {
		super(id, xPos, yPos, width, height, displayString);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		super.drawButton(mc, mouseX, mouseY, partial);
//		ResourceLocation t = new ResourceLocation(Main.MODID, "textures/gui/icon01.png");
//		mc.renderEngine.bindTexture(t);
//		this.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
		int j = 14737632;
		if (packedFGColour != 0)
        {
            j = packedFGColour;
        }
        else
        if (!this.enabled)
        {
            j = 10526880;
        }
        else if (this.hovered)
        {
            j = 16777120;
        }
		this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
	}
	
	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
	}

}
