package io.socol.opticubes.service.editing;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import io.socol.opticubes.network.clientbound.ResetOptiCubeEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeSettingsEditingMessage;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.service.opti.OptiCube;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OptiCubeEditingService {

    private final Map<UUID, OptiCubeRegionEditingSession> regionEditingSessions = new HashMap<>();
    private final Map<UUID, OptiCubeSettingsEditingSession> settingsEditingSessions = new HashMap<>();

    public OptiCubeEditingService() {
        FMLCommonHandler.instance().bus().register(new ForgeListener());
    }

    public void startRegionEditingSession(EntityPlayerMP player, BlockPos opiCubePos, OptiCubeRegionType type) {
        regionEditingSessions.put(player.getUniqueID(), new OptiCubeRegionEditingSession(
            opiCubePos,
            type,
            player.getServerForPlayer().getTotalWorldTime()
        ));
        OptiNetwork.NETWORK.sendTo(new StartOptiCubeRegionEditingMessage(opiCubePos, type), player);
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
        radius = MathHelper.clamp_int(radius, OptiCube.MIN_RADIUS, OptiCube.MAX_RADIUS);
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos.getX(), optiCubePos.getY(), optiCubePos.getZ());
        if (tile instanceof TileEntityOptiCube) {
            ((TileEntityOptiCube) tile).setRadius(radius);
        }
    }

    public void stopSettingsEditingSession(EntityPlayerMP player, BlockPos optiCubePos, long featuresMask) {
        OptiCubeSettingsEditingSession session = settingsEditingSessions.get(player.getUniqueID());
        if (session == null || !session.getOptiCubePos().equals(optiCubePos)) {
            return;
        }
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos.getX(), optiCubePos.getY(), optiCubePos.getZ());
        if (tile instanceof TileEntityOptiCube) {
            ((TileEntityOptiCube) tile).setFeaturesMask(featuresMask);
        }
        settingsEditingSessions.remove(player.getUniqueID());
    }

    public void startSettingsEditingSession(EntityPlayerMP player, BlockPos optiCubePos) {
        TileEntity tile = player.getEntityWorld().getTileEntity(optiCubePos.getX(), optiCubePos.getY(), optiCubePos.getZ());
        if (tile instanceof TileEntityOptiCube) {
            TileEntityOptiCube optiCube = (TileEntityOptiCube) tile;
            settingsEditingSessions.put(player.getUniqueID(), new OptiCubeSettingsEditingSession(
                optiCubePos,
                player.getServerForPlayer().getTotalWorldTime(),
                optiCube.getFeaturesMask()
            ));
            OptiNetwork.NETWORK.sendTo(
                new StartOptiCubeSettingsEditingMessage(
                    optiCubePos,
                    optiCube.getFeaturesMask()
                ),
                player
            );
        }

    }

    protected void resetPlayer(EntityPlayer player) {
        regionEditingSessions.remove(player.getUniqueID());
        settingsEditingSessions.remove(player.getUniqueID());
    }

    protected void resetAllPlayers() {
        regionEditingSessions.clear();
        settingsEditingSessions.clear();
    }

    public class ForgeListener {
        @SubscribeEvent
        public void onPlayerChangeWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
            resetPlayer(event.player);
            OptiNetwork.NETWORK.sendTo(new ResetOptiCubeEditingMessage(), (EntityPlayerMP) event.player);
        }

        @SubscribeEvent
        public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            resetPlayer(event.player);
        }
    }
}
