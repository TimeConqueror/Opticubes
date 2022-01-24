package io.socol.opticubes.utils.pos;

import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class BlockPos {

    public final int x;
    public final int y;
    public final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public static BlockPos ofTile(TileEntity tile) {
        return new BlockPos(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public ChunkPos getChunkPos() {
        return new ChunkPos(x >> 4, z >> 4);
    }
}
