package io.socol.opticubes.utils;

import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Region {

    public static final Region BLOCK = new Region(0, 0, 0, 1, 1, 1);

    private static final int MIN_WORLD_COORD = -30_000_000;
    private static final int MAX_WORLD_COORD = 29_999_999;
    private static final int MIN_WORLD_HEIGHT = 0;
    private static final int MAX_WORLD_HEIGHT = 255;

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

    public Region(BlockPos pos) {
        this(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1);
    }

    public Region(BlockPos pos1, BlockPos pos2) {
        this.x0 = Math.min(pos1.x, pos2.x);
        this.y0 = Math.min(pos1.y, pos2.y);
        this.z0 = Math.min(pos1.z, pos2.z);
        this.x1 = Math.max(pos1.x, pos2.x) + 1;
        this.y1 = Math.max(pos1.y, pos2.y) + 1;
        this.z1 = Math.max(pos1.z, pos2.z) + 1;
    }

    public static Region createProper(int x0, int y0, int z0, int x1, int y1, int z1) {
        return new Region(
                Math.min(x0, x1),
                Math.min(y0, y1),
                Math.min(z0, z1),
                Math.max(x0, x1),
                Math.max(y0, y1),
                Math.max(z0, z1)
        );
    }

    @Nullable
    public static Region tryRecreate(@Nullable Region region) {
        return region == null ? null : tryCreate(region.x0, region.y0, region.z0, region.x1, region.y1, region.z1);
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
    public Region clampByChunk(int cx, int cz) {
        return tryCreate(
                Math.max(x0, cx << 4), y0, Math.max(z0, cz << 4),
                Math.min(x1, (cx + 1) << 4), y1, Math.min(z1, (cz + 1) << 4)
        );
    }

    public Region clampByWorld() {
        return new Region(
                MathHelper.clamp_int(x0, MIN_WORLD_COORD, MAX_WORLD_COORD),
                MathHelper.clamp_int(y0, MIN_WORLD_HEIGHT, MAX_WORLD_HEIGHT),
                MathHelper.clamp_int(z0, MIN_WORLD_COORD, MAX_WORLD_COORD),
                MathHelper.clamp_int(x1, MIN_WORLD_COORD, MAX_WORLD_COORD),
                MathHelper.clamp_int(y1, MIN_WORLD_HEIGHT, MAX_WORLD_HEIGHT),
                MathHelper.clamp_int(z1, MIN_WORLD_COORD, MAX_WORLD_COORD)
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

    public Region inflate(int factor) {
        return new Region(x0 - factor, y0 - factor, z0 - factor, x1 + factor, y1 + factor, z1 + factor);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return x0 == region.x0 && y0 == region.y0 && z0 == region.z0 && x1 == region.x1 && y1 == region.y1 && z1 == region.z1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x0, y0, z0, x1, y1, z1);
    }

    public boolean equals(BlockPos pos) {
        return x0 == pos.x && y0 == pos.y && z0 == pos.z && x1 == (pos.x + 1) && y1 == (pos.y + 1) && z1 == (pos.z + 1);
    }


    public Region asRelative(BlockPos opiCubePos) {
        return move(-opiCubePos.x, -opiCubePos.y, -opiCubePos.z);
    }
}
