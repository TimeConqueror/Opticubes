package io.socol.opticubes.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.socol.opticubes.registry.*;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        OptiItems.register();
        OptiBlocks.register();
        OptiTiles.register();
        OptiNetwork.register();
    }

    public void init(FMLInitializationEvent event) {
        OptiRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
