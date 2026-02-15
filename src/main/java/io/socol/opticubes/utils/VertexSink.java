package io.socol.opticubes.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class VertexSink {

    private final Vector3f translation = new Vector3f();
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
    private final Vector3f pos = new Vector3f();
    private final Vector2f uv = new Vector2f();

    private BufferBuilder builder;

    public VertexSink(BufferBuilder builder) {
        this.builder = builder;
    }

    public void addTranslation(float translationX, float translationY, float translationZ) {
        translation.set(translation.getX() + translationX, translation.getY() + translationY, translation.getZ() + translationZ);
    }

    public void setTranslation(float translationX, float translationY, float translationZ) {
        translation.set(translationX, translationY, translationZ);
    }

    public void resetTranslation() {
        translation.set(0, 0, 0);
    }

    public VertexSink beginQuads() {
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        return this;
    }

    public VertexSink pos(float x, float y, float z) {
        pos.set(x, y, z);
        return this;
    }

    public VertexSink uv(float u, float v) {
        uv.set(u, v);
        return this;
    }

    public VertexSink color(int argb) {
        return color(ColorUtils.getRed(argb), ColorUtils.getGreen(argb), ColorUtils.getBlue(argb), ColorUtils.getAlpha(argb));
    }

    public VertexSink color(int r, int g, int b, int a) {
        return color(r / 255F, g / 255F, b / 255F, a / 255F);
    }

    public VertexSink color(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        return this;
    }

    public VertexSink end() {
        builder.pos(translation.getX() + pos.getX(), translation.getY() + pos.getY(), translation.getZ() + pos.getZ())
                .tex(uv.getX(), uv.getY())
                .color(color.getX(), color.getY(), color.getZ(), color.getW())
                .endVertex();

        return this;
    }

    public BufferBuilder getBuilder() {
        return builder;
    }
}
