package io.socol.opticubes.service.editing;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.fx.TextPanelRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.network.serverbound.StopOptiCubeRegionEditingMessage;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.service.opti.OptiCube;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

public class ClientOptiCubeEditingService extends OptiCubeEditingService {

    private OptiCubeRegionEditingSession currentRegionEditingSession = null;
    private BlockPos firstRegionPoint;

    private OptiCubeRadiusEditingSession currentRadiusEditingSession = null;
    private BlockPos radiusEditingOptiCube = null;

    public ClientOptiCubeEditingService() {
        FMLCommonHandler.instance().bus().register(new ForgeListener());
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public OptiCubeRegionEditingSession getCurrentRegionEditingSession() {
        return currentRegionEditingSession;
    }

    public void startNewRegionEditingSession(BlockPos optiCubePos, OptiCubeRegionType type, World world) {
        this.currentRegionEditingSession = new OptiCubeRegionEditingSession(optiCubePos, type, world.getTotalWorldTime());
        this.firstRegionPoint = null;
    }

    public void stopRegionEditingSession(@Nullable Region region) {
        currentRegionEditingSession = null;
        firstRegionPoint = null;
        OptiNetwork.NETWORK.sendToServer(new StopOptiCubeRegionEditingMessage(region));
    }

    public boolean isEditingRegion(World world, BlockPos optiCubePos, OptiCubeRegionType regionType) {
        return currentRegionEditingSession != null && currentRegionEditingSession.getType() == regionType && currentRegionEditingSession.getOptiCubePos().equals(optiCubePos) && currentRegionEditingSession.isValid(world);
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

        Region region = new Region(firstRegionPoint, pos);

        if (currentRegionEditingSession.validateRegion(ClientProxy.player(), region, true)) {
            stopRegionEditingSession(region);
        }
    }

    public static ClientOptiCubeEditingService getInstance() {
        return (ClientOptiCubeEditingService) OptiCubes.getEditingService();
    }

    private void stopRadiusEditingSession() {
        if (currentRadiusEditingSession != null) {
            currentRadiusEditingSession.commit();
            currentRadiusEditingSession = null;
        }
    }

    public OptiCubeRadiusEditingSession getCurrentRadiusEditingSession() {
        return currentRadiusEditingSession;
    }

    private OptiCube checkRadiusEditingSession(EntityClientPlayerMP player) {
        ItemStack held = player.getHeldItem();
        if (!ItemOptiWrench.isOptiWrench(held)) {
            return null;
        }
        World world = player.getEntityWorld();

        MovingObjectPosition hitResult = Minecraft.getMinecraft().objectMouseOver;
        boolean isBlockSelected = hitResult != null && hitResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;

        if (isBlockSelected && world.getBlock(hitResult.blockX, hitResult.blockY, hitResult.blockZ) == OptiBlocks.OPTICUBE) {
            BlockPos blockPos = new BlockPos(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            return OptiCubes.getOptiService().getOptiCube(blockPos);
        }
        return null;
    }

    public boolean onWheelScroll(int i) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return false;
        }
        OptiCube optiCube = checkRadiusEditingSession(player);
        if (optiCube == null) {
            return false;
        }

        if (currentRadiusEditingSession != null) {
            if (!currentRadiusEditingSession.getOptiCubePos().equals(optiCube.getPos())) {
                stopRadiusEditingSession();
            }
        }
        if (currentRadiusEditingSession == null) {
            currentRadiusEditingSession = new OptiCubeRadiusEditingSession(
                    optiCube.getPos(), optiCube.getRadius(),
                    player.ticksExisted
            );
        }
        currentRadiusEditingSession.modifyRadius(Integer.compare(i, 0));
        return true;
    }

    public void reset() {
        currentRegionEditingSession = null;
        currentRadiusEditingSession = null;
        firstRegionPoint = null;
    }

    public class ForgeListener {
        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent event) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null || event.phase != TickEvent.Phase.END) {
                return;
            }
            if (currentRadiusEditingSession != null) {
                OptiCube optiCube = checkRadiusEditingSession(player);
                if (optiCube == null) {
                    stopRadiusEditingSession();
                } else {
                    currentRadiusEditingSession.update(player.ticksExisted);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
            reset();
            // reset all player on integrated server due to host leave (does nothing if player left from dedicated server)
            resetAllPlayers();
        }
    }

    public BlockPos getRadiusEditingOptiCube() {
        return radiusEditingOptiCube;
    }

    public class EventListener {
        @SubscribeEvent
        public void onRender(RenderWorldLastEvent event) {
            radiusEditingOptiCube = null;
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                return;
            }

            ItemStack held = player.getHeldItem();
            if (!ItemOptiWrench.isOptiWrench(held)) {
                return;
            }

            MovingObjectPosition hitResult = Minecraft.getMinecraft().objectMouseOver;
            boolean isBlockSelected = hitResult != null && hitResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;

            if (currentRegionEditingSession != null) {
                OptiCube optiCube = OptiCubes.getOptiService().getOptiCube(currentRegionEditingSession.getOptiCubePos());
                if (optiCube != null) {
                    if (optiCube.hasExternalRegion()) {
                        RegionRenderer.addRegion(new Region(optiCube.getPos()), 0xFF1CDD7A).inflate(1 / 256f).ignoreDepth();
                    }

                    float time = player.ticksExisted + event.partialTicks;
                    float animation = MathHelper.sin((float) Math.toRadians(time * 20));
                    RegionRenderer.addRegion(optiCube.getRegion(), 0xFF1CDD7A).inflate(1 / 16f + animation * 1 / 32f).ignoreDepth();
                }

                if (firstRegionPoint != null) {
                    if (isBlockSelected) {
                        BlockPos secondRegionPoint = new BlockPos(hitResult.blockX, hitResult.blockY, hitResult.blockZ);

                        RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFFFF9138).inflate(1 / 32f).ignoreDepth();
                        RegionRenderer.addRegion(new Region(secondRegionPoint), 0xFF3590FF).inflate(1 / 32f);

                        Region selectedRegion = new Region(firstRegionPoint, secondRegionPoint);
                        boolean regionValid = currentRegionEditingSession.validateRegion(player, selectedRegion, false);
                        RegionRenderer.addRegion(selectedRegion, regionValid ? 0xFFFFFFFF : 0xFFE52B50).inflate(1 / 256f).ignoreDepth();
                    } else {
                        RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFF3590FF).inflate(1 / 32f).ignoreDepth();
                    }
                }
            }

            if (isBlockSelected && player.getEntityWorld().getBlock(hitResult.blockX, hitResult.blockY, hitResult.blockZ) == OptiBlocks.OPTICUBE) {
                BlockPos blockPos = new BlockPos(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                OptiCube optiCube = OptiCubes.getOptiService().getOptiCube(blockPos);
                if (optiCube != null) {
                    radiusEditingOptiCube = optiCube.getPos();
                    int radius = optiCube.getRadius();
                    int time = player.ticksExisted;
                    if (currentRadiusEditingSession != null && currentRadiusEditingSession.getOptiCubePos().equals(blockPos)) {
                        radius = currentRadiusEditingSession.getRadius();
                        time -= currentRadiusEditingSession.getStartTime();
                    }

                    TextPanelRenderer.renderText(
                            new BlockPos(hitResult.blockX, hitResult.blockY, hitResult.blockZ),
                            radius == -1 ? "x" : Integer.toString(radius),
                            hitResult.sideHit, currentRadiusEditingSession != null,
                            time, event.partialTicks
                    );
                }
            }
        }
    }
}
