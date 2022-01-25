package io.socol.opticubes.utils.pos;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

import java.util.Objects;

public class BlockPos {
    private static final int PACKED_X_LENGTH = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
    private static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
    private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
    private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
    private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
    private static final int Z_OFFSET = PACKED_Y_LENGTH;
    private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

    public final int x;
    public final int y;
    public final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static int unpackX(long packedPos) {
        return (int) ((packedPos << (64 - X_OFFSET - PACKED_X_LENGTH)) >> (64 - PACKED_X_LENGTH));
    }

    public static int unpackY(long packedPos) {
        return (int) ((packedPos << (64 - PACKED_Y_LENGTH)) >> (64 - PACKED_Y_LENGTH));
    }

    public static int unpackZ(long packedPos) {
        return (int) ((packedPos << (64 - Z_OFFSET - PACKED_Z_LENGTH)) >> (64 - PACKED_Z_LENGTH));
    }

    public static BlockPos fromPacked(long packedPos) {
        return new BlockPos(unpackX(packedPos), unpackY(packedPos), unpackZ(packedPos));
    }

    public long packToLong() {
        return packToLong(x, y, z);
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public static long packToLong(int x, int y, int z) {
        long i = 0L;
        i = i | ((long) x & PACKED_X_MASK) << X_OFFSET;
        i = i | ((long) y & PACKED_Y_MASK) << 0;
        return i | ((long) z & PACKED_Z_MASK) << Z_OFFSET;
    }

    public static BlockPos ofTile(TileEntity tile) {
        return new BlockPos(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public ChunkPos getChunkPos() {
        return new ChunkPos(x >> 4, z >> 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
