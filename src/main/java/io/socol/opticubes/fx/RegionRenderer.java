package io.socol.opticubes.fx;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.VertexSink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
        Tessellator tessellator = Tessellator.getInstance();

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

        GlStateManager.enableTexture2D();
        Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        BufferBuilder builder = tessellator.getBuffer();
        VertexSink sink = new VertexSink(builder);

        if (!ignoreDepthRegions.isEmpty()) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(true);

            if (!regionsWithSides.isEmpty()) {
                GlStateManager.enableCull();
                GlStateManager.enableBlend();
                Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_SIDE_TEXTURE);

                sink.beginQuads();
                //FIXME port 12
//                tessellator.setBrightness(240);
                for (FXRegion region : regionsWithSides) {
                    sink.color(region.argbColor);
                    drawSides(
                            sink, region.box, (float) region.inflate,
                            (float)-TileEntityRendererDispatcher.staticPlayerX, (float)-TileEntityRendererDispatcher.staticPlayerY, (float)-TileEntityRendererDispatcher.staticPlayerZ
                    );
                }
                tessellator.draw();

                GlStateManager.disableBlend();
                GlStateManager.disableCull();
                Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
            }

            sink.beginQuads();
            //FIXME port 12
//            tessellator.setBrightness(240);
            for (FXRegion region : ignoreDepthRegions) {
                sink.color(region.argbColor);
                drawFrame(
                        sink, region.box, (float) region.inflate,
                        (float)-TileEntityRendererDispatcher.staticPlayerX, (float)-TileEntityRendererDispatcher.staticPlayerY, (float)-TileEntityRendererDispatcher.staticPlayerZ, false
                );
            }
            tessellator.draw();

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(BOX_TEXTURE);
        sink.beginQuads();
        //FIXME port 12
