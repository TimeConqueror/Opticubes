package io.socol.opticubes.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {

    public static void setRegion(NBTTagCompound tag, String key, Region region) {
        NBTTagCompound boxTag = new NBTTagCompound();
        boxTag.setInteger("X0", region.x0);
        boxTag.setInteger("Y0", region.y0);
        boxTag.setInteger("Z0", region.z0);
        boxTag.setInteger("X1", region.x1);
        boxTag.setInteger("Y1", region.y1);
        boxTag.setInteger("Z1", region.z1);
        tag.setTag(key, boxTag);
    }

    public static Region getRegion(NBTTagCompound tag, String key) {
        NBTTagCompound boxTag = tag.getCompoundTag(key);
        return new Region(
                boxTag.getInteger("X0"),
                boxTag.getInteger("Y0"),
                boxTag.getInteger("Z0"),
                boxTag.getInteger("X1"),
                boxTag.getInteger("Y1"),
                boxTag.getInteger("Z1")
        );
    }
}
