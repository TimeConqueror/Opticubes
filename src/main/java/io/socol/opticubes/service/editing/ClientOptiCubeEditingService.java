package io.socol.opticubes.service.editing;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.fx.TextPanelRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.network.serverbound.StopOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.serverbound.StopOptiCubeSettingsEditingMessage;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.registry.OptiNetwork;
import io.socol.opticubes.screen.OptiCubeSettingsScreen;
import io.socol.opticubes.service.opti.OptiCube;
import io.socol.opticubes.utils.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ClientOptiCubeEditingService extends OptiCubeEditingService {

    private OptiCubeRegionEditingSession currentRegionEditingSession = null;
    private BlockPos firstRegionPoint;

    private OptiCubeRadiusEditingSession currentRadiusEditingSession = null;
    private BlockPos radiusEditingOptiCube = null;

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
        OptiNetwork.INSTANCE.sendToServer(new StopOptiCubeRegionEditingMessage(region));
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

    private OptiCube checkRadiusEditingSession(EntityPlayerSP player) {
        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!ItemOptiWrench.isOptiWrench(held)) {
            return null;
        }
        World world = player.getEntityWorld();

        RayTraceResult hitResult = Minecraft.getMinecraft().objectMouseOver;
        boolean isBlockSelected = hitResult != null && hitResult.typeOfHit == RayTraceResult.Type.BLOCK;

        if (isBlockSelected && world.getBlockState(hitResult.getBlockPos()).getBlock() == OptiBlocks.OPTICUBE) {
            BlockPos blockPos = new BlockPos(hitResult.getBlockPos());
            return OptiCubes.getOptiClientService().getOptiCube(blockPos);
        }
        return null;
    }

    public boolean onWheelScroll(int i) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
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

    public void startSettingsEditingSession(World world, BlockPos optiCubePos, long featuresMask) {
        OptiCubeSettingsEditingSession session = new OptiCubeSettingsEditingSession(
                optiCubePos,
                world.getTotalWorldTime(),
                featuresMask
        );
        Minecraft.getMinecraft().displayGuiScreen(new OptiCubeSettingsScreen(session));
    }

    public void stopSettingsEditingSession(BlockPos optiCubePos, long featuresMask) {
        OptiNetwork.INSTANCE.sendToServer(new StopOptiCubeSettingsEditingMessage(optiCubePos, featuresMask));
    }

    public void onClientTickEnd(EntityPlayerSP player) {
        if (currentRadiusEditingSession != null) {
            OptiCube optiCube = checkRadiusEditingSession(player);
            if (optiCube == null) {
                stopRadiusEditingSession();
            } else {
                currentRadiusEditingSession.update(player.ticksExisted);
            }
        }
        if (currentRegionEditingSession != null) {
            OptiCube optiCube = OptiCubes.getOptiClientService().getOptiCube(currentRegionEditingSession.getOptiCubePos());
            if (optiCube == null) {
                stopRegionEditingSession(null);
            }
        }
    }

    public void onDisconnectFromServer() {
        reset();
        // reset all player on integrated server due to host leave (does nothing if player left from dedicated server)
        resetAllPlayers();
    }

    public void render(float partialTick) {
        radiusEditingOptiCube = null;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }

        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!ItemOptiWrench.isOptiWrench(held)) {
            return;
        }

        RayTraceResult hitResult = Minecraft.getMinecraft().objectMouseOver;
        boolean isBlockSelected = hitResult != null && (hitResult.typeOfHit == RayTraceResult.Type.BLOCK || hitResult.typeOfHit == RayTraceResult.Type.MISS);

        if (currentRegionEditingSession != null) {
            OptiCube optiCube = OptiCubes.getOptiClientService().getOptiCube(currentRegionEditingSession.getOptiCubePos());
            if (optiCube != null) {
                if (optiCube.hasExternalRegion()) {
                    RegionRenderer.addRegion(new Region(optiCube.getPos()), 0xFF1CDD7A).inflate(1 / 256f).ignoreDepth();
                }

                float time = player.ticksExisted + partialTick;
                float animation = MathHelper.sin((float) Math.toRadians(time * 20));
                RegionRenderer.addRegion(optiCube.getRegion(), 0xFF1CDD7A).inflate(1 / 16f + animation * 1 / 32f).ignoreDepth().withSides();
            }

            if (firstRegionPoint != null) {
                if (isBlockSelected) {
                    BlockPos secondRegionPoint = hitResult.getBlockPos();

                    RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFFFF9138).inflate(1 / 32f).ignoreDepth();
                    RegionRenderer.addRegion(new Region(secondRegionPoint), 0xFF3590FF).inflate(1 / 32f);

                    Region selectedRegion = new Region(firstRegionPoint, secondRegionPoint);
                    boolean regionValid = currentRegionEditingSession.validateRegion(player, selectedRegion, false);
                    RegionRenderer.addRegion(selectedRegion, regionValid ? 0xFFFFFFFF : 0xFFE52B50).inflate(1 / 256f).ignoreDepth().withSides();
                } else {
                    RegionRenderer.addRegion(new Region(firstRegionPoint), 0xFF3590FF).inflate(1 / 32f).ignoreDepth();
                }
            }
        }

        if (isBlockSelected && player.getEntityWorld().getBlockState(hitResult.getBlockPos()).getBlock() == OptiBlocks.OPTICUBE) {
            BlockPos blockPos = hitResult.getBlockPos();
            OptiCube optiCube = OptiCubes.getOptiClientService().getOptiCube(blockPos);
            if (optiCube != null) {
                radiusEditingOptiCube = optiCube.getPos();
                int radius = optiCube.getRadius();
                int time = player.ticksExisted;
                if (currentRadiusEditingSession != null && currentRadiusEditingSession.getOptiCubePos().equals(blockPos)) {
                    radius = currentRadiusEditingSession.getRadius();
                    time -= currentRadiusEditingSession.getStartTime();
                }

                TextPanelRenderer.renderText(hitResult.getBlockPos(),
                        radius == -1 ? "x" : Integer.toString(radius),
                        hitResult.sideHit, currentRadiusEditingSession != null,
                        time, partialTick
                );
            }
        }
    }

    public BlockPos getRadiusEditingOptiCube() {
        return radiusEditingOptiCube;
    }
}
