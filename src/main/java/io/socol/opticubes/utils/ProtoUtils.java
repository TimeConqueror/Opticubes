package io.socol.opticubes.utils;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.utils.pos.BlockPos;

public class ProtoUtils {

    public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
        buf.writeLong(pos.asLong());
    }

    public static BlockPos readBlockPos(ByteBuf buf) {
        return BlockPos.of(buf.readLong());
    }

    public static void writeRegion(ByteBuf buf, Region region) {
        buf.writeLong(BlockPos.asLong(region.x0, region.y0, region.z0));
        buf.writeLong(BlockPos.asLong(region.x1, region.y1, region.z1));
    }

    public static Region readRegion(ByteBuf buf) {
        long pos0 = buf.readLong();
        long pos1 = buf.readLong();
        return new Region(
            BlockPos.getX(pos0),
            BlockPos.getY(pos0),
            BlockPos.getZ(pos0),
            BlockPos.getX(pos1),
            BlockPos.getY(pos1),
            BlockPos.getZ(pos1)
        );
    }
}
