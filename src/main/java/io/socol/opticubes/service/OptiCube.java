package io.socol.opticubes.service;

import io.socol.opticubes.utils.pos.BlockPos;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.ChunkPos;

import java.util.Collections;
import java.util.List;

/**
 * Immutable snapshot of TileEntityOptiCube data
 */
public class OptiCube {

    private final BlockPos pos;
    private final Region region; // absolute
    private final double radius;

    private boolean enabled = true; // true -> hide tiles

    private List<ChunkPos> affectedChunks = Collections.emptyList();

    public OptiCube(BlockPos pos, Region region, double radius) {
        this.pos = pos;
        this.region = region;
        this.radius = radius;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Region getRegion() {
        return region;
    }

    public void setAffectedChunks(List<ChunkPos> affectedChunks) {
        this.affectedChunks = affectedChunks;
    }

    public List<ChunkPos> getAffectedChunks() {
        return affectedChunks;
    }

    public void checkEnabled(double cameraX, double cameraY, double cameraZ) {
        enabled = !region.intersects(cameraX, cameraY, cameraZ, radius);
    }
}
