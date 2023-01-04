package io.socol.opticubes.service.editing;

import io.socol.opticubes.tiles.TileEntityOptiCube;
import io.socol.opticubes.utils.ChatComponentExt;
import io.socol.opticubes.utils.Region;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OptiCubeRegionEditingSession {
    private static final int MAX_REGION_SIZE = 48;
    private static final long MAX_DURATION = 20 * 60 * 5; // 5 min

    private final BlockPos optiCubePos;
    private final OptiCubeRegionType type;
    private final long startTime;

    public OptiCubeRegionEditingSession(BlockPos opiCubePos, OptiCubeRegionType type, long startTime) {
        this.optiCubePos = opiCubePos;
        this.type = type;
        this.startTime = startTime;
    }

    public BlockPos getOptiCubePos() {
        return optiCubePos;
    }

    public OptiCubeRegionType getType() {
        return type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void applyRegion(EntityPlayerMP player, World world, @Nullable Region region) {
        region = Region.tryRecreate(region);
        if (region == null || !isValid(world)) {
            return;
        }

        region = region.clampByWorld();

        if (!validateRegion(player, region, true)) {
            return;
        }

        TileEntity tile = world.getTileEntity(optiCubePos.getX(), optiCubePos.getY(), optiCubePos.getZ());
        if (tile instanceof TileEntityOptiCube) {
            if (type == OptiCubeRegionType.AFFECTED_REGION) {
                ((TileEntityOptiCube) tile).setAffectedRegion(region.asRelative(optiCubePos));
            }
        }
    }

    public boolean isValid(World world) {
        return world.getTotalWorldTime() - startTime < MAX_DURATION;
    }

    public boolean validateRegion(EntityPlayer player, Region region, boolean sendFeedback) {
        if (region.sizeX() > MAX_REGION_SIZE || region.sizeZ() > MAX_REGION_SIZE) {
            if (sendFeedback) {
                player.addChatMessage(ChatComponentExt.withColor(new ChatComponentTranslation("chat.opticubes.region_too_big",
                        region.sizeX(),
                        region.sizeY(),
                        region.sizeZ(),
                        MAX_REGION_SIZE,
                        256,
                        MAX_REGION_SIZE
                ), EnumChatFormatting.RED));
            }
            return false;
        }

        Region cubeCheckRegion = region.inflate(1).clampByWorld();
        if (!cubeCheckRegion.contains(optiCubePos)) {
            if (sendFeedback) {
                player.addChatMessage(ChatComponentExt.withColor(new ChatComponentTranslation("chat.opticubes.owner_cube_far_away"), EnumChatFormatting.RED));
            }
            return false;
        }

        return true;
    }
}
