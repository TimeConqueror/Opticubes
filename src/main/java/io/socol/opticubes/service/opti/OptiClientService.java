package io.socol.opticubes.service.opti;

import io.socol.opticubes.OCConfigs;
import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.mixins.access.RenderGlobalExt;
import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.BlockPosExt;
import io.socol.opticubes.utils.ClientMixinAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OptiClientService {

    private final Map<BlockPos, OptiCube> optiCubes = new HashMap<>();

    private final OptiRegionMap regionMap = new OptiRegionMap();

    public OptiClientService() {
        MinecraftForge.EVENT_BUS.register(new ForgeListener());
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public void addOptiCube(TileEntityOptiCube tile) {
        BlockPos optiCubePos = tile.getPos();
        OptiCube prevOptiCube = removeOptiCubeInternal(optiCubePos);

        OptiCube optiCube = new OptiCube(
            optiCubePos,
            tile.getAffectedRegion().move(optiCubePos),
            tile.getRadius(),
            tile.getFeaturesMask()
        );
        optiCube.checkEnabled();
        optiCubes.put(optiCubePos, optiCube);
        regionMap.add(optiCube);

        onOptiCubeUpdate(prevOptiCube, optiCube);
    }

    public void removeOptiCube(TileEntityOptiCube tile) {
        OptiCube prevOptiCube = removeOptiCubeInternal(tile.getPos());
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
        Set<BlockPos> prevAffectedChunks = prevOptiCube == null || !prevOptiCube.isFeatureEnabled(OptiFeature.HIDE_SPECIAL_BLOCKS)
            ? Collections.emptySet()
            : prevOptiCube.getAffectedMicroChunks();
        Set<BlockPos> newAffectedChunks = newOptiCube == null || !newOptiCube.isFeatureEnabled(OptiFeature.HIDE_SPECIAL_BLOCKS)
            ? Collections.emptySet()
            : newOptiCube.getAffectedMicroChunks();

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
            RenderGlobalExt renderGlobal = ClientMixinAccessor.get(Minecraft.getMinecraft().renderGlobal);
            renderGlobal.callMarkBlocksForUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ(), true);
        }
    }

    private void clearOptiCubes() {
        optiCubes.clear();
        regionMap.clear();
    }

    public boolean skipTileRender(TileEntity tile) {
        if (tile instanceof TileEntityOptiCube) {
            return false;
        }

        //FIXME migrate to resourcelocation?
        if (OCConfigs.skipOptiForTile(tile.getClass())) {
            return false;
        }

        return regionMap.contains(tile.getPos(), OptiFeature.HIDE_TILES);
    }

    public boolean skipParticleSpawn(Particle particle) {
        return skipParticleSpawn(BlockPosExt.of(particle));
    }

    public boolean skipParticleSpawn(BlockPos particlePos) {
        return regionMap.contains(particlePos, OptiFeature.HIDE_PARTICLES);
    }

    public boolean skipEntityRender(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return false;
        }
        if (entity instanceof EntityItem) {
            return regionMap.contains(BlockPosExt.of(entity), OptiFeature.HIDE_DROPPED_ITEMS);
        }
        return regionMap.contains(BlockPosExt.of(entity), OptiFeature.HIDE_ENTITIES);
    }

    public boolean skipSpecialBlockRender(IBlockState state, BlockPos pos, IBakedModel model) {
        if(state.isFullBlock()) {
            return false;
        }

        if(state.isOpaqueCube()) {
            return false;
        }

        if(model.isBuiltInRenderer()) {
            return false;
        }

        return regionMap.contains(pos, OptiFeature.HIDE_SPECIAL_BLOCKS);
    }

    public class ForgeListener {
        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent event) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player == null || event.phase != TickEvent.Phase.END) {
                return;
            }

            double cameraX = player.posX;
            double cameraY = player.posY + player.getEyeHeight();
            double cameraZ = player.posZ;

            Set<BlockPos> blocksToUpdate = new HashSet<>();

            for (OptiCube optiCube : optiCubes.values()) {
                if (optiCube.checkEnabled(player.getEntityWorld(), cameraX, cameraY, cameraZ)) {
                    if (optiCube.isFeatureEnabled(OptiFeature.HIDE_SPECIAL_BLOCKS)) {
                        blocksToUpdate.addAll(optiCube.getAffectedMicroChunks());
                    }
                }
            }

            //FIXME if we should really set markBlocksForUpdate every tick?
            for (BlockPos pos : blocksToUpdate) {
                RenderGlobalExt renderGlobal = ClientMixinAccessor.get(Minecraft.getMinecraft().renderGlobal);
                renderGlobal.callMarkBlocksForUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ(), true);
            }
        }

        @SubscribeEvent
        public void onPlayerChangeWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
            clearOptiCubes();
        }

        @SubscribeEvent
        public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            clearOptiCubes();
        }
    }

    public Map<BlockPos, OptiCube> getOptiCubes() {
        return optiCubes;
    }

    public class EventListener {
        @SubscribeEvent
        public void onRender(RenderWorldLastEvent event) {
            OptiServiceRenderer.render(OptiClientService.this, event.getPartialTicks());
        }
    }

    public OptiCube getOptiCube(BlockPos blockPos) {
        return optiCubes.get(blockPos);
    }
}
