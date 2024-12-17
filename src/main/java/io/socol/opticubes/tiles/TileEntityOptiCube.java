package io.socol.opticubes.tiles;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.utils.Mappings;
import io.socol.opticubes.utils.NBTUtils;
import io.socol.opticubes.utils.Region;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOptiCube extends TileEntity {

    private static final int DEFAULT_RADIUS = 16;

    private Region affectedRegion = Region.BLOCK;
    private int radius = DEFAULT_RADIUS;
    private long featuresMask = OptiFeature.DEFAULT_MASK;

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
        compound.setInteger("Radius", radius);
        compound.setLong("Features", featuresMask);
    }

    private void readCommon(NBTTagCompound compound) {
        affectedRegion = NBTUtils.getRegion(compound, "Region");
        radius = compound.hasKey("Radius") ? compound.getInteger("Radius") : DEFAULT_RADIUS;
        featuresMask = compound.hasKey("Features") ? compound.getLong("Features") : OptiFeature.DEFAULT_MASK;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeCommon(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if (Mappings.getNbtCompound(pkt) != null) {
            readCommon(Mappings.getNbtCompound(pkt));
        }
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiClientService().addOptiCube(this);
        }
    }

    @Override
    public void onChunkUnload() {
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiClientService().removeOptiCube(this);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (worldObj != null && worldObj.isRemote) {
            OptiCubes.getOptiClientService().removeOptiCube(this);
        }
    }

    public Region getAffectedRegion() {
        return affectedRegion;
    }

    public int getRadius() {
        return radius;
    }

    public long getFeaturesMask() {
        return featuresMask;
    }

    public void setAffectedRegion(Region affectedRegion) {
        this.affectedRegion = affectedRegion;
        updateData();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        updateData();
    }

    public void setFeaturesMask(long featureMask) {
        this.featuresMask = featureMask;
        updateData();
    }

    private void updateData() {
        if (worldObj != null && !worldObj.isRemote) {
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }
}
