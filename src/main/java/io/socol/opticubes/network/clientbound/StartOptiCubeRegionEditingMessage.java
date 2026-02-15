package io.socol.opticubes.network.clientbound;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.ApiStatus.Internal;

//FIXME migrate to S2C | C2S
public class StartOptiCubeRegionEditingMessage implements IMessage {

    private BlockPos optiCubePos;
    private OptiCubeRegionType regionType;

    public StartOptiCubeRegionEditingMessage(BlockPos optiCubePos, OptiCubeRegionType regionType) {
        this.optiCubePos = optiCubePos;
        this.regionType = regionType;
    }

    @Internal
    public StartOptiCubeRegionEditingMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        optiCubePos = packetBuffer.readBlockPos();
        regionType = OptiCubeRegionType.values()[packetBuffer.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeBlockPos(optiCubePos);
        packetBuffer.writeInt(regionType.ordinal());
    }

    public static class Handler implements IMessageHandler<StartOptiCubeRegionEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StartOptiCubeRegionEditingMessage message, MessageContext ctx) {
            World world = ClientProxy.world();
            if (world != null) {
                ClientOptiCubeEditingService.getInstance().startNewRegionEditingSession(message.optiCubePos, message.regionType, world);
            }
            return null;
        }
    }
}
