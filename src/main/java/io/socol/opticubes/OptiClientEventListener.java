package io.socol.opticubes;

import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.opti.OptiServiceRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class OptiClientEventListener {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null || event.phase != TickEvent.Phase.END) {
            return;
        }

        OptiCubes.getOptiClientService().onClientTickEnd(player);
        getClientEditingService().onClientTickEnd(player);
    }

    @SubscribeEvent
    public static void onPlayerChangeWorld(WorldEvent.Load event) {
        if(!event.getWorld().isRemote) {
            return;
        }

        OptiCubes.getOptiClientService().clearOptiCubes();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        OptiCubes.getOptiClientService().clearOptiCubes();
        getClientEditingService().onDisconnectFromServer();
    }

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        OptiServiceRenderer.render(OptiCubes.getOptiClientService(), event.getPartialTicks());
        getClientEditingService().render(event.getPartialTicks());
    }

    private static ClientOptiCubeEditingService getClientEditingService() {
        return ((ClientOptiCubeEditingService) OptiCubes.getEditingService());
    }
}
