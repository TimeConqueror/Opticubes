package io.socol.opticubes.network.serverbound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.utils.ProtoUtils;
import io.socol.opticubes.utils.Region;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;

public class StopOptiCubeRegionEditingMessage implements IMessage {

    @Nullable
    private Region region = null;

    public StopOptiCubeRegionEditingMessage(@Nullable Region region) {
        this.region = region;
    }

    public StopOptiCubeRegionEditingMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            region = ProtoUtils.readRegion(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (region == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            ProtoUtils.writeRegion(buf, region);
        }
    }

    public static class Handler implements IMessageHandler<StopOptiCubeRegionEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(StopOptiCubeRegionEditingMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            OptiCubes.getEditingService().stopRegionEditingSession(player, message.region);
            return null;
        }
    }
}
