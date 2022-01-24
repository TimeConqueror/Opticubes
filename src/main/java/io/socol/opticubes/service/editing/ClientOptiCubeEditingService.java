package io.socol.opticubes.service.editing;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.network.serverbound.StopOptiCubeRegionEditingMessage;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

public class ClientOptiCubeEditingService extends OptiCubeEditingService {

    private OptiCubeRegionEditingSession currentRegionEditingSession = null;
    private BlockPos firstRegionPoint;

    public ClientOptiCubeEditingService() {
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    @Nullable
    public OptiCubeRegionEditingSession getCurrentRegionEditingSession() {
        return currentRegionEditingSession;
    }

    public void startNewRegionEditingSession(BlockPos opiCubePos, OptiCubeRegionType type, World world) {
        this.currentRegionEditingSession = new OptiCubeRegionEditingSession(opiCubePos, type, world.getTotalWorldTime());
        this.firstRegionPoint = null;
    }

    public void stopRegionEditingSession(@Nullable Region region) {
        currentRegionEditingSession = null;
        firstRegionPoint = null;
        OptiNetwork.NETWORK.sendToServer(new StopOptiCubeRegionEditingMessage(region));
    }

    public boolean isEditingRegion(World world, BlockPos optiCubePos, OptiCubeRegionType regionType) {
        return currentRegionEditingSession != null &&
                currentRegionEditingSession.getType() == regionType &&
                currentRegionEditingSession.getOpiCubePos().equals(optiCubePos) &&
                currentRegionEditingSession.isValid(world);
    }

    public boolean isEditingRegion() {
        return currentRegionEditingSession != null;
    }

    public BlockPos getFirstRegionPoint() {
        return firstRegionPoint;
    }

    public void addRegionPoint(BlockPos pos) {
        if (firstRegionPoint == null) {
            firstRegionPoint = pos;
            return;
        }
        stopRegionEditingSession(new Region(firstRegionPoint, pos));
    }

    public static ClientOptiCubeEditingService getInstance() {
        return (ClientOptiCubeEditingService) OptiCubes.getEditingService();
    }

    public class EventListener {
        @SubscribeEvent
        public void onRender(RenderWorldLastEvent event) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                return;
            }

            ItemStack held = player.getHeldItem();
            if (!ItemOptiWrench.isOptiWrench(held)) {
                return;
            }

            if (firstRegionPoint != null) {


                MovingObjectPosition hitResult = Minecraft.getMinecraft().objectMouseOver;
                if (hitResult != null && hitResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos secondRegionPoint = new BlockPos(hitResult.blockX, hitResult.blockY, hitResult.blockZ);

                    RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFFFF9138, 1 / 32f);
                    RegionRenderer.addRegion(new Region(secondRegionPoint), 0xFF3590FF, 1 / 32f);
                    RegionRenderer.addRegion(new Region(firstRegionPoint, secondRegionPoint), 0xFFFFFFFF, 1 / 48f);
                } else {
                    RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFF3590FF, 1 / 32f);
                }
            }
        }
    }
}
