package io.socol.opticubes.service.opti;

import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import io.socol.opticubes.utils.pos.ChunkPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Immutable snapshot of TileEntityOptiCube data
 */
public class OptiCube {

    public static final int MIN_RADIUS = -1;
    public static final int MAX_RADIUS = 64;

    private final BlockPos pos;
    private final Region region; // absolute
    private final int radius;

    private boolean enabled = true; // true -> hide tiles

    private List<ChunkPos> affectedChunks = Collections.emptyList();

    public OptiCube(BlockPos pos, Region region, int radius) {
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

    public int getRadius() {
        return radius;
    }

    public void setAffectedChunks(List<ChunkPos> affectedChunks) {
        this.affectedChunks = affectedChunks;
    }

    public List<ChunkPos> getAffectedChunks() {
        return affectedChunks;
    }

    public void checkEnabled(World world, double cameraX, double cameraY, double cameraZ) {
        boolean isEditingRegion = ClientOptiCubeEditingService.getInstance().isEditingRegion(world, pos, OptiCubeRegionType.AFFECTED_REGION);
        enabled = !isEditingRegion && (radius == -1 || !region.intersects(cameraX, cameraY, cameraZ, radius));
    }
}
