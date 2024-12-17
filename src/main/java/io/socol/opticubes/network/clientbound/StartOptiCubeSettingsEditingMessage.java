package io.socol.opticubes.network.clientbound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.utils.ProtoUtils;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus.Internal;

//FIXME migrate to S2C | C2S
public class StartOptiCubeSettingsEditingMessage implements IMessage {

    private BlockPos optiCubePos;
    private long featuresMask;

    public StartOptiCubeSettingsEditingMessage(BlockPos optiCubePos, long featuresMask) {
        this.optiCubePos = optiCubePos;
        this.featuresMask = featuresMask;
    }

    @Internal
    public StartOptiCubeSettingsEditingMessage() {

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

    public static class Handler implements IMessageHandler<StartOptiCubeSettingsEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StartOptiCubeSettingsEditingMessage message, MessageContext ctx) {
            World world = ClientProxy.world();
            if (world != null) {
                ClientOptiCubeEditingService.getInstance().startSettingsEditingSession(
                    world, message.optiCubePos,  message.featuresMask
                );
            }
            return null;
        }
    }
}
