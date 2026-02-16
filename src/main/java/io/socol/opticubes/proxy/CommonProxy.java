package io.socol.opticubes.proxy;

import io.socol.opticubes.registry.*;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        OptiBlocks.register();
        OptiItems.register();
        OptiTiles.register();
        OptiNetwork.register();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public static void runOnMainThread(MessageContext ctx, Runnable runnable) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(runnable);
    }
}
