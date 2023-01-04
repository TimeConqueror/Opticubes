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
import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import io.socol.opticubes.service.editing.OptiCubeEditingService;
import io.socol.opticubes.service.opti.OptiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import timecore.api.common.config.Config;

@Mod(modid = OptiCubes.MODID, version = OptiCubes.VERSION, dependencies = "required-after:spongemixins@[1.1.0,);")
public class OptiCubes {
    public static final String MODID = "opticubes";
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final Logger LOGGER = LogManager.getLogger("OptiCubes");

    @SidedProxy(clientSide = "io.socol.opticubes.proxy.ClientProxy", serverSide = "io.socol.opticubes.proxy.CommonProxy")
    public static CommonProxy proxy;

    private static OptiService optiService = null;
    private static OptiCubeEditingService editingService;

    public OptiCubes() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            optiService = new OptiService();
            editingService = new ClientOptiCubeEditingService();
        } else {
            editingService = new OptiCubeEditingService();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        Config.setConfigDir(event.getModConfigurationDirectory());
        OCConfigs.load();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static OptiService getOptiService() {
        return optiService;
    }

    public static OptiCubeEditingService getEditingService() {
        return editingService;
    }
}
