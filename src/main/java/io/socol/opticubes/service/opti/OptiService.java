package io.socol.opticubes.service.opti;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.items.ItemOptiWrench;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

public class OptiService {

    private final Map<BlockPos, OptiCube> optiCubes = new HashMap<>();

    private final OptiRegionMap regionMap = new OptiRegionMap();

    public OptiService() {
        FMLCommonHandler.instance().bus().register(new ForgeListener());
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public void addOptiCube(TileEntityOptiCube tile) {
        BlockPos optiCubePos = BlockPos.ofTile(tile);
        removeOptiCube(optiCubePos);

        OptiCube optiCube = new OptiCube(
                optiCubePos,
                tile.getAffectedRegion().move(optiCubePos),
                tile.getRadius()
        );
        optiCubes.put(optiCubePos, optiCube);
        regionMap.add(optiCube);
    }

    public void removeOptiCube(TileEntityOptiCube tile) {
        removeOptiCube(BlockPos.ofTile(tile));
    }

    public void removeOptiCube(BlockPos optiCubePos) {
        OptiCube optiCube = optiCubes.remove(optiCubePos);
        if (optiCube != null) {
            regionMap.remove(optiCube);
        }
    }

    public boolean skipTileRender(TileEntity tile) {
        if (tile instanceof TileEntityOptiCube) {
            return false;
        }
        return regionMap.contains(BlockPos.ofTile(tile));
    }

    public class ForgeListener {
        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent event) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null || event.phase != TickEvent.Phase.END) {
                return;
            }

            double cameraX = player.posX;
            double cameraY = player.posY + player.getEyeHeight();
            double cameraZ = player.posZ;

            for (OptiCube optiCube : optiCubes.values()) {
                optiCube.checkEnabled(player.getEntityWorld(), cameraX, cameraY, cameraZ);
            }
        }
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

            for (OptiCube optiCube : optiCubes.values()) {
                if (optiCube.getRegion().isInFrustum()) {
                    boolean editing = ClientOptiCubeEditingService.getInstance().isEditingRegion(
                            player.getEntityWorld(), optiCube.getPos(), OptiCubeRegionType.AFFECTED_REGION
                    );

                    if (editing) {
                        if (!optiCube.getRegion().equals(optiCube.getPos())) {
                            RegionRenderer.addRegion(new Region(optiCube.getPos()), 0xFF1CDD7A, 1 / 64f);
                        }
                        float time = player.ticksExisted + event.partialTicks;
                        float animation = MathHelper.sin((float) Math.toRadians(time * 20));
                        RegionRenderer.addRegion(optiCube.getRegion(), 0xFF1CDD7A, 1 / 16f + animation * 1 / 32f);
                    } else {
                        int color = optiCube.isEnabled() ? 0xFF808080 : 0xFF3590FF;
                        RegionRenderer.addRegion(optiCube.getRegion(), color, 1 / 32f);
                    }
                }
            }
        }
    }

    public OptiCube getOptiCube(BlockPos blockPos) {
        return optiCubes.get(blockPos);
    }
}