//        tessellator.setBrightness(240);

        for (FXRegion region : regions) {
            sink.color(region.argbColor);
            drawFrame(
                    sink, region.box, (float) region.inflate,
                    (float)-TileEntityRendererDispatcher.staticPlayerX, (float)-TileEntityRendererDispatcher.staticPlayerY, (float)-TileEntityRendererDispatcher.staticPlayerZ, true
            );
        }

        tessellator.draw();
        GlStateManager.color(1, 1,1, 1);
        GlStateManager.enableBlend();

        regions.clear();
    }

    public static class FXRegion {
        private final Region box;
        private final int argbColor;
        private double inflate;
        private boolean ignoreDepth;
        private boolean withSides;

        public FXRegion(Region box, int argbColor) {
            this.box = box;
            this.argbColor = argbColor;
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

    private static void drawSides(VertexSink sink, Region box, double inflate, float dx, float dy, float dz) {
        float sx = box.sizeX();
        float sy = box.sizeY();
        float sz = box.sizeZ();

        sink.setTranslation(box.x0 + dx, box.y0 + dy, box.z0 + dz);

        sink.pos(0, sy, 0).uv(0, 0).end();
        sink.pos(sx, sy, 0).uv(sx, 0).end();
        sink.pos(sx, 0, 0).uv(sx, sy).end();
        sink.pos(0, 0, 0).uv(0, sy).end();

        sink.pos(sx, sy, sz).uv(-sx, 0).end();
        sink.pos(0, sy, sz).uv(0, 0).end();
        sink.pos(0, 0, sz).uv(0, sy).end();
        sink.pos(sx, 0, sz).uv(-sx, sy).end();

        sink.pos(0, sy, sz).uv(sz, 0).end();
        sink.pos(0, sy, 0).uv(0, 0).end();
        sink.pos(0, 0, 0).uv(0, sy).end();
        sink.pos(0, 0, sz).uv(sz, sy).end();

        sink.pos(sx, sy, 0).uv(0, 0).end();
        sink.pos(sx, sy, sz).uv(-sz, 0).end();
        sink.pos(sx, 0, sz).uv(-sz, sy).end();
        sink.pos(sx, 0, 0).uv(0, sy).end();

        sink.pos(sx, sy, 0).uv(sx, 0).end();
        sink.pos(0, sy, 0).uv(0, 0).end();
        sink.pos(0, sy, sz).uv(0, sz).end();
        sink.pos(sx, sy, sz).uv(sx, sz).end();

        sink.pos(0, 0, 0).uv(0, 0).end();
        sink.pos(sx, 0, 0).uv(-sx, 0).end();
        sink.pos(sx, 0, sz).uv(-sx, sz).end();
        sink.pos(0, 0, sz).uv(0, sz).end();
    }

    private static void drawFrame(VertexSink sink, Region box, float inflate, float dx, float dy, float dz, boolean solid) {
        float sx = box.sizeX();
        float sy = box.sizeY();
        float sz = box.sizeZ();

        sink.setTranslation(box.x0 + dx, box.y0 + dy, box.z0 + dz);
        drawSide(sink,
                1, 0, 0,
                0, 0, 1,
                0, sy, 0,
                sx, sz, inflate, solid
        );

        sink.setTranslation(box.x0 + dx, box.y0 + dy, box.z0 + dz);
        drawSide(sink,
                0, 0, 1,
                0, 1, 0,
                sx, 0, 0,
                sz, sy, inflate, solid
        );

        sink.setTranslation(box.x0 + dx, box.y0 + dy, box.z0 + dz);
        drawSide(sink,
                1, 0, 0,
                0, 1, 0,
                0, 0, sz,
                sx, sy, inflate, solid
        );
    }

    private static void drawSide(VertexSink sink,
                                 float dx1, float dy1, float dz1,
                                 float dx2, float dy2, float dz2,
                                 float dx3, float dy3, float dz3,
                                 float w, float h,
                                 float inflate, boolean solid) {
        // delta coords to move first corner
        float ix = (dx1 + dx2) * inflate; // inflate x
        float iy = (dy1 + dy2) * inflate; // inflate y
        float iz = (dz1 + dz2) * inflate; // inflate z
        sink.addTranslation(-ix, -iy, -iz);

        float delta = inflate / (dx3 + dy3 + dz3);

        // inflated dimensions
        float iw = w + 2 * inflate;
        float ih = h + 2 * inflate;

        // three more inflated corners of plane
        float x1 = dx1 * iw;
        float y1 = dy1 * iw;
        float z1 = dz1 * iw;

        float x3 = dx2 * ih;
        float y3 = dy2 * ih;
        float z3 = dz2 * ih;

        float x2 = x1 + x3;
        float y2 = y1 + y3;
        float z2 = z1 + z3;

        // rescale normals (block units -> pixel units)
        float u = 1 / 16f;
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
                sink.addTranslation(-dx3 * delta, -dy3 * delta, -dz3 * delta);
            } else {
                float d = 1 + 2 * delta;
                sink.addTranslation(dx3 * d, dy3 * d, dz3 * d);
            }

            float u0 = 0;//(i == 0 ? 1 : -1) * System.currentTimeMillis() % 3000 / 3000f;

            sink.pos(0, 0, 0).uv(u0, 0).end();
            sink.pos(x1, y1, z1).uv(u0 + w, 0).end();
            sink.pos(x1 + dx2, y1 + dy2, z1 + dz2).uv(u0 + w, u).end();
            sink.pos(dx2, dy2, dz2).uv( u0, u).end();

            sink.pos(x1, y1, z1).uv( w, u0).end();
            sink.pos(x2, y2, z2).uv( w, u0 + h).end();
            sink.pos(x2 - dx1, y2 - dy1, z2 - dz1).uv( w - u, u0 + h).end();
            sink.pos(x1 - dx1, y1 - dy1, z1 - dz1).uv( w - u, u0).end();

            sink.pos(x2, y2, z2).uv( -u0 + w, h).end();
            sink.pos(x3, y3, z3).uv( -u0, h).end();
            sink.pos(x3 - dx2, y3 - dy2, z3 - dz2).uv( -u0, h - u).end();
            sink.pos(x2 - dx2, y2 - dy2, z2 - dz2).uv( -u0 + w, h - u).end();

            sink.pos(x3, y3, z3).uv( 0, h - u0).end();
            sink.pos(0, 0, 0).uv( 0, -u0).end();
            sink.pos(dx1, dy1, dz1).uv( u, -u0).end();
            sink.pos(x3 + dx1, y3 + dy1, z3 + dz1).uv( u, h - u0).end();
        }
    }

    public static class EventListener {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onRender(RenderWorldLastEvent event) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player == null) {
                return;
            }
            RegionRenderer.drawAll();
        }
    }
}
