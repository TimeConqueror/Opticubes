package io.socol.opticubes.utils;

import io.netty.buffer.ByteBuf;

public class ProtoUtils {

    public static void writeRegion(ByteBuf buf, Region region) {
        buf.writeInt(region.x0);
        buf.writeInt(region.y0);
        buf.writeInt(region.z0);
        buf.writeInt(region.x1);
        buf.writeInt(region.y1);
        buf.writeInt(region.z1);
    }

    public static Region readRegion(ByteBuf buf) {
        long pos0 = buf.readLong();
        long pos1 = buf.readLong();
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
