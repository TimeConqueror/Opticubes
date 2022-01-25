package io.socol.opticubes.network.clientbound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeRegionType;
import io.socol.opticubes.utils.ProtoUtils;
import io.socol.opticubes.utils.pos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
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
        optiCubePos = ProtoUtils.readBlockPos(buf);
        regionType = OptiCubeRegionType.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProtoUtils.writeBlockPos(buf, optiCubePos);
        buf.writeInt(regionType.ordinal());
    }

    public static class Handler implements IMessageHandler<StartOptiCubeRegionEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StartOptiCubeRegionEditingMessage message, MessageContext ctx) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            if (world != null) {
                ClientOptiCubeEditingService.getInstance().startNewRegionEditingSession(message.optiCubePos, message.regionType, world);
            }
            return null;
        }
    }
}
