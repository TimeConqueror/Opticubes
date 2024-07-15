package io.socol.opticubes.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class Mappings {
    public static boolean isFullBlock(Block block) {
        return block.func_149730_j();
    }

    @SideOnly(Side.CLIENT)
    public static NBTTagCompound getNbtCompound(S35PacketUpdateTileEntity packet) {
        return packet.func_148857_g();
    }
}
