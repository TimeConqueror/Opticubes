package io.socol.opticubes.service.editing;

import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OptiCubeRegionEditingSession {

    private static final long MAX_DURATION = 20 * 60 * 5; // 5 min

    private final BlockPos optiCubePos;
    private final OptiCubeRegionType type;
    private final long startTime;

    public OptiCubeRegionEditingSession(BlockPos opiCubePos, OptiCubeRegionType type, long startTime) {
        this.optiCubePos = opiCubePos;
        this.type = type;
        this.startTime = startTime;
    }

    public BlockPos getOptiCubePos() {
        return optiCubePos;
    }

    public OptiCubeRegionType getType() {
        return type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void applyRegion(World world, @Nullable Region region) {
        region = Region.tryRecreate(region);
        if (region == null || !isValid(world)) {
            return;
        }

        TileEntity tile = world.getTileEntity(optiCubePos.x, optiCubePos.y, optiCubePos.z);
        if (tile instanceof TileEntityOptiCube) {
            if (type == OptiCubeRegionType.AFFECTED_REGION) {
                ((TileEntityOptiCube) tile).setAffectedRegion(region.asRelative(optiCubePos));
            }
        }
    }

    public boolean isValid(World world) {
        return world.getTotalWorldTime() - startTime < MAX_DURATION;
    }
}
