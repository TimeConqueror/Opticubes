package io.socol.opticubes.utils;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.utils.pos.BlockPos;

public class ProtoUtils {

    public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
        buf.writeInt(pos.x);
        buf.writeInt(pos.y);
        buf.writeInt(pos.z);
    }

    public static BlockPos readBlockPos(ByteBuf buf) {
        return new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
    }

    public static void writeRegion(ByteBuf buf, Region region) {
        buf.writeInt(region.x0);
        buf.writeInt(region.y0);
        buf.writeInt(region.z0);
        buf.writeInt(region.x1);
        buf.writeInt(region.y1);
        buf.writeInt(region.z1);
    }

    public static Region readRegion(ByteBuf buf) {
        return new Region(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
    }
}
