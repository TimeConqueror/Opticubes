package io.socol.opticubes.proxy;

import io.socol.opticubes.OCClientCommand;
import io.socol.opticubes.fx.RegionRenderer;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.registry.OptiItems;
import io.socol.opticubes.registry.OptiTiles;
import io.socol.opticubes.utils.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.Nullable;

public class ClientProxy extends CommonProxy {
    @Nullable
    public static World world() {
        return Hacks.safeCast(Minecraft.getMinecraft().world);
    }

    @Nullable
    public static EntityPlayer player() {
        return Hacks.safeCast(Minecraft.getMinecraft().player);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        OptiBlocks.registerRenderers();
        OptiItems.registerRenderers();
        OptiTiles.registerRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(new RegionRenderer.EventListener());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        ClientCommandHandler.instance.registerCommand(new OCClientCommand());
    }

    public static void runOnMainThread(Runnable task) {
        Minecraft.getMinecraft().addScheduledTask(task);
    }
}
