package io.socol.opticubes.network.clientbound;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
