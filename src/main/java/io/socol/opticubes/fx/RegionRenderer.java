package io.socol.opticubes.fx;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.TessellatorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RegionRenderer {

    private static final ResourceLocation BOX_TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/entity/box.png");
    private static final ResourceLocation BOX_SIDE_TEXTURE = new ResourceLocation(OptiCubes.MODID, "textures/entity/box_side.png");

    private static final List<FXRegion> regions = new ArrayList<>();

    public static FXRegion addRegion(Region region, int color) {
        FXRegion fxRegion = new FXRegion(region, color);
        regions.add(fxRegion);
        return fxRegion;
    }

    public static void drawAll() {
        if (regions.isEmpty()) {
            return;
        }
        Tessellator tessellator = Tessellator.instance;

        List<FXRegion> ignoreDepthRegions = new ArrayList<>();
        List<FXRegion> regionsWithSides = new ArrayList<>();
        for (FXRegion region : regions) {
            if (region.ignoreDepth) {
                ignoreDepthRegions.add(region);
                if (region.withSides) {
                    regionsWithSides.add(region);
                }
            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        if (!ignoreDepthRegions.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);

            if (!regionsWithSides.isEmpty()) {
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_BLEND);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_SIDE_TEXTURE);

                tessellator.startDrawingQuads();
                tessellator.setBrightness(240);
                for (FXRegion region : regionsWithSides) {
                    TessellatorUtils.setColor(region.color);
                    drawSides(
                            tessellator, region.box, region.inflate,
                            -TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ
                    );
                }
                tessellator.draw();

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_CULL_FACE);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
            }

            tessellator.startDrawingQuads();
            tessellator.setBrightness(240);
            for (FXRegion region : ignoreDepthRegions) {
                TessellatorUtils.setColor(region.color);
                drawFrame(
                        tessellator, region.box, region.inflate,
                        -TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ, false
                );
            }
            tessellator.draw();

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);

        for (FXRegion region : regions) {
            TessellatorUtils.setColor(region.color);
            drawFrame(
                    tessellator, region.box, region.inflate,
                    -TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ, true
            );
        }

        tessellator.draw();

        tessellator.setColorRGBA_F(1f, 1f, 1f, 1f);
        tessellator.setTranslation(0, 0, 0);
        GL11.glEnable(GL11.GL_BLEND);

        regions.clear();
    }

    public static class FXRegion {
        private final Region box;
        private final int color;
        private double inflate;
        private boolean ignoreDepth;
        private boolean withSides;

        public FXRegion(Region box, int color) {
            this.box = box;
            this.color = color;
        }

        public FXRegion inflate(double inflate) {
            this.inflate = inflate;
            return this;
        }

        public FXRegion ignoreDepth() {
            this.ignoreDepth = true;
            return this;
        }

        public FXRegion withSides() {
            this.withSides = true;
            return this;
        }
    }

    private static void drawSides(Tessellator tessellator, Region box, double inflate, double dx, double dy, double dz) {
        double sx = box.sizeX();
        double sy = box.sizeY();
        double sz = box.sizeZ();

        tessellator.setTranslation((float) box.x0 + dx, (float) box.y0 + dy, (float) box.z0 + dz);

        tessellator.addVertexWithUV(0, sy, 0, 0, 0);
        tessellator.addVertexWithUV(sx, sy, 0, sx, 0);
        tessellator.addVertexWithUV(sx, 0, 0, sx, sy);
        tessellator.addVertexWithUV(0, 0, 0, 0, sy);

        tessellator.addVertexWithUV(sx, sy, sz, -sx, 0);
        tessellator.addVertexWithUV(0, sy, sz, 0, 0);
        tessellator.addVertexWithUV(0, 0, sz, 0, sy);
        tessellator.addVertexWithUV(sx, 0, sz, -sx, sy);

        tessellator.addVertexWithUV(0, sy, sz, sz, 0);
        tessellator.addVertexWithUV(0, sy, 0, 0, 0);
        tessellator.addVertexWithUV(0, 0, 0, 0, sy);
        tessellator.addVertexWithUV(0, 0, sz, sz, sy);

        tessellator.addVertexWithUV(sx, sy, 0, 0, 0);
        tessellator.addVertexWithUV(sx, sy, sz, -sz, 0);
        tessellator.addVertexWithUV(sx, 0, sz, -sz, sy);
        tessellator.addVertexWithUV(sx, 0, 0, 0, sy);

        tessellator.addVertexWithUV(sx, sy, 0, sx, 0);
        tessellator.addVertexWithUV(0, sy, 0, 0, 0);
        tessellator.addVertexWithUV(0, sy, sz, 0, sz);
        tessellator.addVertexWithUV(sx, sy, sz, sx, sz);

        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(sx, 0, 0, -sx, 0);
        tessellator.addVertexWithUV(sx, 0, sz, -sx, sz);
        tessellator.addVertexWithUV(0, 0, sz, 0, sz);
    }

    private static void drawFrame(Tessellator tessellator, Region box, double inflate, double dx, double dy, double dz, boolean solid) {
        double sx = box.sizeX();
        double sy = box.sizeY();
        double sz = box.sizeZ();

        tessellator.setTranslation((float) box.x0 + dx, (float) box.y0 + dy, (float) box.z0 + dz);
        drawSide(tessellator,
                1, 0, 0,
                0, 0, 1,
                0, sy, 0,
                sx, sz, inflate, solid
        );

        tessellator.setTranslation((float) box.x0 + dx, (float) box.y0 + dy, (float) box.z0 + dz);
        drawSide(tessellator,
                0, 0, 1,
                0, 1, 0,
                sx, 0, 0,
                sz, sy, inflate, solid
        );

        tessellator.setTranslation((float) box.x0 + dx, (float) box.y0 + dy, (float) box.z0 + dz);
        drawSide(tessellator,
                1, 0, 0,
                0, 1, 0,
                0, 0, sz,
                sx, sy, inflate, solid
        );
    }

    private static void drawSide(Tessellator tessellator, double dx1, double dy1, double dz1, double dx2, double dy2, double dz2, double dx3, double dy3, double dz3, double w, double h, double inflate, boolean solid) {
        // delta coords to move first corner
        double ix = (dx1 + dx2) * inflate; // inflate x
        double iy = (dy1 + dy2) * inflate; // inflate y
        double iz = (dz1 + dz2) * inflate; // inflate z
        tessellator.addTranslation((float) -ix, (float) -iy, (float) -iz);

        float delta = (float) (inflate / (dx3 + dy3 + dz3));

        // inflated dimensions
        double iw = w + 2 * inflate;
        double ih = h + 2 * inflate;

        // three more inflated corners of plane
        double x1 = dx1 * iw;
        double y1 = dy1 * iw;
        double z1 = dz1 * iw;

        double x3 = dx2 * ih;
        double y3 = dy2 * ih;
        double z3 = dz2 * ih;

        double x2 = x1 + x3;
        double y2 = y1 + y3;
        double z2 = z1 + z3;

        // rescale normals (block units -> pixel units)
        double u = 1 / 16f;
        dx1 *= u;
        dy1 *= u;
        dz1 *= u;
        dx2 *= u;
        dy2 *= u;
        dz2 *= u;

        if (solid) {
            w = u;
            h = u;
        }

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                tessellator.addTranslation((float) -dx3 * delta, (float) -dy3 * delta, (float) -dz3 * delta);
            } else {
                float d = 1 + 2 * delta;
                tessellator.addTranslation((float) dx3 * d, (float) dy3 * d, (float) dz3 * d);
            }

            double u0 = 0;//(i == 0 ? 1 : -1) * System.currentTimeMillis() % 3000 / 3000f;

            tessellator.addVertexWithUV(0, 0, 0, u0, 0);
            tessellator.addVertexWithUV(x1, y1, z1, u0 + w, 0);
            tessellator.addVertexWithUV(x1 + dx2, y1 + dy2, z1 + dz2, u0 + w, u);
            tessellator.addVertexWithUV(dx2, dy2, dz2, u0, u);

            tessellator.addVertexWithUV(x1, y1, z1, w, u0);
            tessellator.addVertexWithUV(x2, y2, z2, w, u0 + h);
            tessellator.addVertexWithUV(x2 - dx1, y2 - dy1, z2 - dz1, w - u, u0 + h);
            tessellator.addVertexWithUV(x1 - dx1, y1 - dy1, z1 - dz1, w - u, u0);

            tessellator.addVertexWithUV(x2, y2, z2, -u0 + w, h);
            tessellator.addVertexWithUV(x3, y3, z3, -u0, h);
            tessellator.addVertexWithUV(x3 - dx2, y3 - dy2, z3 - dz2, -u0, h - u);
            tessellator.addVertexWithUV(x2 - dx2, y2 - dy2, z2 - dz2, -u0 + w, h - u);

            tessellator.addVertexWithUV(x3, y3, z3, 0, h - u0);
            tessellator.addVertexWithUV(0, 0, 0, 0, -u0);
            tessellator.addVertexWithUV(dx1, dy1, dz1, u, -u0);
            tessellator.addVertexWithUV(x3 + dx1, y3 + dy1, z3 + dz1, u, h - u0);
        }
    }

    public static class EventListener {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onRender(RenderWorldLastEvent event) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                return;
            }
            RegionRenderer.drawAll();
        }
    }
}
