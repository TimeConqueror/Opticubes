package io.socol.opticubes.network.serverbound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.ProtoUtils;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;

import static org.jetbrains.annotations.ApiStatus.Internal;

public class StopOptiCubeSettingsEditingMessage implements IMessage {

    private BlockPos optiCubePos;
    private long featuresMask;

    public StopOptiCubeSettingsEditingMessage(BlockPos optiCubePos, long featuresMask) {
        this.optiCubePos = optiCubePos;
        this.featuresMask = featuresMask;
    }

    @Internal
    public StopOptiCubeSettingsEditingMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        optiCubePos = ProtoUtils.readBlockPos(buf);
        featuresMask = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProtoUtils.writeBlockPos(buf, optiCubePos);
        buf.writeLong(featuresMask);
    }

    public static class Handler implements IMessageHandler<StopOptiCubeSettingsEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StopOptiCubeSettingsEditingMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            OptiCubes.getEditingService().stopSettingsEditingSession(player, message.optiCubePos, message.featuresMask);
            return null;
        }
    }
}
