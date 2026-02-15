package io.socol.opticubes.tiles;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.OptiFeature;
import io.socol.opticubes.utils.NBTUtils;
import io.socol.opticubes.utils.Region;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.Nullable;

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
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCommon(compound);
        return compound;
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
    public @Nullable SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeCommon(compound);
        return new SPacketUpdateTileEntity(pos, -1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if (pkt.getNbtCompound() != null) {
            readCommon(pkt.getNbtCompound());
        }
        if (world != null && world.isRemote) {
            OptiCubes.getOptiClientService().addOptiCube(this);
        }
    }

    @Override
    public void onChunkUnload() {
        if (world != null && world.isRemote) {
            OptiCubes.getOptiClientService().removeOptiCube(this);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (world != null && world.isRemote) {
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
        if (world != null && !world.isRemote) {
            this.markDirty();
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }
}
