package io.socol.opticubes.network.clientbound;

import io.netty.buffer.ByteBuf;
import io.socol.opticubes.proxy.ClientProxy;
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
            ClientProxy.runOnMainThread(() -> {
                ClientOptiCubeEditingService.getInstance().reset();
            });
            return null;
        }
    }
}
