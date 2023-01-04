package io.socol.opticubes.fx;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.TessellatorUtils;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextPanelRenderer {

    private static final ResourceLocation FRAME_TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/entity/frame.png");

    public static void renderText(BlockPos pos, String text, int side, boolean animate, int time, float partialTicks) {
        FontRenderer font = TileEntityRendererDispatcher.instance.getFontRenderer();
        if (font == null) {
            return;
        }

        float size = 24f;
        float scale = 1 / size;
        GL11.glPushMatrix();

        GL11.glTranslated(pos.getX() - TileEntityRendererDispatcher.staticPlayerX + 0.5, pos.getY() - TileEntityRendererDispatcher.staticPlayerY + 0.5, pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ + 0.5);

        if (side > 1) {
            GL11.glRotated((side > 3 ? 270 : 180) + side * 180, 0, 1, 0);
            GL11.glTranslated(0.025, 60 / 16f * scale, 0.52);
        } else {
            float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
            int angle = (int) (yaw < 0 ? ((yaw - 45) / 90) : ((yaw + 45) / 90));
            GL11.glRotated(180 - angle * 90, 0, 1, 0);
            if (side == 1) {
                GL11.glTranslated(0.025, 0.52, -60 / 16f * scale);
                GL11.glRotated(-90, 1, 0, 0);
            } else {
                GL11.glTranslated(0.025, -0.52, 60 / 16f * scale);
                GL11.glRotated(90, 1, 0, 0);
            }
        }

        GL11.glScalef(scale, -scale, scale);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        float frameScale = animate ? (float) (1.0 + 0.06 * Math.sin(Math.toRadians((time + partialTicks) * 10))) : 1.0f;
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                GL11.glTranslated(0, -0.1, -0.2);
                GL11.glScaled(1.05, 1.05, 1.0);
            }

            int color = i == 0 ? 0xFFFFFFFF : 0xFF808080;

            GL11.glScaled(frameScale, frameScale, 1.0);
            Tessellator tessellator = Tessellator.instance;
            Minecraft.getMinecraft().getTextureManager().bindTexture(FRAME_TEXTURE);
            float dx = -12.5f;
            float dy = -8f;
            tessellator.startDrawingQuads();
            TessellatorUtils.setColor(color);
            tessellator.addVertexWithUV(dx, dy, 0, 0, 0);
            tessellator.addVertexWithUV(dx, size + dy, 0, 1, 0);
            tessellator.addVertexWithUV(size + dx, size + dy, 0, 1, 1);
            tessellator.addVertexWithUV(size + dx, dy, 0, 0, 1);
            tessellator.draw();
            GL11.glScaled(1 / frameScale, 1 / frameScale, 1.0);

            font.drawString(text, -font.getStringWidth(text) / 2, 0, color);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
