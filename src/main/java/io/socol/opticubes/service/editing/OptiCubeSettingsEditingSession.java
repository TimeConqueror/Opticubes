package io.socol.opticubes.service.editing;

import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OptiCubeSettingsEditingSession {

    private static final long MAX_DURATION = 20 * 60 * 5; // 5 min

    private final BlockPos optiCubePos;
    private final long startTime;
    private long featuresMask;

    public OptiCubeSettingsEditingSession(BlockPos optiCubePos, long startTime, long featuresMask) {
        this.optiCubePos = optiCubePos;
        this.startTime = startTime;
        this.featuresMask = featuresMask;
    }

    public BlockPos getOptiCubePos() {
        return optiCubePos;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isFeatureEnabled(OptiFeature feature) {
        return feature.isEnabled(featuresMask);
    }

    public void toggleFeature(OptiFeature feature) {
        this.featuresMask = feature.toggleInMask(this.featuresMask);
    }

    public boolean isValid(World world) {
        TileEntity tileEntity = world.getTileEntity(optiCubePos.getX(), optiCubePos.getY(), optiCubePos.getZ());
        if (!(tileEntity instanceof TileEntityOptiCube)) {
            return false;
        }
        return world.getTotalWorldTime() - startTime < MAX_DURATION;
    }

    public long getFeaturesMask() {
        return featuresMask;
    }
}
