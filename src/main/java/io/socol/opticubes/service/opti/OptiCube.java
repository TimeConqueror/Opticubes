package io.socol.opticubes.service.opti;

import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import io.socol.opticubes.utils.pos.ChunkPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable snapshot of TileEntityOptiCube data
 */
public class OptiCube {

    public static final int MIN_RADIUS = -1;
    public static final int MAX_RADIUS = 64;

    private final BlockPos pos;
    private final Region region; // absolute
    private final int radius;

    private boolean enabled = false; // true -> hide tiles

    private List<ChunkPos> affectedChunks = Collections.emptyList();

    private Set<BlockPos> affectedMicroChunks = Collections.emptySet();

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

        this.affectedMicroChunks = new HashSet<>();
        if (!affectedChunks.isEmpty()) {
            int cy1 = region.y1 >> 4;
            for (int cy = region.y0 >> 4; cy <= cy1; cy++) {
                int blockY = (cy << 4) + 8;
                for (ChunkPos chunkPos : affectedChunks) {
                    affectedMicroChunks.add(new BlockPos(chunkPos.centerX(), blockY, chunkPos.centerZ()));
                }
            }
        }
    }

    public List<ChunkPos> getAffectedChunks() {
        return affectedChunks;
    }

    public Set<BlockPos> getAffectedMicroChunks() {
        return affectedMicroChunks;
    }

    public boolean checkEnabled() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return checkEnabled(player.getEntityWorld(), player.posX, player.posY + player.getEyeHeight(), player.posZ);
    }

    public boolean checkEnabled(World world, double cameraX, double cameraY, double cameraZ) {
        boolean isEditingRegion = ClientOptiCubeEditingService.getInstance().isEditingRegion(world, pos, OptiCubeRegionType.AFFECTED_REGION);
        boolean newEnabled = !isEditingRegion && (radius == -1 || !region.intersects(cameraX, cameraY, cameraZ, radius));
        boolean updated = enabled != newEnabled;
        this.enabled = newEnabled;
        return updated;
    }
}
