package io.socol.opticubes;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import io.socol.opticubes.proxy.CommonProxy;
import io.socol.opticubes.service.OptiService;
import io.socol.opticubes.utils.Region;

@Mod(modid = OptiCubes.MODID, version = OptiCubes.VERSION, dependencies = "required-after:spongemixins@[1.1.0,);")
public class OptiCubes {
    public static final String MODID = "opticubes";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    @SidedProxy(clientSide = "io.socol.opticubes.proxy.ClientProxy", serverSide = "io.socol.opticubes.proxy.CommonProxy")
    public static CommonProxy proxy;

    private static OptiService service = null;

    public OptiCubes() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            service = new OptiService();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static OptiService getService() {
        return service;
    }

    public static boolean isClient() {
        return service != null;
    }
}
