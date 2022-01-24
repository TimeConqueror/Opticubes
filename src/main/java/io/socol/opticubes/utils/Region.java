package io.socol.opticubes.utils;

import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.Nullable;

public class Region {

    public static final Region BLOCK = new Region(0, 0, 0, 1, 1, 1);

    public final int x0;
    public final int y0;
    public final int z0;
    public final int x1;
    public final int y1;
    public final int z1;

    public Region(int x0, int y0, int z0, int x1, int y1, int z1) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }

    @Nullable
    public static Region tryCreate(int x0, int y0, int z0, int x1, int y1, int z1) {
        return x0 < x1 && y0 < y1 && z0 < z1 ? new Region(x0, y0, z0, x1, y1, z1) : null;
    }

    public boolean contains(int x, int y, int z) {
        return x0 <= x && y0 <= y && z0 <= z && x < x1 && y < y1 && z < z1;
    }

    public boolean contains(BlockPos pos) {
        return contains(pos.x, pos.y, pos.z);
    }

    public Region move(int x, int y, int z) {
        return new Region(x0 + x, y0 + y, z0 + z, x1 + x, y1 + y, z1 + z);
    }

    public Region move(BlockPos pos) {
        return move(pos.x, pos.y, pos.z);
    }

    @Nullable
    public Region clampInChunk(int cx, int cz) {
        return tryCreate(
                Math.max(x0, cx << 4), y0, Math.max(z0, cz << 4),
                Math.min(x1, (cx + 1) << 4), y1, Math.min(z1, (cz + 1) << 4)
        );
    }

    @Override
    public String toString() {
        return "Region{" +
                "x0=" + x0 +
                ", y0=" + y0 +
                ", z0=" + z0 +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", z1=" + z1 +
                '}';
    }

    public double centerX() {
        return (x1 + x0) / 2.0;
    }

    public double centerY() {
        return (y1 + y0) / 2.0;
    }

    public double centerZ() {
        return (z1 + z0) / 2.0;
    }

    public int sizeX() {
        return x1 - x0;
    }

    public int sizeY() {
        return y1 - y0;
    }

    public int sizeZ() {
        return z1 - z0;
    }

    public double getDiagonalLength() {
        return Math.sqrt(sizeX() * sizeX() + sizeY() * sizeY() + sizeZ() * sizeZ());
    }

    public Vec3 getCenter() {
        return Vec3.createVectorHelper(centerX(), centerY(), centerZ());
    }

    public boolean intersects(double x, double y, double z, double radius) {
        double dx = Math.max(x0, Math.min(x, x1)) - x;
        double dy = Math.max(y0, Math.min(y, y1)) - y;
        double dz = Math.max(z0, Math.min(z, z1)) - z;
        return (dx * dx + dy * dy + dz * dz) <= radius * radius;
    }

    public boolean isInFrustum() {
        return ClippingHelperImpl.getInstance().isBoxInFrustum(
                x0 - RenderManager.renderPosX,
                y0 - RenderManager.renderPosY,
                z0 - RenderManager.renderPosZ,
                x1 - RenderManager.renderPosX,
                y1 - RenderManager.renderPosY,
                z1 - RenderManager.renderPosZ
        );
    }
}
