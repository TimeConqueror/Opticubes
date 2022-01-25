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
        optiCubePos = ProtoUtils.readBlockPos(buf);
        radius = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProtoUtils.writeBlockPos(buf, optiCubePos);
        buf.writeInt(radius);
    }

    public static class Handler implements IMessageHandler<SetOptiCubeRadiusMessage, IMessage> {

        @Override
        public IMessage onMessage(SetOptiCubeRadiusMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            OptiCubes.getEditingService().setOptiCubeRadius(player, message.optiCubePos, message.radius);
            return null;
        }
    }
}
