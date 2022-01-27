package io.socol.opticubes.utils.pos;

import java.util.Objects;

public class ChunkPos {

    public final int x;
    public final int z;

    public ChunkPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int centerX() {
        return (this.x << 4) + 8;
    }

    public int centerZ() {
        return (this.z << 4) + 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkPos chunkPos = (ChunkPos) o;
        return x == chunkPos.x && z == chunkPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
