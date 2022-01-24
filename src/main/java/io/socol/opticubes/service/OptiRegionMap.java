package io.socol.opticubes.service;

import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import io.socol.opticubes.utils.pos.ChunkPos;

import java.util.*;

public class OptiRegionMap {

    private final Map<ChunkPos, OptiChunk> chunks = new HashMap<>();

    public void add(OptiCube optiCube) {
        optiCube.setAffectedChunks(splitByChunks(optiCube));
    }

    public void drawRegions() {
        for (OptiChunk chunk : chunks.values()) {
            for (OptiRegion region : chunk.regions.values()) {
                RegionRenderer.addRegion(region.boundary, 0xFFFFFFFF, -1 / 16f);
            }
        }
        RegionRenderer.drawAll();
    }

    public void remove(OptiCube optiCube) {
        for (ChunkPos chunkPos : optiCube.getAffectedChunks()) {
            OptiChunk chunk = chunks.get(chunkPos);
            if (chunk != null) {
                chunk.regions.remove(optiCube.getPos());
                if (chunk.regions.isEmpty()) {
                    chunks.remove(chunkPos);
                }
            }
        }
        optiCube.setAffectedChunks(Collections.emptyList());
    }

    private List<ChunkPos> splitByChunks(OptiCube optiCube) {
        List<ChunkPos> affectedChunks = new ArrayList<>();
        Region region = optiCube.getRegion();

        int cx1 = region.x1 >> 4;
        int cz1 = region.z1 >> 4;

        for (int cx = region.x0 >> 4; cx <= cx1; cx++) {
            for (int cz = region.z0 >> 4; cz <= cz1; cz++) {
                Region clamped = region.clampInChunk(cx, cz);
                if (clamped != null) {
                    ChunkPos chunkPos = new ChunkPos(cx, cz);
                    affectedChunks.add(chunkPos);
                    addChunkRegion(chunkPos, new OptiRegion(optiCube, clamped));
                }
            }
        }

        return affectedChunks;
    }

    private void addChunkRegion(ChunkPos chunkPos, OptiRegion region) {
        OptiChunk chunk = chunks.get(chunkPos);
        if (chunk == null) {
            chunk = new OptiChunk();
            chunks.put(chunkPos, chunk);
        }
        chunk.regions.put(region.optiCube.getPos(), region);
    }

    public boolean contains(BlockPos pos) {
        OptiChunk chunk = chunks.get(pos.getChunkPos());
        if (chunk == null) {
            return false;
        }
        for (OptiRegion region : chunk.regions.values()) {
            if (region.optiCube.isEnabled() && region.boundary.contains(pos)) {
                return true;
            }
        }
        return false;
    }

    private static class OptiChunk {
        private final Map<BlockPos, OptiRegion> regions = new HashMap<>(); // (Opti-Cube pos) -> (chunked region part)
    }

    private static class OptiRegion {
        private final OptiCube optiCube;
        private final Region boundary;

        public OptiRegion(OptiCube optiCube, Region boundary) {
            this.optiCube = optiCube;
            this.boundary = boundary;
        }
    }
}
