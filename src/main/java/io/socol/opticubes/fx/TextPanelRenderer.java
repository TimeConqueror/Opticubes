package io.socol.opticubes.fx;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class TextPanelRenderer {

    private static final ResourceLocation FRAME_TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/entity/frame.png");

    public static void renderText(BlockPos pos, String text, EnumFacing side, boolean animate, int time, float partialTicks) {
        FontRenderer font = TileEntityRendererDispatcher.instance.getFontRenderer();
        if (font == null) {
            return;
        }

        float size = 24f;
        float scale = 1 / size;
        GlStateManager.pushMatrix();

        GlStateManager.translate(pos.getX() - TileEntityRendererDispatcher.staticPlayerX + 0.5, pos.getY() - TileEntityRendererDispatcher.staticPlayerY + 0.5, pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ + 0.5);

        int sideIndex = side.getIndex();
        if (sideIndex > 1) {
            GlStateManager.rotate((sideIndex > 3 ? 270 : 180) + sideIndex * 180, 0, 1, 0);
            GlStateManager.translate(0.025, 60 / 16f * scale, 0.52);
        } else {
            float yaw = Minecraft.getMinecraft().player.rotationYaw;
            int angle = (int) (yaw < 0 ? ((yaw - 45) / 90) : ((yaw + 45) / 90));
            GlStateManager.rotate(180 - angle * 90, 0, 1, 0);
            if (sideIndex == 1) {
                GL11.glTranslated(0.025, 0.52, -60 / 16f * scale);
                GlStateManager.rotate(-90, 1, 0, 0);
            } else {
                GL11.glTranslated(0.025, -0.52, 60 / 16f * scale);
                GlStateManager.rotate(90, 1, 0, 0);
            }
        }

        GlStateManager.scale(scale, -scale, scale);

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        Tessellator tessellator = Tessellator.getInstance();

        float frameScale = animate ? (float) (1.0 + 0.06 * Math.sin(Math.toRadians((time + partialTicks) * 10))) : 1.0f;
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                GlStateManager.translate(0, -0.1, -0.2);
                GlStateManager.scale(1.05, 1.05, 1.0);
            }

            int argbColor = i == 0 ? 0xFFFFFFFF : 0xFF808080;

            GlStateManager.scale(frameScale, frameScale, 1.0);
            BufferBuilder builder = tessellator.getBuffer();
            Minecraft.getMinecraft().getTextureManager().bindTexture(FRAME_TEXTURE);
            float dx = -12.5f;
            float dy = -8f;
            ColorUtils.setGlColor(argbColor);
            builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            builder.pos(dx, dy, 0).tex(0, 0).endVertex();
            builder.pos(dx, size + dy, 0).tex(1, 0).endVertex();
            builder.pos(size + dx, size + dy, 0).tex(1, 1).endVertex();
            builder.pos(size + dx, dy, 0).tex(0, 1).endVertex();

            tessellator.draw();
            GlStateManager.scale(1 / frameScale, 1 / frameScale, 1.0F);

            font.drawString(text, -font.getStringWidth(text) / 2, 0, argbColor);
        }

        GlStateManager.enableLighting();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}
