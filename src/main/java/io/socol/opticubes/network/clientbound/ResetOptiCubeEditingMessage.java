package io.socol.opticubes.network.clientbound;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ResetOptiCubeEditingMessage implements IMessage {

    @Internal
    public ResetOptiCubeEditingMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<ResetOptiCubeEditingMessage, IMessage> {

        @Override
        public IMessage onMessage(ResetOptiCubeEditingMessage message, MessageContext ctx) {
            ClientOptiCubeEditingService.getInstance().reset();
            return null;
        }
    }
}
