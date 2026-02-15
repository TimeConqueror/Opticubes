package io.socol.opticubes.network.serverbound;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.OptiCubes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
        PacketBuffer packetBuffer = new PacketBuffer(buf);

        optiCubePos = packetBuffer.readBlockPos();
        featuresMask = packetBuffer.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeBlockPos(optiCubePos);
        packetBuffer.writeLong(featuresMask);
    }

    public static class Handler implements IMessageHandler<StopOptiCubeSettingsEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StopOptiCubeSettingsEditingMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            OptiCubes.getEditingService().stopSettingsEditingSession(player, message.optiCubePos, message.featuresMask);
            return null;
        }
    }
}
