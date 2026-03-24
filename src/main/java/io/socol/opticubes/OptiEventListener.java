package io.socol.opticubes;

import io.socol.opticubes.service.editing.OptiCubeEditingService;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class OptiEventListener {
    @SubscribeEvent
    public static void onPlayerChangeWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        OptiCubeEditingService editingService = OptiCubes.getEditingService();
        if(editingService == null) return;

        editingService.onServerPlayerChangedDimension((EntityPlayerMP) event.player);

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        OptiCubeEditingService editingService = OptiCubes.getEditingService();

        editingService.onServerPlayerLogout((EntityPlayerMP) event.player);
    }
}
