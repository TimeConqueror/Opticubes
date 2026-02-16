package io.socol.opticubes.network.serverbound;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static org.jetbrains.annotations.ApiStatus.Internal;

public class SetOptiCubeRadiusMessage implements IMessage {

    private BlockPos optiCubePos;
    private int radius;

    public SetOptiCubeRadiusMessage(BlockPos optiCubePos, int radius) {
        this.optiCubePos = optiCubePos;
        this.radius = radius;
    }

    @Internal
    public SetOptiCubeRadiusMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        optiCubePos = packetBuffer.readBlockPos();
        radius = packetBuffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeBlockPos(optiCubePos);
        packetBuffer.writeInt(radius);
    }

    public static class Handler implements IMessageHandler<SetOptiCubeRadiusMessage, IMessage> {

        @Override
        public IMessage onMessage(SetOptiCubeRadiusMessage message, MessageContext ctx) {
            CommonProxy.runOnMainThread(ctx, () -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                OptiCubes.getEditingService().setOptiCubeRadius(player, message.optiCubePos, message.radius);
            });
            return null;
        }
    }
}
