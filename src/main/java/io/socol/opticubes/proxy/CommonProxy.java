package io.socol.opticubes.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.socol.opticubes.registry.OptiBlocks;
import io.socol.opticubes.registry.OptiItems;
import io.socol.opticubes.registry.OptiTiles;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        OptiItems.register();
        OptiBlocks.register();
        OptiTiles.register();

//        baublesHere = Loader.isModLoaded("baubles");
//        ModConfig.init(event.getSuggestedConfigurationFile());
//        MinecraftForge.EVENT_BUS.register(new ModConfig());
//        ModItems.init();
//        ModBlocks.register();
//        ModTileEntities.register();
//        ModRecipes.init();
//        ModNetworkHandler.init();
//        NetworkRegistry.INSTANCE.registerGuiHandler(ImprovedBackpacks.INSTANCE, new ModGui());
    }

    public void init(FMLInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public void postInit(FMLPostInitializationEvent event) {
//        if (Loader.isModLoaded("ironbackpacks")) {
//            IronBackpacksIntegration.postInit();
//        }
    }
}
