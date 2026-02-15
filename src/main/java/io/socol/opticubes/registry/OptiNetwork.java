package io.socol.opticubes.registry;

import io.socol.opticubes.OptiCubes;
import io.socol.opticubes.network.clientbound.ResetOptiCubeEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.clientbound.StartOptiCubeSettingsEditingMessage;
import io.socol.opticubes.network.serverbound.SetOptiCubeRadiusMessage;
import io.socol.opticubes.network.serverbound.StopOptiCubeRegionEditingMessage;
import io.socol.opticubes.network.serverbound.StopOptiCubeSettingsEditingMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class OptiNetwork {
    public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(OptiCubes.MODID);
    private static int ID;

    static {
        ID = 0;
    }

    private static int nextID() {
        return ID++;
    }

    public static void register() {
        INSTANCE.registerMessage(StartOptiCubeRegionEditingMessage.Handler.class, StartOptiCubeRegionEditingMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(ResetOptiCubeEditingMessage.Handler.class, ResetOptiCubeEditingMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(StartOptiCubeSettingsEditingMessage.Handler.class, StartOptiCubeSettingsEditingMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(StopOptiCubeRegionEditingMessage.Handler.class, StopOptiCubeRegionEditingMessage.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(SetOptiCubeRadiusMessage.Handler.class, SetOptiCubeRadiusMessage.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(StopOptiCubeSettingsEditingMessage.Handler.class, StopOptiCubeSettingsEditingMessage.class, nextID(), Side.SERVER);
    }
}
