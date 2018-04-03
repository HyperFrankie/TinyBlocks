package com.extraBlocks.block.miniBlock;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.extraBlocks.Main;
import com.extraBlocks.block.ModBlocks;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.gui.toasts.TutorialToast.Icons;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.client.model.obj.OBJModel.TextureCoordinate;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class TESRMiniBlock extends TileEntitySpecialRenderer<TileEntityMiniBlock> {

	TileEntityMiniBlock te = null;

//	@Override
//	public void renderTileEntityFast(TileEntityMiniBlock te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
//		// TODO Auto-generated method stub
//		renderBlock(te, x, y, z, partialTicks, destroyStage, partial, buffer);
//	}
	
	@Override
	public void render(TileEntityMiniBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//		System.out.println("render");
		renderBlock(te, x, y, z, partialTicks, destroyStage, alpha);
//		this.bindTexture(new ResourceLocation("textures/block/stone.png"));
	}

	public void renderBlock(TileEntityMiniBlock te1, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//	public void renderBlock(TileEntityMiniBlock te1, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
//		System.out.println("renderBlock");
		if(Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) != null) {
			if(Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) instanceof TileEntityMiniBlock) {
				te = (TileEntityMiniBlock) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos());
				if(te.renderMap != null) {
					if(!te.renderMap.isEmpty()) {
						
						BufferBuilder ver = Tessellator.getInstance().getBuffer();
						
						GlStateManager.enableRescaleNormal();
//						GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
//						RenderHelper.enableStandardItemLighting();
						
//						GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
						GlStateManager.pushMatrix();
//						RenderHelper.enableGUIStandardItemLighting();
						
//						buffer.reset();
						ver.setTranslation(x, y, z);
						ArrayList<Face> renderMap = te.renderMap;
						ver.endVertex();
						ver.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						
						for(int i = 0; i < renderMap.size(); i++) {
							Face o = renderMap.get(i);
//							Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
							ver.pos(o.r01, o.r02, o.r03).tex(o.maxU, o.maxV).endVertex();
							ver.pos(o.r04, o.r05, o.r06).tex(o.maxU, o.minV).endVertex();
							ver.pos(o.r07, o.r08, o.r09).tex(o.minU, o.minV).endVertex();
							ver.pos(o.r10, o.r11, o.r12).tex(o.minU, o.maxV).endVertex();
						}
//						buffer.endVertex();
						ver.setTranslation(0, 0, 0);
						Tessellator.getInstance().draw();
						GlStateManager.popMatrix();
						GlStateManager.disableRescaleNormal();
						GlStateManager.disableBlend();
						
						
						
						
						
//						BufferBuilder ver = Tessellator.getInstance().getBuffer();
//						ver.addVertexData(vertexData);
////						GlStateManager.pushMatrix();
//						
//						GlStateManager.enableRescaleNormal();
//						GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
////						GlStateManager.enableBlend();
////						GlStateManager.disableAlpha();
////						GlStateManager.enableCull();
//						RenderHelper.enableStandardItemLighting();
//						
////						int li = te1.getWorld().getCombinedLight(te1.getPos(), 15728640);
////						int i1 = li % 65536;
////						int j1 = li / 65536;
////						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
////						te1.getWorld().setLightFor(EnumSkyBlock.BLOCK, te1.getPos().add(0, 1, 0), 15);
//						
//						GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
//						GlStateManager.pushMatrix();
//						
//						ver.reset();
////						GlStateManager.disableLighting();
////						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15.0f * 16.0f, 15.0f * 16.0f);
////						GlStateManager.enableLighting();
////						GlStateManager.enableLight(5);
////						RenderHelper.enableStandardItemLighting();
////						GlStateManager.enableRescaleNormal();
////						GlStateManager.enableBlend();
//						ver.setTranslation(x, y, z);
//						ArrayList<Face> renderMap = te.renderMap;
//						ver.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//						
//						for(int i = 0; i < renderMap.size(); i++) {
//							Face o = renderMap.get(i);
//							Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//							ver.pos(o.r01, o.r02, o.r03).tex(o.maxU, o.maxV).endVertex();
//							ver.pos(o.r04, o.r05, o.r06).tex(o.maxU, o.minV).endVertex();
//							ver.pos(o.r07, o.r08, o.r09).tex(o.minU, o.minV).endVertex();
//							ver.pos(o.r10, o.r11, o.r12).tex(o.minU, o.maxV).endVertex();
//						}
//						ver.endVertex();
//						ver.setTranslation(0, 0, 0);
////						GlStateManager.enableLighting();
//						Tessellator.getInstance().draw();
////						RenderHelper.disableStandardItemLighting();
////						GlStateManager.disableRescaleNormal();
////						GlStateManager.disableBlend();
////						GlStateManager.popMatrix();
//						GlStateManager.popMatrix();
//						GlStateManager.disableRescaleNormal();
//						GlStateManager.disableBlend();
					}
				}
			}
		}
	}
}