package io.socol.opticubes.tiles;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.NBTUtils;
import io.socol.opticubes.utils.Region;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOptiCube extends TileEntity {

    private Region affectedRegion = Region.BLOCK;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCommon(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCommon(compound);
    }

    private void writeCommon(NBTTagCompound compound) {
        NBTUtils.setRegion(compound, "Region", affectedRegion);
    }

    private void readCommon(NBTTagCompound compound) {
        affectedRegion = NBTUtils.getRegion(compound, "Region");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeCommon(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if (pkt.getNbtCompound() != null) {
            readCommon(pkt.getNbtCompound());
        }
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiService().addOptiCube(this);
        }
    }

    @Override
    public void onChunkUnload() {
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiService().removeOptiCube(this);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiService().removeOptiCube(this);
        }
    }

    public Region getAffectedRegion() {
        return affectedRegion;
    }

    public double getRadius() {
        return 8;
    }

    public void setAffectedRegion(Region affectedRegion) {
        this.affectedRegion = affectedRegion;
        if (worldObj != null && !worldObj.isRemote) {
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }
}
