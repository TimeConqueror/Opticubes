package io.socol.opticubes.utils;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.utils.pos.BlockPos;

public class ProtoUtils {

    public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
        buf.writeLong(pos.packToLong());
    }

    public static BlockPos readBlockPos(ByteBuf buf) {
        return BlockPos.fromPacked(buf.readLong());
    }

    public static void writeRegion(ByteBuf buf, Region region) {
        buf.writeLong(BlockPos.packToLong(region.x0, region.y0, region.z0));
        buf.writeLong(BlockPos.packToLong(region.x1, region.y1, region.z1));
    }

    public static Region readRegion(ByteBuf buf) {
        long pos0 = buf.readLong();
        long pos1 = buf.readLong();
        return new Region(
                BlockPos.unpackX(pos0),
                BlockPos.unpackY(pos0),
                BlockPos.unpackZ(pos0),
                BlockPos.unpackX(pos1),
                BlockPos.unpackY(pos1),
                BlockPos.unpackZ(pos1)
        );
    }
}
