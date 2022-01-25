package io.socol.opticubes.service.editing;

import io.socol.opticubes.network.serverbound.SetOptiCubeRadiusMessage;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.util.MathHelper;

public class OptiCubeRadiusEditingSession {

    private static final int SYNC_INTERVAL = 10;

    private final BlockPos optiCubePos;
    private int radius;
    private final int startTime;

    private int lastSyncRadius;
    private int lastSyncTime;

    public OptiCubeRadiusEditingSession(BlockPos optiCubePos, int radius, int startTime) {
        this.optiCubePos = optiCubePos;
        this.radius = radius;
        this.startTime = startTime;
        this.lastSyncRadius = radius;
        this.lastSyncTime = startTime;
    }

    public BlockPos getOptiCubePos() {
        return optiCubePos;
    }

    public int getRadius() {
        return radius;
    }

    public void modifyRadius(int delta) {
        radius = MathHelper.clamp_int(radius + delta, 0, 64);
    }

    public void commit() {
        if (radius != lastSyncRadius) {
            lastSyncRadius = radius;
            OptiNetwork.NETWORK.sendToServer(new SetOptiCubeRadiusMessage(optiCubePos, radius));
        }
    }

    public int getStartTime() {
        return startTime;
    }

    public void update(int currentTime) {
        if (currentTime - lastSyncTime > SYNC_INTERVAL) {
            commit();
            lastSyncTime = currentTime;
        }
    }
}
