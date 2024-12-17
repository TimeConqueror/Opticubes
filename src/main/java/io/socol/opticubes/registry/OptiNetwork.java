package io.socol.opticubes.registry;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.network.clientbound.ResetOptiCubeEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeSettingsEditingMessage;
import io.socol.opticubes.network.serverbound.SetOptiCubeRadiusMessage;
import io.socol.opticubes.network.serverbound.StopOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.serverbound.StopOptiCubeSettingsEditingMessage;

public class OptiNetwork {

    private static int lastMessageId = -1;

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(OptiCubes.MODID);

    public static void register() {
        registerMessage(new StartOptiCubeRegionEditingMessage.Handler(), StartOptiCubeRegionEditingMessage.class, Side.CLIENT);
        registerMessage(new ResetOptiCubeEditingMessage.Handler(), ResetOptiCubeEditingMessage.class, Side.CLIENT);
        registerMessage(new StartOptiCubeSettingsEditingMessage.Handler(), StartOptiCubeSettingsEditingMessage.class, Side.CLIENT);
        registerMessage(new StopOptiCubeRegionEditingMessage.Handler(), StopOptiCubeRegionEditingMessage.class, Side.SERVER);
        registerMessage(new SetOptiCubeRadiusMessage.Handler(), SetOptiCubeRadiusMessage.class, Side.SERVER);
        registerMessage(new StopOptiCubeSettingsEditingMessage.Handler(), StopOptiCubeSettingsEditingMessage.class, Side.SERVER);
    }

    public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestMessageType, Side side) {
        NETWORK.registerMessage(messageHandler, requestMessageType, ++lastMessageId, side);
    }
}
