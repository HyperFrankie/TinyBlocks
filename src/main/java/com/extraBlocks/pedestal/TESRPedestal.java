package com.extraBlocks.pedestal;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TESRPedestal extends TileEntitySpecialRenderer<TileEntityPedestal> {

	TileEntityPedestal te1 = null;

	@Override
	public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//		renderFloatingItem(te, x, y, z, partialTicks, destroyStage, alpha);
		renderBlock(te, x, y, z, partialTicks, destroyStage, alpha);
	}

	public void renderFloatingItem(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		//		if(te1 == null) {
		te1 = (TileEntityPedestal) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te.getPos());
		//		}

		ItemStack stack = te.inventory.getStackInSlot(0);
		if (!stack.isEmpty()) {
			//			Minecraft.getMinecraft().player.canEntityBeSeen(entityIn)
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.pushMatrix();
			double offsetX = 0;
			if(te1.getMotionMultX() != 0 && te1.getMotionSpeedX() != 0) {
				offsetX = Math.sin((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getMotionTimeOffX()) * 2 * Math.PI * te1.getMotionSpeedX()) * te1.getMotionMultX();
			}
			double offsetY = 0;
			if(te1.getMotionMultY() != 0 && te1.getMotionSpeedY() != 0) {
				offsetY = Math.sin((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getMotionTimeOffY()) * 2 * Math.PI * te1.getMotionSpeedY()) * te1.getMotionMultY();
			}
			double offsetZ = 0;
			if(te1.getMotionMultZ() != 0 && te1.getMotionSpeedZ() != 0) {
				offsetZ = Math.sin((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getMotionTimeOffZ()) * 2 * Math.PI * te1.getMotionSpeedZ()) * te1.getMotionMultZ();
			}

			GlStateManager.translate(x + 0.5 + offsetX + te1.getPositionOffsetX(), y + 0.5 + offsetY + te1.getPositionOffsetY(), z + 0.5 + offsetZ + te1.getPositionOffsetZ());

			if(te1.getRotationSpeedX() != 0) {
				GlStateManager.rotate((float) ((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getRotationTimeOffX()) * 3.6 * te1.getRotationSpeedX()), 1.0f, 0.0f, 0.0f);
			}
			if(te1.getRotationSpeedY() != 0) {
				GlStateManager.rotate((float) ((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getRotationTimeOffY()) * 3.6 * te1.getRotationSpeedY()), 0.0f, 1.0f, 0.0f);
			}
			if(te1.getRotationSpeedX() != 0) {
				GlStateManager.rotate((float) ((te1.getWorld().getTotalWorldTime() + partialTicks + te1.getRotationTimeOffZ()) * 3.6 * te1.getRotationSpeedZ()), 0.0f, 0.0f, 1.0f);
			}

			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
			//			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

			GlStateManager.scale(0.5D, 0.5D, 0.5D);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}

		String s = te.name;

		if (s != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
			this.setLightmapDisabled(true);
			this.drawNameplate(te, s, x, y, z, 12);
			this.setLightmapDisabled(false);
		}
	}

	public void renderBlock(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		te1 = (TileEntityPedestal) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te.getPos());

		ItemStack stack = te.inventory.getStackInSlot(0);
		if (!stack.isEmpty()) {
			Tessellator tes = Tessellator.getInstance();

			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
//			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

//			GlStateManager.popAttrib();
//			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}

}