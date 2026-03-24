package io.socol.opticubes.service.editing;

import io.socol.opticubes.network.clientbound.ResetOptiCubeEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeSettingsEditingMessage;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.service.opti.OptiCube;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.Region;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OptiCubeEditingService {

    private final Map<UUID, OptiCubeRegionEditingSession> regionEditingSessions = new HashMap<>();
    private final Map<UUID, OptiCubeSettingsEditingSession> settingsEditingSessions = new HashMap<>();

    public void startRegionEditingSession(EntityPlayerMP player, BlockPos opiCubePos, OptiCubeRegionType type) {
        regionEditingSessions.put(player.getUniqueID(), new OptiCubeRegionEditingSession(
                opiCubePos,
                type,
                player.getServerWorld().getTotalWorldTime()
        ));
        OptiNetwork.INSTANCE.sendTo(new StartOptiCubeRegionEditingMessage(opiCubePos, type), player);
    }

    public void stopRegionEditingSession(EntityPlayerMP player, @Nullable Region region) {
        OptiCubeRegionEditingSession session = regionEditingSessions.remove(player.getUniqueID());
        if (session != null) {
            session.applyRegion(player, player.getEntityWorld(), region);
        }
    }

    public void stopSettingsEditingSession(EntityPlayerMP player) {
        settingsEditingSessions.remove(player.getUniqueID());
    }

    public void setOptiCubeRadius(EntityPlayerMP player, BlockPos optiCubePos, int radius) {
        radius = MathHelper.clamp(radius, OptiCube.MIN_RADIUS, OptiCube.MAX_RADIUS);
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos);
        if (tile instanceof TileEntityOptiCube) {
            ((TileEntityOptiCube) tile).setRadius(radius);
        }
    }

    public void stopSettingsEditingSession(EntityPlayerMP player, BlockPos optiCubePos, long featuresMask) {
        OptiCubeSettingsEditingSession session = settingsEditingSessions.get(player.getUniqueID());
        if (session == null || !session.getOptiCubePos().equals(optiCubePos)) {
            return;
        }
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos);
        if (tile instanceof TileEntityOptiCube) {
            ((TileEntityOptiCube) tile).setFeaturesMask(featuresMask);
        }
        settingsEditingSessions.remove(player.getUniqueID());
    }

    public void startSettingsEditingSession(EntityPlayerMP player, BlockPos optiCubePos) {
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos);
        if (tile instanceof TileEntityOptiCube) {
            TileEntityOptiCube optiCube = (TileEntityOptiCube) tile;
            settingsEditingSessions.put(player.getUniqueID(), new OptiCubeSettingsEditingSession(
                    optiCubePos,
                    player.getServerWorld().getTotalWorldTime(),
                    optiCube.getFeaturesMask()
            ));
            OptiNetwork.INSTANCE.sendTo(
                    new StartOptiCubeSettingsEditingMessage(
                            optiCubePos,
                            optiCube.getFeaturesMask()
                    ),
                    player
            );
        }

    }

    private void resetPlayer(EntityPlayer player) {
        regionEditingSessions.remove(player.getUniqueID());
        settingsEditingSessions.remove(player.getUniqueID());
    }

    protected void resetAllPlayers() {
        regionEditingSessions.clear();
        settingsEditingSessions.clear();
    }

    public void onServerPlayerChangedDimension(EntityPlayerMP player) {
        resetPlayer(player);
        OptiNetwork.INSTANCE.sendTo(new ResetOptiCubeEditingMessage(), player);
    }

    public void onServerPlayerLogout(EntityPlayerMP player) {
        resetPlayer(player);
    }
}
