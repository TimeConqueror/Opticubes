package io.socol.opticubes.service.opti;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OptiService {

    private final Map<BlockPos, OptiCube> optiCubes = new HashMap<>();

    private final OptiRegionMap regionMap = new OptiRegionMap();

    public OptiService() {
        FMLCommonHandler.instance().bus().register(new ForgeListener());
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public void addOptiCube(TileEntityOptiCube tile) {
        BlockPos optiCubePos = BlockPos.of(tile);
        OptiCube prevOptiCube = removeOptiCubeInternal(optiCubePos);

        OptiCube optiCube = new OptiCube(
                optiCubePos,
                tile.getAffectedRegion().move(optiCubePos),
                tile.getRadius()
        );
        optiCube.checkEnabled();
        optiCubes.put(optiCubePos, optiCube);
        regionMap.add(optiCube);

        onOptiCubeUpdate(prevOptiCube, optiCube);
    }

    public void removeOptiCube(TileEntityOptiCube tile) {
        OptiCube prevOptiCube = removeOptiCubeInternal(BlockPos.of(tile));
        onOptiCubeUpdate(prevOptiCube, null);
    }

    private OptiCube removeOptiCubeInternal(BlockPos optiCubePos) {
        OptiCube optiCube = optiCubes.remove(optiCubePos);
        if (optiCube != null) {
            regionMap.remove(optiCube);
        }
        return optiCube;
    }

    private void onOptiCubeUpdate(@Nullable OptiCube prevOptiCube, @Nullable OptiCube newOptiCube) {
        if (prevOptiCube == null && newOptiCube == null) {
            return;
        }

        if (newOptiCube != null) {
            if (prevOptiCube != null) {
                newOptiCube.setColor(prevOptiCube.getColor());
            } else {
                newOptiCube.assignColor();
            }
        }

        boolean prevEnabled = prevOptiCube != null && prevOptiCube.isEnabled();
        boolean newEnabled = newOptiCube != null && newOptiCube.isEnabled();
        Set<BlockPos> prevAffectedChunks = prevOptiCube == null ? Collections.emptySet() : prevOptiCube.getAffectedMicroChunks();
        Set<BlockPos> newAffectedChunks = newOptiCube == null ? Collections.emptySet() : newOptiCube.getAffectedMicroChunks();

        Set<BlockPos> blocksToUpdate = new HashSet<>();

        // should update all prev affected chunks which are not affected by new opti-cube
        if (prevEnabled) {
            for (BlockPos prevChunk : prevAffectedChunks) {
                if (!newAffectedChunks.contains(prevChunk)) {
                    blocksToUpdate.add(prevChunk);
                }
            }
        }

        // should update all new affected chunks which are not affected by old opti-cube
        // and also should update common affected chunks if enabled state changed
        if (newEnabled || prevEnabled) {
            for (BlockPos newChunk : newAffectedChunks) {
                if (!prevAffectedChunks.contains(newChunk)) {
                    if (newEnabled) {
                        blocksToUpdate.add(newChunk);
                    }
                } else {
                    if (newEnabled != prevEnabled) {
                        blocksToUpdate.add(newChunk);
                    }
                }
            }
        }

        for (BlockPos pos : blocksToUpdate) {
            Minecraft.getMinecraft().renderGlobal.markBlocksForUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public boolean skipTileRender(TileEntity tile) {
        if (tile instanceof TileEntityOptiCube) {
            return false;
        }
        return regionMap.contains(BlockPos.of(tile));
    }

    public boolean skipBlockRender(Block block, BlockPos pos) {
        return (!block.isFullBlock() || !block.isOpaqueCube()) && regionMap.contains(pos);
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

            Set<BlockPos> blocksToUpdate = new HashSet<>();

            for (OptiCube optiCube : optiCubes.values()) {
                if (optiCube.checkEnabled(player.getEntityWorld(), cameraX, cameraY, cameraZ)) {
                    blocksToUpdate.addAll(optiCube.getAffectedMicroChunks());
                }
            }

            for (BlockPos pos : blocksToUpdate) {
                Minecraft.getMinecraft().renderGlobal.markBlocksForUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    public Map<BlockPos, OptiCube> getOptiCubes() {
        return optiCubes;
    }

    public class EventListener {
        @SubscribeEvent
        public void onRender(RenderWorldLastEvent event) {
            OptiServiceRenderer.render(OptiService.this, event.partialTicks);
        }
    }

    public OptiCube getOptiCube(BlockPos blockPos) {
        return optiCubes.get(blockPos);
    }
}
